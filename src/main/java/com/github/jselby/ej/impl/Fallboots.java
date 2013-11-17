package com.github.jselby.ej.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.github.jselby.ej.CraftingRecipe;
import com.github.jselby.ej.FlightTypes;
import com.github.jselby.ej.Jetpack;
import com.github.jselby.ej.JetpackEvent;

public class Fallboots extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.DARK_RED + "Fall Boots";
	}

	@Override
	public String getGiveName() {
		return "fallboots";
	}

	@Override
	public String[] getDescription() {
		return new String[] {ChatColor.RESET + "Direct from Aperture Science!",
				ChatColor.RESET + "Outlawed in 170 countries!"};
	}

	@Override
	public Material getMaterial() {
		return Material.LEATHER_BOOTS;
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
		event.setCancelled(true);
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {}

	@Override
	public boolean onFuelCheckEvent(JetpackEvent event) {
		return true;
	}

	@Override
	public FlightTypes getMovementType() {
		return FlightTypes.FALLING;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
		CraftingRecipe recipe = new CraftingRecipe(getItem());
		recipe.setSlot(6, Material.FEATHER);
		recipe.setSlot(7, Material.LEATHER_BOOTS);
		recipe.setSlot(8, Material.FEATHER);
		return recipe;
	}

	@Override
	public Slot getSlot() {
		return Slot.BOOTS;
	}

}
