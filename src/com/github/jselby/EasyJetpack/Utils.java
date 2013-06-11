package com.github.jselby.easyjetpack;

import org.bukkit.entity.Player;

public class Utils {

	public static boolean playerIsWearing(Player player, int armorSlot, int id) {
		if (player.getInventory().getArmorContents()[armorSlot].getTypeId() == id) {
			return true;
		}
		return false;
	}
	
	public static boolean playerIsWearing(Player player, int armorSlot, int id, short durability) {
		if (player.getInventory().getArmorContents()[armorSlot].getTypeId() == id &&
				player.getInventory().getArmorContents()[armorSlot].getDurability() == durability) {
			return true;
		}
		return false;
	}

	public static boolean playerIsHolding(Player player, int jetpackId) {
		if (player.getInventory().getItemInHand().getTypeId() == jetpackId) {
			return true;
		}
		return false;
	}
	
	public static boolean playerIsHolding(Player player, int jetpackId, short durability) {
		if (player.getInventory().getItemInHand().getTypeId() == jetpackId &&
				player.getInventory().getItemInHand().getDurability() == durability) {
			return true;
		}
		return false;
	}

}
