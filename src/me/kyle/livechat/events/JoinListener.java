package me.kyle.livechat.events;

import me.kyle.livechat.manager.ChannelManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
	
	public static String defaultChan;
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if (ChannelManager.getChannels(p).isEmpty()) {
			ChannelManager.getChannel(defaultChan).addPlayer(p);
		}
	}
}
