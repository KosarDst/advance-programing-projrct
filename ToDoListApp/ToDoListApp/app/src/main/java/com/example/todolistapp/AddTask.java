package com.example.todolistapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class AddTask extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtask);
        EditText title = findViewById(R.id.Task_title);
        final EditText description = findViewById(R.id.task_description);
        EditText expire = findViewById(R.id.task_expire);
        Button addbtn = findViewById(R.id.addbutton);
        addbtn.setOnClickListener(v -> {
            Task task = new Task(title.getText().toString(), description.getText().toString(), expire.getText().toString());
            DB.tasklist.add(task);
            startActivity(new Intent(this, MainActivity.class));
        });
    }
}
