package org.firstinspires.ftc.teamcode.managers.telemetry.server;

import org.firstinspires.ftc.teamcode.auxilary.UpdatableWeakReference;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.WeakHashMap;

public class Server {
    public int port;
    private boolean loaded;
    private ServerThread thread;

    public static boolean serverIsRunning = true;

    private final static Server SERVER = new Server();
    public static Server getServer() {
        return SERVER;
    }

    private Server() {
        //add a shutdown hook so the server can shut down itself when Java's over
        Runtime.getRuntime().addShutdownHook(new FratricideCommitterThread());

        thread = new ServerThread(this);
        thread.start();
    }




    public boolean blockUntilLoaded() {
        while(!loaded) {}
        return true;
    }

    public void setDataSource(TelemetryManager m) {
        thread.setDataSource(m);
    }

    private static class ServerThread extends Thread {
        int port;
        ServerSocket serverSocket;
        UpdatableWeakReference<TelemetryManager> dataSource;

        public boolean running = true;

        private WeakHashMap<String, StreamHandler> streamRegistry = new WeakHashMap<>();

        public void setDataSource(TelemetryManager dataSource) {
            this.dataSource.set(dataSource);
        }

        public ServerThread(Server parentProcess) {
            this.dataSource = new UpdatableWeakReference<>();
            this.port = 5564;
            while(port < 5664 && serverSocket == null) {
                try {
                    serverSocket = new ServerSocket(port);
                } catch (IOException err) {
                    port++;
                }
            }
            if(serverSocket == null) {
                FeatureManager.logger.log("Could not reserve TCP port");
            } else {
                parentProcess.port = this.port;
                FeatureManager.logger.add("Go to http://192.168.43.1:" + port);
            }

            parentProcess.loaded = true;
            
        }
        @Override
        public void run() {
            try {
                while (serverIsRunning) {
                    Socket socket = serverSocket.accept();

                    RequestHandlerThread rht = new RequestHandlerThread(socket, dataSource, streamRegistry);
                    new Thread(rht).start();
                }
                serverSocket.close();
            } catch(Exception e) {
                FeatureManager.logger.log("dashboard status" + e.toString());
            }
        }
    }
    private static class FratricideCommitterThread extends Thread {
        @Override
        public void run() {
            serverIsRunning = false;
        }
    }
}
