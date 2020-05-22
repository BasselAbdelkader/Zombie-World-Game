package game;

import java.util.Random;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.WeaponItem;
import java.util.HashMap;

/**
 * Special Action for attacking other Actors.
 */
public class AttackAction extends Action {

	/**
	 * The Actor that is to be attacked
	 */
	protected Actor target;
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();
	/**
	 * HashMap storing verb of weapon used and corresponding hit chance for Zombie
	 * when Zombie attacks with 2 arms
	 */
	protected HashMap<String, float> zombie2armhit = new HashMap<String, float>();
	/**
	 * HashMap storing verb of weapon used and corresponding hit chance for Zombie
	 * when Zombie attacks with 1 arm
	 */
	protected HashMap<String, float> zombie1armhit = new HashMap<String, float>();

	/**
	 * Constructor.
	 * Initializes hit chances of Zombie 1 arm and 2 arm intrinsic attacks.
	 * @param target the Actor to attack
	 */
	public AttackAction(Actor target) {
		this.target = target;
		// initialize hit chances for zombie 2 arm attacks
		zombie2armhit.put("punches", 0.8);
		zombie2armhit.put("bites", 0.4);

		// initialize hit chances for zombie 1 arm attacks
		zombie1armhit.put("punches", 0.6);
		zombie1armhit.put("bites", 0.8);
	}

	// Overloaded method - one for Zombie and one for Actor
	// Zombie attack hit chance calculation
	protected boolean calculateHit(Zombie zActor, String weaponVerb) {
		float chance = rand.nextFloat();

		// success flag is true when hit is successful
		boolean success = false;
			// With 2 arms:
		if (zActor.arms() == 2) {
			// this outer if can be omitted if hit chances for all weapon verbs are added 
			// to HashMap.
			// currently HashMap only contains hit chances for Zombie intrinsic weapons.
			if (zombie2armhit.containsKey(weaponVerb)) {
				if (chance < zombie2armhit.get(weaponVerb)) {
					success = true;
				}
			} else {
				// weapons have 90% hit chance with 2 arms
				if (chance < 0.9) {
					success = true;
				}
			}
		} else if (zActor.arms() == 1) {
			// With 1 arm:
			// checking if containsKey can be omitted if hit chances for all weapon verbs are 
			// added to HashMap.
			// currently HashMap only contains hit chances for Zombie intrinsic weapons.
			if (zombie1armhit.containsKey(weaponVerb)) {
				if (chance < zombie1armhit.get(weaponVerb)) {
					success = true;
				}
			} else {
				// weapons have 45% hit chance with 1 arm
				if (chance < 0.45) {
					success = true;
				}
			}
		} else {
			// With 0 arms:
			// bites have 40% hit chance
			if (weaponVerb == "bites") {
				if (chance < 0.4) {
					success = true;
				}
			}
		}

		return success;
	}
	
	// Actor(except Zombie) attack hit chance calculation
	protected boolean calculateHit(Actor actor, String weaponVerb) {
		// Humans always have 90% hit chance with anything they attack with.
		if (rand.nextFloat() < 0.9) {
			return true;
		}
		return false;
	}

	protected String handleZombieLimb(GameMap map) {
		// target must be Zombie when this method is called
		if (!(target instanceof Zombie)) {
			throw new Exception("tried to drop limb from hitting non-Zombie target")
		}
		Zombie zTarget = (Zombie) target;
		float chance = rand.nextFloat();
		// String to be printed added to execute() method return and hence printed to Display
		String output = "";
		// 25% chance to drop a limb
		if (chance < 0.25) {
			String droppedLimb = zTarget.dropLimb();
			if (droppedLimb == "leg") {
				map.locationOf(zTarget).addItem(new ZombieLeg());
				output += "\n" + zTarget + " dropped a leg!";
				output += " Has " + zTarget.legs() + " leg(s) left.";
			} else if (droppedLimb == "arm") {
				map.locationOf(zTarget).addItem(new ZombieArm());
				output += "\n" + zTarget + " dropped an arm!";
				output += " Has " + zTarget.arms() + " arm(s) left.";
				
				// Handle dropping zombie's current weapon
				if (zTarget.arms() == 1) {
					// if the zombie dropped an arm and has 1 left:
					// 50% chance to drop weapon
					float c = rand.nextFloat();
						if (c < 0.5) {
							if (zTarget.getWeapon() instanceof WeaponItem) {
								output += "\n" + zTarget + " dropped it's " + zTarget.getWeapon() + '!';
								map.locationOf(zTarget).addItem((Item) zTarget.getWeapon());
								zTarget.removeItemFromInventory((Item) zTarget.getWeapon());
							}
						}

				} else {
					// if the zombie dropped an arm and has 0 left:
					// 100% chance to drop weapon
					if (zTarget.getWeapon() instanceof WeaponItem) {
						output += "\n" + zTarget + " dropped it's " + zTarget.getWeapon() + '!';
						map.locationOf(zTarget).addItem((Item) zTarget.getWeapon());
						zTarget.removeItemFromInventory((Item) zTarget.getWeapon());
					}
				}
			}
		}
		return output;
	}

	// Handle Zombie attack mechanics
	protected String handleZombieAttack(Zombie zActor, GameMap map) {
		Weapon weapon = zActor.getWeapon();
		String output = "";
		
		// Calculates if the attack hit based on predefined hit chances
		boolean hit = calculateHit(zActor, weapon.verb());
		if (hit) {
			// deal damage and append to output
			int damage = weapon.damage();
			target.hurt(damage);
			output += zActor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";
			// Heal on bite attack hit
			if (weapon.verb() == "bites") {
				zActor.heal(5);
				output += "\n" + zActor + " heals 5 points from biting " + target;
			}
			// if target is a zombie, handle limb dropping mechanic
			if (target.hasCapability(ZombieCapability.UNDEAD)) {
				output += handleZombieLimb(map);
			}
		} else {
			output += zActor + " misses " + target + ".";
		}
		return output;
	}

	// Handle non-Zombie actor attack mechanics
	protected String handleActorAttack(Actor actor, GameMap map) {
		Weapon weapon = actor.getWeapon();
		String output = "";

		// Calculates if the attack hit based on predefined hit chances
		boolean hit = calculateHit(actor, weapon.verb());
		if (hit) {
			// deal damage and append to output
			int damage = weapon.damage();
			target.hurt(damage);
			output += actor + " " + weapon.verb() + " " + target + " for " + damage + " damage.";
			// if target is a zombie, handle limb dropping mechanic
			if (target.hasCapability(ZombieCapability.UNDEAD)) {
				output += handleZombieLimb(map);
			}
		} else {
			output += actor + " misses " + target + ".";
		}

		return output;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		String result = "";
		
		// if actor is a zombie, downcast actor to zombie and use handleZombieAttack() instead
		if (actor.hasCapability(ZombieCapability.UNDEAD)) {
			Zombie zActor = (Zombie) actor;
			result += handleZombieAttack(zActor, map);
		} else {
			result += handleActorAttack(actor, map);
		}

		if (!target.isConscious()) {
			Item corpse = new PortableItem("dead " + target, '%');
			map.locationOf(target).addItem(corpse);
			
			Actions dropActions = new Actions();
			for (Item item : target.getInventory())
				dropActions.add(item.getDropAction());
			for (Action drop : dropActions)		
				drop.execute(target, map);
			map.removeActor(target);	
			
			result += System.lineSeparator() + target + " is killed.";
			return result;
		}
		return result;
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " attacks " + target;
	}
}
