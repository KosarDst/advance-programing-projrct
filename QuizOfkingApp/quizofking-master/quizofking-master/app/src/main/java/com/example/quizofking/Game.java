package com.example.quizofking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Game extends AppCompatActivity {

    public static int gid;
    int hint = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);


        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                while (MainPage.pause == 1) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateGames();
                        }
                    });
                }
            }
        });
        t.start();

        Button newChance = findViewById(R.id.newchance);
        Button remove = findViewById(R.id.remove);
        newChance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hint == 0) {
                    try {
                        Socket socket = new Socket("192.168.43.248", 7923);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject("hint" + MainPage.username);

                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String message = (String) ois.readObject();

                        if (message.equals("OK")) {
                            Toast.makeText(Game.this, "DONE :)", Toast.LENGTH_LONG).show();
                            hint = 1;
                        } else {
                            Toast.makeText(Game.this, "You don't have enough coins!", Toast.LENGTH_LONG).show();
                        }
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Game.this, "just one hint !", Toast.LENGTH_LONG).show();
                }
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hint == 0) {
                    try {

                        Socket socket = new Socket("192.168.43.248", 7923);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject("hint" + MainPage.username);

                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String message = (String) ois.readObject();

                        if (message.equals("OK")) {
                            Toast.makeText(Game.this, "DONE :)", Toast.LENGTH_LONG).show();
                            hint = 2;
                        } else {
                            Toast.makeText(Game.this, "You don't have enough coins!", Toast.LENGTH_LONG).show();
                        }
                        socket.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(Game.this, "just one hint !", Toast.LENGTH_LONG).show();
                }
            }
        });

        final RadioButton r1 = findViewById(R.id.option1);
        r1.setText("");
        final RadioButton r2 = findViewById(R.id.option2);
        r2.setText("");
        final RadioButton r3 = findViewById(R.id.option3);
        r3.setText("");
        final RadioButton r4 = findViewById(R.id.option4);
        r4.setText("");

        Button ok = findViewById(R.id.next_question);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int num = -1;
                if (r1.isChecked())
                    num = 0;
                if (r2.isChecked())
                    num = 1;
                if (r3.isChecked())
                    num = 2;
                if (r4.isChecked())
                    num = 3;

                if (r1.isChecked() || r2.isChecked() || r3.isChecked() ||  r4.isChecked()) {
                    try {

                        Socket socket = new Socket("192.168.43.248", 7923);

                        ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                        oos.writeObject("resp" + hint + num + gid + "," + MainPage.username);

                        ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                        String message = (String) ois.readObject();

                        if (message.startsWith("wrong")) {
                            Toast.makeText(getBaseContext(), "WRONG ANSWER", Toast.LENGTH_LONG).show();
                        }
                        if (message.startsWith("correct")) {
                            Toast.makeText(getBaseContext(), "CORRECT ANSWER", Toast.LENGTH_LONG).show();
                        }
                        socket.close();
                        if (hint == 1) {
                            hint = 3;
                        } else if (hint != 1) {
                            hint = 0;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                final RadioButton r1 = findViewById(R.id.option1);
                r1.setChecked(false);
                final RadioButton r2 = findViewById(R.id.option2);
                r2.setChecked(false);
                final RadioButton r3 = findViewById(R.id.option3);
                r3.setChecked(false);
                final RadioButton r4 = findViewById(R.id.option4);
                r4.setChecked(false);
            }
        });
    }

    public void updateGames() {
        try {

            Socket socket = new Socket("192.168.43.248", 7923);

            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("level" + hint + gid + "," + MainPage.username);

            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();

            if (message.equals("end")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        MainPage.pause = 0;
                        Thread.currentThread().interrupt();
                        finish();
                    }
                });
            } else {
                int l = Integer.parseInt(message.substring(0, 1));

                if (l == 0 || l == 4 || l == 8) {
                    try {

                        TextView trick = findViewById(R.id.trick);
                        trick.setBackgroundColor(getResources().getColor(R.color.colorTrick));

                        Button newChance = findViewById(R.id.newchance);
                        Button remove = findViewById(R.id.remove);
                        newChance.setVisibility(View.GONE);
                        remove.setVisibility(View.GONE);

                        String[] game = {" عمومی ", " مذهبی ", " سینماوتلوزیون ", " ریاضی و هوش ", " تکنولوژی ", " تاریخ "};
                        TextView q = findViewById(R.id.show_question);
                        q.setText(" یکی از موضوعات را انتخاب  کنید");
                        q.setTextSize(22);

                        String[] games = message.substring(1).split(",");

                        final RadioButton r1 = findViewById(R.id.option1);
                        r1.setText(game[Integer.parseInt(games[0]) - 1]);
                        final RadioButton r2 = findViewById(R.id.option2);
                        r2.setText(game[Integer.parseInt(games[1]) - 1]);
                        final RadioButton r3 = findViewById(R.id.option3);
                        r3.setText(game[Integer.parseInt(games[2]) - 1]);
                        final RadioButton r4 = findViewById(R.id.option4);
                        r4.setText("");


                        TextView textView = findViewById(R.id.timer);
                        textView.setText("");

                    } catch (Exception e) {
                        e.printStackTrace();

                        TextView trick = findViewById(R.id.trick2);
                        trick.setBackgroundColor(getResources().getColor(R.color.colorTrick));

                        Button newChance = findViewById(R.id.newchance);
                        Button remove = findViewById(R.id.remove);
                        Button ok = findViewById(R.id.next_question);
                        newChance.setVisibility(View.GONE);
                        remove.setVisibility(View.GONE);
                        ok.setVisibility(View.GONE);

                        TextView q = findViewById(R.id.show_question);
                        q.setText("صبر کنید تا حریف موضوع انتخاب کند");
                        q.setTextSize(22);
                        String[] games = message.substring(1).split(",");

                        RadioButton r1 = findViewById(R.id.option1);
                        r1.setText("");
                        RadioButton r2 = findViewById(R.id.option2);
                        r2.setText("");
                        RadioButton r3 = findViewById(R.id.option3);
                        r3.setText("");
                        RadioButton r4 = findViewById(R.id.option4);
                        r4.setText("");


                        TextView textView = findViewById(R.id.timer);
                        textView.setText("");

                    }
                } else {

                    TextView trick1 = findViewById(R.id.trick);
                    trick1.setBackgroundColor(getResources().getColor(R.color.colorNull));

                    TextView trick2 = findViewById(R.id.trick2);
                    trick2.setBackgroundColor(getResources().getColor(R.color.colorNull));

                    Button newChance = findViewById(R.id.newchance);
                    Button remove = findViewById(R.id.remove);
                    Button ok = findViewById(R.id.next_question);
                    newChance.setVisibility(View.VISIBLE);
                    remove.setVisibility(View.VISIBLE);
                    ok.setVisibility(View.VISIBLE);

                    String[] mm = message.split("\n");

                    String[] game = message.substring(1).split("\\|");
                    TextView q = findViewById(R.id.show_question);
                    q.setTextSize(17);
                    q.setText(game[0]);

                    final RadioButton r1 = findViewById(R.id.option1);
                    r1.setText(game[1]);
                    final RadioButton r2 = findViewById(R.id.option2);
                    r2.setText(game[2]);
                    final RadioButton r3 = findViewById(R.id.option3);
                    r3.setText(game[3]);
                    final RadioButton r4 = findViewById(R.id.option4);
                    r4.setText(game[4]);


                    if (mm.length == 2) {
                        TextView textView = findViewById(R.id.timer);
                        textView.setText(mm[1]);
                    } else {
                        TextView textView = findViewById(R.id.timer);
                        textView.setText("");
                    }
                }
                socket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}