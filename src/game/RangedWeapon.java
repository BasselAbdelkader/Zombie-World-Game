package game;

import java.util.ArrayList;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Item;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Weapon;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Cannot be used as melee weapon, uses ammo.
 */
public abstract class RangedWeapon extends WeaponItem {
	protected int ticksPassed = 0;
	protected int ammo;
	protected final int MIN_AMMO = 0;
	
	/**
	 * Constructor
	 * @param name Weapon name
	 * @param displayChar Weapon display char
	 * @param damage Weapon damage
	 * @param verb Weapon verb
	 */
	public RangedWeapon(String name, char displayChar, int damage, String verb) {
		super(name, displayChar, damage, verb);
	}
	/**
	 * Prevents Ranged Weapon from being used as melee weapon by AttackAction
	 */
	@Override
	public Weapon asWeapon() {
		return null;
	}
	
	public void clearActions() {
		allowableActions.clear();
	}
	/**
	 * Returns hit chance
	 */
	public abstract double hitChance();
	
	/**
	 * @return Current amount of ammo in the weapon
	 */
	public int currentAmmo() {
		return ammo;
	}
	
	/**
	 * Decreases ammo in the weapon, will not decrease ammo below MIN_AMMO
	 * @param amount to decrease
	 */
	public void decreaseAmmo(int amount) {
		ammo = Math.max(ammo - amount, MIN_AMMO);
	}
	
	public abstract void increaseAmmo(int amount);
	
	protected void loadAmmo(Actor actor) {
		int ammoFound = 0;
		ArrayList<Item> boxes = new ArrayList<Item>();
		for (Item item : actor.getInventory()) {
			// Find ammo for this weapon in actor's inventory
			// increase ammo count by amount found
			if (item instanceof AmmoBox &&
				((AmmoBox) item).ammoType() == this.toString()) {
				ammoFound += ((AmmoBox) item).amount();
				boxes.add(item);
			}
		}
		increaseAmmo(ammoFound);
		// Remove used ammo boxes from actor's inventory
		for (Item item : boxes) {
			actor.removeItemFromInventory(item);
		}
	}
	
	protected String ammoMessage(int currentAmmo) {
		if (currentAmmo == 0) {
			return this + " is out of ammo!";			
		}
		return "Remaining " + this + " ammo: " + currentAmmo;
	}
}
