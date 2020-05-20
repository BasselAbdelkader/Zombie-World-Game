package game;

import edu.monash.fit2099.engine.Actor;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.WeaponItem;

public class ZombieArm extends WeaponItem {
	protected static String craftedName = "Zombie Club";
	protected static char craftedChar = 'C';
	protected static int craftedDamage = 20;
	protected static String craftedVerb = "clubs";
	protected int ticksPassed = 0;
	
	public ZombieArm() {
		super("Zombie Arm", 'a', 15, "slaps");
	}
	
	public void tick(Location currentLocation, Actor actor) {
		// Player can only craft the arm 3 turns after picking it up.
		if (ticksPassed == 3) {
			allowableActions.add(new CraftWeaponAction(
				this, craftedName, craftedChar, craftedDamage, craftedVerb));
		}
		ticksPassed += 1;
	}
}
