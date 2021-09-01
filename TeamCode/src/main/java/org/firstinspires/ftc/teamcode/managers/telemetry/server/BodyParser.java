package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.io.BufferedReader;
import java.io.IOException;

public class BodyParser {

    public static String from(BufferedReader reader, Headers headers) throws IOException {
        int contentLength = headers.getAsInt("content-length");

        FeatureManager.logger.log("debug: body time!");
        FeatureManager.logger.log("debug: content length: " + contentLength);

        //body time!
        StringBuilder reqBodyBuilder = new StringBuilder();
        int nextChar = 0;
        int length = 0;
        while((nextChar = reader.read()) != -1 && length < contentLength) {
            if(nextChar == -1) break;
            FeatureManager.logger.log("debug: length: " + length);
            FeatureManager.logger.log("debug: " + nextChar);
            reqBodyBuilder.append((char)nextChar);
            length++;
        }
        FeatureManager.logger.log("debug: escaped while loop!");

        return reqBodyBuilder.toString();
    }
    public static String from(BufferedReader reader) throws IOException {

        //consume headers.
        Headers headers = Headers.from(reader);
        int contentLength = headers.getAsInt("content-length");

        FeatureManager.logger.log("debug: body time!");
        FeatureManager.logger.log("debug: content length: " + contentLength);

        //body time!
        StringBuilder reqBodyBuilder = new StringBuilder();
        int nextChar = 0;
        int length = 0;
        while((nextChar = reader.read()) != -1 && length < contentLength) {
            if(nextChar == -1) break;
            FeatureManager.logger.log("debug: length: " + length);
            FeatureManager.logger.log("debug: " + nextChar);
            reqBodyBuilder.append((char)nextChar);
            length++;
        }
        FeatureManager.logger.log("debug: escaped while loop!");

        return reqBodyBuilder.toString();
    }
}
