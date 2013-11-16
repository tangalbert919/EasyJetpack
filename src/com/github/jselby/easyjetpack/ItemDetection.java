package com.github.jselby.easyjetpack;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
		return Utils.playerIsWearing(player, 2, getTempJetpackId());
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
	@SuppressWarnings("deprecation")
	public static void noFuel(Player player) {
		ItemStack fuel = new ItemStack(Integer.parseInt((String) EasyJetpack
				.getInstance().getConfig().getString("fuel.id")));
		if (Boolean.parseBoolean((String) EasyJetpack.getInstance().getConfig()
				.getString("chat.messages"))) {
			player.sendMessage(ChatColor.RED
					+ EasyJetpack
							.getInstance()
							.getConfig()
							.getString("chat.nofuel")
							.replaceAll(
									"FUELNAME",
									fuel.getType().name().toLowerCase()
											.replace("_", " ")));
		}
	}

	// Searches a player for a jetpack
	public static boolean jetpackSearch(Player player, boolean shouldModify) {
		boolean isEquiped = false;
		if (ItemDetection.checkPlayerWearingJetpack(player)) {
			isEquiped = Jetpack.spaceFlyingHandler(player, true, shouldModify);
		} else if (ItemDetection.checkPlayerWearingTempJetpack(player)) {
			isEquiped = Jetpack.spaceFlyingHandler(player, true, shouldModify);
		} else {
			isEquiped = Jetpack.spaceFlyingHandler(player, false, shouldModify);
		}
		return isEquiped;
	}

	// Returns if the configuration allows custom items
	public static boolean isUsingCustomItems() {
		return EasyJetpack.getInstance().getConfig()
				.getBoolean("item.usecustomitems");
	}

	@SuppressWarnings({ "unused", "deprecation" })
	public static void useFuel(Player player, boolean mustBeHolding, double factor) {
		if (player.hasPermission("easyjetpack.fuelless")) {
			return;
		}

		if (player == null) {
			return;
		}

		int coalToGiveBack = 0;
		ItemStack myitem;
		if (mustBeHolding) {
			myitem = player.getInventory().getItemInHand();
		} else {
			myitem = player.getInventory().getItem(
					player.getInventory().first(
							EasyJetpack.getInstance().getConfig()
									.getInt("fuel.id")));
		}
		coalToGiveBack = myitem.getAmount() - 1;
		if (coalToGiveBack > 0) {
			player.getInventory().remove(myitem);
			if (!mustBeHolding) {
				player.getInventory().addItem(
						new ItemStack(EasyJetpack.getInstance().getConfig()
								.getInt("fuel.id"), 1));
			} else {
				player.setItemInHand(new ItemStack(EasyJetpack.getInstance()
						.getConfig().getInt("fuel.id"), 1));
			}
		}
		
		ItemStack item;
		if (mustBeHolding) {
			item = player.getItemInHand();
		} else {
			item = player.getInventory().getItem(
					player.getInventory().first(
							EasyJetpack.getInstance().getConfig()
									.getInt("fuel.id")));
		}

		short fuelUsage = 1;
		if (!(item != null
						&& item.hasItemMeta() && item.getItemMeta().hasLore())) {
			// Unused fuel
			fuelUsage = 100;
		} else {
			if (mustBeHolding) {
				item = player.getItemInHand();
			} else {
				item = player.getInventory().getItem(Utils.findCoal(player, false));
			}
			String fuel = item
					.getItemMeta()
					.getLore()
					.get(0)
					.substring(2,
							item.getItemMeta().getLore().get(0).indexOf("%"));
			fuelUsage = Short.parseShort(fuel);
		}
		
		fuelUsage -= (((double)100) / ((double)EasyJetpack.getInstance().getConfig()
				.getInt("fuel.uses")) / factor);

		List<String> newLore = new ArrayList<String>();
		newLore.add("§R" + fuelUsage + "% left");


		int coal = Utils.findCoal(player, true);	
		
		if (mustBeHolding) {
			item = player.getInventory().getItemInHand();
		} else {
			item = player.getInventory().getItem(Utils.findCoal(player, true));
		}
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("§R§4Burning Coal - " + fuelUsage + "% left");
		itemMeta.setLore(newLore);
		item.setItemMeta(itemMeta);
		
		if (fuelUsage < 1) {
			if (mustBeHolding) {
				player.getInventory().setItemInHand(
						new ItemStack(Material.AIR, 1));
			} else {
				player.getInventory().clear(coal);
			}
			Utils.shuffleCoal(player, mustBeHolding);
		} else {
			if (!mustBeHolding) {
				player.getInventory().setItem(coal, item);
			} else {
				player.getInventory().setItemInHand(item);
			}
		}

		if (coalToGiveBack > 0) {
			player.getInventory().addItem(
					new ItemStack(EasyJetpack.getInstance().getConfig()
							.getInt("fuel.id"), coalToGiveBack));
		}
	}
}
