package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.MessageManager;

public class Message extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (sender instanceof Player) {
			
			Player p = (Player) sender;
			
			if (!p.hasPermission("livechat.message")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 0) {
				showHelp(p, label);
				return true;
			}
			
			String value = args[0];
			if (args.length == 1) {
				message(p, "§cError, usage: /" + label + " " + value + " [message]");
				return true;
			}
			
			if (args.length > 1) {
				@SuppressWarnings("deprecation")
				Player target = Bukkit.getPlayer(value);
				
				if (target == null) {
					message(p, "§cError, could not find a player named " + value);
					return true;
				} else {
					String message = "";
					boolean first = true;
					for (String s : args) {
						if (first) {
							first = false;
							continue;
						}
						message+= s + " ";
					}
					
					if (!p.hasPermission("livechat.message.color"))
						message = ChatColor.stripColor(message);
					
					MessageManager.sendMessage(p, target, message);
					return true;
				}
			}
		} else {
			sender.sendMessage("Sorry this command is only allowed in game");
			return true;
		}
		return false;
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cError, usage: /" + label + " <player> [message]");
	}
}
