package com.java.liuyun;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentVirusInformation extends Fragment {

    Button virusDataButton;
    Button virusAtlasButton;
    Button newsClusterButton;
    Button virusScholarButton;

    @Override
    public void onCreate(@Nullable Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_virus_information, container, false);
        virusDataButton = (Button) view.findViewById(R.id.virus_data_button);
        virusAtlasButton = (Button) view.findViewById(R.id.virus_atlas_button);
        newsClusterButton = (Button) view.findViewById(R.id.news_cluster_button);
        virusScholarButton = (Button) view.findViewById(R.id.virus_scholar_button);

        //疫情数据（柱状图）跳转
        virusDataButton.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), VirusDataActivity.class);
                startActivity(intent);
            }
        });

        //疫情图谱跳转
        virusAtlasButton.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), VirusAtlasActivity.class);
                startActivity(intent);
            }
        });

        //新闻聚类跳转
        newsClusterButton.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), NewsClusterActivity.class);
                startActivity(intent);
            }
        });

        //知疫学者跳转
        virusScholarButton.setOnClickListener(new View.OnClickListener() {
            Intent intent;
            @Override
            public void onClick(View view) {
                intent = new Intent(getActivity(), VirusScholarActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
