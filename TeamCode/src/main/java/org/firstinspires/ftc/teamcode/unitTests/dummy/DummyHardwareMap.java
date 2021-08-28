package org.firstinspires.ftc.teamcode.unitTests.dummy;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.UserHandle;
import android.view.Display;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DummyHardwareMap extends HardwareMap {
    public DummyHardwareMap() {
        super(null); //`super(new DummyContext());` won't work bc tests remove the context class
    }

    @Override
    public <T> T get(Class<? extends T> classOrInterface, String deviceName) {
        switch(classOrInterface.getSimpleName()) {
            case "NormalizedColorSensor": return (T) new DummyNormalizedColorSensor();
            case "BNO055IMU": return (T) new DummyImu();
            case "DcMotor": return (T) new DummyDcMotor();
            case "CRServo": return (T) new DummyCrServo();
            case "Servo": return (T) new DummyServo();
        }
        throw new IllegalArgumentException(String.format("Unable to find a hardware device with name \"%s\" and type %s", deviceName, classOrInterface.getSimpleName()));
    }
}
