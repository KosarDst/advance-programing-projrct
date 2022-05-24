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

public class Register extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Button register = findViewById(R.id.btnregisteractivity);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText n = findViewById(R.id.editTextName);
                final EditText u = findViewById(R.id.editTextUsername);
                final EditText p = findViewById(R.id.editTextPassword);
                final EditText rp = findViewById(R.id.editTextrePassword);

                try {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (p.getText().toString().equals(rp.getText().toString()) && !u.getText().toString().equals("")
                                        && !n.getText().toString().equals("") && !p.getText().toString().equals("")) {
                                    check(n.getText().toString(), u.getText().toString(), p.getText().toString());
                                } else {
                                    if (u.getText().toString().isEmpty()) {
                                        u.setError("This field is empty!");
                                    }
                                    if (p.getText().toString().isEmpty()) {
                                        p.setError("This field is empty!");
                                    }
                                    if (n.getText().toString().isEmpty()) {
                                        n.setError("This field is empty!");
                                    }
                                    if (rp.getText().toString().isEmpty()) {
                                        rp.setError("This field is empty!");
                                    }
                                   if(!p.getText().equals(rp.getText())){
                                       rp.setError("password confirmation failed!");
                                   }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


        final EditText n = findViewById(R.id.editTextName);
        n.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String name = n.getText().toString();
                    if (name.isEmpty()) {
                        n.setError("This field is empty!");
                    }
                }
            }
        });


        final EditText u = findViewById(R.id.editTextUsername);
        u.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
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
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String pass = p.getText().toString();
                    if (pass.isEmpty()) {
                        p.setError("This field is empty!");
                    } else if (pass.length() < 5) {
                        p.setError("password length should be more than 5 characters!");
                    }
                }
            }
        });


        final EditText rp = findViewById(R.id.editTextrePassword);
        rp.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (!b) {
                    String repass = rp.getText().toString();
                    String pass = p.getText().toString();
                    if (repass.isEmpty()) {
                        rp.setError("This field is empty!");
                    }
                    if (!pass.equals(repass)) {
                        rp.setError("password confirmation failed!");
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

            if (message.equals("EXIST")) {
                EditText UN = findViewById(R.id.editTextUsername);
                UN.setError("This username has already been taken :(");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        socket.close();
    }

    public void check(String name, String username, String password) throws IOException {

        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        Socket socket = new Socket("192.168.43.248", 7923);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectOutputStream.writeObject("reg" + name + "," + username + "," + password);

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            String message = (String) objectInputStream.readObject();

            if (message.equals("OK")) {
                finish();
                Intent i = new Intent(Register.this, MainPage.class);
                MainPage.username = username;
                startActivity(i);
            }else {
                EditText UN = findViewById(R.id.editTextUsername);
                UN.setError("This username has already been taken :(");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

