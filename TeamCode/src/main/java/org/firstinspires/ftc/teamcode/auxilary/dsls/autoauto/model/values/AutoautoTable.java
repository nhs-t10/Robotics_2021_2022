package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.ParserTools;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoatuoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;

import java.util.HashMap;
import java.util.Map;

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

    public AutoautoTable() {
        this.elems = new HashMap<String, AutoautoValue>();
    }

    public AutoautoTable(HashMap<String, AutoautoValue> elems) {
        this.elems = elems;
    }

    public AutoautoTable(AutoautoValue[] e) {
        elems = new HashMap<>();
        for(int i = 0; i < e.length; i++) {
            if(e[i] == null) throw new AutoatuoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException("Null element attempted to put into a table");
            set(new AutoautoNumericValue(i), e[i]);
        }
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
        for(Map.Entry<String, AutoautoValue> entry : elems.entrySet()) {
            strElems.append(entry.getKey()).append(" = ").append(entry.getValue().getString()).append(", ");
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

    public String getJSONString() {
        StringBuilder strElems = new StringBuilder();
        for(String key : elems.keySet()) {
            strElems
                .append(PaulMath.JSONify(key))
                .append(":")
                .append(this.elems.get(key).getResolvedValue() == null ? "\"null\"" : this.elems.get(key).getResolvedValue().getJSONString())
                .append(",");
        }
        String str = strElems.toString();
        //use substring() to trim off ending comma & space
        return "{" + str.substring(0, Math.max(0, str.length() - 1)) + "}";
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

    public static AutoautoTable fromJSONObject(String str) {
        AutoautoTable table = new AutoautoTable();

        String nonBracketString = str;
        if(str.startsWith("{")) nonBracketString = str.substring(1, str.length() - 1);

        String[] elems = ParserTools.groupAwareSplit(nonBracketString, ',');

        for(int i = 0; i < elems.length; i++) {
            int keyIndex = elems[i].indexOf(':');
            String key = elems[i].substring(0, keyIndex);
            String value = elems[i].substring(keyIndex + 1);
            table.set((AutoautoPrimitive) AutoautoValue.fromJSON(key), AutoautoValue.fromJSON(value));
        }

        return table;
    }

    public static AutoautoTable fromJSONArray(String str) {
        AutoautoTable table = new AutoautoTable();

        String nonBracketString = str;
        if(str.startsWith("[")) nonBracketString = str.substring(1, str.length() - 1);

        String[] elems = ParserTools.groupAwareSplit(nonBracketString, ',');

        for(int i = 0; i < elems.length; i++) {
            table.set(new AutoautoNumericValue(i), AutoautoValue.fromJSON(elems[i]));
        }

        return table;
    }
}
