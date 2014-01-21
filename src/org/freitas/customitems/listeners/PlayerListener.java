package org.freitas.customitems.listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;

/**
 * This class is used when a player interacts with something.  
 *
 */
public class PlayerListener implements Listener {
	
	private CustomItemsNG instance;

	public PlayerListener(CustomItemsNG plugin){
		instance = plugin;
	}
	

	/**
	 * Used when a player uses the right or left click on a block.  First check if 
	 * item in the hand is one of the projectiles and ignore those here, because 
	 * they will get handled in the EntityListener.  
	 */
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event){
		if (event.getPlayer() == null){
			return;
		}
		if (clickBlockAction(event)){
			if (event.getPlayer().getItemInHand() != null && !excludedType(event) ){
				ItemStack itemInHand = event.getPlayer().getItemInHand();
				if (instance.getItemHandler().hasAbility(itemInHand, Constants.Attributes.LIGHTNING.getName()) 
						&& instance.getItemHandler().hasPermission(event.getPlayer(), itemInHand)){
					event.getClickedBlock().getLocation().getWorld().strikeLightning(event.getClickedBlock().getLocation());
				}
				if (instance.getItemHandler().hasAbility(itemInHand, Constants.Attributes.FIRE.getName()) 
						&& instance.getItemHandler().hasPermission(event.getPlayer(), itemInHand)){
					Location loc = event.getClickedBlock().getLocation();
					loc.setY(loc.getY() + 1.0D);
					if (loc.getWorld().getBlockAt(loc).getType() == Material.AIR)
						loc.getWorld().getBlockAt(loc).setType(Material.FIRE);
				}
				if (instance.getItemHandler().hasAbility(itemInHand, Constants.Attributes.TELEPORT.getName()) 
						&& instance.getItemHandler().hasPermission(event.getPlayer(), itemInHand)){
					Location loc = event.getClickedBlock().getLocation();
					loc.setPitch(event.getPlayer().getLocation().getPitch());
					loc.setYaw(event.getPlayer().getLocation().getYaw());
					loc.setY(loc.getY() + 1.0D);
					event.getPlayer().teleport(loc);
				}
				if (instance.getItemHandler().hasAbility(itemInHand, Constants.Attributes.EXPLOSION.getName()) 
						&& instance.getItemHandler().hasPermission(event.getPlayer(), itemInHand)){
					Location loc = event.getClickedBlock().getLocation();
					loc.getWorld().createExplosion(loc, 4F);
				}
			}
		}
	}
	
	private boolean clickBlockAction(PlayerInteractEvent event) {
		if (event.getClickedBlock() == null){
			return false;
		}
		if (event.getAction() == Action.LEFT_CLICK_BLOCK){
			return true;
		}
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK){
			return true;
		}
		return false;
	}
	
	private boolean excludedType(PlayerInteractEvent event){
		if(event.getPlayer().getItemInHand().getType() == Material.BOW){
			return true;
		}
		if(event.getPlayer().getItemInHand().getType() == Material.SNOW_BALL){
			return true;
		}
		if(event.getPlayer().getItemInHand().getType() == Material.EGG){
			return true;
		}
		return false;
	}
	
	
	
}
