package org.firstinspires.ftc.teamcode.auxilary.buildhistory;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.firstinspires.ftc.teamcode.auxilary.FileSaver;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.jetbrains.annotations.NotNull;
import org.junit.Ignore;

@RequiresApi(api = Build.VERSION_CODES.O)
public class BuildHistory {
    public static String builderBrowserFingerprint = "eb1a4f697ff229b78546c1203de1e39a";
    public static String buildTimeIso = "2021-08-15T01:16:18.413Z";
    public static String buildName = "Leonel Ansekalt";
    public static String buildHistory = "Leonel Ansekalt,2021-08-15T01:16:18.413Z\nKaison Ansekalt,2021-08-15T01:15:25.278Z\nAaliyah Ansekalt,2021-08-15T01:15:12.008Z\nCadence Ansekalt,2021-08-15T01:14:13.838Z\nHarlow Ansekalt,2021-08-15T01:13:18.726Z\nLondon Ansekalt,2021-08-15T01:12:30.320Z\nIsaiah Ansekalt,2021-08-15T01:08:48.336Z\nCason Ansekalt,2021-08-15T01:07:56.248Z\nFrancisco Ansekalt,2021-08-15T01:01:08.900Z\nBenson Ansekalt,2021-08-15T01:00:36.163Z\nNadia Ansekalt,2021-08-15T00:58:54.619Z\nJesus Ansekalt,2021-08-15T00:55:44.778Z\nArian Ansekalt,2021-08-03T01:30:51.047Z\nIsabella Ansekalt,2021-08-03T01:27:22.950Z\nIsla Ansekalt,2021-08-03T01:11:32.887Z\nDella Ansekalt,2021-07-31T16:02:06.673Z\nEllis Ansekalt,2021-07-31T15:59:21.124Z\nSage Ansekalt,2021-07-31T15:58:30.854Z\nBrady Ansekalt,2021-07-31T15:52:46.653Z\nChance Ansekalt,2021-07-31T15:52:11.444Z\nKane Ansekalt,2021-07-31T15:49:07.630Z\nWinston Ansekalt,2021-07-31T15:45:43.038Z\nLola Ansekalt,2021-07-31T15:45:18.998Z\nElaine Ansekalt,2021-07-31T15:44:35.451Z\nBexley Ansekalt,2021-07-31T15:43:41.010Z\nKaylie Ansekalt,2021-07-31T15:42:05.338Z\nMarlowe Ansekalt,2021-07-31T15:40:25.281Z\nSierra Ansekalt,2021-07-31T15:37:51.430Z\nDuncan Ansekalt,2021-07-31T15:37:27.474Z\nLevi Ansekalt,2021-07-31T15:36:52.983Z\nLian Ansekalt,2021-07-31T15:35:50.987Z\nYousef Ansekalt,2021-07-31T15:35:08.204Z\nTatum Ansekalt,2021-07-31T15:34:28.476Z\nEithan Ansekalt,2021-07-31T15:31:02.722Z\nAdam Ansekalt,2021-07-31T15:30:17.926Z\nChandler Ansekalt,2021-07-31T15:29:00.165Z\nBlaise Ansekalt,2021-07-31T15:21:44.572Z\nSara Ansekalt,2021-07-31T15:20:42.205Z\nMargo Ansekalt,2021-07-31T15:08:19.453Z\nMauricio Ansekalt,2021-07-31T15:07:23.559Z\nLanden Ansekalt,2021-07-31T15:06:52.044Z\nNovah Ansekalt,2021-07-31T15:06:01.065Z\nSophie Ansekalt,2021-07-31T15:04:02.118Z\nBowen Ansekalt,2021-07-31T14:59:33.784Z\nMeredith Ansekalt,2021-07-31T14:55:25.134Z\nTommy Ansekalt,2021-07-31T14:54:34.898Z\nNovalee Ansekalt,2021-07-31T05:05:44.943Z\nShepard Ansekalt,2021-07-31T05:04:59.380Z\nJaxtyn Ansekalt,2021-07-31T05:02:41.302Z\nLily Ansekalt,2021-07-31T05:02:11.827Z\nGary Ansekalt,2021-07-31T04:59:37.562Z\nRowan Ansekalt,2021-07-31T04:57:40.602Z\nKimberly Ansekalt,2021-07-31T04:56:15.962Z\nKiara Ansekalt,2021-07-31T04:54:55.575Z\nMaci Ansekalt,2021-07-31T04:54:13.079Z\nHenley Ansekalt,2021-07-31T04:53:42.893Z\nJaden Ansekalt,2021-07-31T04:53:15.073Z\nGregory Ansekalt,2021-07-31T04:52:39.627Z\nLondon Ansekalt,2021-07-31T04:52:05.169Z\nMariah Ansekalt,2021-07-31T04:50:45.701Z\nJerry Ansekalt,2021-07-31T04:50:19.879Z\nArya Ansekalt,2021-07-31T04:48:56.943Z\nKhloe Ansekalt,2021-07-31T04:48:26.643Z\nJaiden Ansekalt,2021-07-31T04:46:15.032Z\nValentin Ansekalt,2021-07-31T04:44:31.704Z\nNico Ansekalt,2021-07-31T04:41:38.423Z\nLuka Ansekalt,2021-07-31T04:34:22.835Z\nAnnika Ansekalt,2021-07-31T04:32:55.822Z\nKeily Ansekalt,2021-07-31T04:31:56.930Z\nSaoirse Ansekalt,2021-07-31T04:30:46.096Z\nAhmir Ansekalt,2021-07-31T04:29:02.585Z\nJayda Ansekalt,2021-07-31T04:28:20.739Z\nAxl Ansekalt,2021-07-31T04:27:08.089Z\nArielle Ansekalt,2021-07-31T04:25:57.639Z\nElliot Ansekalt,2021-07-31T04:25:42.096Z\nSolomon Ansekalt,2021-07-31T04:25:25.676Z\nEstelle Ansekalt,2021-07-31T04:21:43.094Z\nTeresa Ansekalt,2021-07-31T04:16:26.503Z\nLouise Ansekalt,2021-07-31T04:12:41.037Z\nSydney Ansekalt,2021-07-31T04:12:32.560Z\nDayana Ansekalt,2021-07-31T04:12:18.707Z\nMarlee Ansekalt,2021-07-31T04:11:20.648Z\nRylee Ansekalt,2021-07-31T04:10:31.579Z\nAmalia Ansekalt,2021-07-31T04:10:02.875Z\nAiden Ansekalt,2021-07-31T04:09:34.861Z\nKayson Ansekalt,2021-07-31T04:09:11.366Z\nTitus Ansekalt,2021-07-31T04:08:55.943Z\nLogan Ansekalt,2021-07-31T04:08:26.804Z\nAri Ansekalt,2021-07-31T04:07:55.755Z\nChristina Ansekalt,2021-07-31T03:58:26.262Z\nGuillermo Ansekalt,2021-07-31T03:56:40.306Z\nCharlotte Ansekalt,2021-07-31T03:55:31.153Z\nNoah Ansekalt,2021-07-31T03:54:29.271Z\nChristopher Ansekalt,2021-07-31T03:52:53.622Z\nNathaniel Ansekalt,2021-07-31T03:51:12.000Z\nKimber Ansekalt,2021-07-31T03:49:15.558Z\nKellan Ansekalt,2021-07-31T03:47:39.653Z\nBrian Ansekalt,2021-07-31T03:46:09.912Z\nEugene Ansekalt,2021-07-31T03:45:06.877Z\nCanaan Ansekalt,2021-07-31T03:44:28.173Z";

