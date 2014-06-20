package me.kyle.livechat.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.entity.Player;

import me.kyle.livechat.Main;

public abstract class MessageDenyManager implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static HashMap<UUID, List<UUID>> ignores = new HashMap<UUID, List<UUID>>();
	public static ArrayList<UUID> mutes = new ArrayList<UUID>();
	
	
	public static void save() {
		File f = new File(Main.p.getDataFolder(), "ignores.ignore");
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(f));
			os.writeObject(ignores);
			os.writeObject(mutes);
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void load() {
		File f = new File(Main.p.getDataFolder(), "ignores.ignore");
		
		if (!f.exists())
			return; 
		
		try {
			ObjectInputStream os = new ObjectInputStream(new FileInputStream(f));
			ignores = (HashMap<UUID, List<UUID>>) os.readObject();
			mutes = (ArrayList<UUID>) os.readObject();
			os.close();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean toggleIgnore(Player p, Player target) {
		if (ignores.containsKey(p.getUniqueId())) {
			List<UUID> ig = ignores.get(p.getUniqueId());
			
			if (ig.contains(target.getUniqueId())) {
				ig.remove(target.getUniqueId());
				ignores.put(p.getUniqueId(), ig);
				return false;
			} else {
				ig.add(target.getUniqueId());
				ignores.put(p.getUniqueId(), ig);
				return true;
			}
		} else {
			List<UUID> temp = new ArrayList<UUID>();
			temp.add(target.getUniqueId());
			ignores.put(p.getUniqueId(), temp);
			return true;
		}
	}
}
