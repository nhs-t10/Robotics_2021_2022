package org.firstinspires.ftc.teamcode.auxilary.units;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class DistanceUnits {
    public String name;
    public String[] abbreviations;
    public double perMeter;

    private DistanceUnits(String name, String abbr, double perMeter) {
        this(name, new String[] {abbr}, perMeter);
    }
    private DistanceUnits(String name, String[] abbreviations, double perMeter) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.perMeter = perMeter;
    }

    public final static DistanceUnits MM = new DistanceUnits("Millimeter", "mm", 1000);
    public final static DistanceUnits M = new DistanceUnits("Meter", "m", 1);
    public final static DistanceUnits NM = new DistanceUnits("Nanometer", "nm", 1e9);
    public final static DistanceUnits CM = new DistanceUnits("Centimeter", "cm", 100);
    public final static DistanceUnits IN = new DistanceUnits("Inch", "in", 39.3700787);
    public final static DistanceUnits FT = new DistanceUnits("Foot", "ft", 3.2808399);
    public final static DistanceUnits YD = new DistanceUnits("Yard", "yd", 1.0936133);
    public final static DistanceUnits FATHOM = new DistanceUnits("Fathom", "fathom", 1 / 1.8288);
    public final static DistanceUnits KM = new DistanceUnits("Kilometer", "km", 1 / 1000.0);
    public final static DistanceUnits MIL = new DistanceUnits("Mile", "mil", 1 / 1609.344);
    public final static DistanceUnits AU = new DistanceUnits("Astronomical Unit", "au", 1 / 149597870700.0);
    public final static DistanceUnits FBF = new DistanceUnits("Football Field", new String[] {"fbfield", "fbf"}, 1 / 109.36133);
    public final static DistanceUnits FTCF = new DistanceUnits("FTC Field", new String[] {"ftcfield", "ftcf", "ftf"}, 1 / 5.4864);
    public final static DistanceUnits RBTW = new DistanceUnits("Robot Width", new String[] {"rbtw", "rbw"}, 2.1872266);

    public final static DistanceUnits naturalDistanceUnit = DistanceUnits.CM;

    private static DistanceUnits[] unitarrcache;

    //use reflection to find all the static timeunit fields and assemble them into an array
    public static DistanceUnits[] getUnits() {
        if(unitarrcache != null) return unitarrcache;

        Field[] fields = DistanceUnits.class.getFields();

        ArrayList<DistanceUnits> units = new ArrayList<>();
        for(Field f : fields) {
            String name = f.getName();
            boolean nameIsUppercase = name.toUpperCase().equals(name);
            if(nameIsUppercase && f.getType().equals(DistanceUnits.class)) {
                try {
                    units.add((DistanceUnits) f.get(null));
                } catch(Exception ignored) { }
            }
        }

        unitarrcache = units.toArray(new DistanceUnits[0]);
        return unitarrcache;
    }

    public static DistanceUnits forAbbreviation(String abbr) {
        String singularName = singular(abbr);

        for(DistanceUnits u : getUnits()) {
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

    public static long convertBetween(DistanceUnits unitFrom, DistanceUnits unitTo, float fromAmount) {
        return (long) (fromAmount * (unitFrom.perMeter / unitTo.perMeter));
    }
}
