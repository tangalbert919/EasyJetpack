package darknesgaming.jetpacks;

import me.paulvogel.easyjetpack.api.FlightTypes;
import me.paulvogel.easyjetpack.api.Jetpack;
import me.paulvogel.easyjetpack.api.JetpackEvent;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import me.paulvogel.easyjetpack.impl.CraftingRecipe;

public class CraftableElytra extends Jetpack{

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
				ChatColor.GREEN + "Infinity Laboratories" + ChatColor.RESET + " have found a way",
				ChatColor.RESET + "to craft unbreakable Elytras! Now you",
				ChatColor.RESET + "can fly for long periods of time!"
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
        if (!getConfig().getBoolean("jetpacks.slimepack.craftable", true)) {
        	return null;
        }
        CraftingRecipe recipe = new CraftingRecipe(getItem(5));
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

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}

	@Override
	public boolean isRepairingDisabled() {
		return false;
	}

}
