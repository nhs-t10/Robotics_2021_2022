package org.firstinspires.ftc.teamcode.auxilary.units;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Unit {
    public String name;
    public String[] abbreviations;
    public double coefficient;

    protected Unit(String name, String[] abbreviations, double coefficient) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.coefficient = coefficient;
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

    public abstract double convertToNaturalUnit(double u);
}
