package org.firstinspires.ftc.teamcode.managers.input.buttonhandles;

import com.qualcomm.robotcore.hardware.Gamepad;

public class BButtonHandle extends ButtonHandle {
    private Gamepad g;
    public BButtonHandle(Gamepad g) {
        this.g = g;
    }
    public float get() {
        return g.b?1f:0f;
    }
}