package org.freitas.customitems;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.file.YamlConfiguration;
import org.freitas.customitems.misc.Util;


/**
 * This class is used to help the ConfigDB with the initial creation of
 * the YAML config files.  These are the default files.  If the files 
 * do not exist, then this will create them with example items.  
 */
public class ConfigHelper {
	
	public enum Items {
		TRANSPORT_BALL("TransportBall", "Transport Ball", Arrays.asList("Technology stolen from Endermen")),
		CREEPER_LIGHTER("CreeperLighter", "Creeper Lighter", Arrays.asList("Be the Master of Creepers and Mobs", "Beware of Pigs")),
		DEATH_DEALER("DeathDealer", "Death Dealer", Arrays.asList("An Ancient Artifact of Alien Origin")),
		SCRAMBLER("Scrambler", "Scrambler", Arrays.asList("Just throw it")),
		THUNDER_BOW("ThunderBow", "Thunder Bow", Arrays.asList("A gift from Zeus")),
		THROW_WAND("ThrowWand", "Throw Wand", Arrays.asList("Found in the Elven caves"))
		;

		private final String name;
		private final String displayName;
		private final List<String> lore;
		
		private Items(String name, String displayName, List<String> lore) {
			this.name = name;
			this.displayName = displayName;
			this.lore = lore;
		}
		
		public boolean equalsName(String otherName){
			return (otherName == null)? false:name.equals(otherName);
		}

	}
	
