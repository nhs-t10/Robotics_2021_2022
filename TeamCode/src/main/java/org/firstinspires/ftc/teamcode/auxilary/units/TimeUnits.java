package org.firstinspires.ftc.teamcode.auxilary.units;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TimeUnits {
    public String name;
    public String[] abbreviations;
    public double perSecond;

    private TimeUnits(String name, String abbr, double perSecond) {
        this(name, new String[] {abbr}, perSecond);
    }
    private TimeUnits(String name, String[] abbreviations, double perSecond) {
        this.name = name;
        this.abbreviations = abbreviations;
        this.perSecond = perSecond;
    }

    public final static TimeUnits S = new TimeUnits("Second", "s", 1);
    public final static TimeUnits MS = new TimeUnits("Millisecond", "ms", 1000);
    public final static TimeUnits NS = new TimeUnits("Nanosecond", "ns", 1e9);
    public final static TimeUnits M = new TimeUnits("Minute", new String[] {"m", "min"}, 1.0/60.0);
    public final static TimeUnits HR = new TimeUnits("Hour", new String[] {"h", "hr"}, 1.0/(60.0*60.0));
    public final static TimeUnits D = new TimeUnits("Day", "d", 1.0/(60.0*60.0*24.0));
    public final static TimeUnits YR = new TimeUnits("Year", new String[] {"y", "yr"}, 1.0/(60.0*60.0*24.0*365.0));

    private static TimeUnits[] unitarrcache;

    //use reflection to find all the static timeunit fields and assemble them into an array
    public static TimeUnits[] getUnits() {
        if(unitarrcache != null) return unitarrcache;

        Field[] fields = TimeUnits.class.getFields();

        ArrayList<TimeUnits> units = new ArrayList<>();
        for(Field f : fields) {
            if(f.getType().equals(TimeUnits.class)) {
                try {
                    units.add((TimeUnits) f.get(null));
                } catch(Exception ignored) { }
            }
        }

        unitarrcache = units.toArray(new TimeUnits[0]);
        return unitarrcache;
    }

    public static TimeUnits forAbbreviation(String abbr) {
        String singularName = singular(abbr);

        for(TimeUnits u : getUnits()) {
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

    public static long convertBetween(TimeUnits unitFrom, TimeUnits unitTo, float fromAmount) {
        return (long) (fromAmount * (unitFrom.perSecond / unitTo.perSecond));
    }
}
