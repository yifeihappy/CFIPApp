package com.example.yifeihappy.cfip;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BarActivity extends AppCompatActivity implements ActionBar.TabListener {

    private static final String SELECTED_ITEM = "selecter_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tab = actionBar.newTab().setText("活动").setTabListener(this);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("话题").setTabListener(this);
        actionBar.addTab(tab);
        tab = actionBar.newTab().setText("灯神").setTabListener(this);
        actionBar.addTab(tab);
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        switch (tab.getPosition()) {
            case 0:
                Fragment frag0 = new ActFragment();
                ft.replace(R.id.container, frag0);
                break;
            case 1:
//                Fragment fragment = new DummyFragment();
//                Bundle args = new Bundle();
//                args.putInt(DummyFragment.ARG_SECTION_NUMBER, tab.getPosition() + 1);
//
//                fragment.setArguments(args);
//                ft.replace(R.id.container, fragment);
                Fragment frag1 = new TopicFragment();
                ft.replace(R.id.container, frag1);
                break;
            case 2:
                Fragment frag2 = new WallFragment();
                ft.replace(R.id.container, frag2);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }
}
