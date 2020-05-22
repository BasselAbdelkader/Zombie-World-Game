package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.WeaponItem;

/**
 * Action that enables conversion of one weapon into another
 */
public class CraftWeaponAction extends Action {
	protected WeaponItem original;
	protected WeaponStats UpgradedWeapon;
	/**
	 * Constructor, store reference to original WeaponItem and constant with fields containing
	 * values of parameters used to instantiate a new CraftedWeapon 
	 */
	public CraftWeaponAction(WeaponItem baseWeapon, WeaponStats newWeapon) {
		original = baseWeapon;
		UpgradedWeapon = newWeapon;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		// For player only
		// If clause not really necessary since humans cant pickup items and zombies cant  
		// craft them after picking it up
		if (actor instanceof Player) {
			actor.removeItemFromInventory(original);
			actor.addItemToInventory(new CraftedWeapon(
				UpgradedWeapon.weaponName(),
				UpgradedWeapon.weaponChar(),
				UpgradedWeapon.weaponDamage(),
				UpgradedWeapon.weaponVerb()));
		}
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " crafts " + original + " into " + UpgradedWeapon.weaponName();
	}

}
