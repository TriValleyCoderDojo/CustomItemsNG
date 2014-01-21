package org.freitas.customitems;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.freitas.customitems.data.Validator;
import org.freitas.customitems.misc.Util;
import org.freitas.customitems.objects.CustomEnchantment;
import org.freitas.customitems.objects.CustomID;
import org.freitas.customitems.objects.CustomItemStack;


/**
 * This class is used to manage the YAML config files and process their contents.  
 * Initially, it will check if they exist, and if not create them.  Otherwise, it 
 * it will read them, store the data into a set of hashes and make those available
 * to the plugin for use.  
 */
public class ConfigDB {
	
	private CustomItemsNG instance;
	private String pluginDir;
	private YamlConfiguration config;
	private YamlConfiguration items;
	private YamlConfiguration recipes;
	private File configFile = null;;
	private File itemsFile = null;
	private File recipesFile = null;
	private File readmeFile = null;
	private ArrayList<String> registeredItems = new ArrayList<String>();
	private Map<String, CustomID> itemTypeMap = new HashMap<String, CustomID>();
	private Map<String, List<String>> itemAbilitiesMap = new HashMap<String, List<String>>();
	private Map<String, Boolean> itemUsePermissionMap = new HashMap<String, Boolean>();
	private Map<String, List<String>> itemLoreMap = new HashMap<String, List<String>>();
	private Map<String, String> itemColorMap = new HashMap<String, String>();
	private Map<String, String> itemNameMap = new HashMap<String, String>();
	private Map<String, Boolean> itemRecipeMap = new HashMap<String, Boolean>();
	private ArrayList<ShapedRecipe> itemRecipes = new ArrayList<ShapedRecipe>();
	private ArrayList<String> itemsToDelete = new ArrayList<String>();
	private Map<String, List<CustomEnchantment>> itemEnchantmentMap = new HashMap<String, List<CustomEnchantment>>();
	private Map<CustomItemStack, String> itemStackName = new HashMap<CustomItemStack, String>();
	private int poisonDuration = Constants.DEF_POISON_DURATION;
	private int disorientDuration = Constants.DEF_DISORIENT_DURATION;
	private int blindDuration = Constants.DEF_BLIND_DURATION;
	
	
	public ConfigDB(CustomItemsNG plugin, String pluginPath, String configPath, String itemsPath, 
			String recipesPath, String readmePath){
		instance = plugin;
		pluginDir = pluginPath;
		configFile = new File(configPath);
		itemsFile = new File(itemsPath);
		recipesFile = new File(recipesPath);
		readmeFile = new File(readmePath);
	}

