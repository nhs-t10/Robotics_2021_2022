package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPropertyBearingObject;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.absNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.acosNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.acoshNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.asinNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.asinhNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.atan2NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.atanNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.atanhNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.cbrtNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.ceilNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.clz32NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.cosNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.coshNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.expNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.expm1NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.floorNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.froundNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.hypotNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.imulNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.log10NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.log1pNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.log2NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.logNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.maxNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.minNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.powNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.randomNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.roundNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.signNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.sinNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.sinhNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.sqrtNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.tanNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.tanhNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.math.truncNativeFunction;

import java.util.HashMap;

public class AutoautoMathMethodsTable extends AutoautoPrimitive implements AutoautoPropertyBearingObject {
    private AutoautoRuntimeVariableScope scope;
    private Location location;

    private HashMap<String, AutoautoPrimitive> methods;

    public AutoautoMathMethodsTable() {
        this.methods = new HashMap<>();

        methods.put("E", new AutoautoNumericValue(Math.E));
        methods.put("LN2", new AutoautoNumericValue(0.6931471805599453));
        methods.put("LN10", new AutoautoNumericValue(2.302585092994046));
        methods.put("LOG2E", new AutoautoNumericValue(1.4426950408889634));
        methods.put("LOG10E", new AutoautoNumericValue(0.4342944819032518));
        methods.put("PI", new AutoautoNumericValue(Math.PI));
        methods.put("SQRT1_2", new AutoautoNumericValue(Math.sqrt(0.5)));
        methods.put("SQRT2", new AutoautoNumericValue(Math.sqrt(2)));

        methods.put("abs", new absNativeFunction());
        methods.put("acos", new acosNativeFunction());
        methods.put("acosh", new acoshNativeFunction());
        methods.put("asin", new asinNativeFunction());
        methods.put("asinh", new asinhNativeFunction());
        methods.put("atan", new atanNativeFunction());
        methods.put("atanh", new atanhNativeFunction());
        methods.put("atan2", new atan2NativeFunction());
        methods.put("cbrt", new cbrtNativeFunction());
        methods.put("ceil", new ceilNativeFunction());
        methods.put("clz32", new clz32NativeFunction());
        methods.put("cos", new cosNativeFunction());
        methods.put("cosh", new coshNativeFunction());
        methods.put("exp", new expNativeFunction());
        methods.put("expm1", new expm1NativeFunction());
        methods.put("floor", new floorNativeFunction());
        methods.put("fround", new froundNativeFunction());
        methods.put("hypot", new hypotNativeFunction());
        methods.put("imul", new imulNativeFunction());
        methods.put("log", new logNativeFunction());
        methods.put("log1p", new log1pNativeFunction());
        methods.put("log10", new log10NativeFunction());
        methods.put("log2", new log2NativeFunction());
        methods.put("max", new maxNativeFunction());
        methods.put("min", new minNativeFunction());
        methods.put("pow", new powNativeFunction());
        methods.put("random", new randomNativeFunction());
        methods.put("round", new roundNativeFunction());
        methods.put("sign", new signNativeFunction());
        methods.put("sin", new sinNativeFunction());
        methods.put("sinh", new sinhNativeFunction());
        methods.put("sqrt", new sqrtNativeFunction());
        methods.put("tan", new tanNativeFunction());
        methods.put("tanh", new tanhNativeFunction());
        methods.put("trunc", new truncNativeFunction());

    }

    @Override
    public AutoautoPrimitive getProperty(AutoautoPrimitive prop) {
        if(hasProperty(prop)) return methods.get(prop.getString());
        else return new AutoautoUndefined();
    }

    @Override
    public boolean hasProperty(AutoautoPrimitive prop) {
        return methods.containsKey(prop.getString());
    }

    @Override
    public String getJSONString() {
        return PaulMath.JSONify(getString());
    }

    @Override
    public String getString() {
        return "[native table Math]";
    }

    @Override
    public AutoautoRuntimeVariableScope getScope() {
        return scope;
    }

    @Override
    public void setScope(AutoautoRuntimeVariableScope scope) {
        this.scope = scope;
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
    public AutoautoPrimitive clone() {
        return new AutoautoMathMethodsTable();
    }

    public String toString() {
        return "Math";
    }
}
