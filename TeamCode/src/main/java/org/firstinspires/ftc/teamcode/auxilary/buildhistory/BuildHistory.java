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

@RequiresApi(api = Build.VERSION_CODES.O)
public class BuildHistory {
    public static String builderBrowserFingerprint = "a16b9bedb6dea6f993b9e90d7af3a317";
    public static String buildTimeIso = "2021-08-31T04:00:15.919Z";
    public static String buildName = "Kody Dinfeba";
    public static String buildHistory = "Kody Dinfeba,2021-08-31T04:00:15.919Z\nAlia Dinfeba,2021-08-31T00:42:47.507Z\nAyaan Dinfeba,2021-08-31T00:40:18.088Z\nLuka Dinfeba,2021-08-31T00:38:31.432Z\nRowan Dinfeba,2021-08-31T00:38:09.664Z\nTeagan Dinfeba,2021-08-31T00:32:54.350Z\nChristina Dinfeba,2021-08-31T00:32:21.551Z\nSelene Dinfeba,2021-08-31T00:29:02.697Z\nAthena Dinfeba,2021-08-31T00:27:00.538Z\nMccoy Dinfeba,2021-08-31T00:26:46.693Z\nEmersyn Dinfeba,2021-08-31T00:26:19.256Z\nShawn Dinfeba,2021-08-31T00:25:42.937Z\nYaretzi Dinfeba,2021-08-31T00:23:41.041Z\nNathalie Dinfeba,2021-08-31T00:21:31.660Z\nElijah Dinfeba,2021-08-31T00:20:52.055Z\nMavis Dinfeba,2021-08-31T00:18:31.753Z\nJudah Dinfeba,2021-08-31T00:18:15.231Z\nAinsley Dinfeba,2021-08-31T00:17:16.815Z\nLangston Dinfeba,2021-08-31T00:16:33.189Z\nTommy Dinfeba,2021-08-31T00:15:58.009Z\nDustin Dinfeba,2021-08-31T00:15:13.765Z\nArianna Dinfeba,2021-08-31T00:15:03.730Z\nWade Dinfeba,2021-08-31T00:14:39.418Z\nPhoenix Dinfeba,2021-08-31T00:12:57.834Z\nOlivia Dinfeba,2021-08-31T00:12:00.297Z\nUlises Dinfeba,2021-08-31T00:09:06.146Z\nMorgan Dinfeba,2021-08-31T00:07:35.179Z\nEduardo Dinfeba,2021-08-31T00:07:15.965Z\nAxel Dinfeba,2021-08-30T23:53:45.511Z\nJacob Dinfeba,2021-08-30T23:53:34.895Z\nArtemis Dinfeba,2021-08-30T23:53:28.117Z\nCeline Dinfeba,2021-08-30T23:51:04.212Z\nJasiah Dinfeba,2021-08-30T23:50:43.989Z\nPaola Dinfeba,2021-08-30T23:50:38.949Z\nEmmie Dinfeba,2021-08-30T23:49:14.607Z\nCole Dinfeba,2021-08-30T23:49:02.385Z\nDevon Dinfeba,2021-08-30T23:47:30.990Z\nNyla Dinfeba,2021-08-30T23:42:45.161Z\nWayne Dinfeba,2021-08-30T23:42:02.827Z\nEvelynn Dinfeba,2021-08-30T23:41:49.068Z\nLandon Dinfeba,2021-08-30T23:41:31.437Z\nJericho Dinfeba,2021-08-30T23:40:23.891Z\nNaomi Dinfeba,2021-08-30T23:38:52.871Z\nFletcher Dinfeba,2021-08-30T23:38:05.818Z\nMaximiliano Dinfeba,2021-08-30T23:37:24.200Z\nSalvatore Dinfeba,2021-08-30T23:33:50.397Z\nKiana Dinfeba,2021-08-30T23:24:25.208Z\nAriya Dinfeba,2021-08-30T23:23:01.139Z\nAlanna Dinfeba,2021-08-30T23:21:20.985Z\nMacie Dinfeba,2021-08-30T23:09:31.884Z\nStetson Dinfeba,2021-08-30T22:39:07.481Z\nKylee Dinfeba,2021-08-30T22:30:26.987Z\nIyla Dinfeba,2021-08-30T22:29:39.384Z\nBradley Dinfeba,2021-08-30T22:24:42.704Z\nApril Dinfeba,2021-08-30T22:22:19.977Z\nAlexis Dinfeba,2021-08-30T22:18:58.575Z\nYosef Dinfeba,2021-08-30T22:18:28.994Z\nAshlynn Dinfeba,2021-08-30T22:14:19.231Z\nAri Dinfeba,2021-08-30T22:11:58.438Z\nBriana Dinfeba,2021-08-30T22:11:29.924Z\nJuniper Dinfeba,2021-08-30T22:10:34.706Z\nAdalyn Dinfeba,2021-08-30T22:09:47.148Z\nAllyson Dinfeba,2021-08-30T22:08:53.107Z\nNatasha Dinfeba,2021-08-30T22:05:30.763Z\nBrycen Dinfeba,2021-08-30T22:03:04.988Z\nGarrett Dinfeba,2021-08-30T22:02:22.591Z\nEdwin Dinfeba,2021-08-30T21:46:34.225Z\nWilliam Dinfeba,2021-08-30T21:45:24.821Z\nNathan Dinfeba,2021-08-30T21:45:12.781Z\nElina Dinfeba,2021-08-30T20:52:10.216Z\nMaximilian Dinfeba,2021-08-30T20:51:40.048Z\nKensley Dinfeba,2021-08-30T20:51:17.387Z\nGiavanna Dinfeba,2021-08-30T20:50:28.477Z\nMyra Dinfeba,2021-08-30T20:42:07.491Z\nHarper Dinfeba,2021-08-30T20:40:19.262Z\nMaddox Dinfeba,2021-08-30T20:39:24.464Z\nLorenzo Dinfeba,2021-08-30T20:38:50.642Z\nAstrid Dinfeba,2021-08-30T20:24:19.868Z\nAlice Dinfeba,2021-08-30T20:23:58.541Z\nChance Dinfeba,2021-08-30T20:20:50.441Z\nMarcelo Dinfeba,2021-08-30T20:20:22.401Z\nKiara Dinfeba,2021-08-30T20:18:30.409Z\nTitus Dinfeba,2021-08-30T20:17:29.023Z\nBaker Dinfeba,2021-08-30T20:16:12.359Z\nLogan Dinfeba,2021-08-30T20:15:53.834Z\nLouise Dinfeba,2021-08-30T20:15:38.548Z\nSteven Dinfeba,2021-08-30T20:15:01.322Z\nAyleen Dinfeba,2021-08-30T20:11:41.351Z\nAyan Dinfeba,2021-08-30T20:11:02.743Z\nManuel Dinfeba,2021-08-30T20:10:19.526Z\nErmias Dinfeba,2021-08-30T20:09:53.693Z\nSky Dinfeba,2021-08-30T19:56:04.002Z\nJoelle Dinfeba,2021-08-30T19:19:18.735Z\nSkyler Dinfeba,2021-08-30T19:18:08.102Z\nCollins Ansekalt,2021-08-30T18:54:49.322Z\nAlyssa Ansekalt,2021-08-30T18:54:07.382Z\nLuciana Ansekalt,2021-08-30T18:53:10.035Z\nBrantley Ansekalt,2021-08-30T18:50:41.214Z\nKameron Ansekalt,2021-08-30T18:43:54.832Z\nGeorge Ansekalt,2021-08-30T18:42:31.422Z";

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
