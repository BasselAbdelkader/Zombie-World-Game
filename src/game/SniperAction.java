package game;

import edu.monash.fit2099.interfaces.ActionInterface;

/**
 * Interface to group Actions used by Sniper Rifle, also allows type checking for SniperAction.
 */
public interface SniperAction extends ActionInterface {
	/**
	 * Returns Sniper Rifle tick that this action was created at
	 * @return tick that this action was instantiated on.
	 */
	public int tickStarted();
}
