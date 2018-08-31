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

public class GridAdapter extends CommonAdapter<String> {

    private int curPosition = -1;

    public void setCurPosition(int position){
        this.curPosition = position;
    }

    public GridAdapter(Context context, List<String> datas) {
        super(context, datas, R.layout.item_grid_text);
    }

    @Override
    protected void convertView(View item, String bean, int position) {
        TextView textView = CommonViewHolder.get(item, R.id.tv_content);
        textView.setText(bean);
        if (position == curPosition) {
            textView.setTextColor(context.getResources().getColor(R.color.white));
            textView.setBackgroundResource(R.drawable.shape_item_select_bg);
        } else {
            textView.setTextColor(context.getResources().getColor(R.color.unselected_text_color));
            textView.setBackgroundResource(R.drawable.shape_item_normal_bg);
        }
    }
}
