package com.three.ldropdownmenu.base;

import android.util.SparseArray;
import android.view.View;

/**
 * 万能的ViewHolder
 * Created by Administrator on 2018/4/17.
 */

public class CommonViewHolder {

    // 添加私有构造函数防止外部实例化
    private  CommonViewHolder() {}
    /**
     * 用来缓存控件，优化加载
     * @param view 所有缓存View的根View
     * @param id   缓存View的唯一标识
     * @return 缓存后的控件（textView、imageView...等控件）
     */
    public static <T extends View> T get(View view, int id) {
        // 获取itemView的ViewHolder对象，并将其转型为SparseArray<View>
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        //如果根view没有用来缓存View的集合
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);//创建集合和根View关联
        }
        View chidlView = viewHolder.get(id);//获取根View储存在集合中的孩纸
        if (chidlView == null) {//如果没有改孩纸
            // 如果还没有缓存该控件，那么就根据itemView找到该控件
            chidlView = view.findViewById(id);
            // 缓存该控件
            viewHolder.put(id, chidlView);
        }
        // 返回缓存好的控件
        return (T) chidlView;
    }
}
