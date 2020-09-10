package com.java.liuyun;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

        //初始化分享的内容
        ShareMethod.initShareImage(ContentActivity.this, title, content);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.navigation_share) {
            //System.out.println("hahahaha");
            //Uri imgURL = ShareMethod.createShareImage(ContentActivity.this);
            //System.out.println("hahaha");
            intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            //分享图片
            //intent.putExtra(Intent.EXTRA_STREAM, imgURL);
            //intent.setType("image/*");
            //分享文字
            intent.putExtra(Intent.EXTRA_TEXT, "["+title+"]"+content);
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, "选择分享应用"));
        }
        return true;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
