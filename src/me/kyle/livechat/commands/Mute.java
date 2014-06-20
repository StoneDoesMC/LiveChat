package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.MessageDenyManager;

public class Mute extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			Player p = (Player) sender;
			
			if (!p.hasPermission("livechat.mute")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 0) {
				showHelp(p, label);
				return true;
			} else {
				@SuppressWarnings("deprecation")
				Player target = Bukkit.getPlayer(args[0]);
				if (target == null) {
					message(p, "§cCould not find a player named " + args[0]);
					return true;
				} else {
					if (MessageDenyManager.mutes.contains(target.getUniqueId())) {
						message(p, "§c" + args[0] + " is already muted!");
						return true;
					} else {
						MessageDenyManager.mutes.add(target.getUniqueId());
						message(p, "§c" + args[0] + " is now muted");
						message(target, "§cYou have been muted by " + p.getName());
						return true;
					}
				}
			}
		} else {
			if (args.length == 0) {
				sender.sendMessage("§cError, usage: /" + label + " <player>");
				return true;
			}
			
			@SuppressWarnings("deprecation")
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				sender.sendMessage("§cCould not find a player named " + args[0]);
				return true;
			} else {
				if (MessageDenyManager.mutes.contains(target.getUniqueId())) {
					sender.sendMessage("§c" + args[0] + " is already muted!");
					return true;
				} else {
					MessageDenyManager.mutes.add(target.getUniqueId());
					sender.sendMessage("§c" + args[0] + " is now muted");
					message(target, "§cYou have been muted by console");
					return true;
				}
			}
		}
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cError, usage: /" + label + " <player>");
	}

}
