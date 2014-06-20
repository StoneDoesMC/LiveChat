package me.kyle.livechat.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import me.kyle.livechat.Main;
import me.kyle.livechat.manager.ChannelManager;
import me.kyle.livechat.manager.MessageDenyManager;
import me.kyle.livechat.obj.Channel;
import me.kyle.livechat.utils.Vars;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;

public class ChatListener implements Listener {

	public static String notPlot;
	public static int localRadius;
	public static boolean convertalltolowercase;
	public static HashMap<String, String> formats = new HashMap<String, String>();
	
	@EventHandler
	public void onPlayerAsyncChatEvent(AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		
		// check if player is muted
		if (MessageDenyManager.mutes.contains(p.getUniqueId())) {
			p.sendMessage("§cYou cannot talk while you are muted!");
		}
		
		
		Channel c = getChannel(p);
		
		// remove ignored players
		if (MessageDenyManager.ignores.containsKey(p.getUniqueId())) {
			List<UUID> ignored = MessageDenyManager.ignores.get(p.getUniqueId());
			e.getRecipients().removeAll(ignored);
		}
		
		// lowercase check
		if (convertalltolowercase)
			e.setMessage(e.getMessage().toLowerCase());
		
		// filter & whatnot
		if (c.getFormat() != null) {
			e.setFormat(Vars.vars(c.getFormat(), p, e.getMessage().replaceAll("%", "")));// will fix later :)
		}
		
		// if it is a default channel we will apply some different functions
		if (Main.defaultChannels.contains(c.getName())) {
			defaultChannel(e, c);
			return;
		}
		
		
		if (!c.isAllUser()) {
			e.getRecipients().retainAll(c.getOnlineMembers());
		}
	}
	
	private Channel getChannel(Player p) {
		List<Channel> c = ChannelManager.getChannels(p);
		if (c.isEmpty()) {
			return ChannelManager.getChannel("Global");
		} else {
			return c.get(0);
		}
	}
	
	public void defaultChannel(AsyncPlayerChatEvent e, Channel c) {
		String name = c.getName();
		if (name.equalsIgnoreCase("Global")) {
			e.getRecipients().retainAll(c.getOnlineMembers());
			return;
		} else if (name.equalsIgnoreCase("PlotMe")) {
			Plot p = PlotManager.getPlotById(e.getPlayer());
			if (p == null) {
				e.getPlayer().sendMessage(notPlot);
				return;
			} else {
				e.getRecipients().retainAll(PlotManager.getPlayersInPlot(e.getPlayer().getWorld(), p.id));
				return;
			}
		} else if (name.equalsIgnoreCase("Local")) {
			Player p = e.getPlayer();
			ArrayList<Player> nearby = new ArrayList<Player>();
			for (Entity entity : p.getNearbyEntities(localRadius, localRadius, localRadius)) {
				if (entity instanceof Player) {
					nearby.add((Player) entity);
				}
			}
			e.getRecipients().retainAll(nearby);
			return;
		}
		
	}
}
