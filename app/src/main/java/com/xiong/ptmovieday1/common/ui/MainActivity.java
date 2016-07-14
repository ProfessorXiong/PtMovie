package com.xiong.ptmovieday1.common.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.umeng.analytics.MobclickAgent;
import com.xiong.ptmovieday1.R;
import com.xiong.ptmovieday1.common.core.BaseFragment;
import com.xiong.ptmovieday1.common.widget.MainFragment;
import com.xiong.ptmovieday1.common.widget.MenuFragment;

import cn.jpush.android.api.JPushInterface;

public class MainActivity extends SlidingFragmentActivity{

    private BaseFragment mContent;

    @Override
    public void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        // set the Above View
        if (savedInstanceState != null)
            mContent = (BaseFragment) getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
        if (mContent == null)
            mContent = new MainFragment();

        new AlertDialog.Builder(this).create();
        

        // set the Above View
        setContentView(R.layout.frame_content);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, mContent)
                .commit();

        // set the Behind View
        setBehindContentView(R.layout.frame_menu);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.menu_content, new MenuFragment())
                .commit();

        //侧滑时触摸的模式
        getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        //侧滑的偏移量
        getSlidingMenu().setBehindOffsetRes(R.dimen.slidingmenu_offset);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        getSupportFragmentManager().putFragment(outState,"mContent",mContent);
    }

}
