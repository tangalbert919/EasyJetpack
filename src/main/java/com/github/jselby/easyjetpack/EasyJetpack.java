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

package com.github.jselby.easyjetpack;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyJetpack extends JavaPlugin implements Listener {
	public final static String pluginVersion = "0.6c";

	private static EasyJetpack me = null;
	public FloatingJetpack floatingjetpack = null;

	// Load plugin
	@Override
	public void onEnable() {
		// Allow external classes to obtain a instance of me
		me = this;

		// Register the events
		getServer().getPluginManager().registerEvents(this, this);

		// Deal with the config
		ConfigHandler.checkConfig();

		CustomArmor.register();

		// Check currently logged in players for a jetpack
		for (Player p : Bukkit.getOnlinePlayers()) {
			ItemDetection.jetpackSearch(p, true);
		}

		// Add the floating jetpack timer
		floatingjetpack = new FloatingJetpack();
		Bukkit.getScheduler().runTaskTimer(this, floatingjetpack,
				getConfig().getInt("floating.usagetimer"),
				getConfig().getInt("floating.usagetimer"));

		// And print out a friendly message
		getLogger()
				.info("EasyJetpack v" + pluginVersion + " has been enabled.");
	}

	// Unload plugin
	@Override
	public void onDisable() {
		// Remove the timer
		Bukkit.getScheduler().cancelTasks(this);

		getLogger().info(
				"EasyJetpack v" + pluginVersion + " has been disabled.");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		ItemDetection.jetpackSearch(event.getPlayer(), true);
	}

	// ALlows everyone else to access me
	public static EasyJetpack getInstance() {
		return me;
	}

	// When the player falls, check for boots, and if wearing configured boots,
	// stop fall damage
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerFall(EntityDamageEvent event) {
		if (event.getCause() == DamageCause.FALL
				&& event.getEntity() instanceof Player) {
			event.setCancelled(Jetpack.fallDamageEvent(
					((Player) (event.getEntity())), event.getDamage()));
		}
	}

	// Deals with the player *spamming* shift
	@EventHandler
	public void onPlayerEvent(PlayerToggleSneakEvent event) {
		if (ConfigHandler.getControlKey().equalsIgnoreCase("shift")
				&& event.getPlayer().isSneaking()) {
			Jetpack.jetpackEvent(event.getPlayer());
		}
	}

	// Space key
	@EventHandler
	public void onPlayerJump(PlayerToggleFlightEvent event) {
		// Code for this found at
		// http://forums.bukkit.org/threads/double-jump.127013/
		Player player = event.getPlayer();

		boolean isWearing = ItemDetection.jetpackSearch(event.getPlayer(),
				false);

		if (!isWearing) {
			Jetpack.revertPlayerState(event.getPlayer());
			return;
		}

		if (event.isFlying()
				&& event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			if (floatingjetpack.shouldBeHoldingFuel(player)) {
				event.setCancelled(true);
				Jetpack.jetpackEvent(player);
			}
		}
	}

	@EventHandler
	public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
		String command = event.getMessage().trim().split(" ")[0].toLowerCase()
				.substring(1);
		if (command.equalsIgnoreCase("fly")) {
			boolean check = ItemDetection.jetpackSearch(event.getPlayer(),
					false);

			if (check) {
				event.getPlayer()
						.sendMessage(
								ChatColor.RED
										+ "You cannot use /fly when you are wearing a jetpack! Take it off first.");
				event.setCancelled(true);
			} else {
				Jetpack.revertPlayerState(event.getPlayer());
			}
		}
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void onPlayerRightClicked(PlayerInteractEvent event) {
		if (event.hasItem()
				&& event.getItem().getTypeId() == CustomArmor.getJetpack()
						.getTypeId() && event.getItem().getDurability() == 1337) {
			event.setUseItemInHand(Result.DENY);

			final Player player = event.getPlayer();

			Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

				public void run() {
					if (player != null) {
						player.sendMessage(ChatColor.GREEN
								+ "Converted into a jetpack.");
						player.getInventory().setItemInHand(
								CustomArmor.getJetpack());

						// Work around a bug in Minecraft or Bukkit, whereas a
						// fake chestplate is shown on the player
						player.updateInventory();
					}
				}

			});
		}
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		// Damage for SPACE jetpackers
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}

		boolean canFly = ItemDetection.jetpackSearch(event.getPlayer(), false);

		if (!canFly) {
			return;
		}

		if (!ConfigHandler.getControlKey().equalsIgnoreCase("SPACE")) {
			event.getPlayer().setAllowFlight(false);
			return;
		}

		double playerX = event.getTo().getX();
		double playerY = event.getTo().getY();
		double playerZ = event.getTo().getZ();
		Location loc = new Location(event.getTo().getWorld(), playerX,
				playerY - 1, playerZ);
		Block block = loc.getBlock();

		Material typeId = block.getType();

		boolean isTouchingGround = ((typeId != Material.AIR)
				&& (typeId != Material.ACTIVATOR_RAIL)
				&& (typeId != Material.DEAD_BUSH) && (typeId != Material.WEB)
				&& (typeId != Material.WHEAT) && (typeId != Material.VINE)
				&& (typeId != Material.TORCH)
				&& (typeId != Material.STONE_PLATE)
				&& (typeId != Material.WOOD_PLATE)
				&& (typeId != Material.SAPLING) && (typeId != Material.SIGN)
				&& (typeId != Material.REDSTONE_TORCH_OFF)
				&& (typeId != Material.REDSTONE_TORCH_ON)
				&& (typeId != Material.REDSTONE)
				&& (typeId != Material.RED_MUSHROOM)
				&& (typeId != Material.RAILS)
				&& (typeId != Material.DETECTOR_RAIL)
				&& (typeId != Material.CROPS)
				&& (typeId != Material.BROWN_MUSHROOM)
				&& (typeId != Material.NETHER_WARTS)
				&& (typeId != Material.LEVER) && (typeId != Material.LADDER)
				&& (typeId != Material.LONG_GRASS) && (!block.isLiquid()));

		if (isTouchingGround
				&& ConfigHandler.getControlKey().equalsIgnoreCase("SPACE")) {
			double damage = ((event.getFrom().getY() - event.getTo().getY()) * 4);
			if (damage > 2) {
				if (Jetpack.fallDamageEvent(event.getPlayer(), (int) damage) == false) {
					event.getPlayer().damage((int) damage);
				}
			}
		}
	}

	// Custom items
	@EventHandler
	public void prepareItemCraftEvent(PrepareItemCraftEvent event) {
		CustomArmor.doItemCraft(event);
	}

	// Reload command
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (sender.hasPermission("easyjetpack.fly") && args.length > 0
				&& args[0].equalsIgnoreCase("switch")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Run this as a player!");
				return true;
			}
			Player player = (Player) sender;
			if (!ItemDetection.checkPlayerWearingJetpack(player)
					&& !ItemDetection.checkPlayerWearingTempJetpack(player)) {
				sender.sendMessage(ChatColor.RED + "You need to be wearing a jetpack!");
			}
			boolean state = floatingjetpack.invertPlayer(player);
			String mode = "burst. Fly by tapping "
					+ ConfigHandler.getControlKey();
			if (state) {
				mode = "creative. You can fly using creative mode controls";
			} else {
				player.setFlying(false);
				player.setAllowFlight(false);
			}
			sender.sendMessage(ChatColor.GREEN + "Switched jetpack mode to "
					+ mode + ".");
			return true;
		}
		if (!sender.hasPermission("easyjetpack.admin")) {
			sender.sendMessage(ChatColor.RED
					+ "You do not have access to these commands.");
			return true;
		}
		if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
			this.reloadConfig();
			ConfigHandler.checkConfig();
			sender.sendMessage(ChatColor.GREEN
					+ "Reloaded config successfully!");
		} else if (args.length > 1 && args[0].equalsIgnoreCase("givejetpack")) {
			int amount = 1;
			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Invalid quantity.");
				return true;
			}
			if (amount < 1) {
				sender.sendMessage(ChatColor.RED + "Invalid quantity.");
				return true;
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack jetpacks = CustomArmor.getJetpack().clone();
				jetpacks.setAmount(amount);
				player.getInventory().addItem(jetpacks);
			} else {
				sender.sendMessage(ChatColor.RED + "Run this as a player!");
			}
		} else if (args.length > 1 && args[0].equalsIgnoreCase("giveboots")) {
			int amount = 1;
			try {
				amount = Integer.parseInt(args[1]);
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Invalid quantity.");
				return true;
			}
			if (amount < 1) {
				sender.sendMessage(ChatColor.RED + "Invalid quantity.");
				return true;
			}
			if (sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack jetpacks = CustomArmor.getBoots().clone();
				jetpacks.setAmount(amount);
				player.getInventory().addItem(jetpacks);
			} else {
				sender.sendMessage(ChatColor.RED + "Run this as a player!");
			}
		} else if (args.length > 0 && args[0].equalsIgnoreCase("givejetpack")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack jetpacks = CustomArmor.getJetpack().clone();
				jetpacks.setAmount(1);
				player.getInventory().addItem(jetpacks);
			} else {
				sender.sendMessage(ChatColor.RED + "Run this as a player!");
			}
		} else if (args.length > 0 && args[0].equalsIgnoreCase("giveboots")) {
			if (sender instanceof Player) {
				Player player = (Player) sender;
				ItemStack jetpacks = CustomArmor.getBoots().clone();
				jetpacks.setAmount(1);
				player.getInventory().addItem(jetpacks);
			} else {
				sender.sendMessage(ChatColor.RED + "Run this as a player!");
			}
		} else {
			sender.sendMessage(ChatColor.BLUE + "EasyJetpack commands:");
			sender.sendMessage("/easyjetpack reload - Reloads the plugin");
			sender.sendMessage("/easyjetpack givejetpack [quantity] - Gives you jetpacks");
			sender.sendMessage("/easyjetpack giveboots [quantity] - Gives you fall boots");
		}
		return true;
	}
}
