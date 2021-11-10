package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoNameException;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnits;
import org.firstinspires.ftc.teamcode.auxilary.units.RotationUnits;
import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnits;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

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

    public static enum UnitType { TIME, DISTANCE, ROTATION };

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
        DistanceUnits distanceUnit = DistanceUnits.forAbbreviation(unit);
        RotationUnits rotationUnit = RotationUnits.forAbbreviation(unit);

        if(timeUnit != null) {
            this.baseAmount = TimeUnits.convertBetween(timeUnit, TimeUnits.naturalTimeUnit, baseAmount);
            this.unit = TimeUnits.naturalTimeUnit.name;
            this.unitType = UnitType.TIME;
        } else if(distanceUnit != null) {
            this.baseAmount = DistanceUnits.convertBetween(distanceUnit, DistanceUnits.naturalDistanceUnit, baseAmount);
            this.unit = DistanceUnits.naturalDistanceUnit.name;
            this.unitType = UnitType.DISTANCE;
        } else if(rotationUnit != null) {
            this.baseAmount = RotationUnits.convertBetween(rotationUnit, RotationUnits.naturalRotationUnit, baseAmount);
            this.unit = RotationUnits.naturalRotationUnit.name;
            this.unitType = UnitType.ROTATION;
        } else {
            FeatureManager.logger.warn("Unknown unit `" + unit + "`; please use a distance, time, or rotational unit listed under the auxilary.units package.");
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
