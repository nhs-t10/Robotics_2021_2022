package org.firstinspires.ftc.teamcode.auxilary;


import android.app.Application;
import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class FileSaver {
    private String filePathname;
    private Context context;
    public String fileName;

    public FileSaver(String _fileName) {
        try {
            this.context = ((Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null)).getApplicationContext();
        } catch (Throwable ignored) {}
        this.fileName = _fileName;
        this.filePathname = context.getExternalFilesDir(null).getPath() + "/" + fileName;
    }

    public ArrayList<String> readLines() {

        String thisLine;
        BufferedReader textFile;
        ArrayList<String> keyframes = new ArrayList<String>();

        try {
            textFile = new BufferedReader(new InputStreamReader(new FileInputStream(filePathname)));
            BufferedReader br = new BufferedReader(textFile);

            while ((thisLine = br.readLine()) != null) {
                keyframes.add(thisLine);
            }
        } catch (Throwable e) {
            //e.printStackTrace();
        }
        return keyframes;
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
            //e.printStackTrace
        }
    }

    public void appendLine(String line) {
        try {
            BufferedWriter output = new BufferedWriter(new FileWriter(filePathname, true));
            output.newLine();
            output.write(line);
            output.close();
        } catch (Throwable e) {
            //e.printStackTrace
        }
    }

}
