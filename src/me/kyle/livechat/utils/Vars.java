package me.kyle.livechat.utils;

import java.util.regex.Matcher;

import me.kyle.livechat.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.massivecraft.factions.entity.UPlayer;
import com.worldcretornica.plotme.Plot;
import com.worldcretornica.plotme.PlotManager;

public abstract class Vars {
	
	private static boolean plotme, factions;
	
	public static void loadPlugins() {
		plotme = Bukkit.getPluginManager().isPluginEnabled("PlotMe");
		factions = Bukkit.getPluginManager().isPluginEnabled("Factions");
	}
	
	
	public static String replaceVarsMessage(String s, Player from, Player to, String message) {
		vars(s, from, message);
		s = s.replaceAll("%FROM%", from.getName());
		s = s.replaceAll("%FROMDISPLAYNAME%", from.getDisplayName());
		s = s.replaceAll("%FROMPREFIX%", Main.chat.getPlayerPrefix(from));
		s = s.replaceAll("%TO%", to.getName());
		s = s.replaceAll("%TODISPLAYNAME%", to.getDisplayName());
		s = s.replaceAll("%TOPREFIX%", Main.chat.getPlayerPrefix(to));

		s = t(s);
		s = s.replaceAll("%MESSAGE%", Matcher.quoteReplacement(message));
		
		if (from.hasPermission("livechat.chat.format"))
			s = ColorCode.translateFormat(s);
		
		if (from.hasPermission("livechat.chat.magic"))
			s = ColorCode.translateMagic(s);
		
		if (from.hasPermission("livechat.chat.color"))
			s = ColorCode.translateColor(s);
		
		return s;
	}
	
	public static String vars(String s, Player p, String message) {
		
		s = s.replaceAll("%PREFIX%", Main.chat.getPlayerPrefix(p));

		s = s.replaceAll("%DISPLAYNAME%", p.getDisplayName());
		s = s.replaceAll("%PLAYER%", p.getName());
		
		s = s.replaceAll("%TIME%", p.getWorld().getTime() + "");
		s = s.replaceAll("%HEALTH%", (int) p.getHealth() + "");
		s = s.replaceAll("%FOOD%", p.getFoodLevel() + "");
		s = s.replaceAll("%LEVEL%", p.getExpToLevel() + "");
		s = s.replaceAll("%TOTALEXP%", p.getExp() + "");
		s = s.replaceAll("%GAMEMODE%", p.getGameMode().name().toLowerCase());
		
		if (plotme) {
			String id;
			Plot plot = PlotManager.getPlotById(p);
			if (plot == null)
				id = "none";
			else
				id = plot.id;
			
			s = s.replaceAll("%PLOTID%", id);
		}
		
		if (factions) {
			UPlayer u = UPlayer.get(p);
			s = s.replaceAll("%FACTIONNAME%", u.getFactionName());
		}
		
		s = t(s);
		s = s.replaceAll("%MESSAGE%", Matcher.quoteReplacement(message));
		
		if (p.hasPermission("livechat.chat.format"))
			s = ColorCode.translateFormat(s);
		
		if (p.hasPermission("livechat.chat.magic"))
			s = ColorCode.translateMagic(s);
		
		if (p.hasPermission("livechat.chat.color"))
			s = ColorCode.translateColor(s);
		
		return s;
	}
	
	private static String t(String s) {
		return ChatColor.translateAlternateColorCodes('&', s);
	}

}
