package org.firstinspires.ftc.teamcode.auxilary.units;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Unit {
    public String name;
    public String pluralName;
    public String[] abbreviations;
    public double coefficient;

    public static Unit UNKNOWN = new Unit("?", new String[]{"?"}, Double.NaN) {
        public double convertToNaturalUnit(double u) { return Double.NaN; }
        public Unit getNaturalUnit() { return this; }
        public String getCommonAbbrev() { return "?"; }
    };

    protected Unit(String name, String[] abbreviations, double coefficient, String pluralName) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.coefficient = coefficient;
        this.pluralName = pluralName;
    }
    protected Unit(String name, String[] abbreviations, double coefficient) {
        this(name, abbreviations, coefficient, name);
    }

    public static Unit[] getAllUnits() {
        ArrayList<Unit> units = new ArrayList<Unit>();

        units.addAll(Arrays.asList(DistanceUnit.getUnits()));
        units.addAll(Arrays.asList(TimeUnit.getUnits()));
        units.addAll(Arrays.asList(RotationUnit.getUnits()));

        return units.toArray(new Unit[0]);
    }

    public static Unit forAbbreviation(String unit) {
        TimeUnit timeUnit = TimeUnit.forAbbreviation(unit);
        if(timeUnit != null) return timeUnit;

        DistanceUnit distanceUnit = DistanceUnit.forAbbreviation(unit);
        if(distanceUnit != null) return distanceUnit;

        return RotationUnit.forAbbreviation(unit);
    }

    public String toString() {
        return name + " (" + Arrays.toString(abbreviations) + "): " + coefficient;
    }

    public double convertToNaturalUnit(double u) {
        return u * coefficient;
    }

    public double convertTo(Unit u, double amountToConvert) {
        return (amountToConvert * coefficient) / u.coefficient;
    }

    public abstract Unit getNaturalUnit();

    public abstract String getCommonAbbrev();
}
