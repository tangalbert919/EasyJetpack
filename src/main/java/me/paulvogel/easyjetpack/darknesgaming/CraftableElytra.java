package main.java.me.paulvogel.easyjetpack.darknesgaming;

import main.java.me.paulvogel.easyjetpack.api.FlightTypes;
import main.java.me.paulvogel.easyjetpack.api.Jetpack;
import main.java.me.paulvogel.easyjetpack.api.JetpackEvent;
import main.java.me.paulvogel.easyjetpack.impl.CraftingRecipe;
import org.bukkit.ChatColor;
import org.bukkit.Material;

public class CraftableElytra extends Jetpack {
    public CraftableElytra() {

    }
    public String getName() {
        return ChatColor.RESET + "Elytra";
    }

    public String getGiveName() {
        return "elytra";
    }

    public String[] getDescription() {
        return new String[] {
                ChatColor.RESET + "The craftable elytra."
        };
    }

    public Material getMaterial() {
        return Material.getMaterial(getConfig().getString("jetpacks.elytra.material", "ELYTRA"));
    }

    public void onFlyEvent(JetpackEvent event) {

    }

    public void onFuelUsageEvent(JetpackEvent event) {

    }

    public boolean onFuelCheckEvent(JetpackEvent event) {
        return false;
    }

    public FlightTypes getMovementType() {
        return null;
    }

    public CraftingRecipe getCraftingRecipe() {
        if(!this.getConfig().getBoolean("jetpacks.elytra.craftable", true)) {
            return null;
        } else {
            CraftingRecipe recipe = new CraftingRecipe(this.getItem(5));
            recipe.setSlot(0, Material.LEATHER);
            recipe.setSlot(2, Material.LEATHER);
            recipe.setSlot(3, Material.LEATHER);
            recipe.setSlot(4, Material.ENDER_PEARL);
            recipe.setSlot(5, Material.LEATHER);
            recipe.setSlot(6, Material.LEATHER);
            recipe.setSlot(7, Material.LEATHER);
            recipe.setSlot(8, Material.LEATHER);
            return recipe;
        }
    }

    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    public boolean isRepairingDisabled() {
        return false;
    }
}
