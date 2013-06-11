package com.github.jselby.easyjetpack;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler {
	// Configuration
	public static void forceDefaults(String why) throws IOException {
		// Force the defaults, for 'why' reason

		// Get the configuration from the main class
		FileConfiguration config = EasyJetpack.getInstance().getConfig();

		// Make a backup of the config
		config.save(EasyJetpack.getInstance().getDataFolder().getPath()
				+ File.separator + "backupconfig.yml");

		// Write values
		config.options().header(
				"EasyJetpack configuration file\nVersion v"
						+ EasyJetpack.pluginVersion + "\nRefer to http://dev.bukkit.org/bukkit-mods/easyjetpack/ for how to use this file.");
		config.set("jetpack.enabled", true);
		config.set("jetpack.id", 315);
		config.set("jetpack.durability", true);
		config.set("jetpack.noFallDamage", false);
		config.set("tempjetpack.enabled", false);
		config.set("tempjetpack.id", 315);
		config.set("tempjetpack.uses", 10);
		config.set("tempjetpack.noFallDamage", false);
		config.set("boots.enabled", true);
		config.set("boots.id", 301);
		config.set("boots.fallEffects", true);
		config.set("boots.noFallDamageAtAll", false);
		config.set("fuel.enabled", true);
		config.set("fuel.uses", 10);
		config.set("fuel.id", 263);
		config.set("chat.messages", true);
		config.set("chat.nofuel", "You don't have any FUELNAME.");
		config.set("chat.useallfuel", "You used all your FUELNAME.");
		config.set("chat.depletefuel", "You used up a FUELNAME.");
		config.set("chat.jetpackbroke", "Your JETPACKNAME jetpack broke!");
		config.set("controls.key", "SPACE");
		config.set("item.usecustomitems", true);

		// Tell the server owner that the configuration was changed
		EasyJetpack.getInstance().getLogger()
				.info("Your configuration file was " + why);
		EasyJetpack.getInstance().getLogger()
				.info("You will need to (re)configure it.");

		// And finally, save it
		EasyJetpack.getInstance().saveConfig();
	}

	public static String getControlKey() {
		// Gets the button for flying
		// Valid keys: SPACE, SHIFT (maybe Spout support)
		// Default: SPACE
		String key = ((String) EasyJetpack.getInstance().getConfig()
				.get("controls.key")).toUpperCase();

		if (!(key.equalsIgnoreCase("SPACE") || key.equalsIgnoreCase("SHIFT"))) {
			// Apply the default
			key = "SPACE";
		}

		return key;
	}

	public static void checkConfig() {
		// Checks if the config exists
		if (EasyJetpack.getInstance().getConfig().get("jetpack.id") == null) {
			try {
				// If it doesn't exist (plugin first being run for example),
				// create it
				forceDefaults("created");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (EasyJetpack.getInstance().getConfig()
				.get("item.usecustomitems") == null) {
			try {
				// Update the config for new versions
				forceDefaults("reset due to a update");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
