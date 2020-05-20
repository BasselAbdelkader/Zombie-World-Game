package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.DoNothingAction;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.IntrinsicWeapon;

// new imports
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
	
	// Newly added attributes
	protected Random rand = new Random();
	protected int arms = 2;
	protected int legs = 2;
	protected boolean skippedPrev = false;

	// Newly added methods
	public int legs() {
		return legs;
	}
	public int arms() {
		return arms;
	}
	// Returns string with name of which limb was removed or none if not possible
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

	public boolean hasSkipped() {
		return skippedPrev;
	}
	public void toggleSkip() {
		skippedPrev = !skippedPrev;
	}

	@Override
	public IntrinsicWeapon getIntrinsicWeapon() {
		float chance = rand.nextFloat();
		// initialize weapon
		// default case: zombie punches
		IntrinsicWeapon weapon = new IntrinsicWeapon(10, "punches");

		// if zombie has 2 arms
		// 50% chance for zombie to bite rather than punch
		if (arms == 2) {
			if (chance > 0.5) {
				weapon = new IntrinsicWeapon(15, "bites");
			}
		} else if (arms == 1) {
		// if zombie has 1 arm
		// 75% chance for zombie to bite rather than punch
			if (chance > 0.25) {
				weapon = new IntrinsicWeapon(15, "bites");
			}
		} else {
		// if zombie has 0 arms, 100% chance to bite
			weapon = new IntrinsicWeapon(15, "bites");
		}

		return weapon;
	}

	/**
	 * If a Zombie can attack, it will.  If not, it will chase any human within 10 spaces.  
	 * If no humans are close enough it will wander randomly.
	 * 
	 * @param actions list of possible Actions
	 * @param lastAction previous Action, if it was a multiturn action
	 * @param map the map where the current Zombie is
	 * @param display the Display where the Zombie's utterances will be displayed
	 */
	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// 10% chance say braains each turn
		float chance = rand.nextFloat();
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
