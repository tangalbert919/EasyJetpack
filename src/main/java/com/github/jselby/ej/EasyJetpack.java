package com.github.jselby.ej;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.jselby.ej.impl.BurstJetpack;
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
		
		if (!Bukkit.getAllowFlight()) {
			getLogger().warning("==========");
			getLogger().warning("allow-flight is set to false in the");
			getLogger().warning("server.properties configuration file!");
			getLogger().warning("This will mean that players will be kicked");
			getLogger().warning("when using Jetpacks for a extended time period");
			getLogger().warning("Consider enabling allow-flight to prevent this");
			getLogger().warning("==========");
		}
		
		// Creates a manager, which internal/external code will use
		JetpackManager manager = new JetpackManager(this);
		
		// Register our default Jetpacks
		manager.addJetpack(new TraditionalJetpack());
		manager.addJetpack(new BurstJetpack());
		manager.addJetpack(new TeleportJetpack());
		
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
	 * @return
	 */
	public static EasyJetpack getInstance() {
		return instance;
	}

	public boolean haveAllowedFlying(Player player) {
		return true;
	}
}
