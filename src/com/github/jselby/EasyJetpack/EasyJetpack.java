package com.github.jselby.EasyJetpack;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class EasyJetpack extends JavaPlugin implements Listener {
	double pluginVersion = 0.1;
	
	// Load plugin
	@Override
	public void onEnable(){ 
		getServer().getPluginManager().registerEvents(this, this);
		getLogger().info("EasyJetpack v" + pluginVersion + " has been enabled.");
	}
	
	// Unload plugin
	@Override
	public void onDisable(){ 
		getLogger().info("EasyJetpack v" + pluginVersion + " has been disabled.");
		 
	}
	
	@EventHandler
    public void onPlayerEvent(PlayerToggleSneakEvent event) {
		
		Player player = event.getPlayer();
		player.sendMessage(Integer.toString((int) player.getFallDistance()));
		if(event.getPlayer().isSneaking()){
			//if()
			Vector dir = player.getLocation().getDirection();
	          Vector vec = new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D);
	          //player.setVelocity(vec);
	          
	          player.setFallDistance((float) 0);
		} else {
			player.setFallDistance((float) 0);
		}
	}
	
}
