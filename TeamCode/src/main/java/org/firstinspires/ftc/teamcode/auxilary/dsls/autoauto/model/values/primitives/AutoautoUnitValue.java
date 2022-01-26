package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives;

import androidx.annotation.NonNull;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.unitvalue.UnitValuePrototype;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.RotationUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnit;
import org.firstinspires.ftc.teamcode.auxilary.units.Unit;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.jetbrains.annotations.NotNull;

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

    public AutoautoUnitValue convertToNaturalUnit() {
        if(unit == null) return this;
        else return new AutoautoUnitValue(unit.convertToNaturalUnit(this.value), unit.getNaturalUnit().getCommonAbbrev());
    }

    public static enum UnitType { TIME, DISTANCE, UNKNOWN, ROTATION };

    public UnitType unitType;
    public final Unit unit;
    public final String originalUnitName;

    public static AutoautoUnitValue E(double baseAmount, String unit) {
        return new AutoautoUnitValue(baseAmount, unit);
    }

    public AutoautoUnitValue(double baseAmount, String unit) {
        super(baseAmount);
        setPrototype(UnitValuePrototype.getMap());

        this.originalUnitName = unit;

        TimeUnit timeUnit = TimeUnit.forAbbreviation(unit);
        DistanceUnit distanceUnit = DistanceUnit.forAbbreviation(unit);
        RotationUnit rotationUnit = RotationUnit.forAbbreviation(unit);

        if(timeUnit != null) {
            this.unit = timeUnit;
            this.unitType = UnitType.TIME;
        } else if(distanceUnit != null) {
            this.unit = distanceUnit;
            this.unitType = UnitType.DISTANCE;
        } else if(rotationUnit != null) {
            this.unit = rotationUnit;
            this.unitType = UnitType.ROTATION;
        } else {
            this.unit = Unit.UNKNOWN;
            this.unitType = UnitType.UNKNOWN;
            FeatureManager.logger.warn("Unknown unit `" + unit + "`; please use a distance, time, or rotational unit listed under the auxilary.units package.");
        }
    }

    //Methods
    @NotNull
    public String getString() {
        return this.value + this.unit.toString();
    }

    public AutoautoUnitValue clone() {
        AutoautoUnitValue c = new AutoautoUnitValue(this.value, unit.name);
        c.setLocation(location);
        return c;
    }

    @NonNull
    public String toString() {
        return this.value + this.unit.toString();
    }
}
