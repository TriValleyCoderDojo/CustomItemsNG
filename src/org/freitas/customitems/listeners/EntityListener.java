package org.freitas.customitems.listeners;

import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.freitas.customitems.Constants;
import org.freitas.customitems.CustomItemsNG;
import org.freitas.customitems.misc.Util;

/**
 * This class is used for when a player inflicts some kind of 
 * damage to some other entity with one of the custom items.  
 *
 */
public class EntityListener implements Listener {
	private CustomItemsNG instance;

	public EntityListener(CustomItemsNG plugin){
		instance = plugin;
	}

	@EventHandler
	public void onEntityDamage(EntityDamageEvent event){
		// make sure that if a player who is holding something with the LIGHTNING 
		// ability won't get hurt by their own lightning
		if (event.getCause() != null && event.getCause() == EntityDamageEvent.DamageCause.LIGHTNING){
			if (event.getEntity() instanceof Player){
				Player player = (Player)event.getEntity();
				boolean hasAbility = instance.getItemHandler().hasAbility(
						player.getItemInHand(), Constants.Attributes.LIGHTNING.getName());
				if (player.getItemInHand() != null && hasAbility){
					event.setDamage(0);
					event.setCancelled(true);
					return;
				}
			}
		}
		// need figure out what kind of damage to inflict
		if (event instanceof EntityDamageByEntityEvent){
			Player player = null;
			EntityDamageByEntityEvent dEvent = (EntityDamageByEntityEvent)event;
			// only work with damage inflicted by players and projectiles
			if (dEvent.getDamager() != null && isAllowedEntity(dEvent.getDamager()) ){
				// need to set the player
				if (dEvent.getDamager() instanceof Player)
					player = (Player)dEvent.getDamager();
				else {
					if (dEvent.getDamager() instanceof Arrow){
						Arrow arrow = (Arrow)dEvent.getDamager();
						if (arrow.getShooter() instanceof Player)
							player = (Player)arrow.getShooter();
					} 
					else if (dEvent.getDamager() instanceof Snowball){
							Snowball arrow = (Snowball)dEvent.getDamager();
							if (arrow.getShooter() instanceof Player) {
								player = (Player)arrow.getShooter();
							}
					}
					else {
						return;
					}
				}
				
				// now we need to figure out which attributes are active and do the damage
				if (player != null && player.getItemInHand() != null){
					Map<String, Boolean> abilitiesMap = Util.getCurrentAbilities(instance, player);
					if (abilitiesMap.containsKey(Constants.Attributes.DEATH.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						event.setDamage(10000);
						return;
					}
					if (abilitiesMap.containsKey(Constants.Attributes.SUPER_HIT.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						event.setDamage(event.getDamage() * 3);
					}
					if (abilitiesMap.containsKey(Constants.Attributes.POISON.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						player.addPotionEffect(new PotionEffect(PotionEffectType.POISON, instance.getConfigDB().getPoisonDuration(), 1));
					}
					if (abilitiesMap.containsKey(Constants.Attributes.DISORIENT.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, instance.getConfigDB().getDisorientDuration(), 1));
					}
					if (abilitiesMap.containsKey(Constants.Attributes.BLIND.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, instance.getConfigDB().getBlindDuration(), 1));
					}
					if (abilitiesMap.containsKey(Constants.Attributes.LIFE_STEAL.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						if(player.getHealth() + event.getDamage() / 3 > 20)
							player.setHealth(20);
						else {
							player.setHealth(player.getHealth() + event.getDamage() / 3);
						}
					}
					if (abilitiesMap.containsKey(Constants.Attributes.HEAL.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						if(player.getHealth() <= 18)
							player.setHealth(player.getHealth() + 2);
						else
							player.setHealth(20);
					}
					if (abilitiesMap.containsKey(Constants.Attributes.FIRE.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						event.getEntity().setFireTicks(100);
					}
					if (abilitiesMap.containsKey(Constants.Attributes.THROW.getName()) 
							&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
						event.setCancelled(true);
						Entity damagee = dEvent.getEntity();
						Vector vector = new Vector(0,2.5,0);
						damagee.setVelocity(vector);
					}
				}
			}
		}
	}
	
	private boolean isAllowedEntity(Entity entity) {
		if (entity instanceof Player){
			return true;
		}
		if (entity instanceof Projectile){
			return true;
		}
		return false;
	}
	
	
	@EventHandler
	public void onProjectileHit(ProjectileHitEvent event){
		// only work with arrows, snowballs and eggs
		if (!allowedType(event)){
			return;
		}
		Projectile projectile = event.getEntity();
		LivingEntity shooter = projectile.getShooter();
		// make sure the shooter is a player
		if (!(shooter instanceof Player)){
			return;
		}
		Player player = (Player)shooter;
		Map<String, Boolean> abilitiesMap = Util.getCurrentAbilities(instance, player);
		if (event.getEntity() instanceof Arrow){
			// make sure player is holding a bow
			if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.BOW){
				return;
			}
			doProjectileActions(projectile, player, abilitiesMap);
			// need to explicitly remove it or else arrows will be left dangling
			projectile.remove();
		}
		else if (event.getEntity() instanceof Snowball){
			// make sure player is holding a snowball
			if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.SNOW_BALL){
				return;
			}
			doProjectileActions(projectile, player, abilitiesMap);
		}
		else if (event.getEntity() instanceof Egg){
			// make sure player is holding a egg
			if (player.getItemInHand() == null || player.getItemInHand().getType() != Material.EGG){
				return;
			}
			doProjectileActions(projectile, player, abilitiesMap);
		}
	}
	
	
	private boolean allowedType(ProjectileHitEvent event){
		if (event.getEntity() instanceof Arrow){
			return true;
		}
		if (event.getEntity() instanceof Snowball){
			return true;
		}
		if (event.getEntity() instanceof Egg){
			return true;
		}
		return false;
	}
	
	private void doProjectileActions(Projectile projectile, Player player, Map<String, Boolean> abilitiesMap){
		if (abilitiesMap.containsKey(Constants.Attributes.TELEPORT.getName()) 
				&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
			Location loc = projectile.getLocation();
			teleportPlayer(loc, player);
		}
		if (abilitiesMap.containsKey(Constants.Attributes.LIGHTNING.getName()) 
				&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
			Location loc = projectile.getLocation();
			lightningStrike(loc);
		}
		if (abilitiesMap.containsKey(Constants.Attributes.EXPLOSION.getName()) 
				&& instance.getItemHandler().hasPermission(player, player.getItemInHand())){
			Location loc = projectile.getLocation();
			explosion(loc);
		}
		
	}
	
	private void teleportPlayer(Location loc, Player player){
		loc.setPitch(player.getLocation().getPitch());
		loc.setYaw(player.getLocation().getYaw());
		loc.setY(loc.getY() + 1.0D);
		player.teleport(loc);
	}
	
	private void lightningStrike(Location loc){
		loc.getWorld().strikeLightning(loc);
	}
	
	private void explosion(Location loc){
		loc.getWorld().createExplosion(loc, 4F);
	}
	
}
