package com.java.liuyun;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class ContentActivity extends AppCompatActivity {

    private String title;
    private String content;
    private String type;
    private String source;
    private String publishTime;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        type = getIntent().getStringExtra("type");
        source = getIntent().getStringExtra("source");
        publishTime = getIntent().getStringExtra("publishTime");

        TextView titleTextView = (TextView)findViewById(R.id.contentTitle);
        TextView contentTextView = (TextView)findViewById(R.id.contentContent);
        TextView typeTextView = (TextView)findViewById(R.id.contentType);
        TextView sourceTextView = (TextView)findViewById(R.id.contentSource);
        TextView publishTimeTextView = (TextView)findViewById(R.id.contentPublishTime);
        titleTextView.setText(title);
        contentTextView.setText(content);
        typeTextView.setText(type);
        sourceTextView.setText(source);
        publishTimeTextView.setText(publishTime);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
