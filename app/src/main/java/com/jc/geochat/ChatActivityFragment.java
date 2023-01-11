package com.jc.geochat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jc.geochat.service.ChatService;

public class ChatActivityFragment extends Fragment {
    private static final String TAG = "ChatActivityFragment";

    private BroadcastReceiver receiver;

    EditText edtMessage;
    String userName = "user1";
    public ChatActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat, container, false);
        Button btnJoinChat = (Button) v.findViewById(R.id.btnJoinChat);
        btnJoinChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending to Chat Service: Join.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                joinChat(userName);
            }
        });


        Button btnGenMessage = (Button) v.findViewById(R.id.btnGenerateMessage);
        btnGenMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending to Chat Service: Generate Message", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                generateMessage(userName);
            }
        });

        Button btnLeaveChat = (Button) v.findViewById(R.id.btnLeaveChat);
        btnLeaveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending to Chat Service: Leave", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                leaveChat();
            }
        });

        Button btnSendMessage = (Button) v.findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Sending Message...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                sendMessage(edtMessage.getText().toString());
            }
        });

        Button btnReceiveMessage = (Button) v.findViewById(R.id.btnReceiveMessage);
        btnReceiveMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New Message Arrived...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                simulateOnMessage();
            }
        });

        Button btnStopMessage = (Button) v.findViewById(R.id.btnStopServiceMessage);
        btnStopMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New Message Arrived...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                stopServiceMessage();
            }
        });
        final TextView textView = (TextView) v.findViewById(R.id.serviceMessageTextView);

        // Create the BroadcastReceiver
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String message = intent.getStringExtra("message");
                textView.setText(message);
            }
        };

        IntentFilter filter = new IntentFilter();
        filter.addAction(ChatService.BROADCAST_ID);
        getActivity().registerReceiver(receiver, filter);

        edtMessage = (EditText) v.findViewById(R.id.edtMessage);

        loadUserNameFromPreferences();

        return v;
    }

    private void loadUserNameFromPreferences() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = prefs.getString(Constants.KEY_USER_NAME, "Name");
    }

    private void joinChat(String userName){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_JOIN_CHAT);
        data.putString(ChatService.KEY_USER_NAME, userName);
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    private void generateMessage(String userName){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_GENERATE_MESSAGE);
        data.putString(ChatService.KEY_USER_NAME, userName);
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    private void leaveChat(){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_LEAVE_CHAT);
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    private void stopServiceMessage(){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_STOP_SERVICE_MESSAGE);
        data.putString(Constants.KEY_ID_LAST_DIGITS, "59");
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }


    private void sendMessage(String messageText){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_SEND_MESSAGE);
        data.putString(ChatService.KEY_MESSAGE_TEXT, messageText);
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    private void simulateOnMessage(){
        Bundle data = new Bundle();
        data.putInt(ChatService.MSG_CMD, ChatService.CMD_RECEIVE_MESSAGE);
        Intent intent = new Intent(getContext(), ChatService.class);
        intent.putExtras(data);
        getActivity().startService(intent);
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(receiver);
        super.onDestroy();
    }
}
