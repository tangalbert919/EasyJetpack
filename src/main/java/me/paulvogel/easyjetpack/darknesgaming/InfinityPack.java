package main.java.me.paulvogel.easyjetpack.darknesgaming;

import main.java.me.paulvogel.easyjetpack.Utils;
import main.java.me.paulvogel.easyjetpack.api.FlightTypes;
import main.java.me.paulvogel.easyjetpack.api.Jetpack;
import main.java.me.paulvogel.easyjetpack.api.JetpackEvent;
import main.java.me.paulvogel.easyjetpack.impl.CraftingRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * The normal Infinity Jetpack.
 *
 * Creator: DarknesGaming
 */
public class InfinityPack extends Jetpack {
    public InfinityPack() {

    }
    public String getName() {
        return ChatColor.GREEN + "Infinity Jetpack";
    }

    public String getGiveName() {
        return "infinitypack";
    }

    public String[] getDescription() {
        return new String[] {
                ChatColor.YELLOW + "The more efficient version of" +
                        "the standard jetpack."
        };
    }

    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString("jetpacks.infinitypack.material", "DIAMOND_CHESTPLATE"));
    }

    public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        event.getPlayer().setVelocity(Utils.addVector(event.getPlayer(), new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D), 0.45D, 0.6D, 0.45D));
        InfinityParticles.jetpackEffect(event.getPlayer());
    }

    public void onFuelUsageEvent(JetpackEvent event) {
        if(this.getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 75.0D);
        }
    }

    public boolean onFuelCheckEvent(JetpackEvent event) {
        if(this.getConfig().getBoolean("fuel.enabled", true)) {
            boolean containsCoal = Utils.playerHasFuel(event.getPlayer());
            if(!containsCoal) {
                event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough fuel!");
            }

            return containsCoal;
        } else {
            return true;
        }
    }

    public FlightTypes getMovementType() {
        return FlightTypes.CROUCH;
    }

    public CraftingRecipe getCraftingRecipe() {
        if(!this.getConfig().getBoolean("jetpacks.infinitypack.craftable", true)) {
            return null;
        } else {
            CraftingRecipe recipe = new CraftingRecipe(this.getItem());
            recipe.setSlot(0, Material.EMERALD);
            recipe.setSlot(3, Material.DIAMOND);
            recipe.setSlot(6, Material.REDSTONE);
            recipe.setSlot(1, Material.DIAMOND);
            recipe.setSlot(4, Material.NETHER_STAR);
            recipe.setSlot(7, Material.FURNACE);
            recipe.setSlot(2, Material.EMERALD);
            recipe.setSlot(5, Material.DIAMOND);
            recipe.setSlot(8, Material.REDSTONE);
            return recipe;
        }
    }

    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    public boolean isRepairingDisabled() {
        return this.getConfig().getBoolean("jetpacks.infinitypack.antianvil", true);
    }
}
