package me.kyle.livechat.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.ChannelManager;
import me.kyle.livechat.obj.Channel;

public class PlotChat extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command is only allowed ingame");
			return true;
		}
		
		Player p = (Player) sender;
		
		for (Channel c : ChannelManager.getChannels(p)) {
			if (c.getOwner() != null && c.getOwner().equals(p.getUniqueId())) {
				message(p, "§cError, you already own another channel called " + c.getName() + " you must leave your current channel before joining another");
				return true;
			}
		}
		
		for (Channel c : ChannelManager.getChannels(p)) {
			c.removePlayer(p);
		}
		
		ChannelManager.getChannel("PlotMe").addPlayer(p);
		message(p, "§3Switched channel to PlotMe");
		return true;
	}

	@Override
	public void showHelp(Player p, String label) {
		// not required
	}

}
