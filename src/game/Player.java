package game;

import java.util.List;

import edu.monash.fit2099.engine.Action;
import edu.monash.fit2099.engine.Actions;
import edu.monash.fit2099.engine.Display;
import edu.monash.fit2099.engine.Exit;
import edu.monash.fit2099.engine.GameMap;
import edu.monash.fit2099.engine.Location;
import edu.monash.fit2099.engine.Menu;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Class representing the Player.
 */
public class Player extends Human {
	private Display gameDisplay;
	private boolean allowDisplayAccess = false;

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

	public int hp() {
		return hitPoints;
	}
	
	public Display display() {
		if (allowDisplayAccess) {
			return gameDisplay;
		}
		return null;
	}
	
	public Menu menu() {
		return menu;
	}

	

	@Override
	public Action playTurn(Actions actions, Action lastAction, GameMap map, Display display) {
		gameDisplay = display;
		allowDisplayAccess = true;

		// if (lastAction.getNextAction() != null) {
		// 	return lastAction.getNextAction();			
		// }
		Action chosenAction = menu.showMenu(this, actions, display);
		

		return chosenAction;
	}
}
