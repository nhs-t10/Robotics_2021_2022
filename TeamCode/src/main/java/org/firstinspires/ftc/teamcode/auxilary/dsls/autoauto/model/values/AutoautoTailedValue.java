package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.jetbrains.annotations.NotNull;

public class AutoautoTailedValue extends AutoautoValue{
    private final AutoautoValue head;
    private final AutoautoValue tail;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static AutoautoTailedValue Z(AutoautoValue head, AutoautoValue tail) {
        return new AutoautoTailedValue(head, tail);
    }
    public static AutoautoTailedValue Z(AutoautoValue head, String tail) {
        return new AutoautoTailedValue(head, new AutoautoString(tail));
    }
    public AutoautoTailedValue(AutoautoValue head, AutoautoValue tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public void init() {
        head.init();
        tail.init();
    }

    @Override
    public void loop() {
        this.head.loop();
        this.tail.loop();


    }

    @NotNull
    @Override
    public AutoautoPrimitive getResolvedValue() {
        AutoautoPrimitive resolvedHead = head.getResolvedValue();
        if(!(resolvedHead instanceof AutoautoPropertyBearingObject)) throw new AutoautoNameException("`"
                + resolvedHead.toString()
                + "` is not a property-bearing object"
                + AutoautoProgram.formatStack(location));

        AutoautoPropertyBearingObject propertyBearingObject = (AutoautoPropertyBearingObject)resolvedHead;

        AutoautoPrimitive resolvedTail = tail.getResolvedValue();
        return propertyBearingObject.getProperty(resolvedTail);
    }

    @Override
    public String getString() {
        return tail.getString();
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        this.head.setScope(scope);
        this.tail.setScope(scope);
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public AutoautoTailedValue clone() {
        return new AutoautoTailedValue(head.clone(), tail.clone());
    }
}
