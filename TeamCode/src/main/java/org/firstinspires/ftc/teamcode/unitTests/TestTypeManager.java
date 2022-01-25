package org.firstinspires.ftc.teamcode.unitTests;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.Locale;

public abstract class TestTypeManager {
    public static boolean testRunTypeIs(String t) {
        if(t == null) return false;

        try {
            String testTypes = System.getenv("TEST_TYPE");
            if(testTypes == null) return false;
            return testTypes.toUpperCase().contains(t.toUpperCase());
        } catch(Throwable onNonPortableProblemIgnore) {
            //ignore the problem
            return false;
        }
    }
}
