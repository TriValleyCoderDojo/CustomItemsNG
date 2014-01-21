package org.freitas.customitems;

/**
 * This class is used to hold contant values that need to be available to different things
 * in the plugin.  It is used for the sake of maintenance, so these only need to be 
 * defined in one place and can be re-used.  
 *
 */
public class Constants {
	
	public static String DEF_DIR = "plugins/CustomItemsNG";
	public static String DEF_CONFIG_YML = DEF_DIR + "/config.yml";
	public static String DEF_ITEMS_YML = DEF_DIR + "/items.yml";
	public static String DEF_RECIPES_YML = DEF_DIR + "/recipes.yml";
	public static String DEF_README = DEF_DIR + "/README.txt";
	
	public static int DEF_POISON_DURATION = 200;
	public static int DEF_DISORIENT_DURATION = 800;
	public static int DEF_BLIND_DURATION = 1000;
	
	public static String VERSION = "1.0.0";
	public static String BUILD = "001";
	
	// for config.yml file
	public static String VERSION_LABEL = "version";
	public static String BUILD_LABEL = "build";
	public static String ABILITIES_DATA = "AbilitiesData";
	public static String POISON_DURATION = "Poison_Duration";
	public static String DISORIENT_DURATION = "Disorient_Duration";
	public static String BLIND_DURATION = "Blind_Duration";
	
	// for items.yml
	public static String REGISTERED_ITEMS = "RegisteredItems";
	public static String ITEMS = "Items";
	public static String ITEM_ID = "ID";
	public static String ITEM_DISPLAY_NAME = "DisplayName";
	public static String ITEM_USE_PERMS = "UsePermission";
	public static String ITEM_ABILITIES = "Abilities";
	public static String ITEM_LORE = "Lore";
	public static String ITEM_COLOR = "Color";
	public static String ITEM_USE_RECIPE = "UseRecipe";
	public static String ITEM_ENCHANTMENTS = "Enchantments";
	public static String ITEM_ENCHANTMENTS_LEVEL = "EnchantmentsLevel";
	
	// for recipes.yml
	public static String RECIPES = "Recipes";
	
	// for permissions
	public static String NO_PERMS_MSG = "You dont have permission!";
	public static String ALL_PERMS = "custom-items.*";
	public static String HELP_PERMS = "custom-items.help";
	public static String RELOAD_PERMS = "custom-items.reload";
	public static String INFO_PERMS = "custom-items.info";
	public static String CREATE_PERMS = "custom-items.create";
	public static String LIST_PERMS = "custom-items.list";
	
	public enum Attributes {
		LIGHTNING("Lightning"),
		SUPER_FORTUNE("SuperFortune"),
		DEATH("Death"),
		SUPER_HIT("SuperHit"),
		BREAK("Break"),
		TELEPORT("Teleport"),
		POISON("Poison"),
		DISORIENT("Disorient"),
		EXPLOSION("Explosion"),
		LIFE_STEAL("LifeSteal"),
		BLIND("Blind"),
		FIRE("Fire"),
		HEAL("Heal"),
		THROW("Throw")
		;

		private final String name;

		private Attributes(String s) {
			name = s;
		}
		
		public String getName(){
			return name;
		}
		
		public boolean equalsName(String otherName){
			return (otherName == null)? false:name.equals(otherName);
		}

	}

}
