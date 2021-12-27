package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.ParserTools;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AutoautoTable extends AutoautoPrimitive implements AutoautoPropertyBearingObject {
    private HashMap<String, AutoautoPrimitive> elems;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public AutoautoTable() {
        this.elems = new HashMap<>();
    }

    public AutoautoTable(HashMap<String, AutoautoPrimitive> elems) {
        this.elems = elems;
    }

    public AutoautoTable(AutoautoPrimitive[] e) {
        elems = new HashMap<>();
        for(int i = 0; i < e.length; i++) {
            if(e[i] == null) throw new AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException("Null element attempted to put into a table");

            if(e[i] instanceof ResolvedTitledArg) {
                elems.put(((ResolvedTitledArg)e[i]).title.getString(), ((ResolvedTitledArg)e[i]).value);
            } else {
                elems.put(i + "", e[i]);
            }
        }
    }

    @Override
    public void loop() {}

    @NotNull
    @Override
    public String getString() {
        StringBuilder strElems = new StringBuilder();
        for(Map.Entry<String, AutoautoPrimitive> entry : elems.entrySet()) {
            strElems.append(entry.getKey()).append(" = ").append(entry.getValue().getString()).append(", ");
        }
        String str = strElems.toString();
        //use substring() to trim off ending comma & space
        return "[" + str.substring(0, Math.max(0, str.length() - 2)) + "]";
    }

    @Override
    public AutoautoTable clone() {
        HashMap<String, AutoautoPrimitive> clonedElems = new HashMap<>();
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
                .append(this.elems.get(key) == null ? "\"null\"" : this.elems.get(key).getJSONString())
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

    public AutoautoPrimitive getProperty(AutoautoPrimitive key) {
        String keyStr = key.getString();
        if(!elems.containsKey(keyStr)) {
            if(keyStr.equals("length")) return new AutoautoNumericValue(arrayLength());
            return super.getProperty(key);
        }


        return elems.get(keyStr);
    }

    public boolean hasProperty(AutoautoPrimitive key) {
        return elems.containsKey(key.getString());
    }

    public final void setProperty(AutoautoPrimitive key, AutoautoPrimitive value) {
        String keyStr = key.getString();
        setProperty(keyStr, value);
    }
    private void setProperty(String key, AutoautoPrimitive value) {
        elems.put(key, value);
    }

    public final void deleteProperty(AutoautoPrimitive key) {
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
            table.setProperty(AutoautoValue.fromJSON(key), AutoautoValue.fromJSON(value));
        }

        return table;
    }

    public static AutoautoTable fromJSONArray(String str) {
        AutoautoTable table = new AutoautoTable();

        String nonBracketString = str;
        if(str.startsWith("[")) nonBracketString = str.substring(1, str.length() - 1);

        String[] elems = ParserTools.groupAwareSplit(nonBracketString, ',');

        for(int i = 0; i < elems.length; i++) {
            table.setProperty(new AutoautoNumericValue(i), AutoautoValue.fromJSON(elems[i]));
        }

        return table;
    }
}
