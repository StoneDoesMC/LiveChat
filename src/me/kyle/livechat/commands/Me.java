package me.kyle.livechat.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.ChannelManager;
import me.kyle.livechat.obj.Channel;
import me.kyle.livechat.utils.Vars;

public class Me extends Cmd {

	public static String format;
	public static boolean meToChannel = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry this command is only allowed in game");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (args.length == 0) {
			showHelp(p, label);
			return true;
		} else {
			String message = "";
			for (String s : args) {
				message+= s + " ";
			}
			String send = Vars.vars(format, p, message);
			if (meToChannel) {
				for (Player player : getChannel(p).getOnlineMembers()) {
					player.sendMessage(send);
				}
				return true;
			} else {
				Bukkit.broadcastMessage(send);
				return true;
			}
		}
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cError, usage: /" + label + " [message]");
	}
	
	private Channel getChannel(Player p) {
		List<Channel> c = ChannelManager.getChannels(p);
		if (c.isEmpty()) {
			return ChannelManager.getChannel("Global");
		} else {
			return c.get(0);
		}
	}

}
