package com.gmail.firework4lj.listeners;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import com.gmail.firework4lj.main.Main;

	/* What this class does:
	* This class is called when The player dies, and when the player respawns.
	* When the player dies, they are checked for if they were holding the flag. If they were holding the flag, the flag is destroyed along with all of there drops, and then the flag is respawned into the game at the flag spawn point.
	*/

public class PlayerRespawn implements Listener{

	private Main main;
	public PlayerRespawn(Main Main) {
		this.main = Main;
	}

	public HashMap<String, Inventory> items = new HashMap<String, Inventory>();

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		// Variables
		String pn = event.getPlayer().getName();
		Player p = event.getPlayer();
		// Locations
		Location LobbySpawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".mains.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".mains.z"));
		Location redspawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".reds.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".reds.z"));
		Location bluespawn = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".blues.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".blues.z"));

		
		if (Main.teamred.containsKey(pn)&&Main.ctfingame.containsKey(p.getName())) {
			event.setRespawnLocation(redspawn);
			p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 14));
			if(Main.ctfclass.containsKey(pn)){
			p.performCommand("classes "+Main.ctfclass.get(p.getName()));
			}else{
				
			}
		} else if (Main.teamblue.containsKey(pn)&&Main.ctfingame.containsKey(p.getName())) {
			event.setRespawnLocation(bluespawn);
			p.getInventory().setHelmet(new ItemStack(Material.WOOL, 1, (short) 11));
			if(Main.ctfclass.containsKey(pn)){
			p.performCommand("classes "+Main.ctfclass.get(p.getName()));
			}else{	
			}
		} else {
			if(Main.ctfingame.containsKey(p.getName())){
			event.setRespawnLocation(LobbySpawn);
			}else {
			}
		}
	}

	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent e) {
		Player p = e.getEntity();
		String pn = e.getEntity().getName();
		
		ItemStack redflag = new ItemStack(Material.WOOL, 1,DyeColor.RED.getData());
		ItemStack blueflag = new ItemStack(Material.WOOL, 1,DyeColor.BLUE.getData());
		
		Location redf = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".redfs.z"));
		Location bluef = new Location(Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.x"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.y"), main.getConfig().getDouble(Main.currentarena.get("arena")+".bluefs.z"));

		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		rflag.setDisplayName("Redflag");
		redflag.setItemMeta(rflag);
		bflag.setDisplayName("Blueflag");
		blueflag.setItemMeta(bflag);
		
		if (p.getInventory().contains(blueflag)&&Main.ctfingame.containsKey(p.getName())) {
			
			e.getDrops().clear();
			Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".bluefs.w")).dropItemNaturally(bluef, blueflag).setVelocity(new Vector(0D, 0D, 0D));;
			Main.blueflag.clear();
			e.setDeathMessage(null);
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.BLUE + "blue "+ ChatColor.GOLD + "flag!");
			}
		} else if (p.getInventory().contains(redflag)&&Main.ctfingame.containsKey(p.getName())) {
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
			}
			e.setDeathMessage(null);
			e.getDrops().clear();
			Bukkit.getWorld(main.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).dropItemNaturally(redf, redflag).setVelocity(new Vector(0D, 0D, 0D));
			Main.redflag.clear();
		} else if(Main.ctfingame.containsKey(p.getName())){
			e.getDrops().clear();
		}else{
			
		}
	}
	
}
