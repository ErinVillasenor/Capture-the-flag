package com.gmail.firework4lj.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.gmail.firework4lj.main.Main;

	/* How this class works:
	 * Player enters '/ctfsetup new test'
	 * config stores the arena name "test".
	 * Player then gets sent a message to use '/arenasetup test'
	 * (If only '/ctfsetup' is entered, player gets an error and is shown how to use command.)
	 * 
	 * Player enters '/arenasetup test'
	 * Player is then guided through a setup process where they have to stand on locations for main spawn, blue team spawn, red team spawn, red flag spawn, and blue flag spawn.
	 * All the location data is stored in the main config. Location data pulled is world, x, y, and z.
	 */

public class Setup implements CommandExecutor{

	private Main main;
	public Setup(Main Main) {
		this.main = Main;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player p = (Player) sender;
		int px = p.getLocation().getBlockX();
		int py = p.getLocation().getBlockY();
		int pz = p.getLocation().getBlockZ();
		String pw = p.getLocation().getWorld().getName();
		if(cmd.getName().equalsIgnoreCase("ctfsetup")){
			if(p.isOp()){
			if(args.length == 2){
			if(args[0].equalsIgnoreCase("new")){
			// Creating a spot for the arena data to be stored at.
			main.getConfig().set("arenas."+args[1], "1");
			main.saveConfig();
			main.msg(p, ChatColor.RED+args[1]+ChatColor.AQUA+" has been created. Please do /arenasetup "+ChatColor.RED+args[1]+ChatColor.AQUA+" to begin setting up the arena.");
			}else{
			main.msg(p, ChatColor.DARK_RED+"Incorrect syntax, please use /ctfsetup new (arena name)");
			}
			
			}else if(args.length == 1){
			main.msg(p, ChatColor.DARK_RED+"Incorrect syntax, please use /ctfsetup new (arena name)");
			}else{
			main.msg(p, ChatColor.AQUA+"Welcome to the capture the flag setup wizard!");
			main.msg(p, ChatColor.AQUA+"First, you need to create the arena with "+ChatColor.DARK_RED+"/ctfsetup new (arena name)");
			}
			}else{
				main.msg(p, ChatColor.DARK_RED+"You must be opped to use this command!");
			}
		}else if(cmd.getName().equalsIgnoreCase("arenasetup")){
			if(p.isOp()){
			if(args.length == 1){
				if(main.getConfig().getString("arenas."+args[0]) != null){
					main.msg(p, ChatColor.AQUA+"Please stand on the LOBBY SPAWN POINT for "+ChatColor.RED+args[0]);
					main.msg(p, ChatColor.AQUA+"Then, use "+ChatColor.BLUE+"/arenasetup "+args[0]+" mains");
				}else{
					main.msg(p, ChatColor.DARK_RED+"Oops! Something messed up, make sure you are typing the commands correctly!");
				}
			}else if(args.length == 2){
				
				// Recording x, y, z, and world information of spawn points.
				
				if(args[1].equalsIgnoreCase("mains") && main.getConfig().getString("arenas."+args[0]) != null){
				main.getConfig().set("arenas."+args[0]+".mains.x", px);
				main.getConfig().set("arenas."+args[0]+".mains.y", py);
				main.getConfig().set("arenas."+args[0]+".mains.z", pz);
				main.getConfig().set("arenas."+args[0]+".mains.w", pw);
				main.saveConfig();
				main.msg(p, ChatColor.AQUA+"Done, now go stand at the BLUE TEAMS SPAWN for "+ChatColor.RED+args[0]);
				main.msg(p, ChatColor.AQUA+"Then, use "+ChatColor.BLUE+"/arenasetup "+args[0]+" blues");
				
				}else if(args[1].equalsIgnoreCase("blues") && main.getConfig().getString("arenas."+args[0]) != null){
				main.getConfig().set("arenas."+args[0]+".blues.x", px);
				main.getConfig().set("arenas."+args[0]+".blues.y", py);
				main.getConfig().set("arenas."+args[0]+".blues.z", pz);
				main.getConfig().set("arenas."+args[0]+".blues.w", pw);
				main.saveConfig();
				main.msg(p, ChatColor.AQUA+"Done, now go stand at the RED TEAMS SPAWN for "+ChatColor.RED+args[0]);
				main.msg(p, ChatColor.AQUA+"Then, use "+ChatColor.BLUE+"/arenasetup "+args[0]+" reds");
				
				}else if(args[1].equalsIgnoreCase("reds") && main.getConfig().getString("arenas."+args[0]) != null){
				main.getConfig().set("arenas."+args[0]+".reds.x", px);
				main.getConfig().set("arenas."+args[0]+".reds.y", py);
				main.getConfig().set("arenas."+args[0]+".reds.z", pz);
				main.getConfig().set("arenas."+args[0]+".reds.w", pw);
				main.saveConfig();
				main.msg(p, ChatColor.AQUA+"Done, now go stand at the RED TEAMS FLAG for "+ChatColor.RED+args[0]);
				main.msg(p, ChatColor.AQUA+"Then, use "+ChatColor.BLUE+"/arenasetup "+args[0]+" redfs");
				
				}else if(args[1].equalsIgnoreCase("redfs") && main.getConfig().getString("arenas."+args[0]) != null){
				main.getConfig().set("arenas."+args[0]+".redfs.x", px);
				main.getConfig().set("arenas."+args[0]+".redfs.y", py);
				main.getConfig().set("arenas."+args[0]+".redfs.z", pz);
				main.getConfig().set("arenas."+args[0]+".redfs.w", pw);
				main.saveConfig();
				main.msg(p, ChatColor.AQUA+"Done, now go stand at the BLUE TEAMS SPAWN for "+ChatColor.RED+args[0]);
				main.msg(p, ChatColor.AQUA+"Then, use "+ChatColor.BLUE+"/arenasetup "+args[0]+" bluefs");
				}else if(args[1].equalsIgnoreCase("bluefs") && main.getConfig().getString("arenas."+args[0]) != null){
				main.getConfig().set("arenas."+args[0]+".bluefs.x", px);
				main.getConfig().set("arenas."+args[0]+".bluefs.y", py);
				main.getConfig().set("arenas."+args[0]+".bluefs.z", pz);
				main.getConfig().set("arenas."+args[0]+".bluefs.w", pw);
				main.saveConfig();
				main.msg(p, ChatColor.AQUA+"Done with setup of "+ChatColor.RED+args[0]+ChatColor.AQUA+" arena");
				main.msg(p, ChatColor.AQUA+"If you placed a spawn at the wrong place or need to move it, just use the command at that spawn again.");
				Main.currentarena.put("arena", "arenas."+args[0]);
				}else{
					main.msg(p, ChatColor.DARK_RED+"Oops! Something messed up, make sure you are typing the commands correctly!");
				}
			}else{
				main.msg(p, ChatColor.DARK_RED+"Oops! Something messed up, make sure you are typing the commands correctly!");
			}
			}else{
				main.msg(p, ChatColor.DARK_RED+"You must be opped to use this command!");
			}
		}
		return false;
	}
}
