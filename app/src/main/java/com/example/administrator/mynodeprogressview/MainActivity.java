package com.example.administrator.mynodeprogressview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.administrator.mynodeprogressview.widget.NodeVerticalProgressView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_vertical).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(NodeVerticalProgressActivity.class);
            }
        });
        findViewById(R.id.btn_horizontal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(NodeHorizontalProgressActivity.class);
            }
        });
    }

    private void start(Class clazz) {
        startActivity(new Intent(this, clazz));
    }
}
