package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.WeaponItem;

public class CraftWeaponAction extends Action {
	protected WeaponItem original;
	protected WeaponStats UpgradedWeapon;

	public CraftWeaponAction(WeaponItem baseWeapon, WeaponStats newWeapon) {
		original = baseWeapon;
		UpgradedWeapon = newWeapon;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		// For player only
		// not really necessary since humans cant pickup items and zombies cant craft them after picking up
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
