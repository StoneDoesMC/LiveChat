package me.kyle.livechat.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

import me.kyle.livechat.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class NSABackend implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static ArrayList<UUID> spys = new ArrayList<UUID>();
	
	@SuppressWarnings("unchecked")
	public static void loadCripticHash() {
		File file = new File(Main.p.getDataFolder(), "spys.spy");
		
		if (!file.exists())
			return;
		
		try {
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(file));
			spys = (ArrayList<UUID>) os.readObject();
			os.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static void sendCreditcardCredentialsToServer() {
		File file = new File(Main.p.getDataFolder(), "spys.spy");
		ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(new FileOutputStream(file));
			os.writeObject(spys);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static ArrayList<Player> getOnlineSpys() {
		ArrayList<Player> players = new ArrayList<Player>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (spys.contains(p.getUniqueId())) {
				players.add(p);
			}
		}
		return players;
	}
}
