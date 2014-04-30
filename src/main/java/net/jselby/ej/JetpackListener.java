package net.jselby.ej;

import net.jselby.ej.api.EasyJetpackAPI;
import net.jselby.ej.api.FlightTypes;
import net.jselby.ej.api.JetpackEvent;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class JetpackListener implements Listener {
	@EventHandler
	public void onPlayerToggleCrouchEvent(PlayerToggleSneakEvent evt) {
		if (evt.isSneaking()) {
			JetpackEvent event = new JetpackEvent(evt.getPlayer(),
					FlightTypes.CROUCH, null);
			EasyJetpackAPI.getManager().onJetpackEvent(event);
			evt.setCancelled(event.isCancelled());
		}
	}

	@EventHandler
	public void onFlightToggleEvent(PlayerToggleFlightEvent evt) {
		if (evt.getPlayer().getGameMode() != GameMode.CREATIVE
				&& EasyJetpack.getInstance().haveAllowedFlying(evt.getPlayer())) {
			if (!evt.isFlying()) {
				JetpackEvent event = new JetpackEvent(evt.getPlayer(),
						FlightTypes.CROUCH, null);
				EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
			}
		}
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent evt) {
		if (evt.getEntity() instanceof Player) {
			if (evt.getCause() == DamageCause.FALL) {
				JetpackEvent event = new JetpackEvent(
						((Player) evt.getEntity()), FlightTypes.FALLING, null);
				EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
			}
			if (evt.getCause() == DamageCause.DROWNING) {
				JetpackEvent event = new JetpackEvent(
						((Player) evt.getEntity()), FlightTypes.DROWNING, null);
				EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
			}
		}
	}

	@EventHandler
	public void playerRenameItem(InventoryClickEvent evt) {
		if (evt.getView().getType() == InventoryType.ANVIL) {
			if (evt.getRawSlot() == 2) {
				if (EasyJetpackAPI.getManager().isJetpack(
						evt.getView().getItem(0))
						|| EasyJetpackAPI.getManager().isJetpack(
								evt.getView().getItem(1))) {
					JetpackEvent event = new JetpackEvent(
							(Player) evt.getWhoClicked(), FlightTypes.ANVIL,
							evt.getView().getItem(0));
					EasyJetpackAPI.getManager().onJetpackEvent(event);
                    if (event.isCancelled()) {
                        evt.setCancelled(true);
                    }
				}
			}
		}
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent evt) {
		JetpackEvent event = new JetpackEvent(evt.getPlayer(),
				FlightTypes.INTERACT, null);
		EasyJetpackAPI.getManager().onJetpackEvent(event);
        if (event.isCancelled()) {
            evt.setCancelled(true);
        }
	}

	@EventHandler
	public void onPlayerAttacked(EntityDamageByEntityEvent evt) {
		if (evt.getEntity() instanceof Player) {
			if (evt.getDamager() instanceof Player) {
				JetpackEvent event = new JetpackEvent((Player) evt.getEntity(),
						FlightTypes.DAMAGED_BY_PLAYER, null);
				EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
			} else {
				JetpackEvent event = new JetpackEvent((Player) evt.getEntity(),
						FlightTypes.DAMAGED_BY_MOB, null);
				EasyJetpackAPI.getManager().onJetpackEvent(event);
                if (event.isCancelled()) {
                    evt.setCancelled(true);
                }
			}
		}
	}
}
