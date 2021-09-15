package org.firstinspires.ftc.teamcode.unitTests.telemetry;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.BodyParser;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Headers;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.HttpHeaderLine;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.Server;
import org.firstinspires.ftc.teamcode.managers.telemetry.server.ServerFiles;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyOpmode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyTelemetry;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Socket;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TelemetryManagerServerTest {
    @Test
    public void test() throws IOException {
        int testDurationMs = 60_000;
        long testStart = System.currentTimeMillis();

        FeatureManager.setIsOpModeRunning(true);

        TelemetryManager telManNoFeat = new TelemetryManager(new DummyTelemetry(), new DummyOpmode(), TelemetryManager.BITMASKS.NONE);

        Server server = new Server(telManNoFeat);

        server.blockUntilLoaded();

        assertTrue(connectAddress("localhost", server.port));

        String httpGetRoot = getResponse("localhost", server.port, "GET / HTTP/1.1\r\nHost: example.com\r\n\r\n");

        FeatureManager.logger.log(PaulMath.JSONify(httpGetRoot));

        BufferedReader httpReader = new BufferedReader(new StringReader(httpGetRoot));

        Headers headers = Headers.from(httpReader);
        String body = BodyParser.from(httpReader, headers);

        assertNotNull(body);
        assertEquals(ServerFiles.indexDotHtml, body);

        while(System.currentTimeMillis() - testStart < testDurationMs) {}

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
    private String getResponse(String host, int port, String sendBody) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(host, port), 30_000);

            PrintWriter sendWriter = new PrintWriter(socket.getOutputStream());
            sendWriter.write(sendBody);
            sendWriter.flush();
            sendWriter.close();

            try(InputStream response = socket.getInputStream()) {
                StringBuilder responseMessage = new StringBuilder();
                while(true) {
                    FeatureManager.logger.log(responseMessage.toString());
                    int nextByte = response.read();
                    if(nextByte < 0) break;
                    else responseMessage.append((char) nextByte);
                }

                return responseMessage.toString();
            }

        } catch (IOException e) {
            return ""; // Either timeout or unreachable or failed DNS lookup.
        }
    }
}
