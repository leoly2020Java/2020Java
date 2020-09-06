package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);

        searchView.ClickSearch(new SearchCallBack() {
            @Override
            public void SearchAction(String keyWord) {
                Intent intent = new Intent();
                intent.putExtra("KeyWord", keyWord);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchView.ClickBack(new BackCallBack() {
            @Override
            public void BackAction() {
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
