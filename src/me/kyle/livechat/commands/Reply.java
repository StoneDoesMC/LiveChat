package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.MessageManager;

public class Reply extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry this command is only allowed in game");
			return true;
		}
		
		Player p = (Player) sender;
		if (!p.hasPermission("livechat.reply")) {
			noPermMessage(p);
			return true;
		}
		
		if (args.length == 0) {
			showHelp(p, label);
			return true;
		}
		
		if (MessageManager.msgs.containsKey(p.getUniqueId())) {
			Player to = Bukkit.getPlayer(MessageManager.msgs.get(p.getUniqueId()));
			if (to == null) {
				message(p, "§cYou have nobody to whom you can reply to");
				return true;
			} else {
				
				String message = "";
				for (String s : args) {
					message+= s + " ";
				}
				
				if (!p.hasPermission("livechat.message.color"))
					message = ChatColor.stripColor(message);
				
				MessageManager.sendMessage(p, to, message);
				return true;
			}
		} else {
			message(p, "§cYou have nobody to whom you can reply to");
		}
		return true;
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cError, usage: /" + label + " [message]");
	}
}
