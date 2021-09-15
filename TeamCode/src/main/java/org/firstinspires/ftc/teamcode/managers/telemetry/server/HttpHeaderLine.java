package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;

public class HttpHeaderLine {
    public String verb;
    public String path;
    public String[] pathSegments;

    QueryStringParams queryStringParams;

    public HttpHeaderLine(String verb, String path) {
        this.verb = verb;
        //remove possible directory escape attacks
        this.path = path.replace("/./", "/").replace("/../", "/");

        this.pathSegments = path.split("/");

        if(path.indexOf('?') != -1) queryStringParams = QueryStringParams.from(path.substring(path.indexOf('?')));
        else queryStringParams = new QueryStringParams();


    }

    public static HttpHeaderLine from(BufferedReader reqReader) throws IOException {

        StringBuilder line = new StringBuilder();
        while(true) {
            int nextByte = reqReader.read();
            if(nextByte < 0) break;
            line.append((char) nextByte);
        }
        String[] words = line.toString().split(" ");

        return new HttpHeaderLine(words[0], words[1]);
    }
}
