package com.github.jselby.easyjetpack;

import java.util.ListIterator;

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
	public static boolean playerIsWearing(Player player, int armorSlot, int id,
			short durability) {
		if (player.getInventory().getArmorContents()[armorSlot].getTypeId() == id
				&& player.getInventory().getArmorContents()[armorSlot]
						.getDurability() == durability) {
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
	public static boolean playerIsHolding(Player player, int jetpackId,
			short durability) {
		if (player.getInventory().getItemInHand().getTypeId() == jetpackId
				&& player.getInventory().getItemInHand().getDurability() == durability) {
			return true;
		}
		return false;
	}

	public static void shuffleCoal(Player player, boolean mustBeHolding) {
		if (player.getInventory().contains(
				EasyJetpack.getInstance().getConfig().getInt("fuel.id"))) {
			int position = player.getInventory().first(
					EasyJetpack.getInstance().getConfig().getInt("fuel.id"));
			ItemStack stack = player.getInventory().getItem(position);
			player.getInventory().removeItem(stack);
			if (mustBeHolding) {
				player.setItemInHand(stack);
			} else {
				player.getInventory().addItem(stack);
			}
		} else {
			player.sendMessage(ChatColor.RED + "You have run out of coal!");
		}
	}

	public static int findCoal(Player player, boolean includeNormalCoal) {
		ListIterator<ItemStack> it = player.getInventory().iterator();
		Material fuelMaterial = Material.getMaterial(EasyJetpack.getInstance()
				.getConfig().getInt("fuel.id"));
		int id = -1;
		int normalCoal = 0;
		int count = 0;
		while (it.hasNext()) {
			try {
				ItemStack stack = (ItemStack) it.next();

				if (id == -1
						&& stack != null
						&& stack.getType().equals(fuelMaterial)
						&& stack.hasItemMeta()
						&& stack.getItemMeta().hasDisplayName()
						&& stack.getItemMeta().getDisplayName()
								.contains("Burning Coal")) {

					id = count;
				} else if (id == -1 && stack != null
						&& stack.getType().equals(fuelMaterial)) {
					normalCoal = count;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			count++;
		}

		if (id == -1 && includeNormalCoal) {
			return normalCoal;
		}

		return id;
	}
}
