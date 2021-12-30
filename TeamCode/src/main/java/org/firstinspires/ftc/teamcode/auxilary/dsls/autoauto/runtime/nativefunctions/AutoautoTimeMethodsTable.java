package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable;
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
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.time.timeNanoNativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions.time.timeNowNativeFunction;

import java.util.HashMap;

public class AutoautoTimeMethodsTable extends AutoautoTable {
    public AutoautoTimeMethodsTable() {
        super(makeMethodsMap());
    }

    private static HashMap<String, AutoautoPrimitive> makeMethodsMap() {
        HashMap<String, AutoautoPrimitive> methods = new HashMap<>();

        methods.put("now", new timeNowNativeFunction());
        methods.put("nano", new timeNanoNativeFunction());

        return methods;
    }
}
