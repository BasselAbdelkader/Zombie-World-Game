package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;
import java.util.Random;

/**
 * A Zombie.
 * 
 * This Zombie is pretty boring.  It needs to be made more interesting.
 * 
 * @author ram
 *
 */
public class Zombie extends ZombieActor {
	private Behaviour[] behaviours = {
			new PickUpBehaviour(),
			new AttackBehaviour(ZombieCapability.ALIVE),
			new HuntBehaviour(Human.class, 10),
			new WanderBehaviour()
	};

	public Zombie(String name) {
		super(name, 'Z', 100, ZombieCapability.UNDEAD);
	}
	/**
	 * Random number generator
	 */
	protected Random rand = new Random();
	/**
	 * Number of arms, starts with 2 and cannot lose more than that
	 */
	protected int arms = 2;
	/**
	 * Number of legs, starts with 2 and cannot lose more than that
	 */
	protected int legs = 2;
	/**
	 * true if zombie tried to move previous turn and did not.
	 */
	protected boolean skippedPrev = false;
	// Zombie punch damage
	private final int BITE_DAMAGE = 15
	// Zombie bite damage
	private final int PUNCH_DAMAGE = 11

	/**
	 * Getter for legs attribute
	 * @return current number of legs the Zombie has
	 */
	public int legs() {
		return legs;
	}
	/**
	 * Getter for arms attribute
	 * @return current number of arms the Zombie has
	 */
	public int arms() {
		return arms;
	}
	/**
	 * Tries to remove a limb from the Zombie, returns result of the operation
	 * @return String "leg" or "arm" if removed dropped an arm or leg, "none" if did \
	not remove any limb.
	 */
	public String dropLimb() {
		// Cases when we return early:
		// no limbs left to drop, return "none"
		if (arms == 0 && legs == 0) {
			return "none";
		}
		// no arms left to drop, drop leg
		if (arms == 0) {
			legs -= 1;
			return "leg";
		}
		// no legs left to drop, drop arm
		if (legs == 0) {
			arms -= 1;
			return "arm";
		}
		
		// can drop either arm or leg
		// if limbIndex = 0 then drop arm
		// if limbIndex = 1 then drop drop
		int limbIndex = rand.nextInt(2);
		String droppedLimb;
		// if the drop fails, loop again to randomly choose a limb again
		if (limbIndex == 0) {
			arms -= 1;
			droppedLimb = "arm";
		} else {
			legs -= 1;
			droppedLimb = "leg";

		}
		return droppedLimb;
	}
	/**
	 * Getter for skippedPrev attribute
	 * @return true if Zombie did not move in the previous turn, false if it has
	 */
	public boolean hasSkipped() {
		return skippedPrev;
	}
	/**
	 * Toggles value of skippedPrev attribute
	 */
	public void toggleSkip() {
		skippedPrev = !skippedPrev;
	}

	@Override
	public IntrinsicWeapon getIntrinsicWeapon() {
		double chance = rand.nextDouble();
		// initialize weapon
		// default case: zombie punches
		IntrinsicWeapon weapon = new IntrinsicWeapon(PUNCH_DAMAGE, "punches");

		// if zombie has 2 arms
		// 50% chance for zombie to bite rather than punch
		if (arms == 2) {
			if (chance < 0.5) {
				weapon = new IntrinsicWeapon(BITE_DAMAGE, "bites");
			}
		} else if (arms == 1) {
		// if zombie has 1 arm
		// 75% chance for zombie to bite rather than punch
			if (chance < 0.75) {
				weapon = new IntrinsicWeapon(BITE_DAMAGE, "bites");
			}
		} else {
		// if zombie has 0 arms, 100% chance to bite
			weapon = new IntrinsicWeapon(BITE_DAMAGE, "bites");
		}

		return weapon;
	}

	/**
	 * If a Zombie can pick up a weapon at it's current location, it will.
	 * If it has a weapon or none is foound, it will try to attack.
	 * If no one is nearby, it will chase any human within 10 spaces.
	 * if no human is found, it will wander randomly.
	 * 
	 * @param actions list of possible Actions
	 * @param lastAction previous Action, if it was a multiturn action
	 * @param map the map where the current Zombie is
	 * @param display the Display where the Zombie's utterances will be displayed
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// 10% chance say braains each turn
		double chance = rand.nextDouble();
		if (chance < 0.1) {
			display.println(this + " says: " + "Braaaaaaains");
		}
		
		for (Behaviour behaviour : behaviours) {
			Action action = behaviour.getAction(this, map);
			if (action != null)
				return action;
		}
		return new DoNothingAction();	
	}
}
