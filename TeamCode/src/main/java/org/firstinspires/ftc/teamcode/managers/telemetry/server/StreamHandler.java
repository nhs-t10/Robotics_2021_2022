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
    private String streamID;

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
                + HTTP_LINE_SEPARATOR);

        writer.print("{\"streamID\": \"" + streamID
                + "\",\"buildHistoryInfo\": " + BuildHistory.getJSON()
                + ",\"sendsPerSecond\": " + sendPerSecond
                + ",\"autoautoDataTransferAmount\": " + programJsonSendingFlag +"}\n");
        writer.flush();

        long streamStartedAt = System.currentTimeMillis();
        while(socket.isConnected() && !socket.isClosed() && FeatureManager.isOpModeRunning && System.currentTimeMillis() - streamStartedAt < 30_000) {
            try {
                if (dataSource.hasNewData()) {
                    dataSource.autoauto.setProgramJsonSendingFlag(programJsonSendingFlag);
                    writer.print(dataSource.readData());
                }
                else {
                    writer.print(ControlCodes.DO_NOT_FRET_MOTHER_I_AM_ALIVE_JUST_BORED);
                }

                writer.print("\n");
                writer.flush();

                //block until the next send
                //noinspection BusyWait
                Thread.sleep((long) (1000 / sendPerSecond));
            } catch(Throwable e) {
                String msg = e.getMessage();
                StringBuilder r = new StringBuilder(msg == null ? "<no message>" : msg);
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
    }

    public void setStreamID(String streamID) {
        this.streamID = streamID;
    }
}
