package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime;

public class ManagerSetupException extends Exception {
    private final String msg;

    public ManagerSetupException(String s) {
        this.msg = s;
    }
    public String toString() {
        return msg;
    }
}
