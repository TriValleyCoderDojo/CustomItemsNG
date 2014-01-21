CustomItemsNG
=============

Craftbukkit plugin to create custom items.


The CustomItemsNG was based upon the original CustomItems done by Jakub1221,
http://dev.bukkit.org/bukkit-plugins/custom-items/.  However, there were
some changes made.

Commands: 
- /ci create <item name> <player> - Gives custom item to the player
                                (defaults to current).
- /ci list                       -  Shows list of defined items.
- /ci info                       -  Shows info about item in hand.
- /ci reload                     -  Reloads config and items.
- /ci help

Abilities: 
- Lightning - Strikes lighting from the sky.
- SuperFortune - This is like enchant fortune but 3x more powerfull.
- Death - Instantly kills the target.
- SuperHit - Hit is 3x bigger.
- Break - Breaks any block instantly. (except Bedrock)
- Teleport - Teleports to target location.
- Poison - Poisons the target.
- Disorient - Disorients the target.
- Explosion - Creates explosion when you click or hit something.
- LifeSteal - Converts damage to life.
- Blind - Blinds the target.
- Fire - Sets clicked block to fire / Sets hit target to fire.
- Throw - Throws the target entity up in the air to fall to their death.

Supported Projectiles: 


Bow, Egg, Snowball
and each can be used with abilities Teleport, Lightning and Explosion

Item Configuration:


Very similar to the original with some slight changes.  Look at the examples in items.yml

Changes:
- Added a list command
- Use current player as default on create command.
- Added the Throw ability
- Added Egg to projectiles
- Removed the UseCustom property from the item definition in items.yml 
- Removed the adding of the Item Name in the lore
- Removed UpdateItems property in config.yml 

