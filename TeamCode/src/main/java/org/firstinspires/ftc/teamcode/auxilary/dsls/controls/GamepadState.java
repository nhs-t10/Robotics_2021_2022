package org.firstinspires.ftc.teamcode.auxilary.dsls.controls;

import com.qualcomm.robotcore.hardware.Gamepad;

public class GamepadState {
    public float left_stick_x;
    public float left_stick_y;
    public boolean left_stick_button;
    public float right_stick_x;
    public float right_stick_y;
    public boolean right_stick_button;
    public float left_trigger;
    public float right_trigger;
    public boolean left_bumper;
    public boolean right_bumper;
    public boolean a;
    public boolean b;
    public boolean x;
    public boolean y;
    public boolean dpad_left;
    public boolean dpad_right;
    public boolean dpad_up;
    public boolean dpad_down;

    public float gamepad2_left_stick_x;
    public float gamepad2_left_stick_y;
    public boolean gamepad2_left_stick_button;
    public float gamepad2_right_stick_x;
    public float gamepad2_right_stick_y;
    public boolean gamepad2_right_stick_button;
    public float gamepad2_left_trigger;
    public float gamepad2_right_trigger;
    public boolean gamepad2_left_bumper;
    public boolean gamepad2_right_bumper;
    public boolean gamepad2_a;
    public boolean gamepad2_b;
    public boolean gamepad2_x;
    public boolean gamepad2_y;
    public boolean gamepad2_dpad_left;
    public boolean gamepad2_dpad_right;
    public boolean gamepad2_dpad_up;
    public boolean gamepad2_dpad_down;

    public long time;

    public GamepadState history;

    public GamepadState getHistory() {
        if(history == null) return GamepadState.initialState();
        else return history;
    }

    public GamepadState() {}

    public GamepadState(Gamepad gamepad) {
        this(gamepad, initialState());
    }

    public static GamepadState initialState() {
        GamepadState st = new GamepadState();

        st.left_stick_x = 0f;
        st.left_stick_y = 0f;
        st.left_stick_button = false;
        st.right_stick_x = 0f;
        st.right_stick_y = 0f;
        st.right_stick_button = false;
        st.left_trigger = 0f;
        st.right_trigger = 0f;
        st.left_bumper = false;
        st.right_bumper = false;
        st.a = false;
        st.b = false;
        st.x = false;
        st.y = false;
        st.dpad_left = false;
        st.dpad_right = false;
        st.dpad_up = false;
        st.dpad_down = false;

        st.gamepad2_left_stick_x = 0f;
        st.gamepad2_left_stick_y = 0f;
        st.gamepad2_left_stick_button = false;
        st.gamepad2_right_stick_x = 0f;
        st.gamepad2_right_stick_y = 0f;
        st.gamepad2_right_stick_button = false;
        st.gamepad2_left_trigger = 0f;
        st.gamepad2_right_trigger = 0f;
        st.gamepad2_left_bumper = false;
        st.gamepad2_right_bumper = false;
        st.gamepad2_a = false;
        st.gamepad2_b = false;
        st.gamepad2_x = false;
        st.gamepad2_y = false;
        st.gamepad2_dpad_left = false;
        st.gamepad2_dpad_right = false;
        st.gamepad2_dpad_up = false;
        st.gamepad2_dpad_down = false;

        st.time = 0;

        return st;
    }

    public GamepadState(Gamepad gamepad, GamepadState history) {
        this.left_stick_x = gamepad.left_stick_x;
        this.left_stick_y = gamepad.left_stick_y;
        this.left_stick_button = gamepad.left_stick_button;
        this.right_stick_x = gamepad.right_stick_x;
        this.right_stick_y = gamepad.right_stick_y;
        this.right_stick_button = gamepad.right_stick_button;
        this.left_trigger = gamepad.left_trigger;
        this.right_trigger = gamepad.right_trigger;
        this.left_bumper = gamepad.left_bumper;
        this.right_bumper = gamepad.right_bumper;
        this.a = gamepad.a;
        this.b = gamepad.b;
        this.x = gamepad.x;
        this.y = gamepad.y;
        this.dpad_left = gamepad.dpad_left;
        this.dpad_right = gamepad.dpad_right;
        this.dpad_up = gamepad.dpad_up;
        this.dpad_down = gamepad.dpad_down;

        this.time = System.currentTimeMillis();

        this.history = history;
        if(history != null) history.history = null;
    }

