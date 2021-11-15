package org.firstinspires.ftc.teamcode.auxilary;


import android.app.Application;
import android.content.Context;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;

public class FileSaver {
    private String filePathname;
    private Context context;
    public String fileName;

    public FileSaver(String _fileName) {
        try {
            Context context = ((Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null)).getApplicationContext();
            this.fileName = _fileName;
            this.filePathname = context.getExternalFilesDir(null).getPath() + "/" + fileName;
        } catch (Throwable ignored) {
            this.fileName = _fileName;
            this.filePathname = System.getProperty("java.io.tmpdir");
        }
    }

    public ArrayList<String> readLines() {

        String thisLine;
        BufferedReader textFile;
        ArrayList<String> lines = new ArrayList<String>();

        try (BufferedReader br = new BufferedReader(new BufferedReader(new InputStreamReader(new FileInputStream(filePathname))))) {

            while ((thisLine = br.readLine()) != null) {
                lines.add(thisLine);
            }
        } catch (Throwable e) {
            FeatureManager.logger.log(e.toString());
            for(StackTraceElement t : e.getStackTrace()) FeatureManager.logger.log(t);
        }
        return lines;
    }

    public String readContent() {

        StringBuilder result = new StringBuilder();

        try(BufferedReader textFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePathname)))) {
            int nextChar;
            while ((nextChar = textFile.read()) != -1) {
                result.append((char)nextChar);
            }
        } catch (Throwable e) {
            FeatureManager.logger.log(e.toString());
            for(StackTraceElement t : e.getStackTrace()) FeatureManager.logger.log(t);
        }
        return result.toString();
    }

    public void deleteFile() {
        File file = new File(filePathname);
        file.delete();
    }

    public void overwriteFile(String newContent) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(filePathname));
            output.write(newContent);
            output.close();
        } catch (Throwable e) {
            FeatureManager.logger.log(e.toString());
            for(StackTraceElement t : e.getStackTrace()) FeatureManager.logger.log(t);
        }
    }

    public void appendLine(String line) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(filePathname, true));
            output.newLine();
            output.write(line);
            output.close();
        } catch (Throwable e) {
            FeatureManager.logger.log(e.toString());
            for(StackTraceElement t : e.getStackTrace()) FeatureManager.logger.log(t);
        }
    }

}
