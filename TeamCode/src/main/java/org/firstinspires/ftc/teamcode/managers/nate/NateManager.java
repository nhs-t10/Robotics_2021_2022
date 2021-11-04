package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class NateManager extends FeatureManager {
    private boolean input;
    private boolean found;

    public void foldOut(){

    }
    public void homing(){
        ManipulationManager manipulationManager = null;
        if (input) {
            boolean found=true;
            //DcMotor.RunMode.STOP_AND_RESET_ENCODER;
        }
        if (found) {
            manipulationManager.setMotorPower("outake",-1);
        } else {
            manipulationManager.setMotorPower("Outake", PaulMath.proportionalPID(0,0));
        }
    }
}
