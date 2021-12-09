package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.buildhistory.BuildHistory;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
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

            FeatureManager.logger.log("clientRequest", path);

            if(path.equals("/stream")) {
                StreamHandler stream = new StreamHandler(writer, dataSource, socket);
                //add this stream to the registry and start it
                synchronized (streamRegistry) {
                    streamID = StreamHandler.genStreamID();
                    streamRegistry.put(streamID, stream);
                    stream.setStreamID(streamID);
                }
                stream.waitForStreamID();
                stream.start();
            } else if(path.equals("/")) {
                try(InputStream file = ServerFiles.getAssetStream("index.html")) {
                    writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                            + "Content-Type: " + "text/html; charset=utf-8" + HTTP_LINE_SEPARATOR
                            + HTTP_LINE_SEPARATOR);
                    writer.flush();
                    PaulMath.pipeStream(file, output);
                    output.flush();
                }
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
            } else if(path.equals("/build-history")) {
                writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                        + "Content-Type: " + "application/json" + HTTP_LINE_SEPARATOR
                        + HTTP_LINE_SEPARATOR);
                writer.print(BuildHistory.getJSON());
            } else if(path.equals("/fallible-hardware-devices")) {
                writer.print(FallableHardwareMapHttpFormatty.summarizeHardwareDevices(dataSource));
            } else {
                try (InputStream file = ServerFiles.getAssetStream(path)) {
                    if (file == null) {
                        writer.print(HttpStatusCodeReplies.Not_Found(path + " not found"));
                    } else {
                        String fileExtension = path.substring(path.lastIndexOf('.') + 1);
                        writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                                + "Content-Type: " + MimeTypeLookup.lookupFileExtension(fileExtension) + HTTP_LINE_SEPARATOR
                                + HTTP_LINE_SEPARATOR);
                        writer.flush();
                        PaulMath.pipeStream(file, output);
                        output.flush();
                    }
                }
            }
            writer.flush();
            socket.close();


        } catch(Exception e) {
            dataSource.log().add("error! " + e.toString() + Arrays.toString(e.getStackTrace()));
        }
    }
}
