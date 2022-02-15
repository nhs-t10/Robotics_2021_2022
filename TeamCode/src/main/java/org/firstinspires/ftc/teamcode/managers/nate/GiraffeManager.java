package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;

public class GiraffeManager extends FeatureManager {
    private TouchSensor input;
    private boolean neckFound;
    private int neckPosition;
    private int neckPositionTall;
    private int neckPositionShort;
    NateManager lift;

    private double resetOffset;

    public GiraffeManager(NateManager lift/*, TouchSensor input*/){
        /*this.input = input;*/
        this.lift = lift;
    }

    public GiraffeManager(NateManager lift, TouchSensor input) {
        this.lift = lift;
        this.input = input;
    }

    public void neckEmergencyStop(){
        lift.hands.encodeMoveToPosition("NeckMotor", (int)lift.hands.getMotorPosition("NeckMotor"), 0);
    }


    public boolean neckMovementFinished() {
        return !lift.hands.hasEncodedMovement("NeckMotor");
    }

    public void neckTall(){
        neckPosition = neckPositionTall;
        lift.hands.encodeMoveToPosition("NeckMotor", neckPositionTall);
    }

    public void neckShort(){
        neckPosition = neckPositionShort;
        lift.hands.encodeMoveToPosition("NeckMotor", neckPositionShort);
    }

    public void neckNeutral(){
        neckPosition = 0;
        lift.hands.encodeMoveToPosition("NeckMotor", 0);
    }

    public boolean neckHoming(){
        //`input` will always be false for now, but we need to replace it with `sensor.isPressed()` or whichever the correct method is
        if (neckFound) {
            lift.hands.encodeMoveToPosition("NeckMotor", 0, 0.25);
        } else if (input.isPressed()) {
            neckFound = true;
            neckPositionTall = 5;
            neckPositionShort = -5;
            lift.hands.setMotorPower("NeckMotor",0);
            lift.hands.setMotorMode("NeckMotor", DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        } else {
            lift.hands.setMotorPower("NeckMotor", -0.75);
        }
        return neckFound;
    }

    public int getNeckPosition() {
        if(neckPosition == 0) return 0;
        else if(neckPosition == neckPositionShort) return 1;
        else if(neckPosition == neckPositionTall) return 2;

        return -1;
    }
}
