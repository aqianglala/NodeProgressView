package com.example.administrator.mynodeprogressview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.mynodeprogressview.widget.NodeVerticalProgressView;

import java.util.ArrayList;
import java.util.List;

public class NodeVerticalProgressActivity extends AppCompatActivity {
    private List<NodeData> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_vertical_progress);

        initData();
        NodeVerticalProgressView nodeProgressView = findViewById(R.id.nodeProgressView);
        nodeProgressView.setData(mData);
    }

    private void initData() {
        for (int i = 0; i < 10; i++) {
            NodeData nodeData = new NodeData();
            nodeData.setIndex(i + "");
            nodeData.setContent("第" + i + "条");
            mData.add(nodeData);
        }
    }
}
