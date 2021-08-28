package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Statepath;

public class NextStatement extends SkipStatement {

    public static NextStatement N() {
        return new NextStatement();
    }

    public NextStatement() {
        super(1);
    }
    public String toString() {
        return "next";
    }
}
