package org.firstinspires.ftc.teamcode.auxilary.units;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class RotationUnits {
    public String name;
    public String[] abbreviations;
    public double perCircle;

    private RotationUnits(String name, String abbr, double perCircle) {
        this(name, new String[] {abbr}, perCircle);
    }
    private RotationUnits(String name, String[] abbreviations, double perCircle) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.perCircle = perCircle;
    }

    public final static RotationUnits DEG = new RotationUnits("Degree", new String[] {"deg", "degs"}, 360);
    public final static RotationUnits RAD = new RotationUnits("Radian", new String[] {"rad", "rads"}, 2 * Math.PI);
    public final static RotationUnits ROT = new RotationUnits("Radian", new String[] {"rot", "rots"}, 1);
    public final static RotationUnits TURNY = new RotationUnits("Turny", "tn", 4);
    public final static RotationUnits HALF_TURNY = new RotationUnits("Half-Turny", "hlftn", 8);

    public final static RotationUnits naturalRotationUnit = RotationUnits.DEG;

    private static RotationUnits[] unitarrcache;

    //use reflection to find all the static timeunit fields and assemble them into an array
    public static RotationUnits[] getUnits() {
        if(unitarrcache != null) return unitarrcache;

        Field[] fields = RotationUnits.class.getFields();

        ArrayList<RotationUnits> units = new ArrayList<>();
        for(Field f : fields) {
            String name = f.getName();
            boolean nameIsUppercase = name.toUpperCase().equals(name);
            if(nameIsUppercase && f.getType().equals(RotationUnits.class)) {
                try {
                    units.add((RotationUnits) f.get(null));
                } catch(Exception ignored) { }
            }
        }

        unitarrcache = units.toArray(new RotationUnits[0]);
        return unitarrcache;
    }

    public static RotationUnits forAbbreviation(String abbr) {
        String singularName = singular(abbr);

        for(RotationUnits u : getUnits()) {
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

    public static long convertBetween(RotationUnits unitFrom, RotationUnits unitTo, float fromAmount) {
        return (long) (fromAmount * (unitFrom.perCircle / unitTo.perCircle));
    }
}
