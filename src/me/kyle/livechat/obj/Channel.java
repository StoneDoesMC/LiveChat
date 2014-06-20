package me.kyle.livechat.obj;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Channel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String name;
	private String pass;
	private UUID owner;
	private ArrayList<UUID> users = new ArrayList<UUID>();
	private String format;
	private boolean allUser = false;
	
	/**
	 * @param name - Name of channel
	 * @param pass - password of channel
	 * @param owner - owner UUID of channel
	 * @param users - Users in channel
	 */
	public Channel(String name, String pass, UUID owner) {
		this.name = name;
		this.pass = pass;
		this.owner = owner;
		users.add(owner);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public UUID getOwner() {
		return owner;
	}
	public void setOwner(UUID owner) {
		this.owner = owner;
	}
	public ArrayList<UUID> getUsers() {
		return users;
	}
	public void setUsers(ArrayList<UUID> users) {
		this.users = users;
	}
	public List<Player> getOnlineMembers() {
		List<Player> in = new ArrayList<Player>();
		for (Player p : Bukkit.getServer().getOnlinePlayers()) {
			if (users.contains(p.getUniqueId()))
				in.add(p);
		}
		return in;
	}
	public void addPlayer(Player p) {
		users.add(p.getUniqueId());
	}
	public void removePlayer(Player p) {
		if (users.contains(p.getUniqueId())) {
			users.remove(p.getUniqueId());
		}
	}
	public boolean containsMember(Player p) {
		return users.contains(p.getUniqueId());
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public boolean isAllUser() {
		return allUser;
	}
	public void setAllUser(boolean allUser) {
		this.allUser = allUser;
	}
	
}
