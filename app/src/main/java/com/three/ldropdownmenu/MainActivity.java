package com.three.ldropdownmenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.three.LDropDownMenu;
import com.three.ldropdownmenu.adapter.GridAdapter;
import com.three.ldropdownmenu.adapter.LeftAdapter;
import com.three.ldropdownmenu.adapter.ListAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private LDropDownMenu mDropDownMenu;
    private String headers[] = {"楼盘", "筛选", "层数"};
    private List<View> popupViews = new ArrayList<>();
    private List<String> data1,data3,leftData,rightData;
    private ListAdapter listAdapter,rightAdapter;
    private LeftAdapter leftAdapter;
    private GridAdapter gridAdapter;
    private ListView lv1,lvLeft,lvRight;
    private GridView gridView;
    private View twoListView;
    private int curSelectedPos = -1;//当前选择位置
    private int singleChoosePosition = -1,gridviewCurPos = -1,leftSelPos = 0,rightSelPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        initValue();
    }

    private void initView() {
        mDropDownMenu = findViewById(R.id.drop_down_menu);
        lv1 = new ListView(this);
        lv1.setDivider(null);
        gridView = new GridView(this);
        gridView.setNumColumns(4);
        gridView.setBackgroundColor(getResources().getColor(R.color.white));
        twoListView = LayoutInflater.from(this).inflate(R.layout.two_listview,null);
        lvLeft = twoListView.findViewById(R.id.lv_left);
        lvRight = twoListView.findViewById(R.id.lv_right);
    }

    private void initListener() {
        lv1.setOnItemClickListener(this);
        lvLeft.setOnItemClickListener(this);
        lvRight.setOnItemClickListener(this);
        gridView.setOnItemClickListener(this);
    }

    private void initValue() {
        data1 = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            data1.add("颐达豪庭"+i);
        }
        listAdapter = new ListAdapter(this,data1);
        listAdapter.setCurPosition(singleChoosePosition);
        lv1.setAdapter(listAdapter);

        leftData = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            leftData.add(""+(i+1)+"期");
        }
        leftAdapter = new LeftAdapter(this,leftData);
        leftAdapter.setCurPosition(leftSelPos);
        lvLeft.setAdapter(leftAdapter);

        rightData = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            rightData.add(""+(i+1)+"栋");
        }
        rightAdapter = new ListAdapter(this,rightData);
        rightAdapter.setCurPosition(rightSelPos);
        lvRight.setAdapter(rightAdapter);

        data3 = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data3.add(""+(i+1) + "层");
        }
        gridAdapter = new GridAdapter(this,data3);
        gridAdapter.setCurPosition(gridviewCurPos);
        gridView.setAdapter(gridAdapter);

        popupViews.add(lv1);
        popupViews.add(twoListView);
        popupViews.add(gridView);
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews);
        mDropDownMenu.setOnSelectedPositionListener(new LDropDownMenu.OnSelectedPositionListener() {
            @Override
            public void currentPosition(int position) {
                curSelectedPos = position;
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (curSelectedPos){
            case 0:
                singleChoosePosition = position;
                listAdapter.setCurPosition(singleChoosePosition);
                listAdapter.notifyDataSetChanged();
                mDropDownMenu.setTabText(data1.get(position));
                mDropDownMenu.closeMenu();
                break;
            case 1:
                if (R.id.lv_left == parent.getId()) {
                    leftSelPos = position;
                    leftAdapter.setCurPosition(leftSelPos);
                    leftAdapter.notifyDataSetChanged();
                    rightData.clear();
                    Random rand = new Random();
                    int j = rand.nextInt(5);
                    for (int i = 0; i < (j+8); i++) {
                        rightData.add(""+(i+1)+"栋");
                    }
                    rightSelPos = -1;
                    rightAdapter.setCurPosition(rightSelPos);
                    rightAdapter.notifyDataSetChanged();
                } else {
                    rightSelPos = position;
                    rightAdapter.setCurPosition(rightSelPos);
                    rightAdapter.notifyDataSetChanged();
                    mDropDownMenu.setTabText(leftData.get(leftSelPos) + rightData.get(rightSelPos));
                    mDropDownMenu.closeMenu();
                }
                break;
            case 2:
                gridviewCurPos = position;
                gridAdapter.setCurPosition(gridviewCurPos);
                gridAdapter.notifyDataSetChanged();
                mDropDownMenu.setTabText(data3.get(position));
                mDropDownMenu.closeMenu();
                break;
        }

    }

    /**
     * 按返回键时
     */
    @Override
    public void onBackPressed() {
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
            return;
        }
        super.onBackPressed();
    }
}
