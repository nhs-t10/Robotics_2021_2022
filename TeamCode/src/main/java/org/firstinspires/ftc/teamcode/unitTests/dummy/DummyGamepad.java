package org.firstinspires.ftc.teamcode.unitTests.dummy;

import com.qualcomm.robotcore.hardware.Gamepad;

public class DummyGamepad extends Gamepad {
    public DummyGamepad() {
        this.left_stick_x = 0f;
        this.left_stick_y = 0f;
        this.right_stick_x = 0f;
        this.right_stick_y = 0f;
        this.left_trigger = 0f;
        this.right_trigger = 0f;
        this.left_bumper = false;
        this.right_bumper = false;
        this.a = false;
        this.b = false;
        this.x = false;
        this.y = false;
        this.dpad_left = false;
        this.dpad_right = false;
        this.dpad_up = false;
        this.dpad_down = false;
    }
}
