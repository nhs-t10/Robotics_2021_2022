package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ServerFiles {
    public static String indexDotHtml;
    public static void loadIndexDotHtml() throws IOException {
        indexDotHtml = getAssetString("index.html");
    }
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
        Context context = null;
        try {
            context = ((Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null)).getApplicationContext();

            AssetManager assets = context.getAssets();

            return assets.open(asset);
        } catch(Exception e) {
            FeatureManager.logger.log("Error getting context");
            File file = new File("src/main/assets", asset);

            if(file.exists()) return new FileInputStream(file);
            else return null;
        }
    }
}