    public static final String MEMORY_FILE = "build-history.csv";
    public static final int MOST_RECENT_INCLUDE_NUM = 200;

    public static void init() {
        loadLocalMemory();
    }
    private static void loadLocalMemory() {
        FileSaver localFile = new FileSaver(MEMORY_FILE);
        String localFileContent = PaulMath.join("\n", localFile.readLines().toArray(new String[0]));

        String[][] localBuildTable = parseCsvToTable(localFileContent);
        String[][] builtBuildTable = getBuildHistoryAsTable();

        HashMap<String, String> records = new HashMap<>();


        for(String[] row : builtBuildTable) {
            if(row.length < 2) continue;
            records.put(row[0], row[1]);
        }
        for(String[] row : localBuildTable) {
            if(row.length < 2) continue;
            records.put(row[0], row[1]);
        }


        Set<Map.Entry<String, String>> entrySet = records.entrySet();

        //copy over to an array
        BuildMemoryData[] rowData = new BuildMemoryData[records.size()];
        int index = 0;
        for(Map.Entry<String, String> entry : entrySet) {
            try {
                Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")).parse(entry.getValue());
                long ms = date.getTime();
                rowData[index] = new BuildMemoryData(entry.getKey(), ms, entry.getValue());
            } catch (ParseException e) {
                rowData[index] = new BuildMemoryData(entry.getKey(), 0, entry.getValue());
            }
            index++;
        }

        //sort ascending
        for(int i = 0; i < rowData.length; i++) {
            long maxTime = rowData[i].getTime();
            int maxIndex = i;

            for(int j = i + 1; j < rowData.length; j++) {
                if(rowData[j].getTime() > maxTime) {
                    maxTime = rowData[j].getTime();
                    maxIndex = j;
                }
            }
            if(maxIndex != i) {
                BuildMemoryData swap = rowData[i];
                rowData[i] = rowData[maxIndex];
                rowData[maxIndex] = swap;
            }
        }

        //only include the most recent 200 values
        String[] sliceRows = new String[Math.min(rowData.length, MOST_RECENT_INCLUDE_NUM)];
        for(int i = sliceRows.length - 1; i >= 0; i--) {
            sliceRows[i] = rowData[i + (rowData.length - sliceRows.length)].toString();
        }

        //save values, both in the file and in the static variable so that others can have up-to-date data
        String content = PaulMath.join("\n", sliceRows);
        localFile.overwriteFile(content);
        buildHistory = content;
    }
    public static String[][] getBuildHistoryAsTable() {
        return parseCsvToTable(buildHistory);
    }

    private static String[][] parseCsvToTable(String csv) {
        String[] rows = csv.split("\n");
        String[][] table = new String[rows.length][];

        for(int i = 0; i < rows.length; i++) {
            table[i] = rows[i].split(",");
        }
        return table;
    }
    private static class BuildMemoryData {
        public String name;
        public long time;
        public String isoTime;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
        }

        public String getIsoTime() {
            return isoTime;
        }

        public void setIsoTime(String isoTime) {
            this.isoTime = isoTime;
        }

        public BuildMemoryData(String name, long time, String isoTime) {
            this.name = name;
            this.time = time;
            this.isoTime = isoTime;
        }

        @NotNull
        public String toString() {
            return this.name = "," + this.isoTime;
        }
    }
}
