package me.kyle.livechat;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Cmd implements HelpInf, CommandExecutor {
	
	public void message(Player p, String message) {
		p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
	}
	
	public void noPermMessage(Player p) {
		p.sendMessage("§4You do not have permission to execute this command");
	}
	
	public void onlyPlayerMessage(CommandSender sender) {
		sender.sendMessage("This command is for players only!");
	}
}