	public void initialize(){
		// create the dir under plugins if not there
		(new File(pluginDir)).mkdirs();
		// create some empty YML files
		recipes = new YamlConfiguration();
		items = new YamlConfiguration();
		config = new YamlConfiguration();
		// check if files exist on disk and create if not
		checkConfigFile();
		checkRecipesFile();
		checkItemsFile();
		checkReadmeFile();
		// load all three YML files
		loadYamlFiles(true);
		// check the attributes in the config YML file
		if (!config.contains(Constants.ABILITIES_DATA)){
			config.set("AbilitiesData.Poison_Duration", Integer.valueOf(200));
			config.set("AbilitiesData.Disorient_Duration", Integer.valueOf(800));
		}
		if (!config.contains("AbilitiesData.Blind_Duration"))
			config.set("AbilitiesData.Blind_Duration", Integer.valueOf(1000));
		if (config.contains(Constants.VERSION_LABEL)){
			if(!config.getString(Constants.VERSION_LABEL).equals(instance.version))
				config.set(Constants.VERSION_LABEL, instance.version);
		} 
		else {
			config.set(Constants.VERSION_LABEL, instance.version);
		}
		
		if (config.contains(Constants.BUILD_LABEL)){
			if (!config.getString(Constants.BUILD_LABEL).equals(instance.build))
				config.set(Constants.BUILD_LABEL, instance.build);
		} 
		else {
			config.set(Constants.BUILD_LABEL, instance.build);
		}
		
		try {
			config.save(configFile);
		}
		catch(IOException e){
			e.printStackTrace();
		}
		reload();
	}
	
	
	@SuppressWarnings("unchecked")
	public void reload(){
		loadYamlFiles(true);
		poisonDuration = config.getInt(Util.concatStrings(Constants.ABILITIES_DATA, ".", Constants.POISON_DURATION));
		disorientDuration = config.getInt(Util.concatStrings(Constants.ABILITIES_DATA, ".", Constants.DISORIENT_DURATION));
		instance.getLog().info(Util.editConsole("Loading config and items..."));
		resetMaps();
		registeredItems = (ArrayList<String>)items.getList(Constants.REGISTERED_ITEMS);
		if (registeredItems.size() > 0){
			int totRegItems = reloadRegisteredItems();
			instance.getItemHandler().setItemType(itemTypeMap);
			instance.getItemHandler().setItemAbilities(itemAbilitiesMap);
			instance.getItemHandler().setItemUsePermission(itemUsePermissionMap);
			instance.getItemHandler().setItemLore(itemLoreMap);
			instance.getItemHandler().setItemColor(itemColorMap);
			instance.getItemHandler().setItemName(itemNameMap);
			instance.getItemHandler().setItemEnchantments(itemEnchantmentMap);
			// remove any that had some kind of syntax error
			for (String deleteItem : itemsToDelete){
				registeredItems.remove(deleteItem);
			}
			// work on the recipes next
			instance.getLog().info(Util.editConsole("Loading recipes..."));
			for (String itemName : registeredItems){
				processRecipe(itemName);
			}
			Server server = Bukkit.getServer();
			server.resetRecipes();
			if (recipes != null){
				for (ShapedRecipe recipe : itemRecipes){
					server.addRecipe(recipe);
				}
			}
			// add the registered items to a hash to be able to translate ItemStack obects
			// into the item name, which is what all the other hashes are keyed on
			for (String itemName : registeredItems){
				ItemStack itemStack = instance.getItemHandler().createItem(itemName);
				CustomItemStack customItemStack = new CustomItemStack(itemStack);
				itemStackName.put(customItemStack, itemName);
			}
			instance.getItemHandler().setItemStackName(itemStackName);
			
			instance.getLog().info(Util.editConsole((new StringBuilder(String.valueOf(totRegItems))).append(" items successfully registered!").toString()));
			instance.getLog().info(Util.editConsole("Config and items loaded!"));
		}
		else {
			instance.getLog().warning(Util.editConsole("There are not any registered items!"));
		}
	}
	
	
	private void resetMaps(){
		registeredItems.clear();
		itemTypeMap.clear();
		itemAbilitiesMap.clear();
		itemUsePermissionMap.clear();
		itemLoreMap.clear();
		itemColorMap.clear();
		itemNameMap.clear();
		itemRecipeMap.clear();
		itemRecipes.clear();
		itemsToDelete.clear();
		itemEnchantmentMap.clear();
		instance.getItemHandler().reload();
	}
	
	
	@SuppressWarnings("unchecked")
	private int reloadRegisteredItems(){
		int i = 0;
		Validator validator = new Validator();
		for (String itemName : registeredItems){
			if (validator.verifyRegisteredItem(instance, items, recipes, itemName)){
				CustomID customId = new CustomID(items.getString(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ID)));
				itemTypeMap.put(itemName, customId);
				itemAbilitiesMap.put(itemName, (ArrayList<String>)items.getList(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ABILITIES)));
				itemUsePermissionMap.put(itemName, Boolean.valueOf(items.getBoolean(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_USE_PERMS))));
				itemNameMap.put(itemName, items.getString(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_DISPLAY_NAME)));
				itemRecipeMap.put(itemName, Boolean.valueOf(items.getBoolean(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_USE_RECIPE))));
				String lorePath = Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_LORE);
				if (items.contains(lorePath)){
					itemLoreMap.put(itemName, (ArrayList<String>)items.getList(lorePath));
				} 
				else {
					// default of lore of unknown, if left empty
					ArrayList<String> emptyLore = new ArrayList<String>();
					emptyLore.add("An unknown lore");
					itemLoreMap.put(itemName, emptyLore);
				}
				String colorPath = Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_COLOR);
				if (items.contains(colorPath)){
					itemColorMap.put(itemName, items.getString(colorPath));
				}
				else {
					// default of 
					itemColorMap.put(itemName, "\247f");
				}
				String enchantmentPath = Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ENCHANTMENTS);
				if (items.contains(enchantmentPath)){
					List<String> yamlEnchants = (List<String>)items.getList(enchantmentPath);
					if (yamlEnchants != null){
						List<CustomEnchantment> enchantments = new ArrayList<CustomEnchantment>();
						for (String enchantName : yamlEnchants){
							int enchantLevel = items.getInt(Util.concatStrings(Constants.ITEMS, ".", itemName, ".", Constants.ITEM_ENCHANTMENTS_LEVEL, ".", enchantName));
							CustomEnchantment newEnchant = new CustomEnchantment(Enchantment.getByName(enchantName), enchantLevel);
							enchantments.add(newEnchant);
						}
						itemEnchantmentMap.put(itemName, enchantments);
					}
				}
				i++;
			} 
			else {
				instance.getLog().warning(Util.editConsole((new StringBuilder(String.valueOf(itemName))).append(" item cannot be loaded!").toString()));
				itemsToDelete.add(itemName);
			}
		}
		return i;
	}
	
	
	@SuppressWarnings("deprecation")
	private void processRecipe(String itemName){
		if (((Boolean)itemRecipeMap.get(itemName)).booleanValue()){
			Map<Integer, Character> ingredientMap = new HashMap<Integer, Character>();
			CustomID [][] recipeGrid = new CustomID[3][3];
			int alphabetIndex = 0;
			for (int recipeIndex = 1; recipeIndex <= 9; recipeIndex++){
				String recipePath = Util.concatStrings(Constants.RECIPES, ".", itemName, ".", String.valueOf(recipeIndex));
				CustomID recipeMaterial = new CustomID(recipes.getString(recipePath));
				// save the items in a grid
				switch (recipeIndex){
				case 1: case 2: case 3:
					recipeGrid[0][recipeIndex-1] = recipeMaterial;
					break;
				case 4: case 5: case 6:
					recipeGrid[1][recipeIndex-4] = recipeMaterial;
					break;
				default:
					// must be 7, 8 or 9
					recipeGrid[2][recipeIndex-7] = recipeMaterial;
					break;
				}
				// save the set of unique ingredients and assign a letter to each
				if (recipeMaterial.getId() != 0){
					if (!ingredientMap.containsKey(recipeMaterial.getId())){
						char recipeChar = (char) ('A' + alphabetIndex);
						ingredientMap.put(recipeMaterial.getId(), recipeChar);
						alphabetIndex++;
					}
				}
			}
			
			ShapedRecipe shapedRecipe = new ShapedRecipe(instance.getItemHandler().createItem(itemName));
			StringBuilder row1 = new StringBuilder();
			StringBuilder row2 = new StringBuilder();
			StringBuilder row3 = new StringBuilder();
			for (int i=0; i < recipeGrid.length ; i++){
				for (int j=0; j < recipeGrid[i].length ; j++){
					switch (i){
					case 0:
						processRecipeRow(row1, recipeGrid[i][j], ingredientMap);
						break;
					case 1: 
						processRecipeRow(row2, recipeGrid[i][j], ingredientMap);
						break;
					default:
						// must be 2
						processRecipeRow(row3, recipeGrid[i][j], ingredientMap);
						break;
					}
				}
			}
			
			shapedRecipe.shape(new String[] {
					row1.toString(), row2.toString(), row3.toString() 
			});
			
			for (Integer materialId : ingredientMap.keySet()){
				Character recipeChar = ingredientMap.get(materialId);
				shapedRecipe.setIngredient(recipeChar, Material.getMaterial(materialId));
			}
			
			itemRecipes.add(shapedRecipe);
		}
	}
	
	
	private void processRecipeRow(StringBuilder row, CustomID recipeMaterial, Map<Integer, Character> ingredientMap){
		if (recipeMaterial.getId() == 0){
			row.append(" ");
		}
		else {
			Character recipeChar = ingredientMap.get(recipeMaterial.getId());
			row.append(recipeChar);
		}
	}
	
	
	private void loadYamlFiles(boolean includeConfig){
		if (includeConfig){
			try {
				config.load(configFile);
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		try {
			items.load(itemsFile);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		try {
			recipes.load(recipesFile);
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	private void checkConfigFile() {
		if (configFile.exists()){
			return;
		}
		ConfigHelper.generateDefaultConfigFile(instance, config, configFile);
	}
	
	private void checkRecipesFile() {
		if (recipesFile.exists()){
			return;
		}
		ConfigHelper.generateDefaultRecipesFile(instance, recipes, recipesFile);
	}
	
	
	private void checkItemsFile() {
		if (itemsFile.exists()){
			return;
		}
		ConfigHelper.generateDefaultItemsFile(instance, items, itemsFile);
	}
	
	
	private void checkReadmeFile() {
		if (readmeFile.exists()){
			return;
		}
		try {
			ConfigHelper.generateReadmeFile(readmeFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String getPluginDir() {
		return pluginDir;
	}
	
	public int getPoisonDuration() {
		return poisonDuration;
	}

	public int getDisorientDuration() {
		return disorientDuration;
	}

	public int getBlindDuration() {
		return blindDuration;
	}
	
	public ArrayList<String> getRegisteredItems(){
		return registeredItems;
	}

}
