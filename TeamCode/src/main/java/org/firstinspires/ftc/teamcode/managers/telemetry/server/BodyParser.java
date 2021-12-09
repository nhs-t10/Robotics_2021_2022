package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.io.BufferedReader;
import java.io.IOException;

public class BodyParser {

    public static String from(BufferedReader reader, Headers headers) throws IOException {
        boolean hasContentLength = headers.hasHeader("content-length");
        int contentLength = headers.getAsInt("content-length");

        if(hasContentLength) return readBodyInPlace(reader, contentLength);
        //body time!
        StringBuilder reqBodyBuilder = new StringBuilder();
        int length = 0;
        while(hasContentLength && length < contentLength) {
            int nextChar = reader.read();
            if(nextChar < 0) break;
            reqBodyBuilder.append((char)nextChar);
            length++;
        }

        return reqBodyBuilder.toString();
    }

    private static String readBodyInPlace(BufferedReader reader, int contentLength) throws IOException {
        char[] charArr = new char[contentLength];
        int readStart = 0;
        while(true) {
            int len = reader.read(charArr, readStart, contentLength - readStart);
            if(len < 0) break;
            readStart += len;
        }
        return new String(charArr);
    }

    public static String from(BufferedReader reader) throws IOException {

        //consume headers.
        Headers headers = Headers.from(reader);
        return from(reader, headers);
    }
}
