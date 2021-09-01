package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.managers.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public Server(TelemetryManager manager) {
        new Thread(new ServerThread(manager)).start();
    }

    private static class ServerThread implements Runnable {
        int port;
        ServerSocket serverSocket;
        TelemetryManager dataSource;

        public ServerThread(TelemetryManager d) {
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
                FeatureManager.logger.add("Go to http://192.168.43.1:" + port);
                d.addData("dashboard status", "Go to http://192.168.43.1:" + port);
            }

            try {
                ServerFiles.loadIndexDotHtml();
            } catch(Exception e) {
                FeatureManager.logger.log("Could not load index.html");
            }

            if(!FeatureManager.isOpModeRunning) FeatureManager.logger.log("TELEMETRY SERVER WARNING: FeatureManager.isOpModeRunning has not been set to true. Server will immediately exit.");
        }
        @Override
        public void run() {
            new Thread(new FratricideCommitterThread(serverSocket)).start();
            try {
                while (FeatureManager.isOpModeRunning) {
                    Socket socket = serverSocket.accept();

                    new Thread(new RequestHandlerThread(socket, dataSource)).start();
                }
                serverSocket.close();
            } catch(Exception e) {
                dataSource.log().add("dashboard status" + e.toString());
            }
        }
    }
}
