package com.github.jselby.ej;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * A event thrown when a player attempts to activate a Jetpack
 * 
 * @author James
 * 
 */
public class JetpackEvent implements Cancellable {
	private Player player;
	private FlightTypes type;
	private boolean cancelled;

	/**
	 * Creates a jetpack event, assuming that the provided player should be
	 * thrown up
	 * 
	 * @param player
	 *            The player in question
	 * @param type
	 *            The type of event
	 */
	public JetpackEvent(Player player, FlightTypes type) {
		this.player = player;
		this.type = type;
	}

	/**
	 * Returns the player that belongs to this event
	 * 
	 * @return A Bukkit player
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * Returns the type of event that was called
	 * 
	 * @return A FlightTypes enum type
	 */
	public FlightTypes getType() {
		return type;
	}

	@Override
	public boolean isCancelled() {
		return cancelled;
	}

	@Override
	public void setCancelled(boolean isCancelled) {
		cancelled = isCancelled;
	}
}
