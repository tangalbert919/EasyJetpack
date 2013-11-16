package com.github.jselby.ej.impl;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import com.github.jselby.ej.CraftingRecipe;
import com.github.jselby.ej.FlightTypes;
import com.github.jselby.ej.Jetpack;
import com.github.jselby.ej.JetpackEvent;

/**
 * A jetpack for teleporting around
 * 
 * @author James
 * 
 */
public class TeleportJetpack extends Jetpack {

	@Override
	public String getName() {
		return ChatColor.RESET + "" + ChatColor.GOLD + "Teleportation Jetpack";
	}

	@Override
	public String[] getDescription() {
		return new String[] { ChatColor.RESET + "Like a ender pearl, but",
				ChatColor.RESET + "more expensive and time ",
				ChatColor.RESET + "consuming to obtain." };
	}

	@Override
	public Material getMaterial() {
		return Material.CHAINMAIL_CHESTPLATE;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onFlyEvent(JetpackEvent event) {
		Block block = event.getPlayer().getTargetBlock(null, 30);
		if (block != null) {
			event.getPlayer().teleport(block.getLocation().add(0, 2, 0));
		}
	}

	@Override
	public void onFuelUsageEvent(JetpackEvent event) {
		event.getPlayer().getInventory()
				.removeItem(new ItemStack(Material.COAL, 1));
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
		recipe.setSlot(0, Material.ENDER_PEARL);
		recipe.setSlot(3, Material.ENDER_PEARL);
		recipe.setSlot(6, Material.ENDER_PEARL);
		
		recipe.setSlot(1, Material.DIAMOND);
		recipe.setSlot(4, Material.GOLD_CHESTPLATE);
		recipe.setSlot(7, Material.FURNACE);
		
		recipe.setSlot(2, Material.ENDER_PEARL);
		recipe.setSlot(5, Material.ENDER_PEARL);
		recipe.setSlot(8, Material.ENDER_PEARL);
		return recipe;
	}

	@Override
	public String getGiveName() {
		return "teleportation";
	}
}
