package me.kyle.livechat.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.NSABackend;

public class Spy extends Cmd {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Sorry this command is only allowed in game");
			return true;
		}
		
		Player p = (Player) sender;
		
		if (!p.hasPermission("livechat.socialspy")) {
			noPermMessage(p);
			return true;
		}
		
		if (args.length == 0) {
			addSpy(p, label);
		} else {
			@SuppressWarnings("deprecation")
			Player target = Bukkit.getPlayer(args[0]);
			if (target == null) {
				message(p, "§cCould not find a player named " + args[0]);
				return true;
			} else {
				message(p, "§cSet " + target.getName() + " spying to " + addSpy(target, label));
				return true;
			}
		}
		return false;
	}
	
	private boolean addSpy(Player p, String label) {
		if (NSABackend.spys.contains(p.getUniqueId())) {
			NSABackend.spys.remove(p.getUniqueId());
			message(p, "§cSet " + label + " to false");
			return false;
		} else {
			NSABackend.spys.add(p.getUniqueId());
			message(p, "§cSet " + label + " to true");
			return true;
		}
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cUsage, /socialspy <player>");
	}
}
