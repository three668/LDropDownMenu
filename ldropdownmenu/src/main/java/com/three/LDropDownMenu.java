package com.three;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2018/8/31.
 */

public class LDropDownMenu extends RelativeLayout {

    //底层参照物控件，作为其他控件布局时候的相对参照
    private LinearLayout tabMenuViewButtom;
    //顶部菜单布局
    private LinearLayout tabMenuView;
    //弹出菜单父布局
    private FrameLayout popupMenuViews;
    //遮罩半透明View，点击可关闭DropDownMenu
    private View maskView;
    //tabMenuView里面选中的tab位置，-1表示未选中
    private int current_tab_position = -1;

    //分割线宽度、高度、颜色等默认值
    private float dividerWith = 1.5f;
    private float dividerHeight = 24.0f;
    private int dividerColor = 0xffcccccc;
    //tab选中颜色
    private int tabTextSelectedColor = 0xff890c85;
    //tab未选中颜色
    private int tabTextUnselectedColor = 0xff111111;
    //遮罩颜色
    private int maskColor = 0x66000000;
    //tab字体大小
    private int tabTextSize = 14;

    //tab选中图标
    private int tabSelectedIcon;
    //tab未选中图标
    private int tabUnselectedIcon;
    //菜单背景色
    private int menuBackgroundColor = 0xffffffff;
    //菜单下面的分割线颜色、高度等默认值
    private int bottonlineColor = 0xffcccccc;
    private float bottonlineHeight = 1.0f;
    //菜单的箭头是否靠右边
    private boolean tabIconToRight = false;
    private OnSelectedPositionListener onSelectedPositionListener;

    public void setOnSelectedPositionListener(OnSelectedPositionListener listener){
        this.onSelectedPositionListener = listener;
    }

    public LDropDownMenu(Context context) {
        super(context, null);
    }

