package org.firstinspires.ftc.teamcode.managers.input.buttonhandles;

import com.qualcomm.robotcore.hardware.Gamepad;

public class AButtonHandle extends ButtonHandle {
    private Gamepad g;
    public AButtonHandle(Gamepad g) {
        this.g = g;
    }
    public float get() {
        return g.a?1f:0f;
    }
}