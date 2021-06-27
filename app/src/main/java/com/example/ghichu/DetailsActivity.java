package com.example.ghichu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {

    private Button btnBack;
    private TextView taskName, taskContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Objects.requireNonNull(getSupportActionBar()).hide();

        btnBack = findViewById(R.id.btnBack);
        taskName = findViewById(R.id.taskNameDetails);
        taskContent = findViewById(R.id.taskContentDetails);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();

        String task = intent.getStringExtra("task");
        String content = intent.getStringExtra("content");

        taskName.setText(task);
        taskContent.setText(content);

    }

}