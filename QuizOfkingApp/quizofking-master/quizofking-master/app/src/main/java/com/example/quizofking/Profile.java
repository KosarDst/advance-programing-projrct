package com.example.quizofking;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Profile extends Fragment {

    public Profile() {
    }

    public void getList(View v){

        try {
            Socket socket = new Socket("192.168.43.248", 7923);
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject("history" + MainPage.username);
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();

            if (!message.equals("")) {
                String[] messages = message.split("\n");
                ArrayList rowItems = new ArrayList<String>();

                TextView pScore = v.findViewById(R.id.Pscore);
                pScore.setText(messages[0]);

                TextView pRank = v.findViewById(R.id.Prank);
                pRank.setText(messages[1]);

                for (int i = 2; i <messages.length ; i++) {
                    rowItems.add("\n" + messages[i]);
                }

                ListView listView = v.findViewById(R.id.games);
                ListAdapter adapter = new ArrayAdapter(getContext(),R.layout.profileitem, rowItems) {
                };
                listView.setAdapter(adapter);
            }
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.profilefragment, container, false);
        Thread t=new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            try {
                                if (MainPage.pause == 0) {
                                    getList(view);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
        t.start();
        return view;
    }

}
