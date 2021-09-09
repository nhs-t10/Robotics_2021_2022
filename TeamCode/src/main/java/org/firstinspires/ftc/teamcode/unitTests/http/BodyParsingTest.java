package org.firstinspires.ftc.teamcode.unitTests.http;

import org.firstinspires.ftc.teamcode.managers.telemetry.server.BodyParser;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Headers;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.HttpHeaderLine;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;

public class BodyParsingTest {
    String HTTP_BODY = "{}";
    String HTTP_REQUEST = "POST / HTTP/1.1\r\n" +
            "Host: www.example.com\r\n" +
            "Age: 3\r\n" +
            "Content-Type: application/json; charset=UTF-8\r\n" +
            "Content-Length: " + HTTP_BODY.getBytes(StandardCharsets.UTF_8).length + "\r\n" +
            "\r\n" +
            HTTP_BODY;

    @Test
    public void test() throws IOException {
        BufferedReader reqReader = new BufferedReader(new StringReader(HTTP_REQUEST));

        HttpHeaderLine headerLine = HttpHeaderLine.from(reqReader);

        assertEquals("/", headerLine.path);
        assertEquals("POST", headerLine.verb);

        Headers headers = Headers.from(reqReader);

        assertEquals("www.example.com", headers.get("Host"));
        assertEquals("3", headers.get("Age"));

        String body = BodyParser.from(reqReader, headers);

        assertEquals(HTTP_BODY, body);

        reqReader.close();
    }

}
