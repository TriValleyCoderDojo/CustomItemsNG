package org.freitas.customitems.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.freitas.customitems.Constants;
import org.freitas.customitems.Constants.Attributes;
import org.freitas.customitems.misc.Util;
import org.freitas.customitems.objects.CustomEnchantment;
import org.freitas.customitems.objects.CustomID;
import org.freitas.customitems.objects.CustomItemStack;

/**
 * This class is a container that provides a common place to access
 * information about all of the custom items that have been defined 
 * by the plugin.  
 *
 */
public class ItemHandler {

	private List<String> abilityList = new ArrayList<String>();
	private Map<String, CustomID> itemTypeMap = new HashMap<String, CustomID>();
	private Map<String, List<String>> itemAbilitiesMap = new HashMap<String, List<String>>();
	private Map<String, Boolean> itemUsePermissionMap = new HashMap<String, Boolean>();
	private Map<String, List<String>> itemLoreMap = new HashMap<String, List<String>>();
	private Map<String, String> itemColorMap = new HashMap<String, String>();
	private Map<String, String> itemNameMap = new HashMap<String, String>();
	private Map<String, List<CustomEnchantment>> itemEnchantmentsMap = new HashMap<String, List<CustomEnchantment>>();
	private Map<CustomItemStack, String> itemStackName = new HashMap<CustomItemStack, String>();
	
	public ItemHandler(){
		for (Attributes attr : Constants.Attributes.values()){
			abilityList.add(attr.getName());
		}
	}

	public void addAbility(String str){
		if (!abilityList.contains(str))
			abilityList.add(str);
	}

	public List<String> getAbilities(){
		return abilityList;
	}

	public void setItemType(Map<String, CustomID> itemTypeMap){
		this.itemTypeMap = itemTypeMap;
	}
	public Map<String, CustomID> getItemType(){
		return itemTypeMap;
	}

	public void setItemAbilities(Map<String, List<String>> itemAbilitiesMap){
		this.itemAbilitiesMap = itemAbilitiesMap;
	}

	public void setItemUsePermission(Map<String, Boolean> itemUsePermissionMap){
		this.itemUsePermissionMap = itemUsePermissionMap;
	}

	public Map<String, List<String>> getItemLore(){
		return itemLoreMap;
	}
	public void setItemLore(Map<String, List<String>> loreMap){
		itemLoreMap = loreMap;
	}

	public void setItemColor(Map<String, String> i){
		itemColorMap = i;
	}
	public Map<String, String> getItemColor(){
		return itemColorMap;
	}

	public void setItemName(Map<String, String> itemNameMap){
		this.itemNameMap = itemNameMap;
	}
	public Map<String, String> getItemName(){
		return itemNameMap;
	}

	public Map<String, List<CustomEnchantment>> getItemEnchantments(){
		return itemEnchantmentsMap;
	}
	public void setItemEnchantments(Map<String, List<CustomEnchantment>> itemEnchantmentsMap){
		this.itemEnchantmentsMap = itemEnchantmentsMap;
	}
	
	public Map<CustomItemStack, String> getItemStackName() {
		return itemStackName;
	}
	public void setItemStackName(Map<CustomItemStack, String> itemStackName) {
		this.itemStackName = itemStackName;
	}
	
	public void reload(){
		itemTypeMap.clear();
		itemAbilitiesMap.clear();
		itemUsePermissionMap.clear();
		itemLoreMap.clear();
		itemColorMap.clear();
		itemNameMap.clear();
		itemEnchantmentsMap.clear();
		itemStackName.clear();
	}

	public boolean isCustom(ItemStack item){
		CustomItemStack cis = new CustomItemStack(item);
		if (itemStackName.containsKey(cis)){
			return true;
		}
		return false;
	}

	public ItemStack createItem(String name){
		if (itemTypeMap.containsKey(name)){
			ItemStack item = ((CustomID)itemTypeMap.get(name)).getItemStack();
			ItemMeta meta = item.getItemMeta();
			meta.setDisplayName(Util.concatStrings(itemColorMap.get(name), itemNameMap.get(name)));
			meta.setLore((List<String>)itemLoreMap.get(name));
			item.setItemMeta(meta);
			item = addEnchantments(item, name);
			return item;
		} 
		else {
			return null;
		}
	}

	public List<String> getItemAbilities(ItemStack item){
		CustomItemStack cis = new CustomItemStack(item);
		String itemName = itemStackName.get(cis);
		if (itemName == null){
			return null;
		}
		return (List<String>)itemAbilitiesMap.get(itemName);
	}

	public boolean hasAbility(ItemStack item, String ab){
		CustomItemStack cis = new CustomItemStack(item);
		String itemName = itemStackName.get(cis);
		if (itemName == null){
			return false;
		}
		List<String> abilities = (List<String>)itemAbilitiesMap.get(itemName);
		if (abilities.contains(ab)){
			return true;
		}
		return false;
	}

	public boolean hasPermission(Player player, ItemStack item){
		CustomItemStack cis = new CustomItemStack(item);
		String itemName = itemStackName.get(cis);
		if (itemName == null){
			return false;
		}
		if (player.hasPermission(Util.concatStrings("custom-items.", itemName))){
			return true;
		}
		return false;
	}


	public ItemStack addEnchantments(ItemStack item, String name){
		List<CustomEnchantment> enchantmentList = itemEnchantmentsMap.get(name);
		if (enchantmentList != null){
			for (CustomEnchantment enchantment : enchantmentList){
				if(enchantment.getEnchantment().canEnchantItem(item))
					item.addUnsafeEnchantment(enchantment.getEnchantment(), enchantment.getLevel());
			}
		}
		return item;
	}

}
