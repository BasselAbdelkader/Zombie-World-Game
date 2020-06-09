package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;

/**
 * Sniper Rifle weapon, subclasses of SniperAction refer to this object to know the state of the sniping process
 */
public class SniperRifle extends RangedWeapon {
	protected Actor target;
	protected boolean targetLost = false;
	protected boolean abortProcess = false;
	protected int turnsSpentAiming = 0;
	protected int prevHp;
	
	protected final int MAX_AIMING_TURNS = 2;
	protected final int MAX_AMMO = 25;
	
	// Corresponding hit chances when aiming at target for 0, 1 and 2 turns
	protected final double TURNS_AIMING_HIT_0 = 0.75;
	protected final double TURNS_AIMING_HIT_1 = 0.9;
	protected final double TURNS_AIMING_HIT_2 = 1;
	// Corresponding damage multiplier when aiming at target for 0, 1 and 2 turns
	protected final int TURNS_AIMING_DAMAGE_MULTIPLIER_0 = 1;
	protected final int TURNS_AIMING_DAMAGE_MULTIPLIER_1 = 2;
	// INSTAKILL case:
	// Assumes damage*3 is enough to instakill any actor in the game
	protected final int TURNS_AIMING_DAMAGE_MULTIPLIER_2 = 3;
	
	/**
	 * Constructor, instantiate with fields defined in WeaponStats.SNIPER_RIFLE
	 */
	public SniperRifle() {
		super(WeaponStats.SNIPER_RIFLE.weaponName(),
				WeaponStats.SNIPER_RIFLE.weaponChar(),
				WeaponStats.SNIPER_RIFLE.weaponDamage(),
				WeaponStats.SNIPER_RIFLE.weaponVerb()
		);
	}
	
	/**
	 * Increases ammo by amount given, up to MAX_AMMO
	 */
	@Override
	public void increaseAmmo(int amount) {
		ammo = Math.min(ammo + amount, MAX_AMMO);
	}
	
	/**
	 * Stores player's Hp at this current moment, used to check if player took damage during sniping process
	 * @param hp Player's current Hp
	 */
	public void setStartHp(int hp) {
		prevHp = hp;
	}
	
	/**
	 * Increases turns spent aiming at target by one
	 */
	public void aim() {
		turnsSpentAiming += 1;
	}
	
	/**
	 * @return Number of turns the player spent aiming at the target
	 */
	public int turnsSpentAiming() {
		return turnsSpentAiming;
	}
	/**
	 * @return Actor being targeted
	 */
	public Actor target() {
		return target;
	}
	
	/**
	 * Target to be damaged
	 * @param newTarget
	 */
	public void setTarget(Actor newTarget) {
		target = newTarget;
	}
	
	/**
	 * @return Number of ticks this item was in player's inventory since being picked it up
	 */
	public int currentTick() {
		return ticksPassed;
	}

	/**
	 * Reset sniping process
	 */
	public void resetProcess() {
		clearActions();
		try {
			addSniperAction(new SniperSetupAction(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
		turnsSpentAiming = 0;
		targetLost = false;
	}
	/**
	 * Adds the given SniperAction to this weapon
	 * @param action SniperAction to add
	 * @throws Exception when given action does not have type SniperAction
	 */
	public void addSniperAction(Action action) throws Exception {
		if (!(action instanceof SniperAction)) {
			throw new Exception("Tried to add non-SniperAction type Action to Sniper Rifle");
		}
		allowableActions.add(action);
	}
	
	/**
	 * Adds a new SniperAimAction to this weapon
	 */
	public void addAimAction() {
		if (turnsSpentAiming < MAX_AIMING_TURNS) {
			try {
				addSniperAction(new SniperAimAction(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a new SniperFireAction to this weapon
	 */
	public void addFireAction() {
		try {
			addSniperAction(new SniperFireAction(this));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * Allows Player to fire the weapon only after picking up.
	 * Resets sniping process and prints a message if player took damage or interrupted the process.
	 * Prevent firing if out of ammo
	 * Notifies player of current ammo
	 */
	public void tick(Location currentLocation, Actor actor) {
		// Only tick when in Player inventory
		if (actor instanceof Player) {
			for (Action action : allowableActions) {
				if (action instanceof SniperSetupAction) {
					continue;
				}
				// If player started aiming with more hp that he/she currently has,
				// lose the target, reset process
				if (prevHp > ((Player) actor).hp()) {
					resetProcess();
					printResetReason(((Player) actor), "took damage");
				}
				if (currentTick() != ((SniperAction) action).tickStarted()) {
					resetProcess();
					printResetReason(((Player) actor), "interrupted sniping process");
				}
			}
			ticksPassed += 1;
			
			// Add Action to fire after player picks it up
			if (ticksPassed == 1) {
				allowableActions.add(new SniperSetupAction(this));
			}
			
			// Check actor's inventory for ammo and load all suitable ammo
			loadAmmo(actor);
			// Prevent firing if out of ammo
			if (ammo == 0) {
				clearActions();
			} else {
				// We have ammo, add Action to fire if not already present
				if (allowableActions.size() == 0) {
					allowableActions.add(new SniperSetupAction(this));					
				}
			}
			// Notify player of the weapon's current ammo
			((Player) actor).display().println(ammoMessage(ammo));
		}
	}
	
	/**
	 * Ensure Player cannot fire the weapon before picking it up, 
	 */
	public void tick(Location currentLocation) {
		// If item is on the ground, return it to same state as before it was picked up, but keep ammo
		allowableActions.clear();
		ticksPassed = 0;
		// reset sniping process state attributes
		turnsSpentAiming = 0;
		targetLost = false;
	}
	
	/**
	 * Formats and prints given reason to player's display
	 * @param player Player instance
	 * @param reason Reason for losing concentration
	 */
	private void printResetReason(Player player, String reason) {
		player.display().println("Sniper Rifle target lost! " + player + " " + reason);
	}

	/**
	 * @return Returns hit chance based on turns spent aiming
	 */
	@Override
	public double hitChance() {
		if (turnsSpentAiming == 0) {
			return TURNS_AIMING_HIT_0;
		} else if (turnsSpentAiming == 1) {
			return TURNS_AIMING_HIT_1;
		} else {
			return TURNS_AIMING_HIT_2;
		}
	}
	
	/**
	 * @return Returns damage value based on turns spent aiming.
	 */
	@Override
	public int damage() {
		if (turnsSpentAiming == 0) {
			return super.damage()*TURNS_AIMING_DAMAGE_MULTIPLIER_0;
		} else if (turnsSpentAiming == 1) {
			return super.damage()*TURNS_AIMING_DAMAGE_MULTIPLIER_1;
		} else {
			return super.damage()*TURNS_AIMING_DAMAGE_MULTIPLIER_2;
		}
	}
}
