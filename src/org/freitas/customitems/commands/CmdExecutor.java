package org.freitas.customitems.commands;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;
import org.freitas.customitems.misc.Util;

/**
 * This class is used to handle all of the in game commands.  
 *
 */
public class CmdExecutor implements CommandExecutor {
	
	private static String USE_HELP = "Usage: /ci help";
	private CustomItemsNG instance;

	public CmdExecutor(CustomItemsNG i){
		instance = i;
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String args[]){
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command can only be run by a player.");
			return true;
		} 
		Player player = (Player)sender;
		if (args.length <= 0){
			player.sendMessage(Util.edit(USE_HELP));
			return true;
		}
		if (args[0].equalsIgnoreCase("help")){
			helpCommand(player);
		}
		else if (args[0].equalsIgnoreCase("create")){
			createCommand(player, args);
		}
		else if (args[0].equalsIgnoreCase("list")){
			listCommand(player);
		}
		else if (args[0].equalsIgnoreCase("info")){
			infoCommand(player);
		}
		else if (args[0].equalsIgnoreCase("reload")){
			reloadCommand(player);
		}
		else {
			player.sendMessage(Util.edit(USE_HELP));
		}
		return true;
	}
	
	private void helpCommand(Player player){
		if (instance.hasPermission(player, Constants.HELP_PERMS)){
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "-------------------------------------------"));
			player.sendMessage(Util.edit("Commands:"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "/ci create <item name> <player> ", ChatColor.WHITE.toString(), "- Gives custom item to the player"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "/ci info ", ChatColor.WHITE.toString(), "- Shows info about item in hand"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "/ci list ", ChatColor.WHITE.toString(), "- Lists the defined custom items"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "/ci reload ", ChatColor.WHITE.toString(), "- Reloads config and items"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "/ci help ", ChatColor.WHITE.toString(), "- Shows all commands"));
			player.sendMessage(Util.concatStrings(ChatColor.GOLD.toString(), "-------------------------------------------"));
		} 
		else {
			Util.edit(Util.concatStrings(ChatColor.RED.toString(), Constants.NO_PERMS_MSG));
		}
	}
	
	
	private void reloadCommand(Player player){
		if (instance.hasPermission(player, Constants.RELOAD_PERMS)){
			instance.getConfigDB().reload();
			player.sendMessage(Util.edit("Config/Items reloaded!"));
		}
		else {
			Util.edit(Util.concatStrings(ChatColor.RED.toString(), Constants.NO_PERMS_MSG));
		}
	}
	
	
	private void infoCommand(Player player){
		if (instance.hasPermission(player, Constants.INFO_PERMS)){
			if (player.getItemInHand() != null){
				if (instance.getItemHandler().isCustom(player.getItemInHand())){
					player.sendMessage(Util.concatStrings(ChatColor.GREEN.toString(), "List of item abilities:"));
					List<String> abilitiesList = instance.getItemHandler().getItemAbilities(player.getItemInHand());
					for (String ability : abilitiesList){
						player.sendMessage(ability);
					}
				} 
				else {
					player.sendMessage(Util.edit("You dont have a custom item in your hands!"));
				}
			} 
			else {
				player.sendMessage(Util.edit("You dont have a custom item in your hands!"));
			}
		} 
		else {
			player.sendMessage(Util.edit((new StringBuilder()).append(ChatColor.RED).append("You dont have permission!").toString()));
		}
	}
	
	
	private void listCommand(Player player){
		if (instance.hasPermission(player, Constants.LIST_PERMS)){
			player.sendMessage(Util.concatStrings(ChatColor.GREEN.toString(), "List of custom items:"));
			for (String regItem : instance.getConfigDB().getRegisteredItems()){
				player.sendMessage(Util.concatStrings(ChatColor.WHITE.toString(), regItem));
			}
		} 
		else {
			Util.edit(Util.concatStrings(ChatColor.RED.toString(), Constants.NO_PERMS_MSG));
		}
	}
	
	
	private void createCommand(Player player, String args[]){
		if (args.length < 2 || args.length > 3){
			player.sendMessage(Util.edit("Usage: /ci create <item name> <player>"));
			return;
		}
		if (instance.hasPermission(player, Constants.CREATE_PERMS)){
			if (instance.getItemHandler().getItemType().containsKey(args[1])){
				Player sendTo = player;
				if (args.length == 3){
					sendTo = Bukkit.getPlayer(args[2]);
					if (sendTo == null){
						player.sendMessage(Util.edit("Player is offline."));
						return;
					}
				}
				if (sendTo.isOnline()){
					if (sendTo.getInventory().firstEmpty() != -1){
						sendTo.getInventory().setItem(sendTo.getInventory().firstEmpty(), instance.getItemHandler().createItem(args[1]));
						player.sendMessage(Util.edit("Item has been added to player\264s inventory."));
					} 
					else {
						player.sendMessage(Util.edit("Player has full inventory."));
					}
				}
				else {
					player.sendMessage(Util.edit("Player is offline."));
				}
			} 
			else {
				player.sendMessage(Util.edit("Item does not exist!"));
			}
		} 
		else {
			Util.edit(Util.concatStrings(ChatColor.RED.toString(), Constants.NO_PERMS_MSG));
		}
	}
	
}
