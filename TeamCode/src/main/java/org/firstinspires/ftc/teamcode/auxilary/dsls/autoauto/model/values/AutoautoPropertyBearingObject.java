package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

public interface AutoautoPropertyBearingObject {
    AutoautoPrimitive getProperty(AutoautoPrimitive prop);
    void setProperty(AutoautoPrimitive prop, AutoautoPrimitive value);
    boolean hasProperty(AutoautoPrimitive prop);
    void deleteProperty(AutoautoPrimitive prop);
}
