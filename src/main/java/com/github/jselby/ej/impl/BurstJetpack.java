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
 * A Jetpack built for speed
 * 
 * @author James
 * 
 */
public class BurstJetpack extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.BLUE + "Burst Jetpack";
	}

	@Override
	public String[] getDescription() {
		return new String[] {
				ChatColor.RESET + "Need to climb into the atmosphere",
				ChatColor.RESET + "at great speeds? The burst jetpack",
				ChatColor.RESET + "is for you!" };
	}

	@Override
	public Material getMaterial() {
		return Material.GOLD_CHESTPLATE;
	}

	@Override
	public void onFlyEvent(JetpackEvent event) {
		Vector dir = event.getPlayer().getLocation().getDirection();
		double y = event.getPlayer().getVelocity().getY();
		if (y < 0.3D) {
			y = 0.3D;
		}
		y *= 1.3D;
		if (y > 10) {
			y = 10;
		}
		Vector vec = new Vector(dir.getX() * 0.5D, y, dir.getZ() * 0.5D);
		event.getPlayer().setVelocity(vec);
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
		Utils.useFuel(event.getPlayer(), false, 4);
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
		return FlightTypes.CROUCH_CONSTANT;
	}

	@Override
	public CraftingRecipe getCraftingRecipe() {
		CraftingRecipe recipe = new CraftingRecipe(getItem());
		recipe.setSlot(0, Material.REDSTONE);
		recipe.setSlot(3, Material.REDSTONE);
		recipe.setSlot(6, Material.REDSTONE);
		
		recipe.setSlot(1, Material.DIAMOND);
		recipe.setSlot(4, Material.GOLD_CHESTPLATE);
		recipe.setSlot(7, Material.FURNACE);
		
		recipe.setSlot(2, Material.REDSTONE);
		recipe.setSlot(5, Material.REDSTONE);
		recipe.setSlot(8, Material.REDSTONE);
		return recipe;
	}

	@Override
	public String getGiveName() {
		return "burst";
	}

	@Override
	public Slot getSlot() {
		return Slot.CHESTPLATE;
	}
}
