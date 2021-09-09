package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

    import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions.*;
    import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
    import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
    import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
    import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
    
    import java.util.ArrayList;
    
    public class RobotFunctionLoader {
    
        public static void loadFunctions(org.firstinspires.ftc.teamcode.managers.FeatureManager manFeature,org.firstinspires.ftc.teamcode.managers.input.InputManager manInput,org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manManipulation,org.firstinspires.ftc.teamcode.managers.movement.MovementManager manMovement,org.firstinspires.ftc.teamcode.managers.sensor.SensorManager manSensor,org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manTelemetry, AutoautoRuntimeVariableScope scope) {
            scope.put("setIsOpModeRunning", new SetIsOpModeRunningFunction(manFeature));
            
            scope.put("getKey", new GetKeyFunction(manInput));
            scope.put("update", new UpdateFunction(manInput));
            
            scope.put("setServoPosition", new SetServoPositionFunction(manManipulation));
            scope.put("setServoPower", new SetServoPowerFunction(manManipulation));
            scope.put("setMotorPower", new SetMotorPowerFunction(manManipulation));
            scope.put("getMotorPower", new GetMotorPowerFunction(manManipulation));
            scope.put("getServoPower", new GetServoPowerFunction(manManipulation));
            scope.put("getServoPosition", new GetServoPositionFunction(manManipulation));
            scope.put("resetEncoders", new ResetEncodersFunction(manManipulation));
            scope.put("runUsingEncoders", new RunUsingEncodersFunction(manManipulation));
            scope.put("closeMotors", new CloseMotorsFunction(manManipulation));
            
            scope.put("driveRaw", new DriveRawFunction(manMovement));
            scope.put("stopDrive", new StopDriveFunction(manMovement));
            scope.put("driveOmni", new DriveOmniFunction(manMovement));
            scope.put("resetEncodersMovementManager", new ResetEncodersMovementManagerFunction(manMovement));
            scope.put("runToPosition", new RunToPositionFunction(manMovement));
            scope.put("runUsingEncodersMovementManager", new RunUsingEncodersMovementManagerFunction(manMovement));
            scope.put("runWithOutEncoders", new RunWithOutEncodersFunction(manMovement));
            scope.put("setTargetPositions", new SetTargetPositionsFunction(manMovement));
            scope.put("driveVertical", new DriveVerticalFunction(manMovement));
            scope.put("getScale", new GetScaleFunction(manMovement));
            scope.put("upScale", new UpScaleFunction(manMovement));
            scope.put("downScale", new DownScaleFunction(manMovement));
            scope.put("driveWithVertical", new DriveWithVerticalFunction(manMovement));
            scope.put("getTicks", new GetTicksFunction(manMovement));
            scope.put("getHorizontalTicks", new GetHorizontalTicksFunction(manMovement));
            scope.put("getVerticalTicks", new GetVerticalTicksFunction(manMovement));
            
            scope.put("updateSensorManager", new UpdateSensorManagerFunction(manSensor));
            scope.put("getColorInteger", new GetColorIntegerFunction(manSensor));
            scope.put("isSpecial", new IsSpecialFunction(manSensor));
            scope.put("isSpecial1", new IsSpecialOneFunction(manSensor));
            
            scope.put("clear", new ClearFunction(manTelemetry));
            scope.put("clearAll", new ClearAllFunction(manTelemetry));
            scope.put("speak", new SpeakFunction(manTelemetry));
            scope.put("updateTelemetryManager", new UpdateTelemetryManagerFunction(manTelemetry));
            scope.put("isAutoClear", new IsAutoClearFunction(manTelemetry));
            scope.put("setAutoClear", new SetAutoClearFunction(manTelemetry));
            scope.put("getMsTransmissionInterval", new GetMsTransmissionIntervalFunction(manTelemetry));
            scope.put("setMsTransmissionInterval", new SetMsTransmissionIntervalFunction(manTelemetry));
            scope.put("getItemSeparator", new GetItemSeparatorFunction(manTelemetry));
            scope.put("setItemSeparator", new SetItemSeparatorFunction(manTelemetry));
            scope.put("getCaptionValueSeparator", new GetCaptionValueSeparatorFunction(manTelemetry));
            scope.put("setCaptionValueSeparator", new SetCaptionValueSeparatorFunction(manTelemetry));
            scope.put("readData", new ReadDataFunction(manTelemetry));
            scope.put("hasNewData", new HasNewDataFunction(manTelemetry));
            scope.put("getGamepad1Info", new GetGamepadOneInfoFunction(manTelemetry));
            scope.put("getGamepad2Info", new GetGamepadTwoInfoFunction(manTelemetry));
            
        }
    }
    