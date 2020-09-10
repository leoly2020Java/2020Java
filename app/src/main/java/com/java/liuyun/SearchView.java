package com.java.liuyun;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Type;

//搜索按键回调接口
interface SearchCallBack {
    void SearchAction(String string);
}

//返回按键回调接口
interface BackCallBack {
    void BackAction();
}

public class SearchView extends LinearLayout {

    private Context context;
    private EditText searchText; //搜索文本框
    private TextView clearTextView; //清除搜索记录
    private LinearLayout searchBlock; //搜索框布局
    private ImageView searchBack; //返回按键

    private SearchListView listView; //ListView列表
    private BaseAdapter baseAdapter; //适配器

    //历史搜索记录数据库
    private SearchSQLite searchSQLite;
    private SQLiteDatabase database;

    //回调接口
    private SearchCallBack searchCallBack;
    private BackCallBack backCallBack;

    //自定义属性设置
    private Float textSize; //搜索字体大小
    private int textColor; //搜索字体颜色
    private int blockHeight; //搜索框高度
    private int blockColor; //搜索框颜色
    private String blockHint; //搜索框默认提示

    //初始化
    //初始化自定义属性
    public void initAttributes(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.search_view);
        textSize = typedArray.getDimension(R.styleable.search_view_TextSize, 20);
        textColor = typedArray.getColor(R.styleable.search_view_TextColor, context.getColor(R.color.Grey));
        blockHeight = typedArray.getInteger(R.styleable.search_view_BlockHeight, 150);
        blockColor = typedArray.getColor(R.styleable.search_view_BlockColor, context.getColor(R.color.White));
        blockHint = typedArray.getString(R.styleable.search_view_BlockHint);
        typedArray.recycle();
    }
    //初始化视图
    public void initView() {
        //初始化搜索框的xml文件
        LayoutInflater.from(context).inflate(R.layout.search_layout, this);
        //searchText
        searchText = (EditText) findViewById(R.id.search_text);
        searchText.setTextSize(textSize);
        searchText.setTextColor(textColor);
        searchText.setHint(blockHint);
        //searchBlock
        searchBlock = (LinearLayout) findViewById(R.id.search_block);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) searchBlock.getLayoutParams();
        layoutParams.height = blockHeight;
        searchBlock.setBackgroundColor(blockColor);
        searchBlock.setLayoutParams(layoutParams);
        //listView
        listView = (SearchListView) findViewById(R.id.search_list_view);
        //clearTextView
        clearTextView = (TextView) findViewById(R.id.clear_text_view);
        clearTextView.setVisibility(INVISIBLE);
        //searchBack
        searchBack = (ImageView) findViewById(R.id.search_back);
    }
    //初始化搜索框
    public void init() {

        initView();

        searchSQLite = new SearchSQLite(context);
        query(""); //首先查询搜索历史记录

        searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView view, int i, KeyEvent keyEvent) {
                //回车键查询
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    String keyWord = searchText.getText().toString();
                    //查询新闻
                    if (!(searchCallBack == null)) searchCallBack.SearchAction(keyWord);
                    //更新搜索记录数据库
                    boolean exist = hasData(keyWord);
                    if (!exist) {
                        insert(keyWord);
                        query("");
                    }
                    //唤醒MainActivity
                    Intent intent = new Intent();
                    intent.putExtra("SearchKeyWord", searchText.getText());
                    ((Activity)context).setResult(Activity.RESULT_OK, intent);
                    ((Activity)context).finish();
                }
                return false;
            }
        });
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                query(searchText.getText().toString());
            }
        });

        clearTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clear();
                query("");
            }
        });

        searchBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(backCallBack == null)) {
                    backCallBack.BackAction();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String keyWord = textView.getText().toString();
                searchText.setText(keyWord);
            }
        });

    }
    public SearchView(Context context) {
        super(context);
        this.context = context;
        init();
    }
    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttributes(attrs);
        init();
    }
    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initAttributes(attrs);
        init();
    }

    //接口回调
    public void ClickSearch(SearchCallBack scb) {
        searchCallBack = scb;
    }
    public void ClickBack(BackCallBack bcb) {
        backCallBack = bcb;
    }

    //数据库操作：清空数据库
    public void clear() {
        database = searchSQLite.getWritableDatabase();
        database.execSQL("delete from search_records");
        database.close();
        clearTextView.setVisibility(INVISIBLE);
    }
    //数据库操作：查询词条
    public void query(String data) {
        Cursor cursor = searchSQLite.getReadableDatabase().rawQuery("select id as _id,name from search_records where name like '%" + data + "%' order by id desc ", null);
        baseAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, cursor, new String[] { "name" }, new int[] { android.R.id.text1 }, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        listView.setAdapter(baseAdapter);
        baseAdapter.notifyDataSetChanged();
        if (data.equals("") && cursor.getCount() > 0) clearTextView.setVisibility(VISIBLE);
        else clearTextView.setVisibility(INVISIBLE);
    }
    //数据库操作：查询数据库中是否存在词条
    public boolean hasData(String data) {
        Cursor cursor = searchSQLite.getReadableDatabase().rawQuery("select id as _id,name from search_records where name =?", new String[]{data});
        return cursor.moveToNext();
    }
    //数据库操作：插入数据
    public void insert(String data) {
        database = searchSQLite.getWritableDatabase();
        database.execSQL("insert into search_records(name) values('" + data + "')");
        database.close();
    }

}