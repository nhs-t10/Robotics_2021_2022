package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.buildhistory.BuildHistory;
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
import java.util.Arrays;
import java.util.HashMap;

public class RequestHandlerThread extends Thread {
    public static final String HTTP_LINE_SEPARATOR = "\r\n";
    private final HashMap<String, StreamHandler> streamRegistry;

    private Socket socket;
    TelemetryManager dataSource;
    private String streamID;

    public RequestHandlerThread(Socket socket, TelemetryManager dataSource, HashMap<String, StreamHandler> streamRegistry) {
        this.socket = socket;
        this.dataSource = dataSource;
        this.streamRegistry = streamRegistry;
    }

    public void run() {
        try {
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, StandardCharsets.UTF_8));

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8));

            HttpStatusLine requestMeta = HttpStatusLine.from(reader);
            Headers headers = Headers.from(reader);
            String body = BodyParser.from(reader, headers);

            String path = requestMeta.path;

            if(path.equals("/stream")) {
                StreamHandler stream = new StreamHandler(writer, dataSource, socket);
                //add this stream to the registry and start it
                synchronized (streamRegistry) {
                    streamID = genStreamID();
                    streamRegistry.put(streamID, stream);
                    stream.setStreamID(streamID);
                    stream.start();
                }
            } else if(path.equals("/")) {
                String file = ServerFiles.indexDotHtml;
                    writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                            //+ "Content-Length: " + (file.getBytes(StandardCharsets.UTF_8).length) + HTTP_LINE_SEPARATOR
                            + "Content-Type: " + "text/html; charset=utf-8" + HTTP_LINE_SEPARATOR
                            + "Content-Length: " + ServerFiles.indexDotHtml.getBytes(StandardCharsets.UTF_8).length + HTTP_LINE_SEPARATOR
                            + HTTP_LINE_SEPARATOR
                            + file);
            } else if(path.startsWith("/command")) {
                String command;
                if(requestMeta.queryStringParams.has("command")) {
                    command = requestMeta.queryStringParams.get("command");
                } else {
                    command = body;
                }
                FeatureManager.logger.log("debug: request body is " + command);

                String[] commaSepValues = command.split(",");

                writer.print(CommandHandler.handle(commaSepValues, dataSource, streamRegistry));
            } else if(path.startsWith("/buildimgs")) {
                try (InputStream file = ServerFiles.getAssetStream(path)) {
                    if (file == null) {
                        writer.print(HttpStatusCodeReplies.Not_Found("buildimgs not found"));
                    } else {
                        writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                                + "Content-Type: " + "image/png" + HTTP_LINE_SEPARATOR
                                + HTTP_LINE_SEPARATOR);
                        writer.flush();
                        //flush the class file into the stream
                        while (true) {
                            int fbyte = file.read();
                            if (fbyte == -1) break;
                            output.write(fbyte);
                        }
                        output.flush();
                    }
                }
            } else if(path.equals("/build-history")) {
                writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                        + "Content-Type: " + "application/json" + HTTP_LINE_SEPARATOR
                        + HTTP_LINE_SEPARATOR);
                writer.print(BuildHistory.getJSON());
            } else {
                writer.print(HttpStatusCodeReplies.Not_Found);
            }
            writer.flush();
            socket.close();


        } catch(Exception e) {
            dataSource.log().add("error! " + e.toString() + Arrays.toString(e.getStackTrace()));
        }
    }
    public String genStreamID() {
        return Long.toHexString(System.nanoTime()) + Integer.toHexString((int)(Math.random() * 1000));
    }
}
