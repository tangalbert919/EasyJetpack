/*
 * EasyJetpack Minecraft Bukkit plugin
 * Written by j_selby
 * 
 * Version 0.1
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

package com.github.jselby.EasyJetpack;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
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
    public void onPlayerFall(EntityDamageEvent event) {
    	if (event.getEntity().getType() == EntityType.PLAYER) {
    		Player player = (Player)event.getEntity();
        	if (player.hasPermission("easyjetpack.softlanding") && player.getInventory().getArmorContents()[0].getTypeId() == 301 && event.getCause() == EntityDamageEvent.DamageCause.FALL) {
        		if (event.getDamage() > 10){
                    PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 140, 2);
                    player.addPotionEffect(confusion, true);
        		}
        		event.setCancelled(true);
        	}
    	}
    }
	@EventHandler
    public void onPlayerEvent(PlayerToggleSneakEvent event) {
		Player player = event.getPlayer();
		if (player.isSneaking() && player.hasPermission("easyjetpack.fly") && player.getInventory().getArmorContents()[2].getTypeId() == 315){
			Vector dir = player.getLocation().getDirection();
		    Vector vec = new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D);
		    player.setVelocity(vec);
		}
	}
}
