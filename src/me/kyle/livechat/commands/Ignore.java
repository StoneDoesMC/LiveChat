package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.MessageDenyManager;

public class Ignore extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry this command is only allowed in game");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("livechat.ignore")) {
			noPermMessage(p);
			return true;
		}
		
		if (args.length == 0) {
			showHelp(p, label);
			return true;
		} else if (args.length == 1) {
			@SuppressWarnings("deprecation")
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				message(p, "§cI could not find a player named " + args[0]);
				return true;
			} else {
				if (target == p) {
					message(p, "§cYou cannot ignore yourself!");
					return true;
				}
				
				boolean ig = MessageDenyManager.toggleIgnore(p, target);
				message(p, "§c" + target.getName() + " has been " + ((ig) ? "ignored" : "unignored"));
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cUsage, /ignore <player>");
	}

}
