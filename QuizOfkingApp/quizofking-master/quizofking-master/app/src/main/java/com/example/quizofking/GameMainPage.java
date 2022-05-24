package com.example.quizofking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

public class GameMainPage extends AppCompatActivity {

    public static int gid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_main_page);

        getSupportActionBar().setTitle("Game Main Page");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button play =  findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent iii = new Intent(getBaseContext(), Game.class);
                Game.gid = gid;
                MainPage.pause = 1;
                startActivity(iii);
            }
        });

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

                while (MainPage.pause == 1) {

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    try {

                        Socket socket = new Socket("192.168.43.248", 7923);

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject("gamedata" + gid);

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        String message = (String) objectInputStream.readObject();

                        if (!message.equals("end")) {
                            final String[] m = message.split("\n");

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    TextView tv =  findViewById(R.id.opponents);
                                    tv.setText(m[0]);

                                    final ArrayList rowItems = new ArrayList<String>();

                                    int i = 0;
                                    for (String mm : m) {
                                        String[] line = mm.split(":");
                                        String txt = "";

                                        if (line[0].equals("-1")) {
                                            txt += "     no answer yet";
                                        }
                                        if (line[0].equals("0")) {
                                            txt += "     wrong answer";
                                        }
                                        if (line[0].equals("1")) {
                                            txt += "     correct answer";
                                        }
                                        if (line[0].equals("-2")) {
                                            txt += "     time's up       ";
                                        }

                                        txt += "             ";
                                        if (line[1].equals("-1")) {
                                            txt += "     no answer yet";
                                        }
                                        if (line[1].equals("0")) {
                                            txt += "     wrong answer";
                                        }
                                        if (line[1].equals("1")) {
                                            txt += "     correct answer";
                                        }
                                        if (line[1].equals("-2")) {
                                            txt += "                time's up";
                                        }

                                        if (i != 0) {
                                            if (i == 1) {
                                                rowItems.add("                                Round 1" );
                                            } else if (i == 5) {
                                                rowItems.add("                                Round 2" );
                                            } else if (i == 9) {
                                                rowItems.add("                                Round 3" );
                                            } else {
                                                rowItems.add(txt);
                                            }
                                        }
                                        i++;
                                    }

                                    ListView listView =  findViewById(R.id.games);
                                    ListAdapter adapter = new ArrayAdapter(getBaseContext(), R.layout.gameitem, rowItems) {
                                    };
                                    listView.setAdapter(adapter);
                                }


                            });
                        } else {
                            socket.close();
                            MainPage.pause = 0;
                            Thread.currentThread().interrupt();
                            finish();
                        }
                        socket.close();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}