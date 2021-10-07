package com.example.knu_matching.main.recruitFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.knu_matching.R;
import com.google.android.material.tabs.TabLayout;

public class RecruitmentFragment extends Fragment {
    TabLayout tabRoot;
    Child1_Fragment child1_fragment;
    Child2_Fragment child2_fragment;
    Child3_Fragment child3_fragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //return super.onCreateView(inflater, container, savedInstanceState);
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_fifth_fragment, container, false);

        child1_fragment = new Child1_Fragment();
        child2_fragment = new Child2_Fragment();
        child3_fragment = new Child3_Fragment();

        tabRoot = rootView.findViewById(R.id.tab_layout);
        tabRoot.removeAllTabs();
        tabRoot.addTab(tabRoot.newTab().setText("팀원 구하기\n현황"));
        tabRoot.addTab(tabRoot.newTab().setText("게시글\n스크랩"));
        tabRoot.addTab(tabRoot.newTab().setText("대외활동\n스크랩"));

        getChildFragmentManager().beginTransaction().replace(R.id.tab1_container, child1_fragment).commit();

        tabRoot.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        getChildFragmentManager().beginTransaction().replace(R.id.tab1_container, child1_fragment).commit();
                        break;
                    case 1:
                        getChildFragmentManager().beginTransaction().replace(R.id.tab1_container, child2_fragment).commit();
                        break;
                    case 2:
                        getChildFragmentManager().beginTransaction().replace(R.id.tab1_container, child3_fragment).commit();
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        return rootView;
    }
}