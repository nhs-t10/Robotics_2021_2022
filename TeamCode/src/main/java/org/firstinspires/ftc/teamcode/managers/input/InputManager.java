package org.firstinspires.ftc.teamcode.managers.input;

import com.qualcomm.ftccommon.FtcEventLoop;
import com.qualcomm.ftccommon.FtcEventLoopBase;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManagerNotifier;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.robotcore.internal.opmode.OpModeManagerImpl;
import org.firstinspires.ftc.robotcore.internal.opmode.OpModeServices;
import org.firstinspires.ftc.robotcore.internal.opmode.RegisteredOpModes;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;

import java.util.HashMap;

/**
 * Handle input (button combos, keybinds, etc.) for gamepads.
 */
public class InputManager extends FeatureManager {
    // TODO: Implement with new, non-C1 format
    public Gamepad gamepad;
    public Gamepad gamepad2;

    private final HashMap<String, InputManagerInputNode> nodes;

    private final InputUpdateThread updateThread;
//
    public InputManager(Gamepad _gamepad, Gamepad _gamepad2) {
        this.gamepad = _gamepad;
        this.gamepad2 = _gamepad2;
        nodes = new HashMap<>();
        updateThread = new InputUpdateThread();
        updateThread.start();
    }

    public Gamepad getGamepad() {
        return this.gamepad;
    }
    public Gamepad getGamepad2() {
        return this.gamepad2;
    }

    public void registerInput(String key, InputManagerInputNode... registerNodes) {
        InputManagerInputNode node = null;
        if(registerNodes.length == 0) throw new IllegalArgumentException("Must have more than 0 args");
        else if(registerNodes.length == 1) node = registerNodes[0];
        else node = new MultiInputNode(registerNodes);

        node.init(this);
        nodes.put(key, node);
        synchronized (updateThread) {
            updateThread.addNode(node);
        }
    }


    public InputManagerInputNode getInputNode(String key) {
        return nodes.get(key);
    }

    public float[] getFloatArrayOfInput(String key) {
        if(!nodes.containsKey(key)) throw new IllegalArgumentException("No key `" + key + "`");
        return nodes.get(key).getResult().getFloatArray();
    }

