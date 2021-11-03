package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.buildhistory.BuildHistory;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.PrintWriter;
import java.net.Socket;

public class StreamHandler {
    private static final String HTTP_LINE_SEPARATOR = RequestHandlerThread.HTTP_LINE_SEPARATOR;

    private final PrintWriter writer;
    private final TelemetryManager dataSource;
    private final Socket socket;
    private volatile String streamID;

    public int programJsonSendingFlag = 2;
    public float sendPerSecond = ControlCodes.STREAM_SENDS_PERSEC;

    public StreamHandler(PrintWriter writer, TelemetryManager dataSource, Socket socket) {
        this.writer = writer;
        this.dataSource = dataSource;
        this.socket = socket;
    }

    public void start() {
        writer.print("HTTP/1.1 200 OK" + HTTP_LINE_SEPARATOR
                + "Content-Type: " + "text/plain; charset=utf-8" + HTTP_LINE_SEPARATOR
                + "Transfer Encoding: chunked" + HTTP_LINE_SEPARATOR
                + HTTP_LINE_SEPARATOR);

        printChunk("{\"streamID\": \"" + streamID + "\""
                + ",\"autoautoProgram\": " + dataSource.autoauto.getProgramJson()
                + ",\"sendsPerSecond\": " + sendPerSecond
                + ",\"autoautoDataTransferAmount\": " + programJsonSendingFlag +"}\n");

        long streamStartedAt = System.currentTimeMillis();
        while(socket.isConnected() && !socket.isClosed() && FeatureManager.isOpModeRunning && System.currentTimeMillis() - streamStartedAt < 30_000) {
            try {
                if (dataSource.hasNewData()) {
                    dataSource.autoauto.setProgramJsonSendingFlag(programJsonSendingFlag);
                    printChunk(dataSource.readData());
                }
                else {
                    printChunk(ControlCodes.DO_NOT_FRET_MOTHER_I_AM_ALIVE_JUST_BORED);
                }

                writer.flush();

                //block until the next send
                //noinspection BusyWait
                Thread.sleep((long) (1000 / sendPerSecond));
            } catch(Throwable e) {
                String msg = e.getMessage();
                StringBuilder r = new StringBuilder(msg == null ? "<no message>" : msg);
                for(StackTraceElement s : e.getStackTrace()) r.append("\n").append(s.toString());

                printChunk("{\"error\":" + PaulMath.JSONify(r.toString()) + "}");
                writer.flush();
            }
        }
        if(!FeatureManager.isOpModeRunning) {
            printChunk(ControlCodes.I_AM_DYING_BUT_I_MAY_BE_BACK_LATER);
        } else {
            printChunk(ControlCodes.THIS_CONVERSATION_IS_GETTING_LONG_PLEASE_START_A_NEW_ONE);
        }
        printChunk("");
        writer.flush();
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }

    public void printChunk(String str) {
        int bytes = str.getBytes().length;
        String hexBytes = Integer.toHexString(bytes).toUpperCase();
        writer.print(hexBytes + HTTP_LINE_SEPARATOR + str + HTTP_LINE_SEPARATOR);
    }

    public void waitForStreamID() {
        while(streamID == null);
    }
}
