package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import java.io.BufferedReader;
import java.io.IOException;

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
        String line = reqReader.readLine();

        String[] words = line.split(" ");

        return new HttpHeaderLine(words[0], words[1]);
    }
}
