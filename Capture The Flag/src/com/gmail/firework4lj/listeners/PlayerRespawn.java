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

import com.gmail.firework4lj.main.Main;

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
		
		Location bluef = new Location(p.getWorld(), main.getConfig().getDouble("location.Xblueflagspawn"), main.getConfig().getDouble("location.Yblueflagspawn"), main.getConfig().getDouble("location.Zblueflagspawn"));
		Location redf = new Location(p.getWorld(), main.getConfig().getDouble("location.Xredflagspawn"), main.getConfig().getDouble("location.Yredflagspawn"), main.getConfig().getDouble("location.Zredflagspawn"));
		
		ItemMeta rflag = redflag.getItemMeta();
		ItemMeta bflag = blueflag.getItemMeta();
		
		if (p.getInventory().contains(blueflag)&&Main.ctfingame.containsKey(p.getName())) {
			
			rflag.setDisplayName("Redflag");
			redflag.setItemMeta(rflag);
			bflag.setDisplayName("Blueflag");
			blueflag.setItemMeta(bflag);
			
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.BLUE + "blue "+ ChatColor.GOLD + "flag!");
			}
			e.getDrops().clear();
			p.getWorld().dropItemNaturally(bluef, blueflag);
			Main.blueflag.clear();

		} else if (p.getInventory().contains(redflag)&&Main.ctfingame.containsKey(p.getName())) {
			for(String pl : Main.ctfingame.keySet()){
				main.msg(Bukkit.getPlayerExact(pl), ChatColor.RED + pn + ChatColor.GOLD+ " Has dropped the " + ChatColor.RED + "red "+ ChatColor.GOLD + "flag!");
			}
			e.getDrops().clear();
			p.getWorld().dropItemNaturally(redf, redflag);
			Main.redflag.clear();
		} else if(Main.ctfingame.containsKey(p.getName())){
			e.getDrops().clear();
		}else{
			
		}
	}
	
}
