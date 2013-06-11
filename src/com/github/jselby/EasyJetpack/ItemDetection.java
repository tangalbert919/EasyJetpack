package com.github.jselby.easyjetpack;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemDetection {
	// Get the boot id
	public static int getBootId() {
		String bootId = (String) EasyJetpack.getInstance().getConfig()
				.getString("boots.id");
		if (bootId.indexOf(":") > -1) {
			return Integer.parseInt(bootId.split(":")[0]);
		} else {
			return Integer.parseInt(bootId);
		}
	}

	// If durability is set, check for it
	public static short getBootDurability() {
		String bootId = (String) EasyJetpack.getInstance().getConfig()
				.getString("boots.id");
		if (bootId.indexOf(":") > -1) {
			return Short.parseShort(bootId.split(":")[1]);
		} else {
			return 0;
		}
	}

	// Checks if the player is wearing the right boots for anti-fall damage
	public static boolean checkPlayerWearingBoots(Player player) {
		if (getBootDurability() < 1) {
			return Utils.playerIsWearing(player, 0, getBootId());
		} else {
			return Utils.playerIsWearing(player, 0, getBootId(),
					getBootDurability());
		}
	}

	// Get the jetpack id
	public static int getJetpackId() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("jetpack.id");
		if (jetpackId.indexOf(":") > -1) {
			return Integer.parseInt(jetpackId.split(":")[0]);
		} else {
			return Integer.parseInt(jetpackId);
		}
	}

	// Get the jetpack durability
	public static short getJetpackDurability() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("jetpack.id");
		if (jetpackId.indexOf(":") > -1) {
			return Short.parseShort(jetpackId.split(":")[1]);
		} else {
			return 0;
		}
	}

	// Checks if the player is wearing the right jetpack for "flying"
	public static boolean checkPlayerWearingJetpack(Player player) {
		if (getJetpackDurability() < 1) {
			return Utils.playerIsWearing(player, 2, getJetpackId());
		} else {
			return Utils.playerIsWearing(player, 2, getJetpackId(),
					getJetpackDurability());
		}
	}

	// Get the temp-jetpack id
	public static int getTempJetpackId() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("tempjetpack.id");
		if (jetpackId.indexOf(":") > -1) {
			return Integer.parseInt(jetpackId.split(":")[0]);
		} else {
			return Integer.parseInt(jetpackId);
		}
	}

	// Get the temp-jetpack durability
	public static short getTempJetpackDurability() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("tempjetpack.id");
		if (jetpackId.indexOf(":") > -1) {
			return Short.parseShort(jetpackId.split(":")[1]);
		} else {
			return 0;
		}
	}

	// Checks if the player is wearing the right jetpack for temp "flying"
	public static boolean checkPlayerWearingTempJetpack(Player player) {
		if (getTempJetpackDurability() < 1) {
			return Utils.playerIsWearing(player, 2, getTempJetpackId());
		} else {
			return Utils.playerIsWearing(player, 2, getTempJetpackId(),
					getTempJetpackDurability());
		}
	}

	// Get the fuel id
	public static int getFuelId() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("fuel.id");
		if (jetpackId.indexOf(":") > -1) {
			return Integer.parseInt(jetpackId.split(":")[0]);
		} else {
			return Integer.parseInt(jetpackId);
		}
	}

	// Get the fuel durability
	public static short getFuelDurability() {
		String jetpackId = (String) EasyJetpack.getInstance().getConfig()
				.getString("fuel.id");
		if (jetpackId.indexOf(":") > -1) {
			return Short.parseShort(jetpackId.split(":")[1]);
		} else {
			return 0;
		}
	}

	// Checks if the player is holding the right fuel
	public static boolean checkPlayerHoldingFuel(Player player) {
		if (getFuelDurability() < 1) {
			return Utils.playerIsHolding(player, getFuelId());
		} else {
			return Utils.playerIsHolding(player, getFuelId(),
					getFuelDurability());
		}
	}

	// Convience method - gets a item stack of air
	public static ItemStack getAir() {
		ItemStack air = new ItemStack(Material.AIR);
		return air;
	}
	
	// Tells the player that they have no fuel
	public static void noFuel(Player player) {
		ItemStack fuel = new ItemStack(Integer.parseInt((String) EasyJetpack.getInstance().getConfig().getString("fuel.id")));
		if (Boolean.parseBoolean((String) EasyJetpack.getInstance().getConfig().getString("chat.messages"))) {
			player.sendMessage(ChatColor.RED + EasyJetpack.getInstance().getConfig().getString("chat.nofuel").replaceAll("FUELNAME", fuel.getType().name().toLowerCase().replace("_", " ")));
		}
	}

	// Searches a player for a jetpack
	public static void jetpackSearch(Player player) {
		if (ItemDetection.checkPlayerWearingJetpack(player)) {
			Jetpack.spaceFlyingHandler(player, true);
		} else if (ItemDetection.checkPlayerWearingTempJetpack(player)) {
			Jetpack.spaceFlyingHandler(player, true);
		} else {
			Jetpack.spaceFlyingHandler(player, false);
		}
	}

	public static boolean isUsingCustomItems() {
		return EasyJetpack.getInstance().getConfig().getBoolean("item.usecustomitems");
	}
}