    public GamepadState(Gamepad gamepad, Gamepad gamepad2, GamepadState history) {
        this.left_stick_x = gamepad.left_stick_x;
        this.left_stick_y = gamepad.left_stick_y;
        this.right_stick_x = gamepad.right_stick_x;
        this.right_stick_y = gamepad.right_stick_y;
        this.left_trigger = gamepad.left_trigger;
        this.right_trigger = gamepad.right_trigger;
        this.left_bumper = gamepad.left_bumper;
        this.right_bumper = gamepad.right_bumper;
        this.a = gamepad.a;
        this.b = gamepad.b;
        this.x = gamepad.x;
        this.y = gamepad.y;
        this.dpad_left = gamepad.dpad_left;
        this.dpad_right = gamepad.dpad_right;
        this.dpad_up = gamepad.dpad_up;
        this.dpad_down = gamepad.dpad_down;

        this.gamepad2_left_stick_x = gamepad2.left_stick_x;
        this.gamepad2_left_stick_y = gamepad2.left_stick_y;
        this.left_stick_button = gamepad2.left_stick_button;
        this.gamepad2_right_stick_x = gamepad2.right_stick_x;
        this.gamepad2_right_stick_y = gamepad2.right_stick_y;
        this.right_stick_button = gamepad2.right_stick_button;
        this.gamepad2_left_trigger = gamepad2.left_trigger;
        this.gamepad2_right_trigger = gamepad2.right_trigger;
        this.gamepad2_left_bumper = gamepad2.left_bumper;
        this.gamepad2_right_bumper = gamepad2.right_bumper;
        this.gamepad2_a = gamepad2.a;
        this.gamepad2_b = gamepad2.b;
        this.gamepad2_x = gamepad2.x;
        this.gamepad2_y = gamepad2.y;
        this.gamepad2_dpad_left = gamepad2.dpad_left;
        this.gamepad2_dpad_right = gamepad2.dpad_right;
        this.gamepad2_dpad_up = gamepad2.dpad_up;
        this.gamepad2_dpad_down = gamepad2.dpad_down;

        this.time = System.currentTimeMillis();

        this.history = history;
        if(history != null) history.history = null;
    }

    /**
     * Get the current state of a button on the gamepad
     * @param button The name of the button to get the state of
     * @return The current state of the button
     */
    public float getButtonState(String button) {
        switch(button) {
            case "left_stick_x":
                return this.left_stick_x;
            case "left_stick_y":
                return this.left_stick_y;
            case "left_stick_button":
                return this.left_stick_button?1f:0f;

            case "right_stick_x":
                return this.right_stick_x;
            case "right_stick_y":
                return this.right_stick_y;
            case "right_stick_button":
                return this.right_stick_button?1f:0f;

            case "left_trigger":
                return this.left_trigger;
            case "right_trigger":
                return this.right_trigger;

            case "left_bumper":
                return this.left_bumper?1f:0f;
            case "right_bumper":
                return this.right_bumper?1f:0f;

            case "a":
            case "cross":
                return this.a?1f:0f;
            case "b":
            case "circle":
                return this.b?1f:0f;
            case "x":
            case "square":
                return this.x?1f:0f;
            case "y":
            case "triangle":
                return this.y?1f:0f;

            case "dpad_left":
                return this.dpad_left?1f:0f;
            case "dpad_right":
                return this.dpad_right?1f:0f;
            case "dpad_up":
                return this.dpad_up?1f:0f;
            case "dpad_down":
                return this.dpad_down?1f:0f;

            case "gamepad2_left_stick_x":
                return this.gamepad2_left_stick_x;
            case "gamepad2_left_stick_y":
                return this.gamepad2_left_stick_y;
            case "gamepad2_left_stick_button":
                return this.gamepad2_left_stick_button?1f:0f;

            case "gamepad2_right_stick_x":
                return this.gamepad2_right_stick_x;
            case "gamepad2_right_stick_y":
                return this.gamepad2_right_stick_y;
            case "gamepad2_right_stick_button":
                return this.gamepad2_right_stick_button?1f:0f;

            case "gamepad2_left_trigger":
                return this.gamepad2_left_trigger;
            case "gamepad2_right_trigger":
                return this.gamepad2_right_trigger;

            case "gamepad2_left_bumper":
                return this.gamepad2_left_bumper?1f:0f;
            case "gamepad2_right_bumper":
                return this.gamepad2_right_bumper?1f:0f;

            case "gamepad2_a":
            case "gamepad2_cross":
                return this.gamepad2_a?1f:0f;
            case "gamepad2_b":
            case "gamepad2_circle":
                return this.gamepad2_b?1f:0f;
            case "gamepad2_x":
            case "gamepad2_square":
                return this.gamepad2_x?1f:0f;
            case "gamepad2_y":
            case "gamepad2_triangle":
                return this.gamepad2_y?1f:0f;

            case "gamepad2_dpad_left":
                return this.gamepad2_dpad_left?1f:0f;
            case "gamepad2_dpad_right":
                return this.gamepad2_dpad_right?1f:0f;
            case "gamepad2_dpad_up":
                return this.gamepad2_dpad_up?1f:0f;
            case "gamepad2_dpad_down":
                return this.gamepad2_dpad_down?1f:0f;

            default:
                return 0f;
        }
    }
}