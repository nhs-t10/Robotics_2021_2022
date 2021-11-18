package org.firstinspires.ftc.teamcode.managers.manipulation;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.Arrays;

public class ManipulationManager extends FeatureManager {
    public CRServo[] crservos;
    public DcMotor[] motors;
    public Servo[] servos;

    public String[] crservoNames;
    public String[] motorNames;
    public String[] servoNames;


    public ManipulationManager(CRServo[] _crservos, Servo[] _servos, DcMotor[] _motors) {
        this.crservos = _crservos;
        this.servos = _servos;
        this.motors = _motors;
    }

    public ManipulationManager(HardwareMap _hardwareMap, String[] _crservos, String[] _servos, String[] _motors) {
        this.crservos = new CRServo[_crservos.length];
        for(int i = 0; i < _crservos.length; i++) {crservos[i]  = _hardwareMap.get(CRServo.class, _crservos[i]); }
        this.crservoNames = _crservos;
        this.servos = new Servo[_servos.length];
        for(int i = 0; i < _servos.length; i++) {servos[i]  = _hardwareMap.get(Servo.class, _servos[i]); }
        this.servoNames = _servos;
        this.motors = new DcMotor[_motors.length];
        for(int i = 0; i < _motors.length; i++) {motors[i]  = _hardwareMap.get(DcMotor.class, _motors[i]); }
        this.motorNames = _motors;
    }

    public ManipulationManager(CRServo[] _crservos, String[] _crservoNames, Servo[] _servos, String[] _servoNames, DcMotor[] _motors, String[] _motorNames) {
        if(_crservoNames.length != _crservos.length) throw new IllegalArgumentException("CRServo Names must be the same length as CRServos");
        if(_servoNames.length != _servos.length) throw new IllegalArgumentException("Servo Names must be the same length as Servos");
        if(_motorNames.length != _motors.length) throw new IllegalArgumentException("Motor Names must be the same length as Motors");

        this.crservos = _crservos;
        this.crservoNames = _crservoNames;
        this.servos = _servos;
        this.servoNames = _servoNames;
        this.motors = _motors;
        this.motorNames = _motorNames;
    }

    public void setCRServoNames(String[] _crservoNames) {
        if(_crservoNames.length != crservos.length) throw new IllegalArgumentException("CRServo Names must be the same length as CRServos");
        this.crservoNames = _crservoNames;
    }

    public void setServoNames(String[] _servoNames) {
        if (_servoNames.length != servos.length) throw new IllegalArgumentException("Servo Names must be the same length as Servos");
        this.servoNames = _servoNames;
    }

    public void setMotorNames(String[] _motorNames) {
        if(_motorNames.length != motors.length) throw new IllegalArgumentException("Motor Names must be the same length as Motors");
        this.motorNames = _motorNames;
    }

    public void setServoPosition(String name, double position) {
        int index = (Arrays.asList(servoNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Servo " + name + " does not exist or is not registered in ManipulationManager");
        servos[index].setPosition(position);
    }

    public void setServoPower(String name, double power) {
        int index = (Arrays.asList(crservoNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Servo " + name + " does not exist or is not registered in ManipulationManager");
        crservos[index].setPower(power);
    }
    public Servo getServo(String name) {
        int index = (Arrays.asList(servoNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Servo " + name + " does not exist or is not registered in ManipulationManager");

        return servos[index];
    }


    public double getMotorPower(String name) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        return motors[index].getPower();
    }

    public double getMotorPower(int i) {
        return motors[i].getPower();
    }

    public double getMotorPosition(String name) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        return motors[index].getCurrentPosition();
    }

    public double getServoPower(String name) {
        int index = (Arrays.asList(crservoNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("CRServo " + name + " does not exist or is not registered in ManipulationManager");
        return crservos[index].getPower();
    }
    public double getServoPower(int i) {
        return crservos[i].getPower();
    }

    public double getServoPosition(String name) {
        int index = (Arrays.asList(servoNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Servo " + name + " does not exist or is not registered in ManipulationManager");
        return servos[index].getPosition();
    }

    public void setMotorModes(DcMotor.RunMode mode) {
        for(DcMotor motor : motors) motor.setMode(mode);
    }

    public void setMotorMode(String name, DcMotor.RunMode mode) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        motors[index].setMode(mode);
    }

    public void resetEncoders() {
        for(DcMotor motor : motors) motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runUsingEncoders() {
        for(DcMotor motor : motors) motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void resetEncoders(String name) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        motors[index].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void runUsingEncoders(String name) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        motors[index].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void setMotorPosition(String name, int position) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        motors[index].setTargetPosition(position);
    }

    public void setMotorPosition(int i, int position) {
        motors[i].setTargetPosition(position);
    }

    public void setMotorPower(String name, double power) {
        int index = (Arrays.asList(motorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered in ManipulationManager");
        motors[index].setPower(power);
    }

    public void setMotorPower(int i, double power) {
        motors[i].setPower(power);
    }

    public void setServoPower(int i, double power) {
        crservos[i].setPower(power);
    }

    public void setServoPosition(int i, double power) {
        servos[i].setPosition(power);
    }

    public void resetEncoders(int i) {
        motors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    }

    public void closeMotors() {
        for(DcMotor m : motors) m.close();
    }

//    public void setServoPosition(int i, double power) {
//        servos[i].setPosition(power);
//    }



}
