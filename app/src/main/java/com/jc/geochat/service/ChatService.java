package com.jc.geochat.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.jc.geochat.Constants;
import com.jc.geochat.Utils;
import com.jc.geochat.notification.NotificationDecorator;

public class ChatService extends Service {
    private static final String TAG = "ChatService";

    public static final String MSG_CMD = "msg_cmd";
    public static final int CMD_JOIN_CHAT = 10;
    public static final int CMD_LEAVE_CHAT = 20;
    public static final int CMD_SEND_MESSAGE = 30;
    public static final int CMD_RECEIVE_MESSAGE = 40;
    public static final int CMD_GENERATE_MESSAGE = 50;
    public static final int CONNECT_ERROR_59 = 59;
    public static final int CMD_STOP_SERVICE_MESSAGE = 60;
    public static final int SEND_RANDOM_ID = 70;

    public static final String KEY_MESSAGE_TEXT = "message_text";
    public static final String KEY_USER_NAME = "user_name";
    public static final String BROADCAST_ID = "com.jc.chatboot.message";

    private NotificationManager notificationMgr;
    private PowerManager.WakeLock wakeLock;
    private NotificationDecorator notificationDecorator;

    public ChatService() {
    }

    @Override
    public void onCreate() {
        Log.v(TAG, "onCreate()");
        super.onCreate();
        notificationMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationDecorator = new NotificationDecorator(this, notificationMgr);

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, "onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        if (intent != null) {
            Bundle data = intent.getExtras();
            data.putString(Constants.KEY_USER_NAME,Utils.getUserNameFromPreferences(getApplicationContext()));
            handleData(data);
            if (!wakeLock.isHeld()) {
                Log.v(TAG, "acquiring wake lock");
                wakeLock.acquire();
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        notificationMgr.cancelAll();
        Log.v(TAG, "releasing wake lock");
        wakeLock.release();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int getResponseCode() {
        return 0;
    }

    public class ChatServiceBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }


    public void sendMessage(String message) {
        Intent intent = new Intent(BROADCAST_ID);
        intent.putExtra("message", message);
        sendBroadcast(intent);
    }

    private void handleData(Bundle data) {
        int command = data.getInt(MSG_CMD);
        final String user = (String) data.get(Constants.KEY_USER_NAME);
        final String testUser = "Bot";

        Log.d(TAG, "-(<- received command data to service: command=" + command);
        if (command == CMD_JOIN_CHAT) {
            String userName = (String) data.get(KEY_USER_NAME);
            notificationDecorator.displaySimpleNotification("Joining Chat...", "Connecting as User: " + userName);
            sendMessage("Connecting as User: " + userName);
        } else if (command == CMD_LEAVE_CHAT) {
            notificationDecorator.displaySimpleNotification("Leaving Chat...", "Disconnecting");
            sendMessage("Disconnecting");
            stopSelf();
        } else if (command == CMD_SEND_MESSAGE) {
            String messageText = (String) data.get(KEY_MESSAGE_TEXT);
            notificationDecorator.displaySimpleNotification("Sending message...", messageText);
            sendMessage(messageText);
        } else if (command == CMD_RECEIVE_MESSAGE) {
            String testMessage = "Simulated Message";
            notificationDecorator.displaySimpleNotification("New message...: "+ testUser, testMessage);
            sendMessage(testMessage);
        }
        else if (command == CONNECT_ERROR_59) {
            final String studentIdLast2Digits = (String) data.get(Constants.KEY_ID_LAST_DIGITS);
            notificationDecorator.displaySimpleNotification("Error", "Connect Error: "+studentIdLast2Digits);
        }
        else if (command == SEND_RANDOM_ID) {
            final int randomNumber = data.getInt(Constants.GENERATED_RANDOM_NUMBER_KEY);
            notificationDecorator.displaySimpleNotification("Random Number", "ChatService Received: "+randomNumber);
        }
        else if (command == CMD_STOP_SERVICE_MESSAGE) {
            String testMessage = "Simulated Message";
            final String studentIdLast2Digits = (String) data.get(Constants.KEY_ID_LAST_DIGITS);
            sendMessage("ChatBot Stopped: "+studentIdLast2Digits);
            notificationDecorator.displaySimpleNotification("Shutdown", "ChatBot Stopped: "+studentIdLast2Digits);
        }
        else if (command == CMD_GENERATE_MESSAGE) {

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        notificationDecorator.displaySimpleNotification("New message...: "+testUser, "Hello "+user,1);
                        sendMessage("Hello "+user);
                        Thread.sleep(1000);
                        notificationDecorator.displaySimpleNotification("New message...: "+testUser, "How are you? ",2);
                        sendMessage("How are you? ");
                        Thread.sleep(1000);
                        notificationDecorator.displaySimpleNotification("New message...: "+testUser, "Good Bye "+user+"!",3);
                        sendMessage("Good Bye "+user+"!");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        else {
            Log.w(TAG, "Ignoring Unknown Command! id=" + command);
        }
    }
}
