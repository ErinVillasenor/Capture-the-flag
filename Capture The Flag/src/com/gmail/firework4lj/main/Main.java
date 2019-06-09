package com.gmail.firework4lj.main;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.gmail.firework4lj.commands.Classes;
import com.gmail.firework4lj.commands.CommandsMain;
import com.gmail.firework4lj.commands.Setup;
import com.gmail.firework4lj.commands.Vote;
import com.gmail.firework4lj.listeners.CommandPreProcess;
import com.gmail.firework4lj.listeners.ItemDespawn;
import com.gmail.firework4lj.listeners.PlayerAttack;
import com.gmail.firework4lj.listeners.PlayerInteractions;
import com.gmail.firework4lj.listeners.PlayerLeaveGame;
import com.gmail.firework4lj.listeners.PlayerPickupItem;
import com.gmail.firework4lj.listeners.PlayerRespawn;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class Main extends JavaPlugin{

	// Hashmaps:
	public final static HashMap<String, Boolean> ctfingame = new HashMap<String, Boolean>();
	public final static HashMap<String, String> teamblue = new HashMap<String, String>();
	public final static HashMap<String, String> teamred = new HashMap<String, String>();
	public final static HashMap<String, String> redflag = new HashMap<String, String>();
	public final static HashMap<String, String> blueflag = new HashMap<String, String>();
	public final static HashMap<String, Integer> redscore = new HashMap<String, Integer>();
	public final static HashMap<String, Integer> bluescore = new HashMap<String, Integer>();
	public final static HashMap<String, String> ctfclass = new HashMap<String, String>();
	public final static HashMap<String, Float> xplevel = new HashMap<String, Float>();
	public final static HashMap<String, ItemStack[]> mainenterinv = new HashMap<String, ItemStack[]>();
	public final static HashMap<String, ItemStack[]> armorenterinv = new HashMap<String, ItemStack[]>();
	public final static HashMap<String, String> currentarena = new HashMap<String, String>();
	public final static HashMap<String, Integer> votes = new HashMap<String, Integer>();
	public final static HashMap<String, Boolean> voted = new HashMap<String, Boolean>();
	public final static HashMap<String, Integer> joinx = new HashMap<String, Integer>();
	public final static HashMap<String, Integer> joiny = new HashMap<String, Integer>();
	public final static HashMap<String, Integer> joinz = new HashMap<String, Integer>();
	public final static HashMap<String, String> joinw = new HashMap<String, String>();
	
	@Override
	public void onEnable(){
		// Creating config:
		File config = new File(getDataFolder()+File.separator+"config.yml");
		if (!config.exists()){
			this.getLogger().info("Generating config.yml...");
			this.getConfig().options().copyDefaults(true);
			this.saveConfig();
			getLogger().info("Config.yml generated!");
		}
		
		// Metrics
	    try {
	        Metrics metrics = new Metrics(this);
	        metrics.start();
	        getLogger().info("Metrics enabled!");
	    } catch (IOException e) {
	        getLogger().info("Enabling of Metrics failed :(");
	    }
	    
		// Registering commands:
		getCommand("ctf").setExecutor(new CommandsMain(this));
		getCommand("ctfsetup").setExecutor(new Setup(this));
		getCommand("arenasetup").setExecutor(new Setup(this));
		getCommand("vote").setExecutor(new Vote(this));
		getCommand("classes").setExecutor(new Classes(this));
		
		// Registering listeners:
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(new PlayerPickupItem(this), this);
		pm.registerEvents(new ItemDespawn(this), this);
		pm.registerEvents(new PlayerInteractions(this), this);
		pm.registerEvents(new PlayerAttack(this), this);
		pm.registerEvents(new PlayerRespawn(this), this);
		pm.registerEvents(new CommandPreProcess(this), this);
		pm.registerEvents(new PlayerLeaveGame(this), this);
	
		// Registering default arena (1st in config)
		if (this.getConfig().getConfigurationSection("arenas") == null){	
			getLogger().info("Welcome to ctf, it appears you have not setup an arena to use yet.");
			getLogger().info("Please do this by using /ctfsetup when you are in-game.");
		}else{
				String arena = this.getConfig().getConfigurationSection("arenas").getKeys(false).iterator().next();
				Main.currentarena.put("arena", "arenas."+arena);
				getLogger().info("Arenas loaded!");
		}
		getLogger().info("Capture the Flag has been enabled.");
	}
	
	@Override
	public void onDisable(){
		getLogger().info("Executing shutdown procedures...");
		// Clean up the flags on reload
		List<Entity> entlist = Bukkit.getWorld(this.getConfig().getString(Main.currentarena.get("arena")+".redfs.w")).getEntities();
		for (Entity current : entlist) {
			if (current instanceof Item) {
				try{
				if (((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Redflag") || ((Item) current).getItemStack().getItemMeta().getDisplayName().equals("Blueflag")){
						current.remove();
				}
				} catch (NullPointerException e){
					// One of the flags is gone.
				}
			}
		}
		getLogger().info("Existing flags removed. Forcing in-game ctf players to use /ctf leave...");
	// Remove all players safely from game
		for(String pl : Main.ctfingame.keySet()){
			Bukkit.getPlayerExact(pl).performCommand("ctf leave");
		}
		getLogger().info("Players removed from game. Shutdown complete.");
}
	
	public final void msg(Player p, String msg) {
		String prefix = (ChatColor.BLACK+"["+ChatColor.GOLD+"Ctf"+ChatColor.BLACK+"]"+ChatColor.WHITE);
		p.sendMessage(prefix + " " + msg);
		
	}
}
