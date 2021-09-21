package org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.field;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.OhNoJavaFieldMonitorAndExposer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class PojoClassProperty {
    public abstract String getJSONFragment();

    public abstract Class getType();
    public abstract String getName();

    public abstract boolean containsKey(String key);

    public abstract void set(String key, Object value);

    static String getJSONFragmentRecursiveBoy(String name, Object value) {
        if(value == null) return PaulMath.JSONify(name) + ":" + null;

        Class type = value.getClass();
        if(PaulMath.isJSONable(value)) return PaulMath.JSONify(name) + ":" + PaulMath.JSONify(value);
        else if(type.isArray()) return arrayToJSONFragmentRecursive(name, value);
        else return objectToJSONFragment(name, value);
    }
    private static String arrayToJSONFragmentRecursive(String name, Object value) {
        StringBuilder result = new StringBuilder();
        Object[] target = (Object[]) value;
        for (int i = 0; i < target.length; i++) {
            result.append(getJSONFragmentRecursiveBoy(name + "[" + i + "]", target[i]));
            if (i + 1 < target.length) result.append(",");
        }
        return result.toString();
    }
    private static String objectToJSONFragment(String name, Object obj) {
        Field[] fields = null;
        Method[] methods = null;

        //requires some recursion protection, because at the low levels of java it starts to get weird
        if(obj.getClass().equals(Object.class)) return name + ":null";
        fields = obj.getClass().getFields();
        methods = obj.getClass().getMethods();
        StringBuilder r = new StringBuilder();

        for(int i = 0; i < fields.length; i++) {
            r.append(new PojoClassFieldCache(fields[i], obj).getJSONFragment(name + "."));
        }
        for(int i = 0; i < methods.length; i++) {
            if(methods[i].getParameterTypes().length == 0 && methods[i].getName().startsWith("get")) {
                r.append(new PojoClassMethodWrapper(methods[i], obj).getJSONFragment(name + "."));
            }
        }
        return r.toString();
    }
}
