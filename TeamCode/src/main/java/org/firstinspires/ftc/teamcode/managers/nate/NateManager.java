package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class NateManager extends FeatureManager {
    private boolean input;
    private boolean found;
    private int position;
    private boolean clawState;
    ManipulationManager hands;

    public NateManager(ManipulationManager hands){
       this.hands = hands;
    }

    public void foldOut(){

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

    public void positionOne(){
        position = 1;

        hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hands.setMotorPosition("ClawMotor", position);
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void positionTwo(){
        position = 2;

        hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hands.setMotorPosition("ClawMotor", position);
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void positionThree(){
        position = 3;

        hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        hands.setMotorPosition("ClawMotor", position);
        hands.setMotorMode("ClawMotor", DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void homing(){
        if (input) {
            boolean found = true;
            hands.setMotorPower("ClawMotor",0);
            hands.setMotorMode("ClawMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (!found) {
            hands.setMotorPower("ClawMotor", PaulMath.proportionalPID((float) hands.getMotorPosition("ClawMotor"),0));
        }
    }
}
