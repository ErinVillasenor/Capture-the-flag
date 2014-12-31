package com.gmail.firework4lj.commands;

import java.io.File;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.gmail.firework4lj.main.Main;
import com.gmail.firework4lj.util.IconMenu;

public class Classes implements CommandExecutor{

	private Main Plugin;
	private Main main;
	public Classes(Main Main) {
		this.main = Main;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		final Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("classes")){
//			if(Main.ctfingame.containsKey(p.getName())){
//				IconMenu Menu = new IconMenu("           Select a Class", 27, new IconMenu.OptionClickEventHandler(){
//			        public void onOptionClick(IconMenu.OptionClickEvent event) {
//			        	main.msg(p, ChatColor.GOLD+"You have chosen " + event.getName());
//			          event.setWillClose(true);
//			        }
//			      }
//			      , this.Plugin)
//			        .setOption(0, new ItemStack(Material.DIAMOND_SWORD, 1), "§6§lSoldier", new String[] { ChatColor.GREEN+"Normal speed, basic weapons, fair health." })
//			        .setOption(1, new ItemStack(Material.BOW, 1), "§6§lSniper", new String[] { ChatColor.GREEN+"Normal speed, good shot, lower health." })
//			        .setOption(2, new ItemStack(Material.COMPASS, 1), "§6§lScout", new String[] { ChatColor.GREEN+"Fast, powerful, but low health." })
//			      	.setOption(3, new ItemStack(Material.DIAMOND_SWORD, 1), "§6§lHeavy", new String[] { ChatColor.GREEN+"Slow, Lots of health, powerful." })
//			      	.setOption(4, new ItemStack(Material.GOLD_SWORD, 1), "§6§lMedic", new String[] { ChatColor.GREEN+"Normal speed, bad weapons, lots of health, powerful healing" });
//			      Menu.open(p);
//			}else{
//				main.msg(p, ChatColor.DARK_RED+"You must join the game first! Use /ctf join");
//			}
			if(args.length == 1){
				if(YamlConfiguration.loadConfiguration(new File("plugins/ctf/classes"+args[0]+".yml")) != null){
					YamlConfiguration c = YamlConfiguration.loadConfiguration(new File("plugins/Capture_the_Flag/classes/"+args[0]+".yml"));
					main.msg(p, ChatColor.GOLD+"Class changed to: "+args[0]);
					ItemStack[] content = ((List<ItemStack>) c.get("Classes."+args[0]+".armor")).toArray(new ItemStack[0]);
					p.getInventory().setArmorContents(content);
					content = ((List<ItemStack>) c.get("Classes."+args[0]+".items")).toArray(new ItemStack[0]);
					p.getInventory().setContents(content);
					main.ctfclass.put(p.getName(), args[0]);
				}else{
					main.msg(p, ChatColor.DARK_RED+"That is not a valid class!");
				}
			}else{
				main.msg(p, ChatColor.GREEN+"Available classes are:");
				p.sendMessage(ChatColor.BLACK+"["+ChatColor.GOLD+"Ctf"+ChatColor.BLACK+"] "+ChatColor.GOLD+main.getConfig().getConfigurationSection("Classes").getKeys(false));
				main.msg(p, ChatColor.GREEN+"Please use /classes (Class Name) to choose a class");
			}
		}
		return false;
	}
}
