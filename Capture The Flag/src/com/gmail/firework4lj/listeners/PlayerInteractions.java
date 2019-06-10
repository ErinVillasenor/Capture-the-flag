package com.gmail.firework4lj.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import com.gmail.firework4lj.main.Main;

	/* How this class works:
	 * 
	 * This class listens for all player interactions.
	 * 
	 * Block placement:
	 * While the player is in game, and if the player is not opped, the event is cancelled, causing the player to not be able to place blocks.
	 * Otherwise, they can place blocks.
	 * 
	 * Block destruction:
	 * Works the same way as block placement only it applies to block destruction and uses a separate event.
	 * 
	 * Item dropping:
	 * If the player is in game, the event is cancelled. If they are not in game, the event is not cancelled.
	 * 
	 * Inventory manipulation:
	 * If the player is in game, they are allowed to move items within there inventory except for there helmet. 
	 * Since there helmet is supposed to house the wool block showing what team they are on, it is necessary to disallow the movement of it.
	 */

public class PlayerInteractions implements Listener{

	private Main main;
	public PlayerInteractions(Main Main) {
		this.main = Main;
	}
	
	
	// BLOCK INTERACTIONS BEGIN
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		Player p = event.getPlayer();
		if (p.isOp()) {
			event.setCancelled(false);
		}else if(Main.ctfingame.containsKey(p.getName())){
			event.setCancelled(true);
		}else{
			event.setCancelled(false);
		}

	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		Player p = event.getPlayer();
		if (p.isOp()) {
			event.setCancelled(false);
		}else if(Main.ctfingame.containsKey(p.getName())){
			event.setCancelled(true);
		}else{
			event.setCancelled(false);
		}
	}
	// BLOCK INTERACTIONS END
	
	
	// INVENTORY INTERACTIONS BEGIN
	@EventHandler
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		Player p = event.getPlayer();
		if(p.isOp()) {
		}else if(Main.ctfingame.containsKey(p.getName())){
			event.setCancelled(true);
		}else{
			event.setCancelled(false);
		}
	}

	
	// CLASS SELECTION GUI
	@EventHandler
	  public void onInventoryClick(InventoryClickEvent e){
		Player p = (Player) e.getWhoClicked();
		if(Main.ctfingame.containsKey(p.getName())){
			if(e.getSlot() == 39){
				e.setCancelled(true);
			}
	    }else{
	    	e.setCancelled(false);
	    }
	  }
}
