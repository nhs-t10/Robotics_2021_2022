package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.ParserTools;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AutoautoTableLiteral extends AutoautoValue {
    private AutoautoValue[] elems;

    private AutoautoRuntimeVariableScope scope;
    private Location location;

    public static AutoautoTableLiteral K(AutoautoValue[] e) {
        return new AutoautoTableLiteral(e);
    }

    public AutoautoTableLiteral(AutoautoValue[] e) {
        this.elems = e;
    }

    @NotNull
    @Override
    public AutoautoTable getResolvedValue() {
        HashMap<String, AutoautoPrimitive> resolved = new HashMap<>();
        for(int i = 0; i < elems.length; i++) {
            if(elems[i] == null) throw new AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException("Null element attempted to put into a table");

            AutoautoPrimitive key = new AutoautoNumericValue(i);
            AutoautoPrimitive value = elems[i].getResolvedValue();

            if(value instanceof ResolvedTitledArg) {
                key = ((ResolvedTitledArg)value).title;
                value = ((ResolvedTitledArg)value).value;
            }

            if(value == null) throw new AutoautoWhatIsGoingOnSomeoneTriedToPutANullIntoATableException("Null element attempted to put into a table");

            resolved.put(key.getString(), value);
        }
        return new AutoautoTable(resolved);
    }

    @Override
    public void init() {
        for(AutoautoValue key : elems) {
            key.init();
        }
    }

    @Override
    public void loop() {
        for(AutoautoValue key : elems) {
            key.loop();
        }
    }

    @NotNull
    @Override
    public String getString() {
        StringBuilder strElems = new StringBuilder();
        for(AutoautoValue entry : elems) {
            strElems.append(entry).append(", ");
        }
        String str = strElems.toString();
        //use substring() to trim off ending comma & space
        return "[" + str.substring(0, Math.max(0, str.length() - 2)) + "]";
    }

    @Override
    public AutoautoTableLiteral clone() {
        AutoautoValue[] clonedElems = new AutoautoValue[elems.length];
        for(int i = 0; i < elems.length; i++) {
            clonedElems[i] = elems[i].clone();
        }

        AutoautoTableLiteral c = new AutoautoTableLiteral(clonedElems);
        c.setLocation(location);
        return c;
    }

    public String toString() {
        StringBuilder strElems = new StringBuilder();
        for(AutoautoValue key : elems) {
            strElems.append(key).append(", ");
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
        for(AutoautoValue key : elems) {
            key.setScope(scope);
        }
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public void setLocation(Location location) {
        this.location = location;
    }

}
