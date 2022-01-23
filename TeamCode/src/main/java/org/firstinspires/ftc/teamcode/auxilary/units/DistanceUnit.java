package org.firstinspires.ftc.teamcode.auxilary.units;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DistanceUnit extends Unit {
    public String name;
    public String[] abbreviations;
    //TODO: refactor this class so it measures in millimeters, not "per meter". See TimeUnitTest for example.
    public double perMeter;

    private DistanceUnit(String name, String abbr, double perMeter) {
        this(name, new String[] {abbr}, perMeter);
    }
    private DistanceUnit(String name, String[] abbreviations, double perMeter) {
        super(name, abbreviations, perMeter);
        this.name = name;
        this.abbreviations = abbreviations;
        this.perMeter = perMeter;
    }

    public final static DistanceUnit MM = new DistanceUnit("Millimeter", "mm", 1000);
    public final static DistanceUnit M = new DistanceUnit("Meter", "m", 1);
    public final static DistanceUnit NM = new DistanceUnit("Nanometer", "nm", 1e9);
    public final static DistanceUnit CM = new DistanceUnit("Centimeter", "cm", 100);
    public final static DistanceUnit IN = new DistanceUnit("Inch", "in", 39.3700787);
    public final static DistanceUnit FT = new DistanceUnit("Foot", "ft", 3.2808399);
    public final static DistanceUnit YD = new DistanceUnit("Yard", "yd", 1.0936133);
    public final static DistanceUnit FATHOM = new DistanceUnit("Fathom", "fathom", 1 / 1.8288);
    public final static DistanceUnit KM = new DistanceUnit("Kilometer", "km", 1 / 1000.0);
    public final static DistanceUnit MIL = new DistanceUnit("Mile", "mil", 1 / 1609.344);
    public final static DistanceUnit AU = new DistanceUnit("Astronomical Unit", "au", 1 / 149597870700.0);
    public final static DistanceUnit FBF = new DistanceUnit("Football Field", new String[] {"fbfield", "fbf"}, 1 / 109.36133);
    public final static DistanceUnit FTCF = new DistanceUnit("FTC Field", new String[] {"ftcfield", "ftcf", "ftf"}, 1 / 5.4864);
    public final static DistanceUnit RBTW = new DistanceUnit("Robot Width", new String[] {"rbtw", "rbw"}, 2.1872266);
    public final static DistanceUnit HTDG = new DistanceUnit("Hot Dog", new String[] {"hd", "htdg", "hotdog"}, 8.2020997);

    public final static DistanceUnit naturalDistanceUnit = DistanceUnit.CM;

    private static DistanceUnit[] unitarrcache;

    //use reflection to find all the static timeunit fields and assemble them into an array
    public static DistanceUnit[] getUnits() {
        if(unitarrcache != null) return unitarrcache;

        Field[] fields = DistanceUnit.class.getFields();

        ArrayList<DistanceUnit> units = new ArrayList<>();
        for(Field f : fields) {
            String name = f.getName();
            boolean nameIsUppercase = name.toUpperCase().equals(name);
            if(nameIsUppercase && f.getType().equals(DistanceUnit.class)) {
                try {
                    units.add((DistanceUnit) f.get(null));
                } catch(Exception ignored) { }
            }
        }

        unitarrcache = units.toArray(new DistanceUnit[0]);
        return unitarrcache;
    }

    public static DistanceUnit forAbbreviation(String abbr) {
        String singularName = singular(abbr);

        for(DistanceUnit u : getUnits()) {
            if(u.name.equalsIgnoreCase(singularName)) return u;
            for(String a : u.abbreviations) {
                if(a.equals(abbr)) return u;
            }
        }
        return null;
    }

    public static String singular(String abbr) {
        if(abbr == null) return null;
        else if(abbr.endsWith("s")) return abbr.substring(0, abbr.length() - 1);
        else return abbr;
    }

    public static double convertBetween(DistanceUnit unitFrom, DistanceUnit unitTo, double fromAmount) {
        return (fromAmount * (unitTo.perMeter / unitFrom.perMeter));
    }
    public static double convertBetween(DistanceUnit unitFrom, DistanceUnit unitTo, float fromAmount) {
        return (fromAmount * (unitTo.perMeter / unitFrom.perMeter));
    }

    @Override
    public double convertToNaturalUnit(double u) {
        return convertBetween(this, naturalDistanceUnit, u);
    }

    @Override
    public String getCommonAbbrev() {
        if(abbreviations.length > 0) return abbreviations[0];
        else return name;
    }

    @Override
    public Unit getNaturalUnit() {
        return naturalDistanceUnit;
    }
}
