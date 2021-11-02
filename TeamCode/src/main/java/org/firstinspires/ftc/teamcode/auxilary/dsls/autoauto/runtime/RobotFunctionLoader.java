package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

    import org.firstinspires.ftc.teamcode.managers.FeatureManager;
    import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.robotfunctions.*;
    import java.util.ArrayList;
    
    public class RobotFunctionLoader {
    
        public static void loadFunctions(AutoautoRuntimeVariableScope scope, FeatureManager... managers) {
            org.firstinspires.ftc.teamcode.managers.FeatureManager manFeature = null;
            org.firstinspires.ftc.teamcode.managers.imu.ImuManager manImu = null;
            org.firstinspires.ftc.teamcode.managers.input.InputManager manInput = null;
            org.firstinspires.ftc.teamcode.managers.macro.MacroManager manMacro = null;
            org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager manManipulation = null;
            org.firstinspires.ftc.teamcode.managers.movement.MovementManager manMovement = null;
            org.firstinspires.ftc.teamcode.managers.sensor.SensorManager manSensor = null;
            org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager manTelemetry = null;
            for(FeatureManager f : managers) {
                if(f instanceof org.firstinspires.ftc.teamcode.managers.FeatureManager) manFeature = (org.firstinspires.ftc.teamcode.managers.FeatureManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.imu.ImuManager) manImu = (org.firstinspires.ftc.teamcode.managers.imu.ImuManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.input.InputManager) manInput = (org.firstinspires.ftc.teamcode.managers.input.InputManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.macro.MacroManager) manMacro = (org.firstinspires.ftc.teamcode.managers.macro.MacroManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager) manManipulation = (org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.movement.MovementManager) manMovement = (org.firstinspires.ftc.teamcode.managers.movement.MovementManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.sensor.SensorManager) manSensor = (org.firstinspires.ftc.teamcode.managers.sensor.SensorManager)f;
                if(f instanceof org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager) manTelemetry = (org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager)f;
            }
            scope.put("setIsOpModeRunning", new SetIsOpModeRunningFunction(manFeature));
            
            scope.put("getThirdAngleOrientation", new GetThirdAngleOrientationFunction(manImu));
            
            scope.put("getKey", new GetKeyFunction(manInput));
            scope.put("update", new UpdateFunction(manInput));
            scope.put("getBool", new GetBoolFunction(manInput));
            scope.put("getFloat", new GetFloatFunction(manInput));
            
            scope.put("runMacro", new RunMacroFunction(manMacro));
            scope.put("stopMacro", new StopMacroFunction(manMacro));
            scope.put("isMacroRunning", new IsMacroRunningFunction(manMacro));
            
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
            scope.put("driveBlue", new DriveBlueFunction(manMovement));
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
            scope.put("getMeters", new GetMetersFunction(manMovement));
            scope.put("getHorizontalMeters", new GetHorizontalMetersFunction(manMovement));
            scope.put("getVerticalMeters", new GetVerticalMetersFunction(manMovement));
            
            scope.put("updateSensorManager", new UpdateSensorManagerFunction(manSensor));
            scope.put("getColorInteger", new GetColorIntegerFunction(manSensor));
            scope.put("isSpecial", new IsSpecialFunction(manSensor));
            scope.put("isSpecialOne", new IsSpecialOneFunction(manSensor));
            scope.put("computerVision", new ComputerVisionFunction(manSensor));
            
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
            scope.put("getGamepadOneInfo", new GetGamepadOneInfoFunction(manTelemetry));
            scope.put("getGamepadTwoInfo", new GetGamepadTwoInfoFunction(manTelemetry));
            
        }
    }
    