/*
 * EasyJetpack Minecraft Bukkit plugin
 * Written by j_selby
 * 
 * Version 0.5
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.jselby.EasyJetpack;

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
