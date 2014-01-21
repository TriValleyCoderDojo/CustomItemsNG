package org.freitas.customitems.listeners;

import java.util.Iterator;
import java.util.Random;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;


/**
 * This class is used for when a player does something with a block 
 * with one of the custom items. 
 *
 */
public class BlockListener implements Listener {
	
	private CustomItemsNG instance;

	public BlockListener(CustomItemsNG plugin){
		instance = plugin;
	}

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event){
		// first make sure it is something that makes sense to get a drop from
		if (event.getPlayer() != null && event.getBlock() != null && event.getPlayer().getItemInHand() != null){
			// next check if the player can use it
			if (instance.getItemHandler().hasAbility(event.getPlayer().getItemInHand(), Constants.Attributes.SUPER_FORTUNE.getName()) 
					&& instance.getItemHandler().hasPermission(event.getPlayer(), event.getPlayer().getItemInHand())){
				// check that the block has drops
				if (event.getBlock().getDrops() != null && !event.getBlock().getDrops().isEmpty()){
					// generate a random chance between 0 and 5
					int chance = (new Random()).nextInt(5);
					if(chance != 0 && !event.isCancelled()){
						int i = 0;
						for(i = 0; i <= chance; i++) {
							Iterator<ItemStack> dropIter = event.getBlock().getDrops().iterator();
							while (dropIter.hasNext()){
								ItemStack drop = dropIter.next();
								event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
							}
						}
					}
				}
			}
		}
	}

	@EventHandler
	public void onBlockDamageEvent(BlockDamageEvent event){
		// first make sure it is something that makes sense to break
		if (event.getPlayer() != null && event.getBlock() != null && event.getPlayer().getItemInHand() != null 
				&& event.getBlock().getType() != Material.BEDROCK){
			// next check if the player can break it
			if (instance.getItemHandler().hasAbility(event.getPlayer().getItemInHand(), Constants.Attributes.BREAK.getName()) 
					&& instance.getItemHandler().hasPermission(event.getPlayer(), event.getPlayer().getItemInHand())){
				event.setInstaBreak(true);
			}
		}
	}

}
