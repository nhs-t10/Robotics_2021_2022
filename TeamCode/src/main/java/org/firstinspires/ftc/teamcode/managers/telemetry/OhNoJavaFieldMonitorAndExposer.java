package org.firstinspires.ftc.teamcode.managers.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.HttpStatusCodeReplies;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class OhNoJavaFieldMonitorAndExposer {

    private OpMode opmode;

    private PojoClassFieldCache[] fields;

    public OhNoJavaFieldMonitorAndExposer(OpMode opmode) {
        this.opmode = opmode;

        this.fields = getUsableFields(opmode);
    }

    public boolean hasKey(String name) {
        for (PojoClassFieldCache field : fields) {
            if (field.containsKey(name)) {
                return true;
            }
        }
        return false;
    }

    public Class<?> getTypeOfField(String name) {
        for (PojoClassFieldCache field : fields) {
            if (field.containsKey(name)) {
                return field.getType();
            }
        }
        return null;
    }

    public void set(String name, Object value) {
        for(PojoClassFieldCache field : fields) {
            if(field.containsKey(name)) {
                try {
                    field.set(name, value);
                } catch(Exception ignored) { }
                break;
            }
        }
    }

    public String getJSON() {
        if(fields == null || opmode == null) return "null";


        StringBuilder r = new StringBuilder("{");
        for(int i = 0; i < fields.length; i++) {
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
    public static PojoClassFieldCache[] getUsableFields(Object obj) {
        Field[] fields = obj.getClass().getFields();
        ArrayList<PojoClassFieldCache> usableFields = new ArrayList<PojoClassFieldCache>();
        int eventualLength = 0;

        for(int i = 0; i < fields.length; i++) {
            if(fields[i].getType().isPrimitive() || fields[i].getType().isAssignableFrom(String.class)) {
                usableFields.add(new PojoClassFieldCache(fields[i], obj));
                fields[i].getType().getName();
                eventualLength++;
            }
        }
        return usableFields.toArray(new PojoClassFieldCache[eventualLength]);
    }
}
