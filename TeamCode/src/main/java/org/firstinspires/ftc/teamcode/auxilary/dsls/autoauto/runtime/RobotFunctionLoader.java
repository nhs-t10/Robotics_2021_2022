package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions.*;
import org.firstinspires.ftc.teamcode.managers.ImuManager;
import org.firstinspires.ftc.teamcode.managers.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.MovementManager;
import org.firstinspires.ftc.teamcode.managers.SensorManager;

import java.util.ArrayList;

public class RobotFunctionLoader {

    public static void addFunctionsToStore(NativeRobotFunction[] fns, AutoautoRuntimeVariableScope store) {
        for(NativeRobotFunction f : fns) store.put(f.name, f);
    }

    public static NativeRobotFunction[] loadFunctions(MovementManager driver, ManipulationManager manip, SensorManager sense, ImuManager imu) {
        ArrayList<NativeRobotFunction> functions = new ArrayList<>();

        DriveOmniFunction driveOmni = new DriveOmniFunction(driver); driveOmni.setName("driveOmni");  functions.add(driveOmni);
        StopDriveFunction stopDrive = new StopDriveFunction(driver); stopDrive.setName("stopDrive");  functions.add(stopDrive);
        GetScaleFunction getScale = new GetScaleFunction(driver); getScale.setName("getScale");  functions.add(getScale);
        DownScaleFunction downScale = new DownScaleFunction(driver); downScale.setName("downScale");  functions.add(downScale);
        RunUsingEncodersFunction runUsingEncoders = new RunUsingEncodersFunction(driver); runUsingEncoders.setName("runUsingEncoders");  functions.add(runUsingEncoders);
        ResetEncodersFunction resetEncoders = new ResetEncodersFunction(driver); resetEncoders.setName("resetEncoders");  functions.add(resetEncoders);
        DriveVerticalFunction driveVertical = new DriveVerticalFunction(driver); driveVertical.setName("driveVertical");  functions.add(driveVertical);
        RunToPositionFunction runToPosition = new RunToPositionFunction(driver); runToPosition.setName("runToPosition");  functions.add(runToPosition);
        UpScaleFunction upScale = new UpScaleFunction(driver); upScale.setName("upScale");  functions.add(upScale);
        DriveRawFunction driveRaw = new DriveRawFunction(driver); driveRaw.setName("driveRaw");  functions.add(driveRaw);
        DriveOmniExponentialFunction driveOmniExponential = new DriveOmniExponentialFunction(driver); driveOmniExponential.setName("driveOmniExponential");  functions.add(driveOmniExponential);
        RunWithOutEncodersFunction runWithOutEncoders = new RunWithOutEncodersFunction(driver); runWithOutEncoders.setName("runWithOutEncoders");  functions.add(runWithOutEncoders);
        SetTargetPositionsFunction setTargetPositions = new SetTargetPositionsFunction(driver); setTargetPositions.setName("setTargetPositions");  functions.add(setTargetPositions);
        GetMotorPositionsFunction getMotorPositions = new GetMotorPositionsFunction(driver); getMotorPositions.setName("getMotorPositions");  functions.add(getMotorPositions);
        DriveWithVerticalFunction driveWithVertical = new DriveWithVerticalFunction(driver); driveWithVertical.setName("driveWithVertical");  functions.add(driveWithVertical);
        GetTicksFunction getTicks = new GetTicksFunction(driver); getTicks.setName("getTicks");  functions.add(getTicks);
        GetHorizontalTicksFunction getHorizontalTicks = new GetHorizontalTicksFunction(driver); getHorizontalTicks.setName("getHorizontalTicks");  functions.add(getHorizontalTicks);
        GetVerticalTicksFunction getVerticalTicks = new GetVerticalTicksFunction(driver); getVerticalTicks.setName("getVerticalTicks");  functions.add(getVerticalTicks);

        SetMotorPowerFunction setMotorPower = new SetMotorPowerFunction(manip); setMotorPower.setName("setMotorPower");  functions.add(setMotorPower);
        SetServoPositionFunction setServoPosition = new SetServoPositionFunction(manip); setServoPosition.setName("setServoPosition");  functions.add(setServoPosition);
        SetServoPowerFunction setServoPower = new SetServoPowerFunction(manip); setServoPower.setName("setServoPower");  functions.add(setServoPower);
        GetMotorPowerFunction getMotorPower = new GetMotorPowerFunction(manip); getMotorPower.setName("getMotorPower");  functions.add(getMotorPower);
        GetServoPowerFunction getServoPower = new GetServoPowerFunction(manip); getServoPower.setName("getServoPower");  functions.add(getServoPower);
        RunMotorUsingEncoderFunction runMotorUsingEncoder = new RunMotorUsingEncoderFunction(manip); runMotorUsingEncoder.setName("runMotorUsingEncoder");  functions.add(runMotorUsingEncoder);
        ResetMotorEncoderFunction resetMotorEncoder = new ResetMotorEncoderFunction(manip); resetMotorEncoder.setName("resetEncoders");  functions.add(resetMotorEncoder);

        GetHSLFunction getHSL = new GetHSLFunction(sense); getHSL.setName("getHSL");  functions.add(getHSL);
        IsSpecialFunction isSpecial = new IsSpecialFunction(sense); isSpecial.setName("isSpecial");  functions.add(isSpecial);
        IsSpecialOneFunction isSpecialOne = new IsSpecialOneFunction(sense); isSpecialOne.setName("isSpecialOne");  functions.add(isSpecialOne);

        OmniCalcFunction omniCalc = new OmniCalcFunction(); omniCalc.setName("omniCalc");  functions.add(omniCalc);
        ProportionalPIDFunction proportionalPID = new ProportionalPIDFunction(); proportionalPID.setName("proportionalPID");  functions.add(proportionalPID);
        DeltaFunction delta = new DeltaFunction(); delta.setName("delta");  functions.add(delta);
        NormalizeArrayFunction normalizeArray = new NormalizeArrayFunction(); normalizeArray.setName("normalizeArray");  functions.add(normalizeArray);
        CartesianToPolarFunction cartesianToPolar = new CartesianToPolarFunction(); cartesianToPolar.setName("cartesianToPolar");  functions.add(cartesianToPolar);
        RoundToPointFunction roundToPoint = new RoundToPointFunction(); roundToPoint.setName("roundToPoint");  functions.add(roundToPoint);
        PolarToCartesianFunction polarToCartesian = new PolarToCartesianFunction(); polarToCartesian.setName("polarToCartesian");  functions.add(polarToCartesian);
        HighestValueFunction highestValue = new HighestValueFunction(); highestValue.setName("highestValue");  functions.add(highestValue);
        EncoderDistanceFunction encoderDistance = new EncoderDistanceFunction(); encoderDistance.setName("encoderDistance");  functions.add(encoderDistance);
        GetThirdAngleOrientationFunction getThirdAngleOrientation = new GetThirdAngleOrientationFunction(imu); getThirdAngleOrientation.setName("getThirdAngleOrientation");  functions.add(getThirdAngleOrientation);

        return functions.toArray(new NativeRobotFunction[0]);
    }
}
