package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements;

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
