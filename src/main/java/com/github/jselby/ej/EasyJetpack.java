package com.github.jselby.ej;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jselby.ej.impl.BurstJetpack;
import com.github.jselby.ej.impl.Fallboots;
import com.github.jselby.ej.impl.TeleportJetpack;
import com.github.jselby.ej.impl.TraditionalJetpack;

/**
 * The main EasyJetpack class. This registers the default Jetpacks, as well as
 * creates the JetpackManager.
 * 
 * @author James
 * 
 */
public class EasyJetpack extends JavaPlugin {
	private static EasyJetpack instance;

	@Override
	public void onEnable() {
		EasyJetpack.instance = this;

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}

		File config = new File(getDataFolder(), "config.yml");
		if (!config.exists()) {
			saveDefaultConfig();
		}

		// Alert the server owner to allow-flight being false
		if (!Bukkit.getAllowFlight()) {
			getLogger().warning("==========");
			getLogger().warning("allow-flight is set to false in the");
			getLogger().warning("server.properties configuration file!");
			getLogger().warning("This will mean that players will be kicked");
			getLogger().warning(
					"when using Jetpacks for a extended time period");
			getLogger().warning(
					"Consider enabling allow-flight to prevent this");
			getLogger().warning("==========");
		}

		// Register the command listener
		getServer().getPluginCommand("ej").setExecutor(new CommandListener());
		;

		// Creates a manager, which internal/external code will use
		JetpackManager manager = new JetpackManager(this);

		// Register our default Jetpacks
		if (getConfig().getBoolean("jetpacks.traditional.enabled", true))
			manager.addJetpack(new TraditionalJetpack());
		if (getConfig().getBoolean("jetpacks.burst.enabled", true))
			manager.addJetpack(new BurstJetpack());
		if (getConfig().getBoolean("jetpacks.teleport.enabled", true))
			manager.addJetpack(new TeleportJetpack());
		if (getConfig().getBoolean("jetpacks.boots.enabled", true))
			manager.addJetpack(new Fallboots());

		getLogger().info(
				"EasyJetpack (v" + getDescription().getVersion()
						+ ") has been successfully enabled!");
	}

	@Override
	public void onDisable() {
		Bukkit.getServer().resetRecipes();
	}

	/**
	 * Obtains a instance of the plugin
	 * 
	 * @return
	 */
	public static EasyJetpack getInstance() {
		return instance;
	}

	public boolean haveAllowedFlying(Player player) {
		return true;
	}
}
