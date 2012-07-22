/*
 * EasyJetpack Minecraft Bukkit plugin
 * Written by j_selby
 * 
 * Version 0.3
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

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public class EasyJetpack extends JavaPlugin implements Listener {
	double pluginVersion = 0.3;
	
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
	
	public void setDefaults() {
		this.getConfig().set("jetpack.enabled", true);
		this.getConfig().set("jetpack.id", 315);
		this.getConfig().set("jetpack.durability", true);
		this.getConfig().set("boots.enabled", true);
		this.getConfig().set("boots.id", 301);
		this.getConfig().set("boots.fallEffects", true);
		this.getConfig().set("fuel.enabled", true);
		this.getConfig().set("fuel.uses", 10);
		this.getConfig().set("fuel.id", 263);
		this.getConfig().set("chat.messages", true);
		this.saveConfig();
	}
	
	public void checkConfig() {
		if (this.getConfig().get("jetpack.id") == null){
			setDefaults();
		}
	}
	
	public void playEffect(Effect e, Location l, int num)
	{
	    for (int i = 0; i < this.getServer().getOnlinePlayers().length; i++)
	    	this.getServer().getOnlinePlayers()[i].playEffect(l, e, num);
	}

    @EventHandler
    public void onPlayerFall(EntityDamageEvent event) {
    	checkConfig();
    	if (event.getEntity().getType() == EntityType.PLAYER) {
    		Player player = (Player)event.getEntity();
        	if (player.hasPermission("easyjetpack.softlanding") && player.getInventory().getArmorContents()[0].getTypeId() == (int) this.getConfig().get("boots.id") && event.getCause() == EntityDamageEvent.DamageCause.FALL && (boolean) this.getConfig().get("boots.enabled")) {
        		if (event.getDamage() > 10 && (boolean) this.getConfig().get("boots.fallEffects")){
                    PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 140, 2);
                    player.addPotionEffect(confusion, true);
        		}
        		event.setCancelled(true);
        	}
    	}
    }
    
    public void damageJetpack(Player player){
		player.getInventory().getArmorContents()[2].setDurability((short) (player.getInventory().getArmorContents()[2].getDurability()+1));
		if (player.getInventory().getArmorContents()[2].getDurability() > 150){
    		if ((boolean) this.getConfig().get("chat.messages")){
    			player.sendMessage(ChatColor.RED + "Your jetpack broke!");
    		}
			player.getInventory().setChestplate(getAir());
		}
    }
    
    public ItemStack getAir(){
    	ItemStack air = new ItemStack(Material.AIR);
    	return air;
    }
    
    public void jetpackEffect(Player player){
    	playEffect(Effect.SMOKE, player.getLocation(), 256);
    	playEffect(Effect.SMOKE, player.getLocation(), 256);
    	playEffect(Effect.GHAST_SHOOT, player.getLocation(), 1);
    }
    
    public void launchPlayer(Player player){
    	Vector dir = player.getLocation().getDirection();
    	Vector vec = new Vector(dir.getX() * 0.8D, 0.8D, dir.getZ() * 0.8D);
    	player.setVelocity(vec);
    }
    
    public void useFuel(Player player){
    	if (!player.hasPermission("easyjetpack.fuelless")){
    		if (player.getItemInHand().getDurability() > 100){
   				if (player.getItemInHand().getAmount() == 1){
   					if ((boolean) this.getConfig().get("chat.messages")){
   						player.sendMessage("You used all your " + player.getItemInHand().getType().name().toLowerCase().replace("_", " ") + ".");
   					}
            		player.setItemInHand(getAir());
            	} else {
            		player.getItemInHand().setAmount(player.getItemInHand().getAmount()-1);
            		player.getItemInHand().setDurability((short) 0);
            		if ((boolean) this.getConfig().get("chat.messages")){
            			player.sendMessage("You used up a " + player.getItemInHand().getType().name().toLowerCase().replace("_", " ") + ".");
            		}
            	}
   			} else {
   				player.getItemInHand().setDurability((short) ((short) player.getItemInHand().getDurability() + (100/((int) this.getConfig().get("fuel.uses") - 1))));
   			}
    	}
    }
    
    public void noFuel(Player player){
		ItemStack fuel = new ItemStack((int)this.getConfig().get("fuel.id")); 
		if ((boolean) this.getConfig().get("chat.messages")){
			player.sendMessage(ChatColor.RED + "You don't have any " + fuel.getType().name().toLowerCase().replace("_", " ") + ".");
		}
	}
        
	@EventHandler
    public void onPlayerEvent(PlayerToggleSneakEvent event) {
		checkConfig();
		Player player = event.getPlayer();
    	
		if (player.isSneaking() && player.hasPermission("easyjetpack.fly")){
			if ((boolean) this.getConfig().get("jetpack.enabled") && player.getInventory().getArmorContents()[2].getTypeId() == (int) this.getConfig().get("jetpack.id"))
			{
				if (player.getItemInHand().getTypeId() == (int) this.getConfig().get("fuel.id") && (boolean) this.getConfig().get("fuel.enabled"))
				{
					jetpackEffect(player);
					useFuel(player);
					if((boolean) this.getConfig().get("jetpack.durability")){
						damageJetpack(player);
					}
					launchPlayer(player);
				} else {
					if ((boolean) this.getConfig().get("fuel.enabled")){
						noFuel(player);
					} else {
						jetpackEffect(player);
						if((boolean) this.getConfig().get("jetpack.durability")){
							damageJetpack(player);
						}
						launchPlayer(player);
					} 
				}
            }
		}
	}
}
