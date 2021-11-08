package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import java.io.BufferedReader;
import java.io.IOException;

public class BodyParser {

    public static String from(BufferedReader reader, Headers headers) throws IOException {
        int contentLength = headers.getAsInt("content-length");

        //use a direct reading method
        if(contentLength > 0) {

        }

        //body time!
        StringBuilder reqBodyBuilder = new StringBuilder();
        int length = 0;
        while(length < contentLength) {
            int nextChar = reader.read();
            if(nextChar < 0) break;
            reqBodyBuilder.append((char)nextChar);
            length++;
        }

        return reqBodyBuilder.toString();
    }
    public static String from(BufferedReader reader) throws IOException {

        //consume headers.
        Headers headers = Headers.from(reader);
        return from(reader, headers);
    }
}
