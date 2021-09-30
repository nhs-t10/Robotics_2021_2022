package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.ControlCodes;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Headers;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.hamcrest.core.StringContains;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PojoInspectionServerTest {
    @Test
    public void test() throws IOException {

        FeatureManager.setIsOpModeRunning(true);

        OpMode testOpmode = new TestPojoOpmode();

        TelemetryManager telemetry = new TelemetryManager(testOpmode, TelemetryManager.BITMASKS.POJO_MONITOR | TelemetryManager.BITMASKS.WEBSERVER);

        telemetry.server.blockUntilLoaded();

        assertTrue(connectAddress("localhost", telemetry.server.port));

        InputStream httpReply = getResponseStream("localhost", telemetry.server.port,
                "GET /stream HTTP/1.1\r\n" +
                        "Host: localhost\r\n\r\n");

        BufferedReader httpBodyReader = new BufferedReader(new InputStreamReader(httpReply, StandardCharsets.UTF_8));

        //discard status line
        assertEquals("HTTP/1.1 200 OK", httpBodyReader.readLine());

        Headers headers = Headers.from(httpBodyReader);
        assertEquals("text/plain; charset=utf-8", headers.get("Content-Type"));


        telemetry.addData("foo", 3);
        telemetry.log().add("bar");
        telemetry.update();

        String dataLine = httpBodyReader.readLine();
        //keep reading until we get data
        while(dataLine.equals(ControlCodes.DO_NOT_FRET_MOTHER_I_AM_ALIVE_JUST_BORED)) {
            dataLine = httpBodyReader.readLine();
        }

        assertThat("Reply data contains pojo data", dataLine, containsString("\"testString\":\"foobar\",\"testInt\":3,\"testFloat\":2.4"));

        httpReply.close();

        FeatureManager.setIsOpModeRunning(false);
    }

    private boolean connectAddress(String host, int port) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 30_000);
            return true;
        } catch (IOException e) {
            return false; // Either timeout or unreachable or failed DNS lookup.
        }
    }
    private InputStream getResponseStream(String host, int port, String sendBody) {
        Socket socket = new Socket();
        try {
            socket.connect(new InetSocketAddress(host, port), 30_000);

            PrintWriter sendWriter = new PrintWriter(socket.getOutputStream());
            sendWriter.write(sendBody);
            sendWriter.flush();

            return socket.getInputStream();
        } catch (IOException e) {
            return null; // Either timeout or unreachable or failed DNS lookup.
        }
    }

    public static class TestPojoOpmode extends DummyOpmode {
        public String testString = "foobar";
        public int testInt = 3;
        public float testFloat = 2.4f;

    }
}
