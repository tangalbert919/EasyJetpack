package com.github.jselby.ej;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandListener implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String name, String[] args) {
		if (!sender.hasPermission("easyjetpack.admin")) {
			sender.sendMessage(ChatColor.RED
					+ "You do not have permission to run this command!");
			return true;
		}
		if (args.length == 0) {
			showHelp(sender);
			return true;
		}

		if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
			Jetpack jetpack = JetpackManager.getInstance().getJetpackByName(
					args[1].toLowerCase());
			if (jetpack == null) {
				sender.sendMessage(ChatColor.RED
						+ "No jetpack was found by that name. "
						+ "Try /ej list to find available jetpacks.");
			} else if (sender instanceof Player) {
				((Player) sender).getInventory().addItem(jetpack.getItem());
				sender.sendMessage(ChatColor.GREEN
						+ "Obtained a " + ChatColor.RESET + jetpack.getName());
			} else {
				sender.sendMessage(ChatColor.RED
						+ "You must be a player to run that command.");
			}
		} else if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
			showHelp(sender);
		} else if (args.length == 1 && args[0].equalsIgnoreCase("list")) {
			Jetpack[] jetpacks = JetpackManager.getInstance().getJetpacks();
			sender.sendMessage(ChatColor.GOLD + "===========");
			sender.sendMessage("Jetpacks:");
			for (Jetpack jetpack: jetpacks) {
				sender.sendMessage("   " + jetpack.getName());
				sender.sendMessage("         Give name: " + jetpack.getGiveName());
			}
			sender.sendMessage(ChatColor.GOLD + "===========");
		} else if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
			EasyJetpack.getInstance().reloadConfig();
			sender.sendMessage(ChatColor.GREEN + "Reloaded configuration!");
		} else {
			sender.sendMessage(ChatColor.RED
					+ "Unknown command. Try /ej help for more information.");
		}
		return true;
	}

	/**
	 * Shows a small help page to a user, or the console
	 * @param sender The player/console to send the help to
	 */
	private void showHelp(CommandSender sender) {
		sender.sendMessage(ChatColor.GOLD + "===========");
		sender.sendMessage("EasyJetpack commands:");
		sender.sendMessage("   /ej give [jetpack name]");
		sender.sendMessage("         Gives a jetpack by it's name.");
		sender.sendMessage("   /ej list");
		sender.sendMessage("         Lists available jetpacks.");
		sender.sendMessage("   /ej reload");
		sender.sendMessage("         Reloads the configuration.");
		sender.sendMessage("   /ej help");
		sender.sendMessage("         Shows this help message.");
		sender.sendMessage(ChatColor.GOLD + "===========");
	}

}
