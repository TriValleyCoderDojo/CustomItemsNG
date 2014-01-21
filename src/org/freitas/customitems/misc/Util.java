package org.freitas.customitems.misc;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;

public class Util {

	private Util(){}

	public static String edit(String text){
		return (concatStrings(ChatColor.GOLD.toString(), "[CustomItems] ", ChatColor.WHITE.toString(), text));
	}

	public static String editConsole(String text){
		return (concatStrings("[CustomItems] ", text));
	}

	public static void itemError(String text){
		CustomItemsNG.getInstance().getLog().warning(text);
	}

	public static void itemWarning(String text){
		CustomItemsNG.getInstance().getLog().warning(text);
	}
	
	public static String concatStrings(String... props){
		StringBuilder sb = new StringBuilder();
		for (String prop : props){
			sb.append(prop);
		}
		return sb.toString();
	}
	
	public static Map<String, Boolean> getCurrentAbilities(CustomItemsNG plugin, Player player){
		// will return a hash with the set of abilities for the item that is 
		// currently held by this player
		Map<String, Boolean> abilitiesMap = new HashMap<String, Boolean>();
		if (player.getItemInHand() == null){
			return abilitiesMap;
		}
		boolean ability = false;
		for(Constants.Attributes attr : Constants.Attributes.values()){
			ability = plugin.getItemHandler().hasAbility(player.getItemInHand(), attr.getName());
			if (ability){
				abilitiesMap.put(attr.getName(), true);
			}
		}
		return abilitiesMap;
	}
	
}
