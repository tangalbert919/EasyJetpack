package net.jselby.ej;

import java.util.ArrayList;

import net.jselby.ej.CheatPluginAdapter.Type;
import net.jselby.ej.impl.AntiCheatImpl;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public abstract class CheatPluginAdapter {
	private static ArrayList<CheatPluginAdapter> adapters = new ArrayList<CheatPluginAdapter>();

	public abstract void exemptPlayer(Player player,
			CheatPluginAdapter.Type type);

	public abstract void unexemptPlayer(Player player,
			CheatPluginAdapter.Type type);
	
	public abstract boolean isExempted(Player player,
			CheatPluginAdapter.Type type);

	public abstract boolean isPluginEnabled();

	public static enum Type {
		FLY, NOCLIP
	}

	public static void run() {
		if (Bukkit.getServer().getPluginManager().getPlugin("AntiCheat") != null) {
			System.out.println("AntiCheat found! Hooking API...");
			adapters.add(new AntiCheatImpl());
		}
	}

	/**
	 * Calls all CheatPluginAdapters with same arguments
	 * 
	 * @param player
	 * @param type
	 */
	public static void addException(Player player, CheatPluginAdapter.Type type) {
		for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[]{})) {
			adp.exemptPlayer(player, type);
		}
	}

	/**
	 * Calls all CheatPluginAdapters with same arguments
	 * 
	 * @param player
	 * @param type
	 */
	public static void removeException(Player player,
			CheatPluginAdapter.Type type) {
		for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[]{})) {
			adp.unexemptPlayer(player, type);
		}
	}

	public static boolean exempted(Player player, CheatPluginAdapter.Type type) {
		for (CheatPluginAdapter adp : adapters.toArray(new CheatPluginAdapter[]{})) {
			if (!adp.isExempted(player, type)) {
				return false;
			}
		}
		return true;
	}
}
