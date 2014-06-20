package me.kyle.livechat.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import me.kyle.livechat.Main;
import me.kyle.livechat.obj.Channel;

public class ChannelManager {
	
	private static ArrayList<Channel> channels = new ArrayList<Channel>();
	
	public static void loadChannels() {
		File channelFolder = new File(Main.p.getDataFolder() + File.separator + "channels");
		
		if (!channelFolder.exists())
			channelFolder.mkdir();
		Main.l.info("Loading channels!");
		for (File f : channelFolder.listFiles()) {
			try {
				ObjectInputStream os = new ObjectInputStream(new FileInputStream(f));
				Channel c = (Channel) os.readObject();
				channels.add(c);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				Main.l.severe("Error while loading channels! " + f.getAbsolutePath() + " is not a valid channel!");
			}
		}
		Main.l.info("Loaded channels!");
	}
	
	public static void writeChannels() {
		File channelFolder = new File(Main.p.getDataFolder() + File.separator + "channels");

		for (Channel c : channels) {
			
			File f = new File(channelFolder, c.getName() + ".channel");
			try {
				ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
				os.writeObject(c);
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void addChannel(Channel c) {
		channels.add(c);
	}
	
	public static void removeChannel(Channel c) {
		if (channels.contains(c))
			channels.remove(c);
		File f = new File(Main.p.getDataFolder() + File.separator + "channels", c.getName() + ".channel");
		f.delete();
	}
	
	public static Channel getChannel(String name) {
		for (Channel c : channels)
			if (c.getName().equalsIgnoreCase(name))
				return c;
		return null;
	}
	
	public static ArrayList<Channel> getChannels() {
		return channels;
	}
	
	public static Channel getChannel(int pos) {
		return (channels.size() > pos) ? channels.get(pos) : null;
	}
	
	public static List<Channel> getChannels(Player p) {
		
		List<Channel> chan = new ArrayList<Channel>();
		for (Channel c : channels) {
			if (c.getOnlineMembers().contains(p))
				chan.add(c);
		}
		return chan;
	}
}
