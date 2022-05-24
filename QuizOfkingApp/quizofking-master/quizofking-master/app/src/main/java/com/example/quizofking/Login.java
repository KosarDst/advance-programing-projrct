package com.example.quizofking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.btnloginactivity);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    final EditText u = findViewById(R.id.editTextUsername);
                    final EditText p = findViewById(R.id.editTextPassword);

                    if (!u.getText().toString().equals("") && !p.getText().toString().equals("")) {
                        check(u.getText().toString(), p.getText().toString());
                    } else {
                        if (u.getText().toString().isEmpty()) {
                            u.setError("This field is empty!");
                        }
                        if (p.getText().toString().isEmpty()) {
                            p.setError("This field is empty!");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final EditText u = findViewById(R.id.editTextUsername);
        u.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        checkUsername(u.getText() + "");
                        String usern = u.getText().toString();
                        if (usern.isEmpty()) {
                            u.setError("This field is empty!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        final EditText p = findViewById(R.id.editTextPassword);
        p.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    try {
                        checkPass(p.getText() + "");
                        String pass = p.getText().toString();
                        if (pass.isEmpty()) {
                            p.setError("This field is empty!");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });


    }

    public void checkUsername(String username) throws IOException {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Socket socket = new Socket("192.168.43.248", 7923);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject("chk" + username);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject();

            if (!message.equals("EXIST")) {
                final EditText u = findViewById(R.id.editTextUsername);
                u.setError("This username doesn't exist!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }

    public void checkPass(String pass) throws IOException {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Socket socket = new Socket("192.168.43.248", 7923);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject("chk2" + pass);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject();

            if (!message.equals("no")) {
                final EditText p = findViewById(R.id.editTextPassword);
                p.setError("Password confirmation failed!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }

    public void check(String username, String password) throws IOException {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Socket socket = new Socket("192.168.43.248", 7923);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject("login" + username + "," + password);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject();

            if (message.equals("OK")) {
                finish();
                Intent i = new Intent(Login.this, MainPage.class);
                MainPage.username = username;
                startActivity(i);
            } else if (message.equals("NOU")) {
                final EditText u = findViewById(R.id.editTextUsername);
                u.setError("This username doesn't exist!");
            } else {
                final EditText p = findViewById(R.id.editTextPassword);
                p.setError("Password confirmation failed!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }

}