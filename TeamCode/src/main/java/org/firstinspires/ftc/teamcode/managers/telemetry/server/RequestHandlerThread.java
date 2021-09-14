package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import android.preference.PreferenceActivity;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.ParserTools;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandlerThread implements Runnable {
    private static final String HTTP_LINE_SEPARATOR = "\r\n";
    private Socket socket;
    TelemetryManager dataSource;

    public RequestHandlerThread(Socket socket, TelemetryManager dataSource) {
        this.socket = socket;
        this.dataSource = dataSource;
    }

    public void run() {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            HttpHeaderLine requestMeta = HttpHeaderLine.from(reader);
            Headers headers = Headers.from(reader);
            String body = BodyParser.from(reader, headers);

            String path = requestMeta.path;

            if(path.equals("/stream")) {
                writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                        + "Content-Type: " + "text/plain; charset=utf-8" + HTTP_LINE_SEPARATOR
                        + HTTP_LINE_SEPARATOR);

                long streamStartedAt = System.currentTimeMillis();
                while(socket.isConnected() && !socket.isClosed() && FeatureManager.isOpModeRunning && System.currentTimeMillis() - streamStartedAt < 30_000) {
                    try {
                        long loopStart = System.currentTimeMillis();
                        if (dataSource.hasNewData()) writer.print(dataSource.readData());
                        else writer.print(ControlCodes.DO_NOT_FRET_MOTHER_I_AM_ALIVE_JUST_BORED);

                        writer.print("\n");
                        writer.flush();

                        //block until the next send
                        while(System.currentTimeMillis() - loopStart < 1000/60) {}
                    } catch(Throwable e) {
                        StringBuilder r = new StringBuilder(e.getMessage());
                        for(StackTraceElement s : e.getStackTrace()) r.append("\n").append(s.toString());

                        writer.print("{\"error\":" + PaulMath.JSONify(r.toString()) + "}");
                        writer.print("\n");
                        writer.flush();
                    }
                }
                if(!FeatureManager.isOpModeRunning) {
                    writer.print(ControlCodes.I_AM_DYING_BUT_I_MAY_BE_BACK_LATER);
                } else {
                    writer.print(ControlCodes.THIS_CONVERSATION_IS_GETTING_LONG_PLEASE_START_A_NEW_ONE);
                }
            } else if(path.equals("/")) {
                String file = ServerFiles.indexDotHtml;
                    writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                            //+ "Content-Length: " + (file.getBytes(StandardCharsets.UTF_8).length) + HTTP_LINE_SEPARATOR
                            + "Content-Type: " + "text/html; charset=utf-8" + HTTP_LINE_SEPARATOR
                            + HTTP_LINE_SEPARATOR
                            + file);
            } else if(path.equals("/command")) {
                String command;
                if(requestMeta.queryStringParams.has("command")) {
                    command = requestMeta.queryStringParams.get("command");
                } else {
                    command = body;
                }
                FeatureManager.logger.log("debug: request body is " + command);

                String[] commaSepValues = command.split(",");

                writer.print(CommandHandler.handle(commaSepValues, dataSource));
            } else if(path.startsWith("/buildimgs")) {
                try (InputStream file = getClass().getResourceAsStream(path)) {
                    if (file == null) {
                        writer.print(HttpStatusCodeReplies.Not_Found);
                    } else {
                        writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                                + "Content-Type: " + "image/png" + HTTP_LINE_SEPARATOR
                                + HTTP_LINE_SEPARATOR);
                        //flush the class file into the stream
                        while (true) {
                            int fbyte = file.read();
                            if (fbyte == -1) break;
                            output.write(fbyte);
                        }
                        output.flush();
                    }
                }
            }else {
                writer.print(HttpStatusCodeReplies.Not_Found);
            }
            writer.flush();
            socket.close();


        } catch(Exception e) {
            dataSource.log().add("dashboard status" + e.toString());
        }
    }
}
