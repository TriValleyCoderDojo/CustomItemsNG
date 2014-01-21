package org.freitas.customitems.data;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;
import org.freitas.customitems.misc.Util;
import org.freitas.customitems.objects.CustomID;

/**
 * This class is used to validate the contents of the YAML config 
 * files.  It checks to see if there are any error in how the YAML
 * has been entered, and rejects any items that it does not know
 * how to process.  
 *
 */
public class Validator {
	
	@SuppressWarnings({ "unchecked", "deprecation" })
	public boolean verifyRegisteredItem(CustomItemsNG instance, YamlConfiguration items, 
			YamlConfiguration recipes, String name){
		boolean status = true;
		List<String> errorText = new ArrayList<String>();
		String itemIdStr = Util.concatStrings(Constants.ITEMS, ".", name, ".", Constants.ITEM_ID);
		if (items.contains(Util.concatStrings(Constants.ITEMS, ".", name))){
			if (items.getInt(itemIdStr) != Math.round(items.getInt(itemIdStr))){
				errorText.add(Util.concatStrings(name, ": ID is not a number!"));
				status = false;
			}
			ArrayList<String> abilities = (ArrayList<String>)items.getList(
					Util.concatStrings(Constants.ITEMS, ".", name, ".", Constants.ITEM_ABILITIES));
			if (!abilities.isEmpty()){
				for(String ability : abilities){
					if (!instance.getItemHandler().getAbilities().contains(ability)){
						errorText.add(Util.concatStrings(name, ": ", ability, ": Ability does not exist!"));
						status = false;
					}
				}
			}
		} 
		else {
			status = false;
			errorText.add(Util.concatStrings(name, " : Item does not exist!"));
		}
		if (status){
			ItemStack item = (new CustomID(items.getString(itemIdStr))).getItemStack();
			String enchantmentsStr = Util.concatStrings(Constants.ITEMS, ".", name, "." , 
					Constants.ITEM_ENCHANTMENTS);
			String enchantemtLevelStr = Util.concatStrings(Constants.ITEMS, ".", name, "." , 
					Constants.ITEM_ENCHANTMENTS_LEVEL);
			if (items.contains(enchantmentsStr) && items.getList(enchantmentsStr) != null){
				ArrayList<String> enchantments = (ArrayList<String>)items.getList(enchantmentsStr);
				for (String enchantment : enchantments){
					if (Enchantment.getByName(enchantment) != null){
						if (Enchantment.getByName(enchantment).canEnchantItem(item)){
							if (items.contains(enchantemtLevelStr)){
								int enchantLvlId = items.getInt(Util.concatStrings(
										enchantemtLevelStr, ".", enchantment));
								if (32767 < enchantLvlId){
									errorText.add(Util.concatStrings("Enchantment ", enchantment, 
											" have level ", String.valueOf(enchantLvlId), " , but max is 32767!"));
									status = false;
								}
							}
							else {
								errorText.add(Util.concatStrings("Enchantment ", enchantment, 
										" doesn\264t have own level! "));
								status = false;
							}
						}
						else {
							errorText.add(Util.concatStrings("Enchantment ", enchantment, 
									" cannot be applied to ", item.getType().toString()));
							status = false;
						}
					}
					else {
						errorText.add(Util.concatStrings("Enchantment ", enchantment, " does not exist!"));
						status = false;
					}
				}
			}
		}
		String useRecipeStr = Util.concatStrings(Constants.ITEMS, ".", name, ".", Constants.ITEM_USE_RECIPE);
		if (status && items.getBoolean(useRecipeStr)){
			int i = 1;
			String recipeStr = Util.concatStrings(Constants.RECIPES, ".", name, ".");
			for (i = 1; i <= 9; i++){
				String recipeIndexStr = Util.concatStrings(recipeStr, String.valueOf(i));
				Integer recipeId = recipes.getInt(recipeIndexStr);
				if (recipeId != 0 && Material.getMaterial(recipeId) == null){
					errorText.add(Util.concatStrings("Recipe error - ID ", recipeId.toString(), " does not exist!"));
					status = false;
				}
			}
		}
		if (!status){
			instance.getLog().warning(Util.editConsole("==========================================="));
			instance.getLog().warning(Util.editConsole((new StringBuilder("======Error while loading ")).append(name).append(":======").toString()));
			for (String err : errorText){
				instance.getLog().warning(err);
			}
			instance.getLog().warning(Util.editConsole("==========================================="));
			instance.getLog().warning(Util.editConsole("==========================================="));
		}
		return status;
	}
	

}
