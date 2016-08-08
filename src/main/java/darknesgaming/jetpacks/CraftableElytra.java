package darknesgaming.jetpacks;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

public class CraftableElytra extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "Elytra";
	}

	@Override
	public String getGiveName() {
		return "elytra";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.DARK_PURPLE + "The Infinity Laboratories found",
				ChatColor.DARK_PURPLE + "a way to craft the Elytra, the",
				ChatColor.DARK_PURPLE + "only wings that will allow players",
				ChatColor.DARK_PURPLE + "to fly around the world!"
		};
	}

	@Override
	public Material getMaterial() {
		return Material.getMaterial(getConfig().getString(
				"jetpacks.elytra.material", "ELYTRA"));
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onFuelCheckEvent(JetpackEvent event) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public FlightTypes getMovementType() {
		return null;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.elytra.craftable", true)) {
        	return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem());
        recipe.setSlot(0, Material.FEATHER);
        recipe.setSlot(2, Material.FEATHER);
        recipe.setSlot(4, Material.FEATHER);
        recipe.setSlot(6, Material.FEATHER);
        recipe.setSlot(8, Material.FEATHER);
        return recipe;
	}

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}

	@Override
	public boolean isRepairingDisabled() {
		return getConfig().getBoolean("jetpacks.elytra.antianvil", true);
	}
	public String getPermission() {
		return "easyjetpack." + getGiveName();
	}

}
