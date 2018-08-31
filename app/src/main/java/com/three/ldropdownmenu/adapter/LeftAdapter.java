package com.three.ldropdownmenu.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.three.ldropdownmenu.R;
import com.three.ldropdownmenu.base.CommonAdapter;
import com.three.ldropdownmenu.base.CommonViewHolder;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

public class LeftAdapter extends CommonAdapter<String> {

    private int curPosition = -1;

    public void setCurPosition(int position){
        this.curPosition = position;
    }

    public LeftAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.item_list_left);
    }

    @Override
    protected void convertView(View item, String s, int position) {
        TextView textView = CommonViewHolder.get(item, R.id.tv_content);
        textView.setText(s);
        if (position == curPosition) {
            textView.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.main_bg));
        }
    }
}
