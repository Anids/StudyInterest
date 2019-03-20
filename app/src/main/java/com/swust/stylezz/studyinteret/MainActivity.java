package com.swust.stylezz.studyinteret;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.swust.stylezz.studyinteret.fragment.FileLibraryFragment;
import com.swust.stylezz.studyinteret.fragment.InSetFragment;
import com.swust.stylezz.studyinteret.fragment.IndexFragment;
import com.swust.stylezz.studyinteret.fragment.RecentlyBrowseFragment;
import com.swust.stylezz.studyinteret.ui.ViewPagerAdapter;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  implements BottomNavigationBar.OnTabSelectedListener, ViewPager.OnPageChangeListener {

    //BottomNavigationBar实现底部导航栏切换
    private BottomNavigationBar bottomNavigationBar;
    //ViewPager实现联动切换
    private ViewPager viewPager;
    private List<Fragment> fragments;
    int lastSelectedPosition=0;
    private SharedPreferences sharedPreferencesLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );
        InitStatus();
        BottomNavigationBarInit();
        ViewPagerInit();
    }


    private void InitStatus() {
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferencesLogin.edit ();
        editor.putString ( "update_state","0" );
        editor.commit ();
    }

    private void ViewPagerInit() {
        UMConfigure.setLogEnabled(true);
        UMConfigure.setEncryptEnabled(true);
        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
        viewPager = (ViewPager) findViewById(R.id.root_viewpager);

        fragments = new ArrayList<Fragment> ();
        fragments.add ( IndexFragment.getNewInstance () );
        fragments.add ( FileLibraryFragment.getNewInstance () );
        fragments.add ( RecentlyBrowseFragment.getNewInstance () );
        fragments.add ( InSetFragment.getNewInstance ());

        viewPager.setAdapter(new ViewPagerAdapter (getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(this);
        viewPager.setCurrentItem(lastSelectedPosition);
    }

    //BottomNavigationBar实现底部导航栏切换
    private void BottomNavigationBarInit() {
        //底部导航栏用BottomNavigationBar实现
        bottomNavigationBar = findViewById ( R.id.bottom_navigation_bar );
        bottomNavigationBar.setTabSelectedListener ( this );
        bottomNavigationBar.clearAll ();
        bottomNavigationBar.setMode ( BottomNavigationBar.MODE_FIXED );

        //无波纹效果切换
        bottomNavigationBar.setBackgroundStyle ( BottomNavigationBar.BACKGROUND_STYLE_STATIC );

        //文字颜色，背景颜色等
        bottomNavigationBar.setActiveColor ( R.color.colorLightSlateBlue )
                .setInActiveColor ( R.color.colorSnow3 )
                .setBarBackgroundColor ( R.color.colorGrey2 );

        bottomNavigationBar.addItem ( new BottomNavigationItem ( R.mipmap.btn_home_2,"首页" )
                        .setInactiveIcon ( ContextCompat.getDrawable (MainActivity.this,R.mipmap.btn_home_1 ) ));
        bottomNavigationBar .addItem ( new BottomNavigationItem ( R.mipmap.btn_filelibrary_2 ,"资料")
                        .setInactiveIcon ( ContextCompat.getDrawable ( MainActivity.this,R.mipmap.btn_filelibrary_1 ) ) );
        bottomNavigationBar .addItem ( new BottomNavigationItem ( R.mipmap.btn_recentread_2,"痕迹" )
                        .setInactiveIcon ( ContextCompat.getDrawable ( MainActivity.this,R.mipmap.btn_recentread_1 ) ));
        bottomNavigationBar .addItem ( new BottomNavigationItem ( R.mipmap.btn_head_2,"我的" )
                        .setInactiveIcon ( ContextCompat.getDrawable ( MainActivity.this,R.mipmap.btn_head_1 ) ));
        bottomNavigationBar .setFirstSelectedPosition ( lastSelectedPosition );
        bottomNavigationBar.initialise ();
    }

    @Override
    public void onTabSelected(int position) {
        viewPager.setCurrentItem ( position,false );
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        bottomNavigationBar.selectTab ( position );
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    // Activity页面onResume函数重载
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏

    }

    // Activity页面onResume函数重载
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 不能遗漏
    }
}
