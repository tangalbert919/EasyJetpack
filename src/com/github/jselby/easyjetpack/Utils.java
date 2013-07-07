package com.github.jselby.easyjetpack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Utils {
	// Checks if the player is wearing something
	public static boolean playerIsWearing(Player player, int armorSlot, int id) {
		if (player.getInventory().getArmorContents()[armorSlot].getTypeId() == id) {
			return true;
		}
		return false;
	}
	
	// Checks if the player is wearing something with durability
	public static boolean playerIsWearing(Player player, int armorSlot, int id, short durability) {
		if (player.getInventory().getArmorContents()[armorSlot].getTypeId() == id &&
				player.getInventory().getArmorContents()[armorSlot].getDurability() == durability) {
			return true;
		}
		return false;
	}

	// Checks if the player is holding something
	public static boolean playerIsHolding(Player player, int jetpackId) {
		if (player.getInventory().getItemInHand().getTypeId() == jetpackId) {
			return true;
		}
		return false;
	}
	
	// Checks if the player is holding something with durability
	public static boolean playerIsHolding(Player player, int jetpackId, short durability) {
		if (player.getInventory().getItemInHand().getTypeId() == jetpackId &&
				player.getInventory().getItemInHand().getDurability() == durability) {
			return true;
		}
		return false;
	}

	public static void shuffleCoal(Player player) {
		if (player.getInventory().contains(Material.COAL)) {
			int position = player.getInventory().first(Material.COAL);
			ItemStack stack = player.getInventory().getItem(position);
			player.getInventory().removeItem(stack);
			player.setItemInHand(stack);
		} else {
			player.sendMessage(ChatColor.RED + "You have run out of coal!");
		}
	}

}
