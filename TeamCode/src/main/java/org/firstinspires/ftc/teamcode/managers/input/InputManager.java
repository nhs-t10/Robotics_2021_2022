package org.firstinspires.ftc.teamcode.managers.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.util.HashMap;

/**
 * Handle input (button combos, keybinds, etc.) for gamepads.
 */
public class InputManager extends FeatureManager {
    // TODO: Implement with new, non-C1 format
//    public Gamepad gamepad;
//    public Gamepad gamepad2;
//
//    public ControlModel controlModel;
//    public GamepadState gamepadHistory;
//
//    public static String lastKey;
//
//    public float currentSpeed = 0.6f;
//
//    public boolean dpad_leftPress = false;
//    public boolean dpad_rightPress = false;
//    public boolean dpad_leftBumper = false;
//
//    private long explosion;
//
//    public HashMap<String, Float> lastPresses = new HashMap<>();
//    public HashMap<String, Boolean> togglePresses = new HashMap<>();
//
//    public InputManager(Gamepad _gamepad, ControlMap _controlMap) {
//        this.gamepad = _gamepad;
//        this.controlModel = new ControlModel(_controlMap);
//        this.gamepadHistory = new GamepadState(gamepad, null);
//
//        this.explosion = System.currentTimeMillis() + 60_000;
//    }
//
//    public InputManager(Gamepad _gamepad, Gamepad _gamepad2, ControlMap _controlMap) {
//        this.gamepad = _gamepad;
//        this.gamepad2 = _gamepad2;
//
//        this.controlModel = new ControlModel(_controlMap);
//        this.gamepadHistory = new GamepadState(gamepad, null);
//
//        this.explosion = System.currentTimeMillis() + 60_000;
//    }
//
//    public Gamepad getGamepad() {
//        return this.gamepad;
//    }
//    public Gamepad getGamepad2() {
//        return this.gamepad2;
//    }
//
//    public void update() {
//        if(gamepad2 != null) {
//            gamepadHistory = new GamepadState(gamepad, gamepad2, gamepadHistory);
//        }
//        else {
//            gamepadHistory = new GamepadState(gamepad, gamepadHistory);
//        }
//
//    }
//
//    public float[] getVector(String name) {
//        if(controlModel.get(name) == null) throw new IllegalArgumentException("Unknown control name " + name);
//        return controlModel.get(name).res(gamepadHistory);
//
//    }
//
//    public boolean getBoolean(String name) {
//        if(controlModel.get(name) == null) throw new IllegalArgumentException("Unknown control name " + name);
//        return controlModel.get(name).res(gamepadHistory)[0] != 0;
//    }
//
//    public float getScalar(String name) {
//        if(controlModel.get(name) == null) throw new IllegalArgumentException("Unknown control name " + name);
//        return controlModel.get(name).res(gamepadHistory)[0];
//    }
//
//    public ControlModel.Control getControl(String name) {
//        if(controlModel.get(name) == null) throw new IllegalArgumentException("Unknown control name " + name);
//        return controlModel.get(name);
//    }


}

