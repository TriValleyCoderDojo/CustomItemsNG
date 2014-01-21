package org.freitas.customitems.objects;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.freitas.customitems.data.CustomItemHashCodeBuilder;

/**
 * This is a custom implementation of the ItemStack to be used in a Map 
 * to be able to lookup the item without considering the amount in the 
 * stack.  Need to override the default equals() and hashCode(), because
 * they don't do quite what is needed here.  
 */
public class CustomItemStack {
	
	private ItemStack itemStack;
	
	public CustomItemStack(ItemStack itemStack) {
		super();
		this.itemStack = itemStack;
	}
	
	public ItemStack getItemStack() {
		return itemStack;
	}
	public void setItemStack(ItemStack itemStack) {
		this.itemStack = itemStack;
	}

	@SuppressWarnings("deprecation")
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CustomItemStack)) {
			return false;
		}
		ItemStack other = ((CustomItemStack) obj).getItemStack();
		if (itemStack.getTypeId() != other.getTypeId() ){
			return false;
		}
		if (itemStack.getDurability() != other.getDurability()){
			return false;
		}
		if (!itemStack.getData().equals(other.getData())){
			return false;
		}
		if ( (itemStack.hasItemMeta() && !other.hasItemMeta()) || (!itemStack.hasItemMeta() && other.hasItemMeta()) ){
			return false;
		}
		if (!itemStack.hasItemMeta() && !other.hasItemMeta()){
			// if no meta on both then can stop here
			return true;
		}
		if (itemStack.getItemMeta().getDisplayName() != null && other.getItemMeta().getDisplayName() != null){
			if(!itemStack.getItemMeta().getDisplayName().equals(other.getItemMeta().getDisplayName())){
				return false;
			}
		}
		else if (itemStack.getItemMeta().getDisplayName() == null && other.getItemMeta().getDisplayName() == null){
			// they are both null, should not happen with our custom items though
		}
		else {
			return false;
		}
		
		if (itemStack.getItemMeta().getLore() != null && other.getItemMeta().getLore() != null){
			if (itemStack.getItemMeta().getLore().size() != other.getItemMeta().getLore().size()){
				return false;
			}
			if (itemStack.getItemMeta().getLore().size() > 0){
				for (int i = 0; i < itemStack.getItemMeta().getLore().size(); i++){
					if (!itemStack.getItemMeta().getLore().get(i).equals(other.getItemMeta().getLore().get(i))){
						return false;
					}
				}
			}
		}
		else if (itemStack.getItemMeta().getLore() == null && other.getItemMeta().getLore() == null){
			// they are both null, should not happen with our custom items though
		}
		else {
			return false;
		}
		if (itemStack.getItemMeta().getEnchants() != null && other.getItemMeta().getEnchants() != null){
			if (itemStack.getItemMeta().getEnchants().size() != other.getItemMeta().getEnchants().size()){
				return false;
			}
			if (itemStack.getItemMeta().getEnchants().size() > 0){
				for (Enchantment itemStackEnch : itemStack.getItemMeta().getEnchants().keySet()){
					if (!other.getItemMeta().getEnchants().containsKey(itemStackEnch)){
						return false;
					}
					int itemStackEnchLvl = itemStack.getEnchantmentLevel(itemStackEnch);
					int otherEnchLvl = other.getEnchantmentLevel(itemStackEnch);
					if (itemStackEnchLvl != otherEnchLvl){
						return false;
					}
				}
			}
		}
		else if (itemStack.getItemMeta().getEnchants() == null && other.getItemMeta().getEnchants() == null){
			// they are both null, should not happen with our custom items though
		}
		else {
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("deprecation")
	public int hashCode() {
		CustomItemHashCodeBuilder hashBuilder = new CustomItemHashCodeBuilder();
		hashBuilder.append(itemStack.getTypeId());
		hashBuilder.append(itemStack.getDurability());
		if (itemStack.getData() != null){
			hashBuilder.append(itemStack.getData().getItemTypeId());
			hashBuilder.append(itemStack.getData().getData());
		}
		if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getDisplayName() != null){
			hashBuilder.append(itemStack.getItemMeta().getDisplayName());
		}
		if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getLore() != null){
			for (String lore : itemStack.getItemMeta().getLore()){
				hashBuilder.append(lore);
			}
		}
		if (itemStack.getItemMeta() != null && itemStack.getItemMeta().getEnchants() != null){
			for (Enchantment enchantment : itemStack.getItemMeta().getEnchants().keySet()){
				hashBuilder.append(enchantment.getId())
					.append(itemStack.getEnchantmentLevel(enchantment));
			}
		}
		return hashBuilder.toHashCode();
	}

}
