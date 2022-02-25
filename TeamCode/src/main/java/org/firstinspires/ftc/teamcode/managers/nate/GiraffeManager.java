package org.firstinspires.ftc.teamcode.managers.nate;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

public class GiraffeManager extends FeatureManager {
    private TouchSensor input;
    private boolean neckFound;
    private int neckPosition;
    private int neckPositionTall = -1406;
    private int neckPositionShort = 716;
    private int giraffePositionOne = 504;
    private int giraffePositionTwo = 164;
    private int giraffePositionThree = -194;
    private int giraffePositionShared = 630;

    private int giraffeShortPositionHome = -561;
    private int giraffeTallPositionHome = -402;


    NateManager lift;

    private double resetOffset;

    public GiraffeManager(NateManager lift/*, TouchSensor input*/){
        /*this.input = input;*/
        this.lift = lift;
        lift.positionHomeLocation = giraffeShortPositionHome;
        lift.positionOneLocation = giraffePositionOne;
        lift.positionTwoLocation = giraffePositionTwo;
        lift.positionThreeLocation = giraffePositionThree;
        lift.positionSharedLocation = giraffePositionShared;
    }

    public GiraffeManager(NateManager lift, TouchSensor input) {
        this.lift = lift;
        this.input = input;
        lift.positionHomeLocation = giraffeShortPositionHome;
        lift.positionOneLocation = giraffePositionOne;
        lift.positionTwoLocation = giraffePositionTwo;
        lift.positionThreeLocation = giraffePositionThree;
    }

    public void neckEmergencyStop(){
        lift.hands.encodeMoveToPosition("NeckMotor", (int)lift.hands.getMotorPosition("NeckMotor"), 0);
    }


    public boolean neckMovementFinished() {
        return !lift.hands.hasEncodedMovement("NeckMotor");
    }

    public void neckTall(){
        neckPosition = neckPositionTall;

        boolean isAtHome = lift.getClawPosition() == 0;
        lift.positionHomeLocation = giraffeTallPositionHome;
        if (isAtHome) lift.positionHome();

        lift.hands.encodeMoveToPosition("NeckMotor", neckPositionTall, 0.7);
    }

    public void neckShort(){
        neckPosition = neckPositionShort;

        lift.positionHomeLocation = giraffeShortPositionHome;
        lift.hands.encodeMoveToPosition("NeckMotor", neckPositionShort, 0.7);
    }

    public void neckNeutral(){
        neckPosition = 0;
        lift.hands.encodeMoveToPosition("NeckMotor", 0, 0.3);
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
