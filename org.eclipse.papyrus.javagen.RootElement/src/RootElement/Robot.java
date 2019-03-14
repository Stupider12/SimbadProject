// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement;

import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;

/************************************************************/
/**
 * 
 */
public abstract class Robot extends Agent {
	/**
	 * 
	 */
	public RangeSensorBelt bumpers;
	public RangeSensorBelt sonars;
	public boolean myTurn = false;
	/**
	 * 
	 */
	public Robot(Vector3d position, String name){
		super(position, name);
	}

	/**
	 * 
	 */
	public abstract void initBehavior();
	
	public void avoidObstacle(double l, double fl, double f, double fr, double r){
    	if((fl < fr) && fl < 0.5){
    		rotateY(-5);
    	} else if ((fr < fl)  && fr < 0.5) {
    		rotateY(5);
    	} else if((fr > f) && f < 0.5 && (fl > f)){
    		rotateY(-5);
    	} else if (l < 0.3){
    		rotateY(-5);
    	} else if (r < 0.3){
    		rotateY(5);
    	}	    
    }
	/**
	 * 
	 */
	public boolean itExists(){
		if(this.detachedFromSceneGraph){
			return false;
		}
		return true;
	}
	public abstract boolean missionComplete();
	
	public abstract void performBehavior();

};
