package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.RotationUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnit;
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

    public static enum UnitType { TIME, DISTANCE, UNKNOWN, ROTATION };

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

        TimeUnit timeUnit = TimeUnit.forAbbreviation(unit);
        DistanceUnit distanceUnit = DistanceUnit.forAbbreviation(unit);
        RotationUnit rotationUnit = RotationUnit.forAbbreviation(unit);

        if(timeUnit != null) {
            this.baseAmount = (long)Math.round(TimeUnit.convertBetween(timeUnit, TimeUnit.naturalTimeUnit, baseAmount));
            this.unit = TimeUnit.naturalTimeUnit.name;
            this.unitType = UnitType.TIME;
        } else if(distanceUnit != null) {
            this.baseAmount = (long)Math.round(DistanceUnit.convertBetween(distanceUnit, DistanceUnit.naturalDistanceUnit, baseAmount));
            this.unit = DistanceUnit.naturalDistanceUnit.name;
            this.unitType = UnitType.DISTANCE;
        } else if(rotationUnit != null) {
            this.baseAmount = (long)Math.round(RotationUnit.convertBetween(rotationUnit, RotationUnit.naturalRotationUnit, baseAmount));
            this.unit = RotationUnit.naturalRotationUnit.name;
            this.unitType = UnitType.ROTATION;
        } else {
            this.unitType = UnitType.UNKNOWN;
            FeatureManager.logger.warn("Unknown unit `" + unit + "`; please use a distance, time, or rotational unit listed under the auxilary.units package.");
        }

        this.value = this.baseAmount;
    }

    //Methods
    public String getString() {
        return this.baseAmount + this.unit;
    }

    public AutoautoUnitValue clone() {
        AutoautoUnitValue c = new AutoautoUnitValue(baseAmount, unitType == UnitType.TIME ? "ms" : "ticks");
        c.setLocation(location);
        return c;
    }

    @NonNull
    public String toString() {
        return this.baseAmount + this.unit;
    }
}