    public float getKey(String key) {
        String normalizedKey = key.toLowerCase().replace("_", "").replace(".", "");
        switch(normalizedKey) {
            case "dpadup":
            case "gamepad1dpadup":
                return gamepad.dpad_up?1f:0f;
            case "dpaddown":
            case "gamepad1dpaddown":
                return gamepad.dpad_down?1f:0f;
            case "dpadleft":
            case "gamepad1dpadleft":
                return gamepad.dpad_left?1f:0f;
            case "dpadright":
            case "gamepad1dpadright":
                return gamepad.dpad_right?1f:0f;
            case "a":
            case "gamepad1a":
                return gamepad.a?1f:0f;
            case "b":
            case "gamepad1b":
                return gamepad.b?1f:0f;
            case "x":
            case "gamepad1x":
                return gamepad.x?1f:0f;
            case "y":
            case "gamepad1y":
                return gamepad.y?1f:0f;
            case "guide":
            case "gamepad1guide":
                return gamepad.guide?1f:0f;
            case "start":
            case "gamepad1start":
                return gamepad.start?1f:0f;
            case "back":
            case "gamepad1back":
                return gamepad.back?1f:0f;
            case "leftbumper":
            case "gamepad1leftbumper":
                return gamepad.left_bumper?1f:0f;
            case "rightbumper":
            case "gamepad1rightbumper":
                return gamepad.right_bumper?1f:0f;
            case "leftstickbutton":
            case "gamepad1leftstickbutton":
                return gamepad.left_stick_button?1f:0f;
            case "rightstickbutton":
            case "gamepad1rightstickbutton":
                return gamepad.right_stick_button?1f:0f;
            case "circle":
            case "gamepad1circle":
                return gamepad.circle?1f:0f;
            case "cross":
            case "gamepad1cross":
                return gamepad.cross?1f:0f;
            case "triangle":
            case "gamepad1triangle":
                return gamepad.triangle?1f:0f;
            case "square":
            case "gamepad1square":
                return gamepad.square?1f:0f;
            case "share":
            case "gamepad1share":
                return gamepad.share?1f:0f;
            case "options":
            case "gamepad1options":
                return gamepad.options?1f:0f;
            case "touchpad":
            case "gamepad1touchpad":
                return gamepad.touchpad?1f:0f;
            case "ps":
            case "gamepad1ps":
                return gamepad.ps?1f:0f;
            case "leftstickx":
            case "gamepad1leftstickx":
                return gamepad.left_stick_x;
            case "leftsticky":
            case "gamepad1leftsticky":
                return gamepad.left_stick_y;
            case "rightstickx":
            case "gamepad1rightstickx":
                return gamepad.right_stick_x;
            case "rightsticky":
            case "gamepad1rightsticky":
                return gamepad2.right_stick_y;
            case "lefttrigger":
            case "gamepad1lefttrigger":
                return gamepad.left_trigger;
            case "righttrigger":
            case "gamepad1righttrigger":
                return gamepad.right_trigger;
            case "gamepad2leftstickx":
                return gamepad2.left_stick_x;
            case "gamepad2leftsticky":
                return gamepad2.left_stick_y;
            case "gamepad2rightstickx":
                return gamepad2.right_stick_x;
            case "gamepad2rightsticky":
                return gamepad2.right_stick_y;
            case "gamepad2lefttrigger":
                return gamepad2.left_trigger;
            case "gamepad2righttrigger":
                return gamepad2.right_trigger;
            case "gamepad2dpadup":
                return gamepad2.dpad_up?1f:0f;
            case "gamepad2dpaddown":
                return gamepad2.dpad_down?1f:0f;
            case "gamepad2dpadleft":
                return gamepad2.dpad_left?1f:0f;
            case "gamepad2dpadright":
                return gamepad2.dpad_right?1f:0f;
            case "gamepad2a":
                return gamepad2.a?1f:0f;
            case "gamepad2b":
                return gamepad2.b?1f:0f;
            case "gamepad2x":
                return gamepad2.x?1f:0f;
            case "gamepad2y":
                return gamepad2.y?1f:0f;
            case "gamepad2guide":
                return gamepad2.guide?1f:0f;
            case "gamepad2start":
                return gamepad2.start?1f:0f;
            case "gamepad2back":
                return gamepad2.back?1f:0f;
            case "gamepad2leftbumper":
                return gamepad2.left_bumper?1f:0f;
            case "gamepad2rightbumper":
                return gamepad2.right_bumper?1f:0f;
            case "gamepad2leftstickbutton":
                return gamepad2.left_stick_button?1f:0f;
            case "gamepad2rightstickbutton":
                return gamepad2.right_stick_button?1f:0f;
            case "gamepad2circle":
                return gamepad2.circle?1f:0f;
            case "gamepad2cross":
                return gamepad2.cross?1f:0f;
            case "gamepad2triangle":
                return gamepad2.triangle?1f:0f;
            case "gamepad2square":
                return gamepad2.square?1f:0f;
            case "gamepad2share":
                return gamepad2.share?1f:0f;
            case "gamepad2options":
                return gamepad2.options?1f:0f;
            case "gamepad2touchpad":
                return gamepad2.touchpad?1f:0f;
            case "gamepad2ps":
                return gamepad2.ps?1f:0f;
        }
        return -1f;
    }

    public void update() {
    }


    public boolean getBool(String key) {
        if(!nodes.containsKey(key)) throw new IllegalArgumentException("No key `" + key + "`");
        return nodes.get(key).getResult().getFloat() != 0;
    }
    public float getFloat(String key) {
        if(!nodes.containsKey(key)) throw new IllegalArgumentException("No key `" + key + "`");
        return nodes.get(key).getResult().getFloat();
    }
}

