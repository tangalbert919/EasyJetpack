package com.github.jselby.ej;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * A convenience class, containing simple methods for manipulation
 * 
 * @author James
 * 
 */
public class Utils {

	/**
	 * A method to convert a String array to a List of Strings
	 * 
	 * @param array
	 *            The string array to convert
	 * @return A ArrayList containing the array elements
	 */
	public static List<String> convertToList(String[] array) {
		ArrayList<String> list = new ArrayList<String>();
		for (String str : array) {
			list.add(str);
		}
		return list;
	}

	/**
	 * A method to compare two items, and see if they are similar
	 * 
	 * @param item
	 *            The first item
	 * @param item2
	 *            The second item
	 * @return If the items are equal
	 */
	public static boolean isItemStackEqual(ItemStack item, ItemStack item2) {
		if (item != null
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
						.getItemMeta().getLore())) {
			return true;
		}

		return false;
	}

	/**
	 * Checks if two lists have the same contents
	 * 
	 * @param list
	 *            The first list
	 * @param list2
	 *            The second list
	 * @return If the lists are the same
	 */
	public static boolean isArrayEqual(List<String> list, List<String> list2) {
		if (list == null || list2 == null) {
			return false;
		}
		try {
			if (list.size() == list2.size()) {
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
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	public static void useFuel(Player player, boolean mustBeHolding,
			double factor) {

		int coalToGiveBack = 0;
		ItemStack myitem;
		if (mustBeHolding) {
			myitem = player.getInventory().getItemInHand();
		} else {
			myitem = player.getInventory().getItem(
					player.getInventory().first(Material.COAL));
		}
		coalToGiveBack = myitem.getAmount() - 1;
		if (coalToGiveBack > 0) {
			player.getInventory().remove(myitem);
			if (!mustBeHolding) {
				player.getInventory().addItem(
						new ItemStack(Material.COAL, 1));
			} else {
				player.setItemInHand(new ItemStack(Material.COAL, 1));
			}
		}

		ItemStack item;
		if (mustBeHolding) {
			item = player.getItemInHand();
		} else {
			item = player.getInventory().getItem(
					player.getInventory().first(
							Material.COAL));
		}

		short fuelUsage = 1;
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

		fuelUsage -= (((double) 100)
				/ ((double) 10) / factor);

		List<String> newLore = new ArrayList<String>();
		newLore.add("§R" + fuelUsage + "% left");

		int coal = Utils.findCoal(player, true);

		if (mustBeHolding) {
			item = player.getInventory().getItemInHand();
		} else {
			item = player.getInventory().getItem(Utils.findCoal(player, true));
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§R§4Burning Coal - " + fuelUsage + "% left");
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
					new ItemStack(Material.COAL, coalToGiveBack));
		}
	}

	public static void shuffleCoal(Player player, boolean mustBeHolding) {
		if (player.getInventory().contains(
				Material.COAL)) {
			int position = player.getInventory().first(
					Material.COAL);
			ItemStack stack = player.getInventory().getItem(position);
			player.getInventory().removeItem(stack);
			if (mustBeHolding) {
				player.setItemInHand(stack);
			} else {
				player.getInventory().addItem(stack);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You have run out of coal!");
		}
	}

	public static int findCoal(Player player, boolean includeNormalCoal) {
		ListIterator<ItemStack> it = player.getInventory().iterator();
		Material fuelMaterial = Material.COAL;
		int id = -1;
		int normalCoal = 0;
		int count = 0;
		while (it.hasNext()) {
			try {
				ItemStack stack = (ItemStack) it.next();

				if (id == -1
						&& stack != null
						&& stack.getType().equals(fuelMaterial)
						&& stack.hasItemMeta()
						&& stack.getItemMeta().hasDisplayName()
						&& stack.getItemMeta().getDisplayName()
								.contains("Burning Coal")) {

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
}
