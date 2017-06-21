package main.java.me.paulvogel.easyjetpack;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import main.java.me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import main.java.me.paulvogel.easyjetpack.impl.BurstJetpack;
import main.java.me.paulvogel.easyjetpack.impl.Fallboots;
import main.java.me.paulvogel.easyjetpack.impl.HoverJetpack;
import main.java.me.paulvogel.easyjetpack.impl.HoverJetpackController;
import main.java.me.paulvogel.easyjetpack.impl.TeleportJetpack;
import main.java.me.paulvogel.easyjetpack.impl.TraditionalJetpack;

/**
 * The main EasyJetpack class. This registers the default Jetpacks, as well as
 * creates the JetpackManager.
 *
 * @author James
 */
public class EasyJetpack extends JavaPlugin {
    private static EasyJetpack instance;

    /**
     * Obtains a instance of the plugin
     *
     * @return A EasyJetpack instance
     */
    public static EasyJetpack getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        EasyJetpack.instance = this;

        if (!getDataFolder().exists()) {
            boolean success = getDataFolder().mkdir();
            if (!success) {
                getLogger().warning("Failed to create plugin directory!");
            }
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
            getLogger().warning("when using Jetpacks for a extended time period");
            getLogger().warning("Consider enabling allow-flight to prevent this");
            getLogger().warning("==========");
        }

        // Register the command listener
        getServer().getPluginCommand("ej").setExecutor(new CommandListener());

        // Creates a manager, which internal/external code will use
        JetpackManager manager = new JetpackManager(this);

        // Register our default Jetpacks
        if (getConfig().getBoolean("jetpacks.traditional.enabled", true))
            manager.addJetpack(new TraditionalJetpack());
        if (getConfig().getBoolean("jetpacks.burst.enabled", true))
            manager.addJetpack(new BurstJetpack());
        if (getConfig().getBoolean("jetpacks.teleport.enabled", true))
            manager.addJetpack(new TeleportJetpack());
        if (getConfig().getBoolean("jetpacks.rocket.enabled", true)) {
            manager.addJetpack(new HoverJetpack());
            manager.addJetpack(new HoverJetpackController());
        }
        if (getConfig().getBoolean("jetpacks.boots.enabled", true))
            manager.addJetpack(new Fallboots());
        
        // Everything created by other companies.

        // Make the API ready for use
        new EasyJetpackAPI(manager);

        // Load Metrics

        // TODO Add new metrics here

        /**
         * Anti-Cheat plugin InfinityJetpacks can hook up to:
         * AntiCheatReloaded, NoCheatPlus
         */
        try {
            CheatPluginAdapter.run();
        } catch (Exception ignored) {
        }

        getLogger().info("InfinityJetpacks (v" + getDescription().getVersion() + ") has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        Bukkit.getServer().resetRecipes();
    }

    /**
     * Checks if EasyJetpack has allowed a player to fly
     *
     * @param player The player to check
     * @return If the player has been allowed to fly
     */
    public boolean haveAllowedFlying(Player player) {
        // TODO: Add compatibility with Essentials and other /fly plugins
        return true;
    }
}
