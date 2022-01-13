package org.firstinspires.ftc.teamcode.managers.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.input.buttonhandles.*;
import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;
import org.firstinspires.ftc.teamcode.managers.input.nodes.MultiInputNode;

import java.util.Collection;
import java.util.HashMap;

/**
 * Handle input (button combos, keybinds, etc.) for gamepads.
 */
public class InputManager extends FeatureManager {
    public Gamepad gamepad;
    public Gamepad gamepad2;

    public InputOverlapResolutionMethod overlapResolutionMethod = InputOverlapResolutionMethod.BOTH_CHILDREN_CAN_SPEAK;

    private final HashMap<String, InputManagerInputNode> nodes;

    public InputManager(Gamepad _gamepad, Gamepad _gamepad2) {
        this.gamepad = _gamepad;
        this.gamepad2 = _gamepad2;
        nodes = new HashMap<>();
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

        node.setName(key);

        node.init(this);
        nodes.put(key.toLowerCase(), node);
        rebuildOverlaps();
        (new InputUpdateThread(node)).start();
    }


    public InputManagerInputNode getInputNode(String key) {
        return nodes.get(key.toLowerCase());
    }

    public ButtonHandle getButtonHandle(String key) {
        String normalizedKey = key.toLowerCase().replace("_", "").replace(".", "");

        //remove 'gamepad1'
        if(normalizedKey.startsWith("gamepad1")) normalizedKey = normalizedKey.replace("gamepad1", "");

        switch(normalizedKey) {
            case "touchpadfinger1": return new TouchpadFinger1ButtonHandle(gamepad);
            case "touchpadfinger2": return new TouchpadFinger2ButtonHandle(gamepad);
            //Add 1xy and 2xy
            case "dpadup": return new DpadUpButtonHandle(gamepad);
            case "dpaddown": return new DpadDownButtonHandle(gamepad);
            case "dpadleft": return new DpadLeftButtonHandle(gamepad);
            case "dpadright": return new DpadRightButtonHandle(gamepad);
            case "a": return new AButtonHandle(gamepad);
            case "b": return new BButtonHandle(gamepad);
            case "x": return new XButtonHandle(gamepad);
            case "y": return new YButtonHandle(gamepad);
            case "guide":
            case "select": return new GuideButtonHandle(gamepad);
            case "start": return new StartButtonHandle(gamepad);
            case "back": return new BackButtonHandle(gamepad);
            case "leftbumper": return new LeftBumperButtonHandle(gamepad);
            case "rightbumper": return new RightBumperButtonHandle(gamepad);
            case "leftstickbutton": return new LeftStickButtonButtonHandle(gamepad);
            case "rightstickbutton": return new RightStickButtonButtonHandle(gamepad);
            case "circle": return new CircleButtonHandle(gamepad);
            case "cross": return new CrossButtonHandle(gamepad);
            case "triangle": return new TriangleButtonHandle(gamepad);
            case "square": return new SquareButtonHandle(gamepad);
            case "share": return new ShareButtonHandle(gamepad);
            case "options": return new OptionsButtonHandle(gamepad);
            case "touchpad": return new TouchpadButtonHandle(gamepad);
            case "ps": return new PsButtonHandle(gamepad);
            case "leftstickx": return new LeftStickXButtonHandle(gamepad);
            case "leftsticky": return new LeftStickYButtonHandle(gamepad);
            case "rightstickx": return new RightStickXButtonHandle(gamepad);
            case "rightsticky": return new RightStickYButtonHandle(gamepad);
            case "lefttrigger": return new LeftTriggerButtonHandle(gamepad);
            case "righttrigger": return new RightTriggerButtonHandle(gamepad);
            case "gamepad2dpadup": return new DpadUpButtonHandle(gamepad2);
            case "gamepad2dpaddown": return new DpadDownButtonHandle(gamepad2);
            case "gamepad2dpadleft": return new DpadLeftButtonHandle(gamepad2);
            case "gamepad2dpadright": return new DpadRightButtonHandle(gamepad2);
            case "gamepad2a": return new AButtonHandle(gamepad2);
            case "gamepad2b": return new BButtonHandle(gamepad2);
            case "gamepad2x": return new XButtonHandle(gamepad2);
            case "gamepad2y": return new YButtonHandle(gamepad2);
            case "gamepad2guide":
            case "gamepad2select": return new GuideButtonHandle(gamepad2);
            case "gamepad2start": return new StartButtonHandle(gamepad2);
            case "gamepad2back": return new BackButtonHandle(gamepad2);
            case "gamepad2leftbumper": return new LeftBumperButtonHandle(gamepad2);
            case "gamepad2rightbumper": return new RightBumperButtonHandle(gamepad2);
            case "gamepad2leftstickbutton": return new LeftStickButtonButtonHandle(gamepad2);
            case "gamepad2rightstickbutton": return new RightStickButtonButtonHandle(gamepad2);
            case "gamepad2circle": return new CircleButtonHandle(gamepad2);
            case "gamepad2cross": return new CrossButtonHandle(gamepad2);
            case "gamepad2triangle": return new TriangleButtonHandle(gamepad2);
            case "gamepad2square": return new SquareButtonHandle(gamepad2);
            case "gamepad2share": return new ShareButtonHandle(gamepad2);
            case "gamepad2options": return new OptionsButtonHandle(gamepad2);
            case "gamepad2touchpad": return new TouchpadButtonHandle(gamepad2);
            case "gamepad2ps": return new PsButtonHandle(gamepad2);
            case "gamepad2leftstickx": return new LeftStickXButtonHandle(gamepad2);
            case "gamepad2leftsticky": return new LeftStickYButtonHandle(gamepad2);
            case "gamepad2rightstickx": return new RightStickXButtonHandle(gamepad2);
            case "gamepad2rightsticky": return new RightStickYButtonHandle(gamepad2);
            case "gamepad2lefttrigger": return new LeftTriggerButtonHandle(gamepad2);
            case "gamepad2righttrigger": return new RightTriggerButtonHandle(gamepad2);
        }
        throw new IllegalArgumentException("Bad key " + key);
    }

    public void update() {
    }

    public float[] getFloatArrayOfInput(String key) {
        if(!nodes.containsKey(key.toLowerCase())) throw new IllegalArgumentException("No control `" + key.toLowerCase() + "` registered");
        return nodes.get(key.toLowerCase()).getOverlappedResult().getFloatArray();
    }

    public boolean getBool(String key) {
        if(!nodes.containsKey(key.toLowerCase())) throw new IllegalArgumentException("No control `" + key.toLowerCase() + "` registered");
        return nodes.get(key.toLowerCase()).getOverlappedResult().getBool();
    }
    public float getFloat(String key) {
        if(!nodes.containsKey(key.toLowerCase())) throw new IllegalArgumentException("No control `" + key.toLowerCase() + "` registered");
        return nodes.get(key.toLowerCase()).getOverlappedResult().getFloat();
    }

    public void setOverlapResolutionMethod(InputOverlapResolutionMethod newMethod) {
        this.overlapResolutionMethod = newMethod;
        rebuildOverlaps();
    }

    private void rebuildOverlaps() {
        InputManagerInputNode[] allRootNodes = nodes.values().toArray(new InputManagerInputNode[0]);
        for(InputManagerInputNode node : allRootNodes) {
            node.updateOverlaps(overlapResolutionMethod, allRootNodes);
        }
    }

    public Collection<InputManagerInputNode> getNodes() {
        return nodes.values();
    }
}

