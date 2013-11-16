package com.github.jselby.ej.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

import com.github.jselby.ej.CraftingRecipe;
import com.github.jselby.ej.FlightTypes;
import com.github.jselby.ej.Jetpack;
import com.github.jselby.ej.JetpackEvent;
import com.github.jselby.ej.Utils;

/**
 * A legacy Jetpack from a older version of the plugin. Also maintains
 * compatibility with it.
 * 
 * @author James
 * 
 */
public class TraditionalJetpack extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.DARK_RED + "Jetpack";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "Fly away in the Jetpack 3000!",
				ChatColor.RESET + "Limited time only! Only $9.95",
				ChatColor.RESET + "at your local black market." };
	}

	@Override
	public Material getMaterial() {
		return Material.GOLD_CHESTPLATE;
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
		Vector dir = event.getPlayer().getLocation().getDirection();
		Vector vec = new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D);
		event.getPlayer().setVelocity(vec);
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
		Utils.useFuel(event.getPlayer(), false, 1);
	}

	@Override
	public boolean onFuelCheckEvent(JetpackEvent event) {
		boolean containsCoal = event.getPlayer().getInventory()
				.contains(Material.COAL);
		if (!containsCoal)
			event.getPlayer().sendMessage(
					ChatColor.RED + "You don't have enough fuel!");
		return containsCoal;
	}

	@Override
	public FlightTypes getMovementType() {
		return FlightTypes.CROUCH;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
		CraftingRecipe recipe = new CraftingRecipe(getItem());
		recipe.setSlot(1, Material.DIAMOND);
		recipe.setSlot(4, Material.GOLD_CHESTPLATE);
		recipe.setSlot(7, Material.FURNACE);
		return recipe;
	}

	@Override
	public String getGiveName() {
		return "standard";
	}
}
