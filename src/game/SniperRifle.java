package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;

/**
 * Sniper Rifle weapon, subclasses of SniperAction refer to this object to know the state of the sniping process
 */
public class SniperRifle extends RangedWeapon {
	protected int ticksPassed = 0;
	protected Actor target;
	protected boolean targetLost = false;
	protected boolean abortProcess = false;
	protected int turnsSpentAiming = 0;
	protected int prevHp;
	
	protected final int MAX_AIMING_TURNS = 2;
	protected final int MAX_AMMO = 25;
	
	public SniperRifle() {
		super(WeaponStats.SNIPER_RIFLE.weaponName(),
				WeaponStats.SNIPER_RIFLE.weaponChar(),
				WeaponStats.SNIPER_RIFLE.weaponDamage(),
				WeaponStats.SNIPER_RIFLE.weaponVerb()
		);
	}
	
	public void increaseAmmo(int amount) {
		ammo = Math.min(ammo + amount, MAX_AMMO);
	}

	public void setStartHp(int hp) {
		prevHp = hp;
	}
	
	public void aim() {
		turnsSpentAiming += 1;
	}
	
	public int turnsSpentAiming() {
		return turnsSpentAiming;
	}
	
	public Actor target() {
		return target;
	}
	
	public void setTarget(Actor newTarget) {
		target = newTarget;
	}

	public int currentTick() {
		return ticksPassed;
	}
	
	public void clearActions() {
		allowableActions.clear();
	}

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
	
	public void addSniperAction(Action action) throws Exception {
		if (!(action instanceof SniperAction)) {
			throw new Exception("Tried to add non-SniperAction type Action to Sniper Rifle");
		}
		allowableActions.add(action);
	}

	public void addAimAction() {
		if (turnsSpentAiming < MAX_AIMING_TURNS) {
			try {
				addSniperAction(new SniperAimAction(this));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

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
		}
		// Add Action to fire after player picks it up
		if (ticksPassed == 1) {
			allowableActions.add(new SniperSetupAction(this));
		}
		// Remove Action to fire if out of ammo
		// Notify player by printing to their display
	}
	/**
	 * Formats and prints given reason to player's display
	 * @param player Player instance
	 * @param reason Reason for losing concentration
	 */
	private void printResetReason(Player player, String reason) {
		player.display().println("Sniper Rifle target lost! " + player + " " + reason);
	}
}
