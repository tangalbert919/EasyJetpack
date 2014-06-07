package net.jselby.ej;

import net.jselby.ej.api.Jetpack.Slot;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import java.util.*;

/**
 * A convenience class, containing simple methods for manipulation
 *
 * @author James
 */
public class Utils {

    /**
     * A method to convert a String array to a List of Strings
     *
     * @param array The string array to convert
     * @return A ArrayList containing the array elements
     */
    public static List<String> convertToList(String[] array) {
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, array);
        return list;
    }

    /**
     * A method to compare two items, and see if they are similar
     *
     * @param item  The first item
     * @param item2 The second item
     * @return If the items are equal
     */
    public static boolean isItemStackEqual(ItemStack item, ItemStack item2) {
        return item != null
                && item2 != null
                && item.hasItemMeta()
                && item2.hasItemMeta()
                && item.getType().equals(item2.getType())
                && item.getItemMeta().hasDisplayName()
                && item2.getItemMeta().hasDisplayName()
                && item.getItemMeta().hasLore()
                && item2.getItemMeta().hasLore()
                && item.getItemMeta().getDisplayName()
                .equalsIgnoreCase(item2.getItemMeta().getDisplayName())
                && isArrayEqual(item.getItemMeta().getLore(), item2
                .getItemMeta().getLore());

    }

    /**
     * Checks if two lists have the same contents
     *
     * @param list  The first list
     * @param list2 The second list
     * @return If the lists are the same
     */
    public static boolean isArrayEqual(List<String> list, List<String> list2) {
        if (list == null || list2 == null) {
            return false;
        }
        try {
            Iterator<String> it = list.iterator();
            int count = 0;
            while (it.hasNext()) {
                String next = it.next();
                if (!list2.get(count).equalsIgnoreCase(next)) {
                    return false;
                }
                count++;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void useFuel(Player player, boolean mustBeHolding,
                               double factor) {

        int coalToGiveBack;
        ItemStack myitem;

        if (mustBeHolding) {
            myitem = player.getInventory().getItemInHand();
        } else {
            myitem = player.getInventory().getItem(
                    player.getInventory().first(
                            Material.getMaterial(EasyJetpack.getInstance()
                                    .getConfig()
                                    .getString("fuel.material", "COAL"))));
        }

        coalToGiveBack = myitem.getAmount() - 1;
        if (coalToGiveBack > 0) {
            player.getInventory().remove(myitem);
            if (!mustBeHolding) {
                player.getInventory().addItem(
                        new ItemStack(Material.getMaterial(EasyJetpack
                                .getInstance().getConfig()
                                .getString("fuel.material", "COAL")), 1));
            } else {
                player.setItemInHand(new ItemStack(Material
                        .getMaterial(EasyJetpack.getInstance().getConfig()
                                .getString("fuel.material", "COAL")), 1));
            }
        }

        ItemStack item;
        if (mustBeHolding) {
            item = player.getItemInHand();
        } else {
            item = player.getInventory().getItem(
                    player.getInventory().first(
                            Material.getMaterial(EasyJetpack.getInstance()
                                    .getConfig()
                                    .getString("fuel.material", "COAL"))));
        }

        short fuelUsage;
        if (!(item != null && item.hasItemMeta() && item.getItemMeta()
                .hasLore())) {
            // Unused fuel
            fuelUsage = 100;
        } else {
            if (mustBeHolding) {
                item = player.getItemInHand();
            } else {
                item = player.getInventory().getItem(
                        Utils.findCoal(player, false));
            }
            String fuel = item
                    .getItemMeta()
                    .getLore()
                    .get(0)
                    .substring(2,
                            item.getItemMeta().getLore().get(0).indexOf("%"));
            fuelUsage = Short.parseShort(fuel);
        }

        fuelUsage -= (((double) 100) / ((double) 10) / factor);

        List<String> newLore = new ArrayList<String>();
        newLore.add(ChatColor.RESET + "" + fuelUsage + "% left");

        int coal = Utils.findCoal(player, true);

        if (mustBeHolding) {
            item = player.getInventory().getItemInHand();
        } else {
            item = player.getInventory().getItem(Utils.findCoal(player, true));
        }
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ChatColor.RESET + "" + ChatColor.DARK_RED + "Burning "
                + Material
                .getMaterial(
                        EasyJetpack.getInstance().getConfig()
                                .getString("fuel.material", "COAL"))
                .name().toLowerCase() + " - " + fuelUsage + "% left");
        itemMeta.setLore(newLore);
        item.setItemMeta(itemMeta);

        if (fuelUsage < 1) {
            if (mustBeHolding) {
                player.getInventory().setItemInHand(
                        new ItemStack(Material.AIR, 1));
            } else {
                player.getInventory().clear(coal);
            }
            Utils.shuffleCoal(player, mustBeHolding);
        } else {
            if (!mustBeHolding) {
                player.getInventory().setItem(coal, item);
            } else {
                player.getInventory().setItemInHand(item);
            }
        }

        if (coalToGiveBack > 0) {
            player.getInventory().addItem(
                    new ItemStack(Material.getMaterial(EasyJetpack
                            .getInstance().getConfig()
                            .getString("fuel.material", "COAL")),
                            coalToGiveBack));
        }
    }

    public static void shuffleCoal(Player player, boolean mustBeHolding) {
        if (player.getInventory().contains(
                Material.getMaterial(EasyJetpack.getInstance().getConfig()
                        .getString("fuel.material", "COAL")))) {
            int position = player.getInventory().first(
                    Material.getMaterial(EasyJetpack.getInstance().getConfig()
                            .getString("fuel.material", "COAL")));
            ItemStack stack = player.getInventory().getItem(position);
            player.getInventory().removeItem(stack);
            if (mustBeHolding) {
                player.setItemInHand(stack);
            } else {
                player.getInventory().addItem(stack);
            }
        } else {
            player.sendMessage(ChatColor.RED
                    + "You have run out of "
                    + Material
                    .getMaterial(
                            EasyJetpack.getInstance().getConfig()
                                    .getString("fuel.material", "COAL"))
                    .name().toLowerCase() + "!");
        }
    }

    public static int findCoal(Player player, boolean includeNormalCoal) {
        ListIterator<ItemStack> it = player.getInventory().iterator();
        Material fuelMaterial = Material.getMaterial(EasyJetpack.getInstance()
                .getConfig().getString("fuel.material", "COAL"));
        int id = -1;
        int normalCoal = 0;
        int count = 0;
        while (it.hasNext()) {
            try {
                ItemStack stack = it.next();

                if (id == -1
                        && stack != null
                        && stack.getType().equals(fuelMaterial)
                        && stack.hasItemMeta()
                        && stack.getItemMeta().hasDisplayName()
                        && stack.getItemMeta()
                        .getDisplayName()
                        .contains(
                                "Burning "
                                        + fuelMaterial.name()
                                        .toLowerCase())) {

                    id = count;
                } else if (id == -1 && stack != null
                        && stack.getType().equals(fuelMaterial)) {
                    normalCoal = count;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
        }

        if (id == -1 && includeNormalCoal) {
            return normalCoal;
        }

        return id;
    }

    /**
     * Obtains the item that the player has in the relevant slot. Supports the
     * slots from Jetpack.Slot.
     *
     * @param player The player to obtain the item from
     * @param slot   The slot that should be checked
     * @return A ItemStack, or null
     */
    public static ItemStack getSlot(Player player, Slot slot) {
        switch (slot) {
            case HELD_ITEM:
                return player.getItemInHand();
            case HELMET:
                return player.getInventory().getHelmet();
            case CHESTPLATE:
                return player.getInventory().getChestplate();
            case LEGGINGS:
                return player.getInventory().getLeggings();
            case BOOTS:
                return player.getInventory().getBoots();
            default:
                return null;
        }
    }

    /**
     * Sets the specified slot to the new item, using slots from Jetpack.Slot.
     *
     * @param player The player who will be modified
     * @param slot   The slot that will be modified
     */
    public static void setSlot(Player player, Slot slot, ItemStack is) {
        switch (slot) {
            case HELD_ITEM:
                player.setItemInHand(is);
                break;
            case HELMET:
                player.getInventory().setHelmet(is);
                break;
            case CHESTPLATE:
                player.getInventory().setChestplate(is);
                break;
            case LEGGINGS:
                player.getInventory().setLeggings(is);
                break;
            case BOOTS:
                player.getInventory().setBoots(is);
                break;
            default:
                break;
        }
    }

    public static void damage(Player player, Slot slot, int maximum) {
        ItemStack jetpack = Utils.getSlot(player, slot);
        jetpack.setDurability((short) (jetpack.getDurability() + 1));
        if (jetpack.getDurability() > maximum) {
            player.sendMessage(ChatColor.RED + "Your "
                    + jetpack.getItemMeta().getDisplayName() + ChatColor.RESET
                    + "" + ChatColor.RED + " is broken!");
            Utils.setSlot(player, slot, new ItemStack(Material.AIR, 1));
        }
    }

    public static Vector addVector(Player p, Vector v, double maxX, double maxY, double maxZ) {
        Vector curr = p.getVelocity();
        Vector added = curr.add(v).normalize();
        if (added.getX() > maxX) {
            added.setX(maxX);
        }
        if (added.getX() < -maxX) {
            added.setX(-maxX);
        }
        if (added.getY() > maxY) {
            added.setY(maxY);
        }
        if (added.getZ() > maxZ) {
            added.setZ(maxZ);
        }
        if (added.getZ() < -maxZ) {
            added.setZ(-maxZ);
        }
        return added;
    }
}
