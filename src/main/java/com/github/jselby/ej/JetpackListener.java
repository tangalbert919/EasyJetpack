package com.github.jselby.ej;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class JetpackListener implements Listener {
	@EventHandler
	public void onPlayerToggleCrouchEvent(PlayerToggleSneakEvent evt) {
		if (evt.isSneaking()) {
			JetpackManager.getInstance().onJetpackEvent(
					new JetpackEvent(evt.getPlayer(), FlightTypes.CROUCH));
		}
	}

	@EventHandler
	public void onFlightToggleEvent(PlayerToggleFlightEvent evt) {
		if (evt.getPlayer().getGameMode() != GameMode.CREATIVE
				&& EasyJetpack.getInstance().haveAllowedFlying(evt.getPlayer())) {
			if (!evt.isFlying()) {
				JetpackManager.getInstance().onJetpackEvent(
						new JetpackEvent(evt.getPlayer(), FlightTypes.CROUCH));
			}
		}
	}
}