    public LDropDownMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LDropDownMenu(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //为DropDownMenu添加自定义属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LDropDownMenu);
        bottonlineColor = a.getColor(R.styleable.LDropDownMenu_bottonlineColor, bottonlineColor);
        bottonlineHeight = a.getFloat(R.styleable.LDropDownMenu_bottonlineHeight,bottonlineHeight);
        dividerColor = a.getColor(R.styleable.LDropDownMenu_dividerColor, dividerColor);
        dividerWith = a.getFloat(R.styleable.LDropDownMenu_dividerWith,dividerWith);
        dividerHeight = a.getFloat(R.styleable.LDropDownMenu_dividerHeight,dividerHeight);
        tabTextSelectedColor = a.getColor(R.styleable.LDropDownMenu_tabTextSelectedColor, tabTextSelectedColor);
        tabTextUnselectedColor = a.getColor(R.styleable.LDropDownMenu_tabTextUnselectedColor, tabTextUnselectedColor);
        menuBackgroundColor = a.getColor(R.styleable.LDropDownMenu_menuBackgroundColor, menuBackgroundColor);
        maskColor = a.getColor(R.styleable.LDropDownMenu_maskColor, maskColor);
        tabTextSize = a.getDimensionPixelSize(R.styleable.LDropDownMenu_tabTextSize, tabTextSize);
        tabSelectedIcon = a.getResourceId(R.styleable.LDropDownMenu_tabSelectedIcon, tabSelectedIcon);
        tabUnselectedIcon = a.getResourceId(R.styleable.LDropDownMenu_tabUnselectedIcon, tabUnselectedIcon);
        tabIconToRight = a.getBoolean(R.styleable.LDropDownMenu_tabIconToRight,tabIconToRight);
        a.recycle();

        //初始化tabMenuView并添加到tabMenuView的参照物
        tabMenuViewButtom = new LinearLayout(context);
        tabMenuViewButtom.setId(R.id.dropDownMenu_tabMenuView);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(tabMenuViewButtom,0);

        //为tabMenuView添加下划线
        View underLine = new View(getContext());
        underLine.setId(R.id.dropDownMenu_bottonLine);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dpTpPx(bottonlineHeight));
        lp.addRule(RelativeLayout.BELOW, R.id.dropDownMenu_tabMenuView);
        underLine.setLayoutParams(lp);
        underLine.setBackgroundColor(bottonlineColor);
        addView(underLine, 1);
    }

    /**
     * 初始化DropDownMenu
     *
     * @param tabTexts
     * @param popupViews
     */
    public void setDropDownMenu(@NonNull List<String> tabTexts, @NonNull List<View> popupViews) {
        if (tabTexts.size() != popupViews.size()) {
            throw new IllegalArgumentException("params not match, tabTexts.size() should be equal popupViews.size()");
        }

        LayoutParams plp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        plp.addRule(RelativeLayout.BELOW, R.id.dropDownMenu_bottonLine);

        maskView = new View(getContext());
        maskView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
        maskView.setBackgroundColor(maskColor);
        maskView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeMenu();
            }
        });
        maskView.setVisibility(GONE);
        maskView.setLayoutParams(plp);
        addView(maskView);

        popupMenuViews = new FrameLayout(getContext());
        popupMenuViews.setVisibility(GONE);
        popupMenuViews.setLayoutParams(plp);
        addView(popupMenuViews);

        for (int i = 0; i < popupViews.size(); i++) {
            popupViews.get(i).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            popupMenuViews.addView(popupViews.get(i), i);
        }

        //初始化tabMenuView并添加到tabMenuView，为了能tabMenuView使位于顶层，所以最后添加tabMenuView
        tabMenuView = new LinearLayout(getContext());
        tabMenuView.setId(R.id.dropDownMenu_tabMenuView);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tabMenuView.setOrientation(LinearLayout.HORIZONTAL);
        tabMenuView.setBackgroundColor(menuBackgroundColor);
        tabMenuView.setLayoutParams(params);
        addView(tabMenuView);

        for (int i = 0; i < tabTexts.size(); i++) {
            addTab(tabTexts, i);
        }

        //为了使tabMenuViewButtom和tabMenuView的高度一样
        LayoutParams tparams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,tabMenuView.getHeight());
        tabMenuViewButtom.setLayoutParams(tparams);
    }

    private void addTab(@NonNull List<String> tabTexts,final int i) {
        final TextView tab = new TextView(getContext());
        LinearLayout menuItenmView = new LinearLayout(getContext());
        menuItenmView.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
        menuItenmView.setGravity(Gravity.CENTER);
        menuItenmView.setOrientation(LinearLayout.HORIZONTAL);
        tab.setSingleLine();//单行文本
        tab.setEllipsize(TextUtils.TruncateAt.END);//末尾省略号
        tab.setGravity(Gravity.CENTER);
        tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
        if(tabIconToRight)
            tab.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        else
            tab.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        tab.setTextColor(tabTextUnselectedColor);
        tab.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(tabUnselectedIcon), null);
        tab.setText(tabTexts.get(i) + "  ");
        tab.setPadding(dpTpPx(10), dpTpPx(12), dpTpPx(10), dpTpPx(12));
        menuItenmView.addView(tab, 0);
        //添加点击事件
        menuItenmView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectedPositionListener != null) {
                    onSelectedPositionListener.currentPosition(i);
                }
                switchMenu(tab);
            }
        });
        tabMenuView.addView(menuItenmView);
        //添加分割线
        if (i < tabTexts.size() - 1) {
            View view = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dpTpPx(dividerWith), dpTpPx(dividerHeight));
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            view.setLayoutParams(layoutParams);
            view.setBackgroundColor(dividerColor);
            tabMenuView.addView(view);
        }
    }

    /**
     * 改变tab文字
     *
     * @param text
     */
    public void setTabText(String text) {
        if (current_tab_position != -1) {
            ((TextView) ((ViewGroup)tabMenuView.getChildAt(current_tab_position)).getChildAt(0)).setText(text+"  ");
        }
    }

    public void setTabClickable(boolean clickable) {
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            tabMenuView.getChildAt(i).setClickable(clickable);
        }
    }

    /**
     * 关闭菜单
     */
    public void closeMenu() {
        if (current_tab_position != -1) {
            TextView tab = (TextView) ((ViewGroup)tabMenuView.getChildAt(current_tab_position)).getChildAt(0);
            tab.setTextColor(tabTextUnselectedColor);
            tab.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    getResources().getDrawable(tabUnselectedIcon), null);
            popupMenuViews.setVisibility(View.GONE);
            popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_menu_out));
            maskView.setVisibility(GONE);
            maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_menu_mask_out));
            current_tab_position = -1;
        }

    }

    /**
     * DropDownMenu是否处于可见状态
     *
     * @return
     */
    public boolean isShowing() {
        return current_tab_position != -1;
    }

    /**
     * 切换菜单
     *
     * @param target
     */
    private void switchMenu(View target) {
        //System.out.println(current_tab_position);
        for (int i = 0; i < tabMenuView.getChildCount(); i = i + 2) {
            TextView tab = (TextView) ((ViewGroup)tabMenuView.getChildAt(i)).getChildAt(0);
            if (target == tab) {
                if (current_tab_position == i) {
                    closeMenu();
                } else {
                    if (current_tab_position == -1) {
                        popupMenuViews.setVisibility(View.VISIBLE);
                        popupMenuViews.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_menu_in));
                        maskView.setVisibility(VISIBLE);
                        maskView.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.drop_menu_mask_in));
                        View view = popupMenuViews.getChildAt(i / 2);
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    } else {
                        popupMenuViews.getChildAt(i / 2).setVisibility(View.VISIBLE);
                    }
                    current_tab_position = i;
                    tab.setTextColor(tabTextSelectedColor);
                    tab.setCompoundDrawablesWithIntrinsicBounds(null, null,
                            getResources().getDrawable(tabSelectedIcon), null);
                }
            } else {
                tab.setTextColor(tabTextUnselectedColor);
                tab.setCompoundDrawablesWithIntrinsicBounds(null, null,
                        getResources().getDrawable(tabUnselectedIcon), null);
                popupMenuViews.getChildAt(i / 2).setVisibility(View.GONE);
            }
        }
    }

    public int dpTpPx(float value) {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, dm) + 0.5);
    }

    public interface OnSelectedPositionListener{
        void currentPosition(int position);
    }
}
