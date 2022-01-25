package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.ControlCodes;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Headers;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
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

        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("localhost", telemetry.server.port), 30_000);

            PrintWriter sendWriter = new PrintWriter(socket.getOutputStream());
            sendWriter.write("GET /stream HTTP/1.1\r\n" +
                    "Host: localhost\r\n\r\n");
            sendWriter.flush();
            InputStream httpReply = socket.getInputStream();

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
            while (!isDataLine(dataLine)) {
                dataLine = httpBodyReader.readLine();
            }

            assertThat("Reply data contains pojo data", dataLine, containsString("\"testString\":\"foobar\",\"testInt\":3,\"testFloat\":2.4"));

            httpReply.close();
        }
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

    public static class TestPojoOpmode extends DummyOpmode {
        public String testString = "foobar";
        public int testInt = 3;
        public float testFloat = 2.4f;

    }

    public static boolean isDataLine(String dataLine) {
        return dataLine.length() > 0 && !Character.isDigit(dataLine.charAt(0)) &&
                !(dataLine.equals(ControlCodes.DO_NOT_FRET_MOTHER_I_AM_ALIVE_JUST_BORED) || dataLine.startsWith("{\"streamID\""));
    }
}
