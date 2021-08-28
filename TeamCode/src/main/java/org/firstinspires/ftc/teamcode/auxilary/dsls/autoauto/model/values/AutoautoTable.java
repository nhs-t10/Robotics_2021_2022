package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.util.HashMap;

public class AutoautoTable extends AutoautoPrimitive {
    private HashMap<String, AutoautoValue> elems;
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static AutoautoTable K(AutoautoValue[] e) {
        return new AutoautoTable(e);
    }
    public static AutoautoTable K(HashMap<String, AutoautoValue> e) {
        return new AutoautoTable(e);
    }

    public AutoautoTable(HashMap<String, AutoautoValue> elems) {
        this.elems = elems;
    }

    public AutoautoTable(AutoautoValue[] e) {
        elems = new HashMap<>();
        for(int i = 0; i < e.length; i++) set(new AutoautoNumericValue(i), e[i]);
    }

    public void init() {
        for(String key : elems.keySet()) {
            this.elems.get(key).init();
        }
    }

    @Override
    public void loop() {
        for(String key : elems.keySet()) {
            this.elems.get(key).loop();
        }
    }

    @Override
    public String getString() {
        StringBuilder strElems = new StringBuilder();
        for(String key : elems.keySet()) {
            strElems.append(key).append(" = ").append(this.elems.get(key).getString()).append(", ");
        }
        String str = strElems.toString();
        //use substring() to trim off ending comma & space
        return "[" + str.substring(0, Math.max(0, str.length() - 2)) + "]";
    }

    @Override
    public AutoautoTable clone() {
        HashMap<String, AutoautoValue> clonedElems = new HashMap<>();
        for(String key : elems.keySet()) {
            clonedElems.put(key, this.elems.get(key));
        }

        AutoautoTable c = new AutoautoTable(clonedElems);
        c.setLocation(location);
        return c;
    }

    public String toString() {
        StringBuilder strElems = new StringBuilder();
        for(String key : elems.keySet()) {
            strElems.append(key + " = " + this.elems.get(key).toString()).append(", ");
        }
        String str = strElems.toString();
        //use substring() to trim off ending comma & space
        return "[" + str.substring(0, Math.max(0, str.length() - 2)) + "]";
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return this.scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
        for(String key : elems.keySet()) {
            this.elems.get(key).setScope(scope);
        }
    }

    public AutoautoValue[] array() {
        AutoautoValue[] array = new AutoautoValue[arrayLength()];
        for(int i = 0; i < array.length; i++) {
            array[i] = elems.get("" + i);
        }
        return array;
    }

    public int arrayLength() {
        int length = 0;
        for(int i = 0; ; i++) {
            if(elems.containsKey(i + "")) {
                length++;
            } else {
                break;
            }
        }
        return length;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

    public final AutoautoPrimitive get(AutoautoPrimitive key) {
        String keyStr = key.getString();
        if(!elems.containsKey(keyStr)) return new AutoautoUndefined();


        AutoautoValue value = elems.get(keyStr);
        value.loop();
        return value.getResolvedValue();
    }

    public final void set(AutoautoPrimitive key, AutoautoValue value) {
        String keyStr = key.getString();
        elems.put(keyStr, value);
    }

    public final void delete(AutoautoPrimitive key) {
        String keyStr = key.getString();
        elems.remove(keyStr);
    }
}
