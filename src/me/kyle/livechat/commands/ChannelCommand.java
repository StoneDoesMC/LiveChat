package me.kyle.livechat.commands;

import java.util.UUID;

import me.kyle.livechat.Cmd;
import me.kyle.livechat.manager.ChannelManager;
import me.kyle.livechat.obj.Channel;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ChannelCommand extends Cmd {

	private static boolean red = false;
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			onlyPlayerMessage(sender);
			return true;
		}
		
		Player p = (Player) sender;
		if (args.length == 0) {
			showHelp(p, label);
			return true;
		}
		
		String sub = args[0];
		if (sub.equalsIgnoreCase("create")) {// /channel
			
			if (!p.hasPermission("livechat.channel.create")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 1) {
				message(p, "§4Usage: /" + label + " create <name>");
				return true;
			} else {
				String name = args[1];
				
				if (ChannelManager.getChannel(name) != null) {// channel name is taken
					message(p, "§cError, this channel name is already in use!");
					return true;
				}
				
				for (Channel c : ChannelManager.getChannels(p)) {
					if (c.getOwner() != null && c.getOwner().equals(p.getUniqueId())) {
						message(p, "§cError, you already own another channel called " + c.getName());
						return true;
					}
				}
				
				for (Channel c : ChannelManager.getChannels(p)) {
					c.removePlayer(p);
				}
				
				ChannelManager.addChannel(new Channel(name, null, p.getUniqueId()));
				message(p, "§bCreated channel " + name);
				return true;
			}
		} else if (sub.equalsIgnoreCase("list")) {
			
			if (!p.hasPermission("livechat.channel.list")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 1) {
				showChannelList(p, 1);
				return true;
			} else {
				int page = 1;
				try {
					page = Integer.parseInt(args[1]);
				} catch (NumberFormatException e) {}
				showChannelList(p, page);
			}
		} else if (sub.equalsIgnoreCase("join")) {
			
			if (!p.hasPermission("livechat.channel.join")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 1) {
				message(p, "§cError, usage: /" + label + " join <name>");
				return true;
			} else {
				String name = args[1];
				if (ChannelManager.getChannel(name) == null) {
					message(p, "§cError, I could find a channel with that name");
					return true;
				} else {
					Channel c = ChannelManager.getChannel(name);
					
					if (c.getName().equalsIgnoreCase("Staff")) {
						if (!p.hasPermission("livechat.channel.staff")) {
							message(p, "§cYou do not have permission to join this channel!");
							return true;
						}
					}
					
					if (c.getName().equalsIgnoreCase("Admin")) {
						if (!p.hasPermission("livechat.channel.admin")) {
							message(p, "§cYou do not have permission to join this channel!");
							return true;
						}
					}
					
					if (c.containsMember(p)) {
						message(p, "§cError, you are already a member of this channel!");
						return true;
					} else {
						for (Channel chan : ChannelManager.getChannels()) {
							if (chan.containsMember(p))
								chan.removePlayer(p);
						}
						
						c.addPlayer(p);
						message(p, "§bYou have been added to " + c.getName() + "!");
						return true;
					}
				}
			}
		} else if (sub.equalsIgnoreCase("leave")) {
			
			if (!p.hasPermission("livechat.channel.leave")) {
				noPermMessage(p);
				return true;
			}
			
			if (args.length == 1) {
				int left = 0;
				for (Channel c : ChannelManager.getChannels(p)) {
					c.removePlayer(p);
					left++;
					
					if (red) {
						if (c.getOwner() != null && c.getOwner().equals(p.getUniqueId())) {
							ChannelManager.removeChannel(c);
						}
					}
				}
				
				message(p, "§9You have " + ((left == 0) ? "not left any channels" : "left " + left + " channel" + ((left > 1) ? "s" : "")));
				return true;
			}
		} else if (sub.equalsIgnoreCase("set")) {
			
			if (args.length == 1 || args.length == 2) {
				String s = "";
				if (p.hasPermission("livechat.channel.set.owner")) {
					s+="owner|";
				}
				if (p.hasPermission("livechat.channel.set.pass")) {
					s+="|pass|";
				}
				if (p.hasPermission("livechat.channel.set.name")) {
					s+="name";
				}
				s = s.replaceAll("||", "");
				message(p, "§cError, usage: /" + label + " set <channel name> <" + s + ">");
				return true;
			} else if (args.length == 3) {
				String ss = args[2];
				if (ss.equalsIgnoreCase("owner")) {
					if (p.hasPermission("livechat.channel.set.owner")) {
						message(p, "§cError, usage: /" + label + " set owner <name>");
						return true;
					} else {
						noPermMessage(p);
						return true;
					}
				} else if (ss.equalsIgnoreCase("pass")) {
					if (p.hasPermission("livechat.channel.set.pass")) {
						message(p, "§cError, usageL /" + label + " set pass <password>");
						return true;
					} else {
						noPermMessage(p);
						return true;
					}
				} else if (ss.equalsIgnoreCase("name")) {
					if (p.hasPermission("livechat.channel.set.name")) {
						message(p, "§cError, usage: /" + label + " set name <name>");
						return true;
					} else {
						noPermMessage(p);
						return true;
					}
				}
			} else if (args.length == 4) {
				String name = args[1];
				String ss = args[2];
				String value = args[3];
				Channel c = ChannelManager.getChannel(name);

				if (c == null) {// channel could not be found
					message(p, "§cCould not find channel " + name);
					return true;
				}
				
				if (ss.equalsIgnoreCase("owner")) {
					if (p.hasPermission("livechat.channel.set.owner")) {
						if (c.getOwner().equals(p.getUniqueId())
								|| p.isOp() || p.hasPermission("livechat.channel.set.admin")) {
							
							UUID id;
							
							@SuppressWarnings("deprecation")
							Player player = Bukkit.getPlayer(value);
							if (player == null) {
								@SuppressWarnings("deprecation")
								OfflinePlayer op = Bukkit.getOfflinePlayer(value);
								if (op == null) {
									message(p, "§cCould not find a player named " + value);
									return true;
								} else {
									id = op.getUniqueId();
								}
							} else {
								id = player.getUniqueId();
							}
							
							
							c.setOwner(id);
							message(p, "§bSet channel owner to " + value);
							return true;
						} else {
							noPermMessage(p);
							return true;
						}
					} else {
						noPermMessage(p);
						return true;
					}
				} else if (ss.equalsIgnoreCase("pass")) {
					if (p.hasPermission("livechat.channel.set.pass")) {
						if (c.getOwner().equals(p.getUniqueId())
								|| p.isOp() || p.hasPermission("livechat.channel.set.admin")) {
							c.setPass(value);
							message(p, "§bSet channel password to " + value);

							return true;
						} else {
							noPermMessage(p);
							return true;
						}
					} else {
						noPermMessage(p);
						return true;
					}
				} else if (ss.equalsIgnoreCase("name")) {
					if (p.hasPermission("livechat.channel.set.name")) {
						if (c.getOwner().equals(p.getUniqueId())
								|| p.isOp() || p.hasPermission("livechat.channel.set.admin")) {
							
							
							if (ChannelManager.getChannel(value) != null) {
								message(p, "§cError, that channel name is already taken");
								return true;
							}
							
							c.setName(value);
							message(p, "§bSet channel name to " + value);

							return true;
						} else {
							noPermMessage(p);
							return true;
						}
					} else {
						noPermMessage(p);
						return true;
					}
				}
			}
		} else {
			showHelp(p, label);
			return true;
		}
		
		return false;
	}

	@Override
	public void showHelp(Player p, String label) {
		message(p, "§cUsage: /" + label + " <create|list|join|leave>");
	}
	
	private void showChannelList(Player p, int page) {
		int min = (page * 5) - 5;
		int max = page * 5;
		int size = ChannelManager.getChannels().size();
		int pages = (size / 5) + ((size % 5 == 0) ? 0 : 1);
		
		if (pages == 0) {
			message(p, "§cError, no channels have been created yet");
			return;
		}
		
		message(p, "§9Channels page (" + page + "/" + pages + ")");
		for (int i = min;i<max;i++) {
			
			if (size <= i)
				break;
			
			Channel c = ChannelManager.getChannel(i);
			
			Player[] onlinePlayers = c.getOnlineMembers().toArray(new Player[c.getOnlineMembers().size()]);
			StringBuilder online = new StringBuilder();
			
			if (onlinePlayers.length == 0) {
				online.append("[]");
			} else {
				for (Player player : onlinePlayers) {
					online.append(player.getName() + ", ");
				}
			}
			message(p, "§9" + c.getName());
			message(p, "§b -> Online members: " + 
			(online.toString().contains(",") ? 
					online.toString().substring(0, online.toString().lastIndexOf(',')) 
					: 
					online.toString()));
		}
	}
	
	public static void setRed(boolean b) {
		red = b;
	}
}
