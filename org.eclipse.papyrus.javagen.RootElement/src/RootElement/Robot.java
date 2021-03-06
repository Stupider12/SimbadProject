// --------------------------------------------------------
// Code generated by Papyrus Java
// --------------------------------------------------------

package RootElement;

import java.util.ArrayList;
import java.util.Random;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import simbad.sim.Agent;
import simbad.sim.RangeSensorBelt;
import simbad.sim.LampActuator;

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
	public LampActuator lamp;
	public boolean myTurn = false;
	private String mode;
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
	
	public String getMode(){
		return this.mode;
	}
	
	public void setMode(String str){
		if(str == "discover" | str == "found"){
			if(this instanceof Mapper){
				this.mode = str;							
			} else {
				System.err.println("mapper robot can't access this mode");				
			}
		} else if (str == "reach"){
			if(this instanceof Rescuer){
				this.mode = str;
			} else {
				System.err.println("rescuer robot can't access this mode");
			}
		} else if(str == "minorFault" | str == "severeFault" | str == "done" | str == "avoid"){
			this.mode = str;
		} else {	
			System.err.println("The selected mode is incorrect");
		}
	}
	
	public void obstacleDetected(){
		double leftSonar = sonars.getMeasurement(4);
		double rightSonar = sonars.getMeasurement(2);
		
		if((sonars.getFrontQuadrantHits() > 0) || rightSonar < 0.3 || leftSonar < 0.3){
			setMode("avoid");
		} else {
			if(this instanceof Mapper){
				setMode("discover");
			} else if (this instanceof Rescuer){
				setMode("rescue");
			}
		}
	}
	
	public void avoidObstacle(double l, double fl, double f, double fr, double r){
    	if((fl < fr) && fl < 0.5){
    		rotateY(-15);
    	} else if ((fr < fl)  && fr < 0.5) {
    		rotateY(15);
    	} else if((fr > f) && f < 0.5 && (fl > f)){
    		rotateY(-15);
    	} else if (l < 0.3){
    		rotateY(-15);
    	} else if (r < 0.3){
    		rotateY(15);
    	}	    
    }
	/**
	 * 
	 */
	public String direction(){
		Point3d agentLoc = new Point3d();
		this.getCoords(agentLoc);
		
		agentLoc = null;
		return "mm";
	}
	
	public boolean itExists(){
		if(this.detachedFromSceneGraph){
			return false;
		}
		return true;
	}
	
	//simulates an hardware problem
	public void isWorking(){
		
		if(this.getTranslationalVelocity()!=0){
			lamp.setOn(true);
		} else {
			lamp.setOn(false);
		}
		
		Random randomNum = new Random();
		int hardwareValue = randomNum.nextInt(1000000)+100;
		
		String hardwareValueString = Integer.toString(hardwareValue);
		int counter = 1;
			
		for (int i=0; i< hardwareValueString.length()-1;i++){
			if (hardwareValueString.charAt(i) == hardwareValueString.charAt(i+1)){
				counter += 1 ;
			} else {
				counter = 1;
			}
		}
		
		//TODO maybe limit error for numbers with more than 3 digits?
		//HAS to be very unlikely
		if(counter == hardwareValueString.length() && ((counter==3)||(counter==4))){
			System.out.println("Sensor Fault: Reducing speed");
			setTranslationalVelocity(0.2);
			setRotationalVelocity(0);
			lamp.setBlink(true);
			setMode("minorFault");
		}
		else if(counter == hardwareValueString.length() && counter == 5){
			System.out.println("Camera Fault: Returning to base for repair");
			lamp.setBlink(true);
			moveToStartPosition();
			lamp.setOn(true);
			setMode("severeFault");
		}
		
		else if (counter == hardwareValueString.length() && counter == 6){
			System.out.println("Severe Hardware Fault: Shutting Down");
			setTranslationalVelocity(0);
			lamp.setOn(false);
			setMode("severeFault");
		} 
	}
	
	public void setLocations(ArrayList<Point3d> pos){};
	
	public abstract void performBehavior();

};

