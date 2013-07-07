package com.github.jselby.easyjetpack;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class Jetpack {
	// Plays a jetpack effect
	public static void jetpackEffect(Player player) {
		playEffect(Effect.SMOKE, player.getLocation(), 256);
		playEffect(Effect.SMOKE, player.getLocation(), 256);
		playEffect(Effect.GHAST_SHOOT, player.getLocation(), 1);
	}

	// Launches the player into the air
	public static void launchPlayer(Player player) {
		Vector dir = player.getLocation().getDirection();
		Vector vec = new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D);
		player.setVelocity(vec);
	}

	// Plays the respective effect at a specified location
	public static void playEffect(Effect e, Location l, int num) {
		for (int i = 0; i < EasyJetpack.getInstance().getServer()
				.getOnlinePlayers().length; i++)
			EasyJetpack.getInstance().getServer().getOnlinePlayers()[i]
					.playEffect(l, e, num);
	}

	// Deals damage to the player
	public static void damageJetpack(Player player) {
		player.getInventory().getArmorContents()[2]
				.setDurability((short) (player.getInventory()
						.getArmorContents()[2].getDurability() + 1));
		if (player.getInventory().getArmorContents()[2].getDurability() > 150) {
			if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
					.getConfig().getString("chat.messages"))) {
				player.sendMessage(ChatColor.RED
						+ EasyJetpack
								.getInstance()
								.getConfig()
								.getString("chat.jetpackbroke")
								.replaceAll(
										"JETPACKNAME",
										player.getInventory()
												.getArmorContents()[2]
												.getType().name().toLowerCase()
												.replace("_", " ")));
			}
			player.getInventory().setChestplate(ItemDetection.getAir());
		}
	}

	// Jetpack event
	public static void jetpackEvent(Player player) {
		// Checks the configuration
		ConfigHandler.checkConfig();

		if ((Boolean) EasyJetpack.getInstance().getConfig()
				.get("tempjetpack.enabled")
				&& ItemDetection.checkPlayerWearingTempJetpack(player)
				&& CustomArmor.isValidItem(player.getInventory()
						.getArmorContents()[2])) {
			// Temp jetpack

			// Damages the jetpack
			if (player.getInventory().getArmorContents()[2].getDurability() < 150 - (Integer) EasyJetpack
					.getInstance().getConfig().get("tempjetpack.uses")) {
				player.getInventory().getArmorContents()[2]
						.setDurability((short) (player.getInventory()
								.getArmorContents()[2].getDurability() + (short) (150 / (Integer) EasyJetpack
								.getInstance().getConfig()
								.get("tempjetpack.uses"))));
			}

			// Tells the player that the jetpack is broken
			if (player.getInventory().getArmorContents()[2].getDurability() > 150) {
				if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
						.getConfig().getString("chat.messages"))) {
					player.sendMessage(ChatColor.RED
							+ EasyJetpack
									.getInstance()
									.getConfig()
									.getString("chat.jetpackbroke")
									.replaceAll(
											"JETPACKNAME",
											player.getInventory()
													.getArmorContents()[2]
													.getType().name()
													.toLowerCase()
													.replace("_", " ")));
				}
				player.getInventory().setChestplate(ItemDetection.getAir());
			}

			// Does all the fancy effects
			Jetpack.jetpackEffect(player);
			Jetpack.damageJetpack(player);
			Jetpack.launchPlayer(player);
		} else if (player.hasPermission("easyjetpack.fly")
				&& CustomArmor.isValidItem(player.getInventory()
						.getArmorContents()[2])) {
			// Normal jetpack

			if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
					.getConfig().getString("jetpack.enabled"))
					&& ItemDetection.checkPlayerWearingJetpack(player)) {

				if ((Boolean.parseBoolean((String) EasyJetpack.getInstance()
						.getConfig().getString("fuel.enabled")) && ItemDetection
						.checkPlayerHoldingFuel(player))
						|| player.hasPermission("easyjetpack.fuelless")) {

					// Do the jetpack effects
					Jetpack.jetpackEffect(player);
					// Use fuel
					ItemDetection.useFuel(player);

					// Damage the jetpack
					if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
							.getConfig().getString("jetpack.durability"))) {
						Jetpack.damageJetpack(player);
					}

					// Throw them in the air
					Jetpack.launchPlayer(player);
				} else {
					if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
							.getConfig().getString("fuel.enabled"))) {
						// Tell the player they have no fuel
						ItemDetection.noFuel(player);
					} else {
						// Do the jetpack effects
						Jetpack.jetpackEffect(player);

						// Damage the jetpack
						if (Boolean.parseBoolean((String) EasyJetpack
								.getInstance().getConfig()
								.getString("jetpack.durability"))) {
							Jetpack.damageJetpack(player);
						}

						// Throw them in the air
						Jetpack.launchPlayer(player);
					}
				}
			}
		}
	}

	// Allow players to fly if the key is space
	public static void spaceFlyingHandler(Player player, boolean isEquiped) {
		if (player.getGameMode() != GameMode.CREATIVE
				&& ConfigHandler.getControlKey().equalsIgnoreCase("SPACE")) {
			player.setAllowFlight(isEquiped);
		} else if (!ConfigHandler.getControlKey().equalsIgnoreCase("SPACE")
				&& !isEquiped) {
			player.setAllowFlight(false);
		}
	}

	// Fall damage event
	public static boolean fallDamageEvent(Player player, int amount) {
		ConfigHandler.checkConfig();
		boolean cancel = false;
		if (player.hasPermission("easyjetpack.softlanding")
				&& ItemDetection.checkPlayerWearingBoots(player)
				&& Boolean.parseBoolean((String) EasyJetpack.getInstance()
						.getConfig().getString("boots.enabled"))
				&& CustomArmor.isValidItem(player.getInventory()
						.getArmorContents()[0])) {

			if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
					.getConfig().getString("boots.noFallDamageAtAll"))) {

				// Stop player getting fall damage
				cancel = true;
			} else if (ItemDetection.checkPlayerWearingJetpack(player)
					&& Boolean.parseBoolean((String) EasyJetpack.getInstance()
							.getConfig().getString("jetpack.noFallDamage"))) {

				// Stop player getting fall damage
				cancel = true;
			} else {
				// Stop player getting fall damage
				cancel = true;
			}

			// Confuse the player, if they take more then 5 hearts of damage
			// (Enabled in config)
			if (amount > 10
					&& Boolean.parseBoolean((String) EasyJetpack.getInstance()
							.getConfig().getString("boots.fallEffects"))) {
				PotionEffect confusion = new PotionEffect(
						PotionEffectType.CONFUSION, 140, 2);
				player.addPotionEffect(confusion, true);
			}

		}
		return cancel;
	}
}