	public static void generateDefaultConfigFile(CustomItemsNG instance, YamlConfiguration config, File configFile) {
		try {
			instance.getLog().info(Util.editConsole("Creating new config..."));
			configFile.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
			return;
		}
		config.set(Constants.VERSION_LABEL, instance.version);
		config.set(Constants.BUILD_LABEL, instance.build);
		config.set(Util.concatStrings(Constants.ABILITIES_DATA, ".", Constants.POISON_DURATION), Integer.valueOf(200));
		config.set(Util.concatStrings(Constants.ABILITIES_DATA, ".", Constants.DISORIENT_DURATION), Integer.valueOf(800));
		config.set(Util.concatStrings(Constants.ABILITIES_DATA, ".", Constants.BLIND_DURATION), Integer.valueOf(1000));
		try {
			config.save(configFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static void generateDefaultRecipesFile(CustomItemsNG instance, YamlConfiguration recipes, File recipesFile) {
		try {
			instance.getLog().info(Util.editConsole("Creating new recipes..."));
			recipesFile.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "1"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "2"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "3"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "4"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "5"), Integer.valueOf(276));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "6"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "7"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "8"), Integer.valueOf(138));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.CREEPER_LIGHTER.name, ".", "9"), Integer.valueOf(0));
		
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "1"), Integer.valueOf(287));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "2"), Integer.valueOf(280));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "3"), Integer.valueOf(0));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "4"), Integer.valueOf(287));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "5"), Integer.valueOf(46));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "6"), Integer.valueOf(280));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "7"), Integer.valueOf(287));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "8"), Integer.valueOf(280));
		recipes.set(Util.concatStrings(Constants.RECIPES, ".", Items.THUNDER_BOW.name, ".", "9"), Integer.valueOf(0));
		try {
			recipes.save(recipesFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static void generateDefaultItemsFile(CustomItemsNG instance, YamlConfiguration items, File itemsFile) {
		try {
			instance.getLog().info(Util.editConsole("Creating new items..."));
			itemsFile.createNewFile();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		ArrayList<String> regItems = new ArrayList<String>();
		ArrayList<String> abilities = new ArrayList<String>();
		ArrayList<String> lore = new ArrayList<String>();
		ArrayList<String> enchantments = new ArrayList<String>();
		Map<String, Integer> enhantmentLevels = new HashMap<String, Integer>();
		
		regItems.add(Items.TRANSPORT_BALL.name);
		regItems.add(Items.CREEPER_LIGHTER.name);
		regItems.add(Items.DEATH_DEALER.name);
		regItems.add(Items.SCRAMBLER.name);
		regItems.add(Items.THUNDER_BOW.name);
		regItems.add(Items.THROW_WAND.name);
		items.set(Constants.REGISTERED_ITEMS, regItems);
		
		abilities.add("Teleport");
		lore.addAll(Items.TRANSPORT_BALL.lore);
		createItem(items, Items.TRANSPORT_BALL.name, 332, Items.TRANSPORT_BALL.displayName, 
				false, abilities, lore, "\2476", false,
				null, null);
		
		abilities.clear();
		lore.clear();
		
		abilities.add("Lightning");
		lore.addAll(Items.CREEPER_LIGHTER.lore);
		enchantments.add("FIRE_ASPECT");
		enchantments.add("KNOCKBACK");
		enchantments.add("DAMAGE_ALL");
		enchantments.add("DURABILITY");
		enchantments.add("LOOT_BONUS_MOBS");
		enhantmentLevels.put("FIRE_ASPECT", 10);
		enhantmentLevels.put("KNOCKBACK", 4);
		enhantmentLevels.put("DAMAGE_ALL", 10);
		enhantmentLevels.put("DURABILITY", 100);
		enhantmentLevels.put("LOOT_BONUS_MOBS", 100);
		createItem(items, Items.CREEPER_LIGHTER.name, 276, Items.CREEPER_LIGHTER.displayName, 
				false, abilities, lore, "\2476", true,
				enchantments, enhantmentLevels);
		
		abilities.clear();
		lore.clear();
		enchantments.clear();
		enhantmentLevels.clear();
		
		abilities.add("Death");
		lore.addAll(Items.DEATH_DEALER.lore);
		createItem(items, Items.DEATH_DEALER.name, 280, Items.DEATH_DEALER.displayName, 
				false, abilities, lore, "\2476", false,
				null, null);
		
		abilities.clear();
		lore.clear();
		
		abilities.add("Explosion");
		lore.addAll(Items.SCRAMBLER.lore);
		createItem(items, Items.SCRAMBLER.name, 344, Items.SCRAMBLER.displayName, 
				false, abilities, lore, "\2476", false,
				null, null);
		
		abilities.clear();
		lore.clear();
		
		abilities.add("Explosion");
		abilities.add("Lightning");
		lore.addAll(Items.THUNDER_BOW.lore);
		createItem(items, Items.THUNDER_BOW.name, 261, Items.THUNDER_BOW.displayName, 
				false, abilities, lore, "\247a", true,
				null, null);
		
		abilities.clear();
		lore.clear();
		
		abilities.add("Throw");
		lore.addAll(Items.THROW_WAND.lore);
		createItem(items, Items.THROW_WAND.name, 76, Items.THROW_WAND.displayName, 
				false, abilities, lore, "\2476", false,
				null, null);
		
		abilities.clear();
		lore.clear();
		
		try {
			items.save(itemsFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private static void createItem(YamlConfiguration items, String itemName, Integer itemId, String displayName, 
			boolean usePerms, ArrayList<String> abilities, ArrayList<String> lore, String color, boolean useRecipe,
			ArrayList<String> enchantments, Map<String, Integer> enhantmentLevels){
		
		// need to make copies of the data because it will get cleared with the next one
		List<String> localAbilities = Arrays.asList(new String[abilities.size()]); 
		Collections.copy(localAbilities, abilities);
		List<String> localLore = Arrays.asList(new String[lore.size()]); 
		Collections.copy(localLore, lore);
		
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ID), Integer.valueOf(itemId));
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_DISPLAY_NAME), displayName);
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_USE_PERMS), Boolean.valueOf(usePerms));
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ABILITIES), localAbilities);
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_LORE), localLore);
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_COLOR), color);
		items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_USE_RECIPE), Boolean.valueOf(useRecipe));
		if (enchantments != null && enchantments.size() > 0){
			List<String> localEnchantments = Arrays.asList(new String[enchantments.size()]); 
			Collections.copy(localEnchantments, enchantments);
			HashMap<String, Integer> localEnhantmentLevels = new HashMap<String, Integer>();
			localEnhantmentLevels = (HashMap<String, Integer>) ((HashMap<String, Integer>)enhantmentLevels).clone();
			items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ENCHANTMENTS), localEnchantments);
			for (String enchantment : localEnchantments){
				Integer level = localEnhantmentLevels.get(enchantment);
				if (level == null){
					level = 1;
				}
				items.set(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ENCHANTMENTS_LEVEL, ".", enchantment), Integer.valueOf(level));
			}
		}
	}
	
	
	public static void generateReadmeFile(File readmeFile) throws IOException {
		FileWriter fw = new FileWriter(readmeFile);
		BufferedWriter bw = new BufferedWriter(fw);
		
		bw.write("\n");
		bw.write("The CustomItemsNG was based upon the original CustomItems done by Jakub1221,\n");
		bw.write("http://dev.bukkit.org/bukkit-plugins/custom-items/.  However, there were\n");
		bw.write("some changes made.\n");
		bw.write("\n");
		bw.write("Commands: \n");
		bw.write("/ci create <item name> <player> Gives custom item to the player\n");
		bw.write("                                (defaults to current).\n");
		bw.write("/ci list                        Shows list of defined items.\n");
		bw.write("/ci info                        Shows info about item in hand.\n");
		bw.write("/ci reload                      Reloads config and items.\n");
		bw.write("/ci help\n");
		bw.write("\n");
		bw.write("Abilities: \n");
		bw.write("Lightning - Strikes lighting from the sky.\n");
		bw.write("SuperFortune - This is like enchant fortune but 3x more powerfull.\n");
		bw.write("Death - Instantly kills the target.\n");
		bw.write("SuperHit - Hit is 3x bigger.\n");
		bw.write("Break - Breaks any block instantly. (except Bedrock)\n");
		bw.write("Teleport - Teleports to target location.\n");
		bw.write("Poison - Poisons the target.\n");
		bw.write("Disorient - Disorients the target.\n");
		bw.write("Explosion - Creates explosion when you click or hit something.\n");
		bw.write("LifeSteal - Converts damage to life.\n");
		bw.write("Blind - Blinds the target.\n");
		bw.write("Fire - Sets clicked block to fire / Sets hit target to fire.\n");
		bw.write("Throw - Throws the target entity up in the air to fall to their death.\n");
		bw.write("\n");
		bw.write("Supported Projectiles: \n");
		bw.write("Bow, Egg, Snowball\n");
		bw.write("and each can be used with abilities Teleport, Lightning and Explosion\n");
		bw.write("\n");
		bw.write("Item Configuration:\n");
		bw.write("Very similar to the original with some slight changes.  Look at the examples in items.yml\n");
		bw.write("\n");
		bw.write("Changes:\n");
		bw.write("- Added a list command\n");
		bw.write("- Use current player as default on create command.\n");
		bw.write("- Added the Throw ability\n");
		bw.write("- Added Egg to projectiles\n");
		bw.write("- Removed the UseCustom property from the item definition in items.yml \n");
		bw.write("- Removed the adding of the Item Name in the lore\n");
		bw.write("- Removed UpdateItems property in config.yml \n");
		bw.write("\n");
		bw.write("\n");
		bw.close();
	}
	
}
