package org.freitas.customitems.objects;

import org.bukkit.inventory.ItemStack;

public class CustomID {
	
	private int id;
	private int data;

	public CustomID(String _data){
		if (_data != null && _data.length() > 0){
			String both[] = _data.split(":");
			id = Integer.parseInt(both[0]);
			if(both.length > 1)
				data = Integer.parseInt(both[1]);
			else
				data = 0;
		} 
		else {
			id = 0;
			data = 0;
		}
	}

	public int getId(){
		return id;
	}

	public int getData(){
		return data;
	}

	public boolean isData(){
		return data > 0;
	}

	@SuppressWarnings("deprecation")
	public ItemStack getItemStack(){
		ItemStack item = null;
		if (data > 0)
			item = new ItemStack(id, 1, (byte)data);
		else
			item = new ItemStack(id);
		return item;
	}


}
