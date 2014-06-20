package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.MessageDenyManager;

public class Unmute extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if (!p.hasPermission("livechat.unmute")) {
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
						MessageDenyManager.mutes.remove(target.getUniqueId());
						message(p, "§c" + args[0] + " has been unmuted!");
						message(target, "§cYou have been unmuted by " + p.getName());
						return true;
					} else {
						message(p, "§c" + args[0] + " is not muted");
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
					MessageDenyManager.mutes.remove(target.getUniqueId());
					sender.sendMessage("§c" + args[0] + " is now unmuted");
					message(target, "§cYou have been unmuted by console");
					return true;
				} else {
					sender.sendMessage("§c" + target.getName() + " is not muted");
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
