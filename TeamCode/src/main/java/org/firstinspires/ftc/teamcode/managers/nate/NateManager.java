package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class NateManager extends FeatureManager {
    private boolean input;
    private boolean found;
    ManipulationManager hands;

    public NateManager(ManipulationManager hands){
       this.hands = hands;
    }

    public void foldOut(){

    }
    public void homing(){
        if (input) {
            boolean found=true;
            hands.setMotorPower("outake",0);
            hands.setMotorMode("outake", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        }
        if (found) {
            hands.setMotorPower("outake",-1);
        } else {
            hands.setMotorPower("outake", PaulMath.proportionalPID((float) hands.getMotorPosition("outake"),0));
        }
    }
}
