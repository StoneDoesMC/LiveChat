package me.kyle.livechat.manager;

import java.util.HashMap;
import java.util.UUID;

import me.kyle.livechat.utils.Vars;

import org.bukkit.entity.Player;

public class MessageManager {
	
	private static String informat, outformat, spyformat;
	
						// reply, sender
	public static HashMap<UUID, UUID> msgs = new HashMap<UUID, UUID>();
	
	public static void setFormat(String out, String in, String spy) {
		outformat = out;
		informat = in;
		MessageManager.spyformat = spy;
	}
	
	public static void sendMessage(Player from, Player to, String message) {
		String in = Vars.replaceVarsMessage(informat, from, to, message);
		String out = Vars.replaceVarsMessage(outformat, from, to, message);
		msgs.put(from.getUniqueId(), to.getUniqueId());
		msgs.put(to.getUniqueId(), from.getUniqueId());
		to.sendMessage(in);
		from.sendMessage(out);
		
		String spy = Vars.replaceVarsMessage(spyformat, from, to, message);
		for (Player p : NSABackend.getOnlineSpys()) {
			p.sendMessage(spy);
		}
	}
}
