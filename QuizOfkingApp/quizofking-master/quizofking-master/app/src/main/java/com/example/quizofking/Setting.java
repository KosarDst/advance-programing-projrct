package com.example.quizofking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Setting extends Fragment {

    Button play, pause;

    public Setting() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.settingfragment, container, false);

        play =  rootView.findViewById(R.id.play);
        pause = rootView.findViewById(R.id.pause);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!MainActivity.sound.isPlaying()) {
                    MainActivity.sound.setLooping(true);
                    MainActivity.sound.start();
                }
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MainActivity.sound.isPlaying()) {
                    MainActivity.sound.pause();
                }
            }
        });


        final EditText u = rootView.findViewById(R.id.Username);
        Button changeU = rootView.findViewById(R.id.changUsername);
        changeU.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView tv = rootView.findViewById(R.id.Username);
                try {
                    Socket socket = new Socket("192.168.43.248", 7923);

                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectOutputStream.writeObject("newname" + MainPage.username + "," + tv.getText());

                    ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                    String message = (String) objectInputStream.readObject();

                    if (message.equals("OK")) {
                        Toast.makeText(getActivity().getBaseContext(), "DONE :)", Toast.LENGTH_LONG).show();
                        MainPage.username = tv.getText().toString();
                    } else if (message.startsWith("NO") && message.endsWith(MainPage.username)) {
                        u.setError("This is your current username! Choose another one");
                    } else {
                        u.setError("This username has already been taken :(");
                    }
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final EditText p = rootView.findViewById(R.id.Password);
        final Button changeP = rootView.findViewById(R.id.changPass);
        changeP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = rootView.findViewById(R.id.Password);
                try {

                    boolean check = true;
                    String pass = p.getText().toString();
                    if (pass.length() < 5) {
                        p.setError("password length should be more than 5 characters!");
                        check = false;
                    }
                    if (check) {
                        Socket socket = new Socket("192.168.43.248", 7923);

                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        objectOutputStream.writeObject("newpass" + MainPage.username + "," + tv.getText());

                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        String message = (String) objectInputStream.readObject();

                        if (message.equals("OK")) {
                            Toast.makeText(getActivity().getBaseContext(), "DONE :)", Toast.LENGTH_LONG).show();
                        } else {
                            p.setError("This is your current password! Choose another one");
                        }
                        socket.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return rootView;
    }

}
