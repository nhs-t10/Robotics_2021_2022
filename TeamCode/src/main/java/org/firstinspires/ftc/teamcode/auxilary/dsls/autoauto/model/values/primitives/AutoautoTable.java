package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.ParserTools;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPropertyBearingObject;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.table.TablePrototype;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.prototype.universal.UniversalPrototype;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AutoautoTable extends AutoautoPrimitive implements AutoautoPropertyBearingObject {
    public Map<String, AutoautoPrimitive> elems;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    //cached array length
    private int aLength = -1;

    public AutoautoTable() {
        this(new HashMap<>());
    }

    public AutoautoTable(HashMap<String, AutoautoPrimitive> elems) {
        setPrototype(TablePrototype.getMap());
        this.elems = elems;
    }

    public AutoautoTable(AutoautoPrimitive[] e) {
        elems = new HashMap<>();
        for(int i = 0; i < e.length; i++) {
            if(e[i] == null) throw new AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException("Null element attempted to put into a table");

            if(e[i] instanceof AutoautoRelation) {
                elems.put(((AutoautoRelation)e[i]).title.getString(), ((AutoautoRelation)e[i]).value);
            } else {
                elems.put(i + "", e[i]);
            }
        }
    }


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
            clonedElems.put(key, this.elems.get(key).clone());
        }

        AutoautoTable c = new AutoautoTable(clonedElems);
        c.setLocation(location);
        return c;
    }

    public AutoautoTable combineWith(AutoautoTable other) {
        HashMap<String, AutoautoPrimitive> combinedElems = new HashMap<>();
        for(String key : this.elems.keySet()) {
            combinedElems.put(key, this.elems.get(key));
        }
        for(String key : other.elems.keySet()) {
            combinedElems.put(key, other.elems.get(key));
        }
        return new AutoautoTable(combinedElems);
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
        if(aLength != -1) return aLength;

        int length = 0;
        for(int i = 0; ; i++) {
            if(elems.containsKey(i + "")) {
                length++;
            } else {
                break;
            }
        }
        aLength = length;
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

    @Override
    public AutoautoPrimitive getProperty(AutoautoPrimitive key) {
        String keyStr = key.getString();
        if(!elems.containsKey(keyStr)) {
            return super.getProperty(key);
        }


        return elems.get(keyStr);
    }

    @Override
    public boolean hasProperty(AutoautoPrimitive key) {
        return elems.containsKey(key.getString());
    }

    @Override
    public final void setProperty(AutoautoPrimitive key, AutoautoPrimitive value) {
        String keyStr = key.getString();
        setProperty(keyStr, value);
    }
    private void setProperty(String key, AutoautoPrimitive value) {
        elems.put(key, value);
        aLength = -1;
    }

    public final void deleteProperty(AutoautoPrimitive key) {
        String keyStr = key.getString();
        elems.remove(keyStr);
        aLength = -1;
    }

    public boolean isArray() {
        return elems.size() == 0 || arrayLength() > 0;
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

    public boolean isEmpty() {
        return elems.isEmpty();
    }
    public int size() {
        return elems.size();
    }
}
