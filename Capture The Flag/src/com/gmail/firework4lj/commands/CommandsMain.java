package com.gmail.firework4lj.commands;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

public class CommandsMain implements CommandExecutor{

	private Main main;
	public CommandsMain(Main Main) {
		this.main = Main;
	}
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,String[] args) {
	// Main capture the flag command handler.
		// Variables:
		Player p = (Player) sender;
		ItemStack redflag = new ItemStack(Material.WOOL, 1,DyeColor.RED.getData());
		ItemStack blueflag = new ItemStack(Material.WOOL, 1,DyeColor.BLUE.getData());
		String redName = ChatColor.RED + p.getName() + ChatColor.WHITE;
		String blueName = ChatColor.BLUE + p.getName() + ChatColor.WHITE;
		String defaultName = ChatColor.WHITE + p.getName()+ ChatColor.WHITE;
		// Locations:
		Location LobbySpawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".mains.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.z"));
		Location RedFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.z"));
		Location BlueFlag = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.z"));
		Location bluespawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".blues.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.z"));
		Location redspawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".reds.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.z"));
		
		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		if(cmd.getName().equalsIgnoreCase("ctf")){

			rflag.setDisplayName("Redflag");
			redflag.setItemMeta(rflag);
			bflag.setDisplayName("Blueflag");
			blueflag.setItemMeta(bflag);
			
			if(args.length == 0){
				p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
				p.sendMessage(ChatColor.GOLD+"Welcome to Capture the Flag version 3.0");
				p.sendMessage(ChatColor.AQUA+"To join the game, type /ctf join");
				p.sendMessage(ChatColor.AQUA+"To get a full list of commands, type /ctf help");
				p.sendMessage(ChatColor.DARK_RED+"---Capture the Flag---");
			}else if(args.length == 1){
				// Capture the flag joining handler
				
				
				if(args[0].equalsIgnoreCase("join")){
					
					
					if(Main.ctfingame.containsKey(p.getName())){
						main.msg(p, ChatColor.DARK_RED+"You are already in the game!");
					}else if(Main.ctfingame.isEmpty()){
						// Startup sequence ON FIRST PLAYER JOIN
							p.getActivePotionEffects().clear();
							Main.mainenterinv.put(p.getName(), p.getInventory().getContents());
							Main.armorenterinv.put(p.getName(), p.getInventory().getArmorContents());
							Main.xplevel.put(p.getName(), p.getExp());
							p.setGameMode(GameMode.ADVENTURE);
							p.teleport(LobbySpawn);
							p.getInventory().clear();
							p.getInventory().setHelmet(new ItemStack(Material.AIR));
							p.getInventory().setChestplate(new ItemStack(Material.AIR));
							p.getInventory().setLeggings(new ItemStack(Material.AIR));
							p.getInventory().setBoots(new ItemStack(Material.AIR));
						Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(RedFlag, redflag).setVelocity(new Vector(0D, 0D, 0D));
						Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(BlueFlag, blueflag).setVelocity(new Vector(0D, 0D, 0D));
						Main.redscore.put("red", 0);
						Main.bluescore.put("blue", 0);
						Main.ctfingame.put(p.getName(), true);
						main.msg(p, ChatColor.GREEN+ "Ctf game has started! Choose a team with /ctf "+ ChatColor.RED + "red " + ChatColor.GREEN + "or /ctf "+ ChatColor.BLUE + "blue");
						// END Game startup
					}else{
						// Startup sequence ON SECOND+ PLAYER JOIN
						p.getActivePotionEffects().clear();
						Main.mainenterinv.put(p.getName(), p.getInventory().getContents());
						Main.armorenterinv.put(p.getName(), p.getInventory().getArmorContents());
						Main.xplevel.put(p.getName(), p.getExp());
						p.setGameMode(GameMode.ADVENTURE);
						p.teleport(LobbySpawn);
						p.getInventory().clear();
						p.getInventory().setHelmet(new ItemStack(Material.AIR));
						p.getInventory().setChestplate(new ItemStack(Material.AIR));
						p.getInventory().setLeggings(new ItemStack(Material.AIR));
						p.getInventory().setBoots(new ItemStack(Material.AIR));
						Main.ctfingame.put(p.getName(), true);
						main.msg(p, ChatColor.GREEN+ "Ctf game has started! Choose a team with /ctf "+ ChatColor.RED + "red " + ChatColor.GREEN + "or /ctf "+ ChatColor.BLUE + "blue");
						// END Game startup
					}
					
					
				}else if(args[0].equalsIgnoreCase("red")){
					if(Main.ctfingame.containsKey(p.getName())){
						Main.teamblue.remove(p.getName());
						p.setGameMode(GameMode.ADVENTURE);
						main.msg(p, ChatColor.GREEN + "You are now on the "+ ChatColor.RED + "red " + ChatColor.GREEN + "team");
						p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
						Main.teamred.put(p.getName(), "red");
						main.msg(p, ChatColor.GREEN+ "Choose a class with /classes!");
						p.teleport(redspawn);
						if (p.getName().length() <= 12) {
							p.setDisplayName(redName);
							p.setPlayerListName(redName);
						} else {
							p.setPlayerListName(redName.substring(0, 13));
							p.setDisplayName(redName.substring(0, 13));
						}
					}else{
						main.msg(p, ChatColor.RED+"You must join the game first! Use /ctf join");
					}
						
						
				}else if(args[0].equalsIgnoreCase("blue")){
					
					if(Main.ctfingame.containsKey(p.getName())){
					Main.teamred.remove(p.getName());
					p.setGameMode(GameMode.ADVENTURE);
					main.msg(p, ChatColor.GREEN+"You are now on the "+ ChatColor.BLUE + "blue " + ChatColor.GREEN + "team");
					p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
					Main.teamblue.put(p.getName(), "blue");
					main.msg(p, ChatColor.GREEN+"Choose a class with /classes!");
					p.teleport(bluespawn);
					if (p.getName().length() <= 12) {
						p.setDisplayName(blueName);
						p.setPlayerListName(blueName);
					} else {
						p.setPlayerListName(blueName.substring(0, 13));
						p.setDisplayName(blueName.substring(0, 13));
					}
				}else{
					main.msg(p, ChatColor.RED+"You must join the game first! Use /ctf join");
				}
					
					
				}else if(args[0].equalsIgnoreCase("leave")){
					
					
					if(Main.ctfingame.containsKey(p.getName())){
						p.setHealth(20);
						p.setFoodLevel(20);
						Main.ctfingame.remove(p.getName());
						Main.teamred.remove(p.getName());
						Main.teamblue.remove(p.getName());
						Main.ctfclass.remove(p.getName());
						if (p.getInventory().contains(redflag)) {
							for(String pl : Main.ctfingame.keySet()){
								main.msg(Bukkit.getPlayerExact(pl), ChatColor.BLUE+ p.getName() + ChatColor.GOLD+ " has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
							}
							Main.redflag.clear();
							Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(RedFlag, redflag).setVelocity(new Vector(0D, 0D, 0D));
							} else if (p.getInventory().contains(blueflag)) {
							for(String pl : Main.ctfingame.keySet()){
								main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED+ p.getName() + ChatColor.GOLD+ " has dropped the " + ChatColor.BLUE+ "blue " + ChatColor.GOLD + "flag!");
							}
							Main.blueflag.clear();
							Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(BlueFlag, blueflag).setVelocity(new Vector(0D, 0D, 0D));
						}
						p.getInventory().setHelmet(new ItemStack(Material.AIR));
						p.getInventory().setBoots(new ItemStack(Material.AIR));
						p.getInventory().setChestplate(new ItemStack(Material.AIR));
						p.getInventory().setLeggings(new ItemStack(Material.AIR));
						p.getActivePotionEffects().clear();
						p.getInventory().clear();
	// TODO Change this next line so it brings them back to there spot before joining ctf
						p.teleport(LobbySpawn);
						p.setGameMode(GameMode.SURVIVAL);
						main.msg(p, ChatColor.GREEN+ "You have left the game");
						p.getInventory().setArmorContents(Main.armorenterinv.get(p.getName()));
						p.getInventory().setContents(Main.mainenterinv.get(p.getName()));
						p.setExp(Main.xplevel.get(p.getName()));
						p.updateInventory();
						if (p.getName().length() <= 12) {
							p.setPlayerListName(defaultName);
							p.setDisplayName(defaultName);
						} else {
							p.setPlayerListName(defaultName.substring(0, 14));
							p.setDisplayName(defaultName.substring(0, 13));
						}
						}else{
							main.msg(p, ChatColor.AQUA+"You are not in a game!");
						}
				}else if(args[0].equalsIgnoreCase("class")){
					p.performCommand("classes");
				}
				}else if(args.length == 2){
					p.sendMessage("test");
					if(args[0].equalsIgnoreCase("classcreate")){
							PlayerInventory inv = p.getInventory();
								YamlConfiguration c = new YamlConfiguration();
								c.set("Classes."+args[1]+".items", inv.getContents());
								c.set("Classes."+args[1]+".armor", inv.getArmorContents());
								try {
									c.save(new File("plugins/Capture_the_Flag/classes/"+args[1]+".yml"));
								} catch (IOException e) {
									e.printStackTrace();
								}
							main.msg(p, ChatColor.AQUA+"Class created sucessfully");
				}
			}
		}
		return false;
	}
}
