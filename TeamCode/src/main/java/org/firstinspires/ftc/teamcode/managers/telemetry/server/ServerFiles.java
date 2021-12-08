package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ServerFiles {
    public static String getAssetString(String asset) throws IOException {
        InputStream file = getAssetStream(asset);

        if(file == null) return null;

        StringBuilder r = new StringBuilder();
        while(true) {
            int nextByte = file.read();
            if(nextByte < 0) break;
            r.append((char)nextByte);
        }

        file.close();
        return r.toString();
    }
    public static InputStream getAssetStream(String asset) throws FileNotFoundException {
        try {
            Context context = ((Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null)).getApplicationContext();

            AssetManager assets = context.getAssets();

            return assets.open(asset);
        } catch(Exception e) {
            FeatureManager.logger.log("Unable to get context -- assuming text environment; falling back to FileInputStream loader");
            File file = new File("src/main/assets", asset);

            if(file.exists()) return new FileInputStream(file);
            else return null;
        }
    }
}
