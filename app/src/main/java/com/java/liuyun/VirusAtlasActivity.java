package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VirusAtlasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_atlas);
        SearchView searchView = (SearchView) findViewById(R.id.search_view_atlas);

        searchView.ClickSearch(new SearchCallBack() {
            @Override
            public void SearchAction(String keyWord) {
                //打开新的Activity，显示疫情图谱
                Toast.makeText(getApplicationContext(), "Altas search: "+keyWord, Toast.LENGTH_SHORT).show(); //Debug

                //TODO:keyWord是疫情图谱的查询关键词，寻找是否存在名为keyWord的疫情图谱
                // 如果不存在，输出一个toast提示信息即可
                // 如果存在，执行以下内容，获取数据并传入intent后startActivity即可
                
                Intent intent = new Intent(VirusAtlasActivity.this, AtlasSpecificActivity.class);

                //TODO:查询指定疫情图谱的以下6类信息
                // Name:图谱名称，直接写入intent
                // Description:图谱描述，直接写入intent
                // relationTitle:图谱每种关系的名称
                // relationDescription:图谱每种关系的描述
                // attributeTitle:图谱每种属性的名称
                // attributeDescription:图谱每种属性的描述
                // [重要]要求：relationTitle.size()==relationDescription.size()，attributeTitle.size()==attributeDescription.size()
                // 下面是一个例子

                intent.putExtra("Name", "新型冠状病毒");
                intent.putExtra("Description", "新型冠状病毒的描述");
                List<String> relationTitle = new ArrayList<>();
                relationTitle.add("疑似宿主"); relationTitle.add("疑似传播途径");
                List<String> relationDescription = new ArrayList<>();
                relationDescription.add("中华菊头蝠"); relationDescription.add("气溶胶传播");
                List<String> attributeTitle = new ArrayList<>();
                attributeTitle.add("潜伏期"); attributeTitle.add("鉴别诊断"); attributeTitle.add("基本传染指数");
                List<String> attributeDescription = new ArrayList<>();
                attributeDescription.add("1-14天，多为3-7天"); attributeDescription.add("主要与流感病毒、副流感病毒、腺病毒、呼吸道合胞病毒、鼻病毒、人偏肺病毒、SARS冠状病毒等其他已知病毒性肺炎鉴别，与肺炎支原体、衣原体肺炎及细菌性肺炎等鉴别。此外，还要与非感染性疾病，如血管炎、皮肌炎和机化性肺炎等鉴别。"); attributeDescription.add("2.2");
                intent.putExtra("RelationTitle", (Serializable) relationTitle);
                intent.putExtra("RelationDescripton", (Serializable) relationDescription);
                intent.putExtra("AttributeTitle", (Serializable) attributeTitle);
                intent.putExtra("AttributeDescription", (Serializable) attributeDescription);

                //intent.putExtra()
                startActivity(intent);
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
