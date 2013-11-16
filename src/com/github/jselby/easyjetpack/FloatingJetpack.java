package com.github.jselby.easyjetpack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class FloatingJetpack implements Runnable {
	public HashMap<String, Boolean> map = null;

	public FloatingJetpack() {
		map = new HashMap<String, Boolean>();
	}

	@SuppressWarnings("deprecation")
	public void run() {
		Iterator<Entry<String, Boolean>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			try {
				Entry<String, Boolean> entry = it.next();
				Player player = Bukkit.getPlayer(entry.getKey());
				if (entry.getValue() && player != null) {
					if (!player.getInventory().contains(EasyJetpack.getInstance().getConfig().getInt("fuel.id"))) {
						player.sendMessage(ChatColor.RED + "You have run out of fuel. Switched back to burst mode.");
						invertPlayer(player);
						player.setFlying(false);
						player.setAllowFlight(false);
					} else {
						if (!ItemDetection.checkPlayerWearingJetpack(player) && !ItemDetection.checkPlayerWearingTempJetpack(player)) {
							invertPlayer(player);
						}
						Jetpack.jetpackEffect(player);
						if (Boolean.parseBoolean((String) EasyJetpack.getInstance()
								.getConfig().getString("jetpack.durability"))) {
							Jetpack.damageJetpack(player);
						}
						ItemDetection.useFuel(player, false, 1.5);
					}
				}
			} catch (Exception e) {
			}
		}
	}

	public boolean shouldBeHoldingFuel(Player player) {
		//System.out.println("Should " + player + " be holding fuel: " + !(map.containsKey(player.getName()) && map.get(player.getName())));
		if (map.containsKey(player.getName()) && map.get(player.getName())) {
			return false;
		} else {
			return true;
		}
	}

	public boolean invertPlayer(Player player) {
		if (!map.containsKey(player.getName())) {
			map.put(player.getName(), true);
		} else {
			map.put(player.getName(), !map.get(player.getName()));
		}
		return map.get(player.getName());
	}

}
