package com.example.kareem.spotthatfire.Connection;

import android.util.Log;

import com.example.kareem.spotthatfire.Config;
import com.example.kareem.spotthatfire.Consts;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class ServerConnection {
    private ArrayList<ServerNotificationListener>  serverNotificationListeners = new ArrayList<>();
    private Socket socket;
    private ServerConnection()
    {

    }
    private static ServerConnection serverConnection = new ServerConnection();
    public static ServerConnection getInstance() {
        return serverConnection;
    }
    public void startConnection()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    socket = new Socket(Config.SERVER_IP, Consts.SERVER_PORT);
                    socket.setTcpNoDelay(false);
                    Scanner scanner = new Scanner(socket.getInputStream());
                    while (scanner.hasNext()) {
                        String s = scanner.nextLine();
                        Log.d("SenderTag", "run: " + s);
                        triggerCallBacks(Request.fromString(s));
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    public void sendRequest(final Request request)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (socket == null) return;
                    OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                    String stringBuilder = request.toString();
                    outputStream.write(stringBuilder.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void triggerCallBacks(Request request)
    {
        for (ServerNotificationListener serverNotificationListener:  serverNotificationListeners
             ) {
            serverNotificationListener.uponServerNotification(request);
        }
    }
    private void setOnNotificationReceivedListener(ServerNotificationListener  onNotificationReceivedListener)
    {
        serverNotificationListeners.add(onNotificationReceivedListener);
    }
    private void removeOnNotificationReceivedListener(ServerNotificationListener  onNotificationReceivedListener)
    {
        serverNotificationListeners.remove(onNotificationReceivedListener);
    }
}
