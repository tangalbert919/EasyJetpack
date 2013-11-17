package com.github.jselby.ej;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

/**
 * This class manages Jetpacks, and calls the Jetpack events when they are
 * required. Plugins should register their custom jetpacks here.
 * 
 * @author James
 * 
 */
public class JetpackManager {

	private EasyJetpack plugin;
	private static JetpackManager instance;
	private ArrayList<Jetpack> jetpacks;
	private HashMap<Runnable, Integer> runnables;

	/**
	 * A internally used constructor to create a JetpackManager instance.
	 * 
	 * @param plugin
	 *            The main Jetpack plugin
	 */
	JetpackManager(EasyJetpack plugin) {
		instance = this;
		this.plugin = plugin;

		jetpacks = new ArrayList<Jetpack>();
		runnables = new HashMap<Runnable, Integer>();

		this.plugin.getServer().getPluginManager()
				.registerEvents(new JetpackListener(), this.plugin);
	}

	/**
	 * Registers a Jetpack. It can now be created
	 * 
	 * @param jetpack
	 *            The Jetpack to add
	 */
	public void addJetpack(Jetpack jetpack) {
		jetpacks.add(jetpack);
		CraftingRecipe recipe = jetpack.getCraftingRecipe();
		if (recipe != null) {
			recipe.register();
		}
	}

	/**
	 * De-registers a Jetpack. Its custom events will no longer be invoked.
	 * 
	 * @param jetpack
	 *            The Jetpack to remove
	 */
	public void removeJetpack(Jetpack jetpack) {
		jetpacks.remove(jetpack);
	}

	/**
	 * Method called either internally or externally that passes the event onto
	 * the Jetpack which the player is wearing
	 * 
	 * @param event
	 *            A JetpackEvent containing a player who triggered the event
	 * @return
	 */
	public boolean onJetpackEvent(final JetpackEvent event) {
		Iterator<Jetpack> it = jetpacks.iterator();
		while (it.hasNext()) {
			Jetpack next = it.next();
			if ((next.getMovementType() == event.getType() || (next
					.getMovementType() == FlightTypes.CROUCH_CONSTANT && event
					.getType() == FlightTypes.CROUCH))
					&& Utils.isItemStackEqual(next.getItem(),
							Utils.getSlot(event.getPlayer(), next.getSlot()))) {
				if (!event.getPlayer().hasPermission(next.getPermission())) {
					event.getPlayer()
							.sendMessage(
									ChatColor.RED
											+ "You do not have permission to use this jetpack.");
					return false;
				}
				if (next.getMovementType() == FlightTypes.CROUCH_CONSTANT
						&& event.getType() == FlightTypes.CROUCH) {
					Runnable runnable = new Runnable() {
						@Override
						public void run() {
							if (event.getPlayer().isSneaking()) {
								boolean success = onJetpackEvent(new JetpackEvent(
										event.getPlayer(),
										FlightTypes.CROUCH_CONSTANT));
								if (!success) {
									Bukkit.getScheduler().cancelTask(
											runnables.get(this));
								}
							} else {
								Bukkit.getScheduler().cancelTask(
										runnables.get(this));
							}
						}
					};
					int id = Bukkit.getScheduler().scheduleSyncRepeatingTask(
							plugin, runnable, 2, 2);
					runnables.put(runnable, id);
					return true;
				} else {
					boolean hasFuel = next.onFuelCheckEvent(event);
					if (hasFuel) {
						next.onFuelUsageEvent(event);
						next.onFlyEvent(event);
					}
					return hasFuel;
				}
			}
		}

		return false;
	}

	/**
	 * Finds a Jetpack by it's give name
	 * 
	 * @param name
	 *            The name of the Jetpack
	 * @return A Jetpack, or null if one cannot be found
	 */
	public Jetpack getJetpackByName(String name) {
		Iterator<Jetpack> it = jetpacks.iterator();
		while (it.hasNext()) {
			Jetpack next = it.next();
			if (next.getGiveName().equalsIgnoreCase(name)) {
				return next;
			}
		}
		return null;
	}

	/**
	 * Obtains a array of all registered jetpacks
	 * 
	 * @return A Jetpack array
	 */
	public Jetpack[] getJetpacks() {
		return jetpacks.toArray(new Jetpack[jetpacks.size()]);
	}

	/**
	 * Obtains a instance of the JetpackManager, for plugins to register and
	 * modify Jetpacks
	 * 
	 * @return A instance of the JetpackManager
	 * @throws NullPointerException
	 *             Thrown if the plugin hasn't been enabled yet
	 */
	public static JetpackManager getInstance() {
		if (instance == null) {
			throw new NullPointerException(
					"Tried to obtain a JetpackManager instance when the plugin wasn't enabled!");
		}
		return instance;
	}
}
