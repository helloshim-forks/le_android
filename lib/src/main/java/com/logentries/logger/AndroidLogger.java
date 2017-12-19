package com.logentries.logger;

import android.content.Context;

import java.io.IOException;

public class AndroidLogger {

    private static boolean useHttpPost = false;
    private static boolean useSsl = false;
    private static boolean isUsingDataHub = false;
    private static String dataHubAddr = null;
    private static int dataHubPort = 0;
    private static boolean logHostName = false;
    private static boolean sendRawLogMessage = false;

    private static AndroidLogger instance;

    private AsyncLoggingWorker loggingWorker;

    public static void setUseSsl(boolean ssl) {
        useSsl = ssl;
    }

    public static void setSendRawLogMessage(boolean rawLogMessage) {
        sendRawLogMessage = rawLogMessage;
    }

    public static synchronized AndroidLogger createInstance(Context context, String token) throws IOException {
        if (instance != null) {
            instance.loggingWorker.close();
        }

        instance = new AndroidLogger(context, token);
        return instance;
    }

    public static synchronized AndroidLogger getInstance() {
        if (instance != null) {
            return instance;
        } else {
            throw new IllegalArgumentException("Logger instance is not initialized. Call createInstance() first!");
        }
    }

    private AndroidLogger(Context context, String token) throws IOException {
        loggingWorker = new AsyncLoggingWorker(context, useSsl, useHttpPost, isUsingDataHub, token, dataHubAddr, dataHubPort, logHostName, sendRawLogMessage);
    }

    public void log(String message) {
        loggingWorker.addLineToQueue(message);
    }

}
