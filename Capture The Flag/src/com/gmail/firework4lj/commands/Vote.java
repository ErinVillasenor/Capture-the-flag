package com.gmail.firework4lj.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.firework4lj.main.Main;

	/* How this class works.
	 * Player enters '/vote'
	 * Player gets an error message telling them to retry their command.
	 * 
	 * Player enters '/vote test'
	 * A number is recorded to arena test.
	 * Every time a player enters '/vote test', another "vote" is added to the hashmap votes under test arena.
	 * 
	 * Another class takes care of choosing which arena has the highest amount of votes, and setting the new current arena.
	 */

public class Vote implements CommandExecutor{

	private Main main;
	public Vote(Main Main) {
		this.main = Main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("vote")){
			if(args.length == 1){
				if(main.getConfig().getString("arenas."+args[0]) != null){
					if(main.voted.containsKey(p.getName())){
						main.msg(p, ChatColor.DARK_RED+"You cant vote more than once!");
					}else{
						if(Main.votes.containsKey(args[0])){
							int b = Main.votes.get(args[0]);
							int a = b + 1;
							Main.votes.put(args[0], a);
						}else{
							Main.votes.put(args[0], 1);
						}
							main.voted.put(p.getName(), true);
							main.msg(p, ChatColor.AQUA+"Your vote has been cast for: "+ChatColor.GOLD+args[0]);
					}
				}else{
					main.msg(p, ChatColor.DARK_RED+"Oops! Something messed up, make sure you are typing the commands correctly!");
				}
			}else{
				main.msg(p, ChatColor.DARK_RED+"Oops! Something messed up, make sure you are typing the commands correctly!");
			}
		}
		return false;
	}
}
