package org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker;


import org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.field.PojoClassFieldCache;
import org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.field.PojoClassMethodWrapper;
import org.firstinspires.ftc.teamcode.managers.telemetry.pojotracker.field.PojoClassProperty;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class OhNoJavaFieldMonitorAndExposer {

    private Object object;

    private PojoClassProperty[] fields;

    public OhNoJavaFieldMonitorAndExposer() {}

    public OhNoJavaFieldMonitorAndExposer(Object object) {
        this.object = object;

        this.fields = getUsableFields(object);
    }

    public boolean hasKey(String name) {
        for (PojoClassProperty field : fields) {
            if (field.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public Class<?> getTypeOfField(String name) {
        for (PojoClassProperty field : fields) {
            if (field.containsKey(name)) {
                return field.getType();
            }
        }
        return null;
    }

    public void set(String name, Object value) {
        for(PojoClassProperty field : fields) {
            if(field.containsKey(name)) {
                try {
                    field.set(name, value);
                } catch(Exception ignored) { }
                break;
            }
        }
    }

    public String getJSON() {
        if(fields == null || object == null) return "null";


        StringBuilder r = new StringBuilder("{");
        for(int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getName());
            r.append(fields[i].getJSONFragment());

            if(i != fields.length - 1) {
                r.append(",");
            }
        }
        r.append("}");
        return r.toString();
    }

    public void parseAndSet(String key, String value) {
        Class type = getTypeOfField(key);
        if(type == null) throw new IllegalArgumentException();

        if(type.isPrimitive()) {
                if(type.equals(Integer.class)) set(key, Integer.valueOf(value));
                else if(type.equals(Boolean.class)) set(key, Boolean.valueOf(value));
                else if(type.equals(Byte.class)) set(key, Byte.valueOf(value));
                else if(type.equals(Long.class)) set(key, Long.valueOf(value));
                else if(type.equals(Float.class)) set(key, Float.valueOf(value));
                else if(type.equals(Double.class)) set(key, Double.valueOf(value));
                else if(type.equals(Character.class)) set(key, value.charAt(0));
                else if(type.equals(Short.class)) set(key, Short.valueOf(value));
        }
        //if not, it *has* to be a String
        else {
            set(key, value);
        }
    }
    public static PojoClassProperty[] getUsableFields(Object obj) {
        Field[] fields = obj.getClass().getFields();
        Method[] methods = obj.getClass().getMethods();

        ArrayList<PojoClassProperty> usableFields = new ArrayList<PojoClassProperty>();
        int eventualLength = 0;

        for(int i = 0; i < fields.length; i++) {
            usableFields.add(new PojoClassFieldCache(fields[i], obj));
            eventualLength++;
        }
        for(int i = 0; i < methods.length; i++) {
            //no sense in including Class's methods
            if(methods[i].getDeclaringClass().equals(Class.class)) continue;
            if(methods[i].getName().equals("getClass")) continue;

            if(methods[i].getParameterTypes().length == 0 && methods[i].getName().startsWith("get")) {
                usableFields.add(new PojoClassMethodWrapper(methods[i], obj));
                eventualLength++;
            }
        }
        return usableFields.toArray(new PojoClassProperty[eventualLength]);
    }
}
