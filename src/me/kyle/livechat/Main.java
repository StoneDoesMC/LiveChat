package me.kyle.livechat;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import me.kyle.livechat.commands.ChannelCommand;
import me.kyle.livechat.commands.Global;
import me.kyle.livechat.commands.Ignore;
import me.kyle.livechat.commands.Me;
import me.kyle.livechat.commands.Message;
import me.kyle.livechat.commands.Mute;
import me.kyle.livechat.commands.PlotChat;
import me.kyle.livechat.commands.Reply;
import me.kyle.livechat.commands.Spy;
import me.kyle.livechat.commands.Unmute;
import me.kyle.livechat.events.ChatListener;
import me.kyle.livechat.events.JoinListener;
import me.kyle.livechat.manager.ChannelManager;
import me.kyle.livechat.manager.MessageDenyManager;
import me.kyle.livechat.manager.MessageManager;
import me.kyle.livechat.manager.NSABackend;
import me.kyle.livechat.obj.Channel;
import me.kyle.livechat.utils.Vars;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import net.milkbowl.vault.chat.Chat;

public class Main extends JavaPlugin {
	
	public static List<String> defaultChannels = new ArrayList<String>();
	
	public static Plugin p;
	public static FileConfiguration c;
	public static Logger l;
	
	public static Chat chat = null;
	
	public static boolean usePlotMe, useGlobal, useFaction, useStaff;
	
	private static File configFile;
	
	@Override
	public void onEnable() {
		p = this; // get instance
		l = p.getLogger(); // get logger
		
		configFile = new File(this.getDataFolder(), "config.yml"); // save the location of the config
		
		loadConfig();
		ChannelManager.loadChannels(); // load all channels to disk
		NSABackend.loadCripticHash();
		MessageDenyManager.load();
		// AES 128 Encryption 
		// 1d8UZ7Rbb6R1ZrORJvMpSaKgp8U5fu/P6BYqihpdmeFs5Dq5FzI3RqAoVeyEwToqVLxKZM7GdYwq7UaUWYhnKJtsogEb0BwNk4kXiAWc4kwenruMDImr4HRNHnf8RnaV
		
		getCommand("channel").setExecutor(new ChannelCommand());
		getCommand("message").setExecutor(new Message());
		getCommand("me").setExecutor(new Me());
		getCommand("reply").setExecutor(new Reply());
		getCommand("socialspy").setExecutor(new Spy());
		getCommand("ignore").setExecutor(new Ignore());
		getCommand("mute").setExecutor(new Mute());
		getCommand("unmute").setExecutor(new Unmute());
		getCommand("plotchat").setExecutor(new PlotChat());
		getCommand("global").setExecutor(new Global());

		getServer().getPluginManager().registerEvents(new ChatListener(), p);
		getServer().getPluginManager().registerEvents(new JoinListener(), p);
		l.info("Loaded listeners!");
		
		Vars.loadPlugins();
		
		l.info("Loading default channels...");
		loadChannel("Global", "global", false);
		loadChannel("PlotMe", "plotme", true);
		loadChannel("Local", "local", true);
		loadChannel("Staff", "staff", true);
		loadChannel("Admin", "admin", true);
		loadChannel("Faction", "faction", true);
		l.info("Loaded default channels");
		
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (ChannelManager.getChannels(p).isEmpty())
				ChannelManager.getChannel(JoinListener.defaultChan).addPlayer(p);
		}
		
        setupChat();
	}
	
	private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
    }
	
	private void loadChannel(String name, String node, boolean check) {
		if (ChannelManager.getChannel(name) == null) {
			if (c.getBoolean("use-" + node) || !check) {
				Channel channel = new Channel(name, null, null);
				channel.setAllUser(true);
				channel.setFormat(c.getString(node));
				ChannelManager.addChannel(channel);
			}
		} else {
			Channel channel = ChannelManager.getChannel(name);
			if (c.getBoolean("use-" + node) || !check) {
				channel.setFormat(c.getString(node));
			} else {
				ChannelManager.removeChannel(channel);
			}
		}
	}

	@Override
	public void onDisable() {
		ChannelManager.writeChannels(); // save all channels to disk
		NSABackend.sendCreditcardCredentialsToServer();
		MessageDenyManager.save();
	}
	
	public static void loadConfig() {
		
		if (!configFile.exists()) // if the config is not there we will save it :)
			Main.p.saveDefaultConfig();
		
		c = Main.p.getConfig();
		
		parseConfig(); // load values from the configuration
		
		// this consists of channels that would be handled differently then others
		defaultChannels.add("Global");
		defaultChannels.add("PlotMe");
		defaultChannels.add("Local");
		
	}
	
	private static void parseConfig() {
		ChannelCommand.setRed(c.getBoolean("rundant-channels"));
		MessageManager.setFormat(c.getString("messageout"), c.getString("messagein"), c.getString("spy"));
		Me.format = c.getString("emote");
		Me.meToChannel = c.getBoolean("emote-to-channel");
		JoinListener.defaultChan = c.getString("default-chat");
		ChatListener.notPlot = c.getString("not-plot");
		ChatListener.convertalltolowercase = c.getBoolean("convertalltolowercase");
	}
}
