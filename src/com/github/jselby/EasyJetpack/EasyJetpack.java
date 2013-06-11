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
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EasyJetpack extends JavaPlugin implements Listener {
	public final static double pluginVersion = 0.6;

	private static EasyJetpack me = null;

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
			ItemDetection.jetpackSearch(p);
		}

		// And print out a friendly message
		getLogger()
				.info("EasyJetpack v" + pluginVersion + " has been enabled.");
	}

	// Unload plugin
	@Override
	public void onDisable() {
		// Nothing needs to be done - just show message
		getLogger().info(
				"EasyJetpack v" + pluginVersion + " has been disabled.");
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		ItemDetection.jetpackSearch(event.getPlayer());
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

		ItemDetection.jetpackSearch(event.getPlayer());
		
		if (event.getPlayer().getAllowFlight() == false) {
			return;
		}
		
		if (event.isFlying()
				&& event.getPlayer().getGameMode() != GameMode.CREATIVE) {
			event.setCancelled(true);
			Jetpack.jetpackEvent(player);
		}
	}

	// Detect the equiping of the chestplate
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onInventoryClose(InventoryCloseEvent event) {
		//ItemDetection.jetpackSearch((Player) (event.getPlayer()));
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerMove(PlayerMoveEvent event) {
		// Damage for SPACE jetpackers
		if (event.getPlayer().getGameMode() == GameMode.CREATIVE) {
			return;
		}
		
		ItemDetection.jetpackSearch(event.getPlayer());
		
		if (event.getPlayer().getAllowFlight() == false) {
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
		if (cmd.getName().equalsIgnoreCase("easyjetpack")) {
			if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
				this.reloadConfig();
				ConfigHandler.checkConfig();
				sender.sendMessage("Reloaded config successfully!");
			} else {
				sender.sendMessage("Unknown arguments.");
			}
			return true;
		}
		return false;
	}
}
