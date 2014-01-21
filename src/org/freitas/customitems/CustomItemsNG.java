package org.freitas.customitems;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.freitas.customitems.commands.CmdExecutor;
import org.freitas.customitems.item.ItemHandler;
import org.freitas.customitems.listeners.BlockListener;
import org.freitas.customitems.listeners.EntityListener;
import org.freitas.customitems.listeners.PlayerListener;
import org.freitas.customitems.misc.Util;

/**
 * This is main Bukkit plugin class.  It controls everything about this plugin.
 * It will do the following:
 *    1) use the ConfigDB class to read the YAML config files
 *    2) create and register the listeners for events
 *    3) collect the set of information on the items to create and
 *       populate hashMaps in the ItemHandler class to share them
 *
 */
public class CustomItemsNG extends JavaPlugin implements Listener {
	
	public String version;
	public String build;
	private ConfigDB configDB;
	private ItemHandler itemHandler;
	private static CustomItemsNG instance = null;
	private Logger log;

	public CustomItemsNG(){
		version = Constants.VERSION;
		build = Constants.BUILD;
		configDB = null;
		itemHandler = null;
		log = Logger.getLogger("Minecraft");
	}

	public void onEnable(){
		instance = this;
		configDB = new ConfigDB(this, Constants.DEF_DIR, Constants.DEF_CONFIG_YML, 
				Constants.DEF_ITEMS_YML, Constants.DEF_RECIPES_YML, Constants.DEF_README);
		itemHandler = new ItemHandler();
		configDB.initialize();
		getServer().getPluginManager().registerEvents(new BlockListener(this), this);
		getServer().getPluginManager().registerEvents(new EntityListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
		getCommand("ci").setExecutor(new CmdExecutor(this));
		log.info(Util.editConsole((new StringBuilder("Plugin loaded! Version ")).append(version).append(" / Build: ").append(build).toString()));
	}

	public void onDisable(){
		log.info(Util.editConsole("Plugin stopped!"));
	}

	public boolean hasPermission(Player player, String str){
		if(player.hasPermission(str))
			return true;
		return player.hasPermission(Constants.ALL_PERMS);
	}

	public ConfigDB getConfigDB(){
		return configDB;
	}

	public ItemHandler getItemHandler(){
		return itemHandler;
	}

	public Logger getLog(){
		return log;
	}

	public static CustomItemsNG getInstance(){
		return instance;
	}

}
