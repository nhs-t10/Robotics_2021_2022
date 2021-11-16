package org.firstinspires.ftc.teamcode.auxilary.units;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class TimeUnit extends Unit {
    public String name;
    public String[] abbreviations;
    public double perSecond;

    private TimeUnit(String name, String abbr, double perSecond) {
        this(name, new String[] {abbr}, perSecond);
    }
    private TimeUnit(String name, String[] abbreviations, double perSecond) {
        super(name, abbreviations, perSecond);
        this.name = name;
        this.abbreviations = abbreviations;
        this.perSecond = perSecond;
    }

    public final static TimeUnit S = new TimeUnit("Second", "s", 1);
    public final static TimeUnit MS = new TimeUnit("Millisecond", "ms", 1000);
    public final static TimeUnit NS = new TimeUnit("Nanosecond", "ns", 1e9);
    public final static TimeUnit MIN = new TimeUnit("Minute", new String[] {"min", "mn"}, 1.0/60.0);
    public final static TimeUnit HR = new TimeUnit("Hour", new String[] {"h", "hr"}, 1.0/(60.0*60.0));
    public final static TimeUnit D = new TimeUnit("Day", "d", 1.0/(60.0*60.0*24.0));
    public final static TimeUnit YR = new TimeUnit("Year", new String[] {"y", "yr"}, 1.0/(60.0*60.0*24.0*365.0));
    public final static TimeUnit JIF = new TimeUnit("Jiffy", new String[] {"jif", "jiff", "jiffy", "jiffies"}, 100);

    public final static TimeUnit naturalTimeUnit = TimeUnit.MS;

    private static TimeUnit[] unitarrcache;

    //use reflection to find all the static timeunit fields and assemble them into an array
    public static TimeUnit[] getUnits() {
        if(unitarrcache != null) return unitarrcache;

        Field[] fields = TimeUnit.class.getFields();

        ArrayList<TimeUnit> units = new ArrayList<>();
        for(Field f : fields) {
            String name = f.getName();
            boolean nameIsUppercase = name.toUpperCase().equals(name);
            if(nameIsUppercase && f.getType().equals(TimeUnit.class)) {
                try {
                    units.add((TimeUnit) f.get(null));
                } catch(Exception ignored) { }
            }
        }

        unitarrcache = units.toArray(new TimeUnit[0]);
        return unitarrcache;
    }

    public static TimeUnit forAbbreviation(String abbr) {
        String singularName = singular(abbr);

        for(TimeUnit u : getUnits()) {
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

    public static double convertBetween(TimeUnit unitFrom, TimeUnit unitTo, double fromAmount) {
        return (fromAmount * (unitTo.perSecond / unitFrom.perSecond));
    }
    public static double convertBetween(TimeUnit unitFrom, TimeUnit unitTo, float fromAmount) {
        return (fromAmount * (unitTo.perSecond / unitFrom.perSecond));
    }
}
