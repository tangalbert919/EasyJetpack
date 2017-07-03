package main.java.me.paulvogel.easyjetpack.darknesgaming;

import main.java.me.paulvogel.easyjetpack.api.FlightTypes;
import main.java.me.paulvogel.easyjetpack.api.Jetpack;
import main.java.me.paulvogel.easyjetpack.api.JetpackEvent;
import main.java.me.paulvogel.easyjetpack.impl.CraftingRecipe;
import main.java.me.paulvogel.easyjetpack.Utils;
import main.java.me.paulvogel.easyjetpack.api.EasyJetpackAPI;
import main.java.me.paulvogel.easyjetpack.impl.HoverJetpackController;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.util.Vector;

/**
 * The fuel-efficient version of the Hover Jetpack.
 *
 * Creator: DarknesGaming
 */
public class InfinityRocketPack extends Jetpack {

    public InfinityRocketPack() {
    }
    public String getName() {
        return ChatColor.GREEN + "Infinity Rocket Pack";
    }

    public String getGiveName() {
        return "infinityrocket";
    }

    public String[] getDescription() {
        return new String[] {
                ChatColor.RESET + "This jetpack is much more fuel"
                + "efficient, and doesn't break under normal use."
        };
    }

    public Material getMaterial() {
        return Material.getMaterial(this.getConfig().getString("jetpacks.infinityrocket.material", "DIAMOND_CHESTPLATE"));
    }

    public void onFlyEvent(JetpackEvent event) {
        Vector dir = event.getPlayer().getLocation().getDirection();
        double y = event.getPlayer().getVelocity().getY();
        if(y < 1.3D) {
            y = 1.3D;
        }

        y *= 1.3D;
        if(y > 40.0D) {
            y = 40.0D;
        }

        double verticalSpeed = 7.0D;
        double horSpeed = 0.0D;
        double multiplier = 1.5D;
        boolean fixFly = false;
        if(event.getType() == FlightTypes.TIMER) {
            verticalSpeed = 5.0D;
            multiplier = 0.0D;
            horSpeed = 0.0D;
            fixFly = true;
            if(HoverJetpackController.playersToTrigger.containsKey(event.getPlayer().getName())) {
                fixFly = false;
                if(((Boolean)HoverJetpackController.playersToTrigger.get(event.getPlayer().getName())).booleanValue()) {
                    verticalSpeed = 0.9D;
                    y = -0.1D;
                } else {
                    multiplier = 1.0D;
                    horSpeed = 0.8D;
                }

                HoverJetpackController.playersToTrigger.remove(event.getPlayer().getName());
            }
        }

        Vector vector = Utils.addVector(event.getPlayer(), new Vector(dir.getX() * multiplier, y, dir.getZ() * multiplier), horSpeed, verticalSpeed, horSpeed);
        if(fixFly) {
            vector.setY(0);
        }

        event.getPlayer().setVelocity(vector);
        InfinityParticles.jetpackEffect(event.getPlayer());
    }

    public void onFuelUsageEvent(JetpackEvent event) {
        if(this.getConfig().getBoolean("fuel.enabled", true)) {
            Utils.useFuel(event.getPlayer(), false, 100.0D);
        }
    }

    public boolean onFuelCheckEvent(JetpackEvent event) {
        if(event.getType() == FlightTypes.TIMER && event.getPlayer().isOnGround()) {
            return false;
        } else if(event.getType() == FlightTypes.TIMER && EasyJetpackAPI.getManager().isCrouching(event.getPlayer())) {
            return false;
        } else if(this.getConfig().getBoolean("fuel.enabled", true)) {
            boolean containsCoal = Utils.playerHasFuel(event.getPlayer());
            if(!containsCoal) {
                event.getPlayer().sendMessage(ChatColor.RED + "You don't have enough fuel!");
            }

            return containsCoal;
        } else {
            return true;
        }
    }

    public FlightTypes getMovementType() {
        return FlightTypes.CROUCH_CONSTANT;
    }

    public CraftingRecipe getCraftingRecipe() {
        if(!this.getConfig().getBoolean("jetpacks.infinityrocket.craftable", true)) {
            return null;
        } else {
            CraftingRecipe recipe = new CraftingRecipe(this.getItem());
            recipe.setSlot(0, Material.EMERALD);
            recipe.setSlot(3, Material.ENDER_PEARL);
            recipe.setSlot(6, Material.EMERALD);
            recipe.setSlot(1, Material.DIAMOND);
            recipe.setSlot(4, Material.DIAMOND_CHESTPLATE);
            recipe.setSlot(7, Material.BEACON);
            recipe.setSlot(2, Material.EMERALD);
            recipe.setSlot(5, Material.ENDER_PEARL);
            recipe.setSlot(8, Material.EMERALD);
            return recipe;
        }
    }

    public Slot getSlot() {
        return Slot.CHESTPLATE;
    }

    public boolean isRepairingDisabled() {
        return true;
    }
    public boolean keepCalling() {
        return true;
    }
}
