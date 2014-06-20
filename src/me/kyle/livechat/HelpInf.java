package me.kyle.livechat;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public interface HelpInf {
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
	public void showHelp(Player p, String label);
}
