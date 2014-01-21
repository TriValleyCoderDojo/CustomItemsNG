package org.freitas.customitems.objects;

import org.bukkit.enchantments.Enchantment;

public class CustomEnchantment {
	
	private Enchantment ench;
	private int level;

	public CustomEnchantment(Enchantment e, int l){
		ench = e;
		level = l;
	}

	public Enchantment getEnchantment(){
		return ench;
	}

	public int getLevel(){
		return level;
	}

}
