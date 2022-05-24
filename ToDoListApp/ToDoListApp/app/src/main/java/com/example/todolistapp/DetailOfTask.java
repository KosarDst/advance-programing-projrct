package com.example.todolistapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DetailOfTask extends AppCompatActivity {
    private Switch switchOfDetails;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailoftask);
        Intent intent = getIntent();
        Task task = (Task) intent.getSerializableExtra("my_Task");
        TextView TextTittle,TextDes,TextExpir;
        switchOfDetails = findViewById(R.id.switchdetail);
        TextTittle = findViewById(R.id.detailtitel);
        TextDes = findViewById(R.id.detaildescription);
        TextExpir = findViewById(R.id.detailexpire);
        TextTittle.setText(DB.touchedtask.getTitle());
        TextDes.setText(DB.touchedtask.getDescription());
        TextExpir.setText(DB.touchedtask.getExpire());
        switchOfDetails.setChecked(DB.touchedtask.getIs_done());
        switchOfDetails.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    DB.touchedtask.setIs_done(true);
                } else {
                    DB.touchedtask.setIs_done(false);
                }
                MainActivity.adapter.notifyDataSetChanged();
            }
        });
    }
}

