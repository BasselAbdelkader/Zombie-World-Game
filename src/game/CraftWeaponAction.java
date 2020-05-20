package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.WeaponItem;

public class CraftWeaponAction extends Action {
	protected WeaponItem original;
	protected String craftedName;
	protected char craftedChar;
	protected int craftedDamage;
	protected String craftedVerb;

	public CraftWeaponAction(
		WeaponItem baseWeapon,
		String newName,
		char newChar,
		int newDamage,
		String newVerb) {
		
		original = baseWeapon;
		craftedName = newName;
		craftedChar = newChar;
		craftedDamage = newDamage;
		craftedVerb = newVerb;
	}

	@Override
	public String execute(Actor actor, GameMap map) {
		// For player only
		// not really necessary since humans cant pickup items and zombies cant craft them after picking up
		if (actor instanceof Player) {
			actor.removeItemFromInventory(original);
			actor.addItemToInventory(new CraftedWeapon(
				craftedName, craftedChar, craftedDamage, craftedVerb));
		}
		return menuDescription(actor);
	}

	@Override
	public String menuDescription(Actor actor) {
		return actor + " crafts " + original + " into " + craftedName;
	}

}
