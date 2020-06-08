package game;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Menu;
import edu.monash.fit2099.engine.Weapon;

/**
 * Class representing the Player.
 */
public class Player extends Human {
	private Display gameDisplay;
	private boolean allowDisplayAccess = false;
	
	// If activeWeapon is not null, activeWeapon is used by Player to attack
	private Weapon activeWeapon;

	private Menu menu = new Menu();

	/**
	 * Constructor.
	 *
	 * @param name        Name to call the player in the UI
	 * @param displayChar Character to represent the player in the UI
	 * @param hitPoints   Player's starting number of hitpoints
	 */
	public Player(String name, char displayChar, int hitPoints) {
		super(name, displayChar, hitPoints);
	}
	
	/**
	 * @return Return Player's current Hp
	 */
	public int hp() {
		return hitPoints;
	}
	
	/**
	 * @return Display object
	 */
	public Display display() {
		if (allowDisplayAccess) {
			return gameDisplay;
		}
		return null;
	}
	
	/**
	 * @return Return menu object
	 */
	public Menu menu() {
		return menu;
	}
	
	/**
	 * Sets the Player's active weapon, if set will be prioritized by getWeapon()
	 * @param weapon
	 */
	public void setActiveWeapon(RangedWeapon weapon) {
		if (weapon != null) {
			activeWeapon = weapon;			
		}
	}
	
	/**
	 * Resets Player's activeWeapon to null
	 */
	public void resetActiveWeapon() {
		activeWeapon = null;
	}
	
	/**
	 * Return the weapon that should be used to Attack. If activeWeapon has not been set, use original Actor getWeapon()
	 */
	@Override
	public Weapon getWeapon() {
		if (activeWeapon != null) {
			return activeWeapon;
		}
		return super.getWeapon();
	}

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		// Expose game display to be used by RangedWeapons to print information
		gameDisplay = display;
		allowDisplayAccess = true;

		Action chosenAction = menu.showMenu(this, actions, display);
		

		return chosenAction;
	}
}
