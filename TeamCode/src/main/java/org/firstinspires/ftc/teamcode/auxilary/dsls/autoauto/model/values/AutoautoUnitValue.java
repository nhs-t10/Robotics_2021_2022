package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnits;
import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnits;

public class AutoautoUnitValue extends AutoautoNumericValue {
    //Attributes
    Location location;
    AutoautoRuntimeVariableScope scope;
    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public static enum UnitType { TIME, DISTANCE };

    public UnitType unitType;
    public long baseAmount;
    public String unit;

    //Constructors
    public static AutoautoUnitValue E(long baseAmount, String unit) {
        return new AutoautoUnitValue(baseAmount, unit);
    }
    public static AutoautoUnitValue E(double baseAmount, String unit) {
        return new AutoautoUnitValue((long)baseAmount, unit);
    }

    public AutoautoUnitValue(long baseAmount, String unit) {
        super(baseAmount);
        this.baseAmount = baseAmount;
        this.unit = unit;

        TimeUnits timeUnit = TimeUnits.forAbbreviation(unit);
        if(timeUnit != null) {
            this.baseAmount = TimeUnits.convertBetween(timeUnit, TimeUnits.MS, baseAmount);
            this.unit = "ms";
            this.unitType = UnitType.TIME;
        } else {
            DistanceUnits distanceUnit = DistanceUnits.forAbbreviation(unit);
            if(distanceUnit != null) {
                this.baseAmount = DistanceUnits.convertBetween(distanceUnit, DistanceUnits.CM, baseAmount);
                this.unit = "cm";
            }
            this.unitType = UnitType.DISTANCE;
        }

        this.value = this.baseAmount;
    }

    //Methods
    public String getString() {
        return this.baseAmount +
                ((unitType == UnitType.TIME) ? "ms" : "ticks");
    }

    public AutoautoUnitValue clone() {
        AutoautoUnitValue c = new AutoautoUnitValue(baseAmount, unitType == UnitType.TIME ? "ms" : "ticks");
        c.setLocation(location);
        return c;
    }

    @NonNull
    public String toString() {
        return this.baseAmount +
                ((unitType == UnitType.TIME) ? "ms" : "ticks");
    }
}
