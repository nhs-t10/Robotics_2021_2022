package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {
    public int port;
    private boolean loaded;
    public Server(TelemetryManager manager) {
        new Thread(new ServerThread(manager, this)).start();
    }

    public boolean blockUntilLoaded() {
        while(!loaded) {}
        return true;
    }

    private static class ServerThread implements Runnable {
        int port;
        ServerSocket serverSocket;
        TelemetryManager dataSource;

        private HashMap<String, RequestHandlerThread> streamRegistry = new HashMap<>();

        public ServerThread(TelemetryManager d, Server parentProcess) {
            this.dataSource = d;
            this.port = 5564;
            while(port < 5664 && serverSocket == null) {
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException err) {
                    port++;
                }
            }
            if(serverSocket == null) d.addData("dashboard status","Could not reserve TCP port");
            else {
                parentProcess.port = this.port;
                FeatureManager.logger.add("Go to http://192.168.43.1:" + port);
                d.addData("dashboard status", "Go to http://192.168.43.1:" + port);
            }

            try {
                ServerFiles.loadIndexDotHtml();
            } catch(Exception e) {
                FeatureManager.logger.log("Could not load index.html");
            }

            parentProcess.loaded = true;

            if(!FeatureManager.isOpModeRunning) FeatureManager.logger.log("TELEMETRY SERVER WARNING: FeatureManager.isOpModeRunning has not been set to true. Server will immediately exit.");
        }
        @Override
        public void run() {
            new Thread(new FratricideCommitterThread(serverSocket)).start();
            try {
                while (FeatureManager.isOpModeRunning) {
                    Socket socket = serverSocket.accept();

                    RequestHandlerThread rht = new RequestHandlerThread(socket, dataSource, streamRegistry);
                    new Thread(rht).start();
                }
                serverSocket.close();
            } catch(Exception e) {
                dataSource.log().add("dashboard status" + e.toString());
            }
        }
    }
}
