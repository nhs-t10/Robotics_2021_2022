package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class NateManager extends FeatureManager {
    private boolean input;
    private boolean found;
    private int position;
    private int currentPosition;
    private double currentPos;
    private boolean clawState = false;
    ManipulationManager hands;

    private double resetOffset;

    public NateManager(ManipulationManager hands){

        this.hands = hands;
    }

    public void foldOutLift(){

    }

    public void toggleClawOpen(){
        if (clawState == false) {
            hands.setServoPosition("nateClaw", 1.0);
            clawState = true;
        }
        else if (clawState == true){
            hands.setServoPosition("nateClaw", 0.0);
            clawState = false;
        }
        else{
            FeatureManager.logger.log("This message should not appear, if it does, we have realized a quantum claw that is both open and closed at the same time.");
        }
    }

    public void emergencyStop(){
//        currentPos = hands.getMotorPosition("ClawMotor");
//        currentPosition = (int) Math.round(currentPos);
//        hands.encodeMoveToPosition("ClawMotor", currentPosition, 0.75);
        hands.cancelEncodedMovement("ClawMotor");
    }

    public void positionOne(){
        if (!found){
            position = -1490;
        }
        else {
            position = -3470;
        }


        hands.encodeMoveToPosition("ClawMotor", position, 0.75);
    }

    public void positionTwo(){
        if(!found) {
            position = -3024;
        } else{
            position = -5295;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 0.75);
    }

    public void positionThree(){
        if(!found) {
            position = -4679;
        } else {
            position = -6893;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 0.75);
    }

    public void positionHome(){
        if(!found){
            position = 2537;
        } else {
            position = 570;
        }

        hands.encodeMoveToPosition("ClawMotor", position, 0.75);
    }

    public void homing(){
        if (input) {
            found = true;
            hands.setMotorPower("ClawMotor",0);
            hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (found) {
            hands.setMotorPower("ClawMotor", PaulMath.proportionalPID((float) hands.getMotorPosition("ClawMotor"),0));
        } else {
            hands.setMotorPower("ClawMotor", -0.75);
        }
    }
}
