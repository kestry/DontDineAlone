package com.hu.tyler.dontdinealone.net;

import android.app.Activity;

import com.hu.tyler.dontdinealone.LobbyActivity;
import com.hu.tyler.dontdinealone.MatchedActivity;
import com.hu.tyler.dontdinealone.MyApp;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class Connection extends Thread
{
    private Socket s = null;
    private AtomicBoolean running = new AtomicBoolean(true);
    private Activity activity = null;

    public Connection()
    {
    }

    public void run() {
        try {
            s = new Socket("169.233.193.75", 7575);
            s.setKeepAlive(true);
            sendMatch();

            while (running.get()) {
                try {
                    DataInputStream input = new DataInputStream(s.getInputStream());

                    if(input.available() > 0)
                    {
                        Reader r = new Reader(input);
                        short opcode = r.read16();
                        //print("Received: " + opcode);
                        switch (opcode)
                        {
                            case 0x01: // Send user info
                            {
                                byte status = r.read8();
                                if(status == 1)
                                    //print("Update Successful!");
                                break;
                            }
                            case 0x02: // Start Match
                            {
                                byte status = r.read8();
                                //print("Match: " + (status == 1 ? "Found" : "None"));
                                if(status == 1)
                                {
                                    callMatch();
                                }
                                break;
                            }
                            case 0x03: // Cancel Match
                            {
                                byte status = r.read8();
                                //print("Match: " + (status == 1 ? "Canceled" : "Error"));
                                break;
                            }
                            case 0x04: // Chat Messaging
                            {
                                String message = r.readStr();
                                processChat(message);
                                break;
                            }
                        }
                        r.terminate();
                    }
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }

            }
        } catch (IOException e) {
            //print(e.getMessage());
        }
    }

    public void send (Writer w)
    {
        final Writer packet = w;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DataOutputStream output = new DataOutputStream(s.getOutputStream());
                    output.write(packet.getData());
                    output.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void close() throws IOException {
        if(s != null)
            s.close();
        //print("Closed!");
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public boolean isConnected()
    {
        return s.isConnected();
    }

    public void sendMatch() {

        if(activity instanceof LobbyActivity)
        {
            final LobbyActivity lobbyActivity = ((LobbyActivity)activity);
            lobbyActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lobbyActivity.doMatch();
                }
            });
        }
    }

    public void callMatch() {

        if(activity instanceof LobbyActivity)
        {
            final LobbyActivity lobbyActivity = ((LobbyActivity)activity);
            lobbyActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    lobbyActivity.goToMatchingActivity();
                }
            });
        }
    }
    public void processChat(String msg) {
        final String s = msg;
        if(activity instanceof MatchedActivity)
        {
            final MatchedActivity matchedActivity = ((MatchedActivity)activity);
            matchedActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    matchedActivity.postMessage(s);
                }
            });
        }
    }
}
