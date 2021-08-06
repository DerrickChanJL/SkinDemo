package com.jchan.skindemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.jchan.skindemo.databinding.ActivityMainBinding;
import com.jchan.skindemo.dialog.SelectSkinDialog;
import com.jchan.skindemo.fragment.DiscoverFragment;
import com.jchan.skindemo.fragment.MessageFragment;
import com.jchan.skindemo.fragment.MineFragment;
import com.jchan.skinlib.manager.SkinManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Administrator
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private List<Fragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        setSupportActionBar(mBinding.toolbar);
        initFragment();
        mBinding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switchFragment(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mBinding.viewPager.setAdapter(adapter);


        mBinding.llDiscover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(0);
            }
        });

        mBinding.llMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(1);
            }
        });

        mBinding.llMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBinding.viewPager.setCurrentItem(2);
            }
        });

        mBinding.tvSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SkinManager.getInstance().loadSkin("/sdcard/skin.apk");
                new SelectSkinDialog().show(getSupportFragmentManager(),"select");
            }
        });
        mBinding.tvReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinManager.getInstance().loadSkin(null);
            }
        });
        XXPermissions.with(this)
                .permission(Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {

                    }
                });
        mBinding.ivDiscover.setSelected(true);
        mBinding.tvDiscover.setSelected(true);
    }

    private void initFragment() {
        mFragmentList.add(new DiscoverFragment());
        mFragmentList.add(new MessageFragment());
        mFragmentList.add(new MineFragment());

    }

    private void switchFragment(int position) {
        switch (position) {
            case 0:
//                mBinding.ivDiscover.setImageDrawable(getDrawable(R.drawable.ic_discover_press));
//                mBinding.tvDiscover.setTextColor(getResources().getColor(R.color.color_theme));
                mBinding.ivDiscover.setSelected(true);
                mBinding.tvDiscover.setSelected(true);
                mBinding.ivMessage.setSelected(false);
                mBinding.tvMessage.setSelected(false);
                mBinding.ivMine.setSelected(false);
                mBinding.tvMine.setSelected(false);
                break;
            case 1:
                mBinding.ivDiscover.setSelected(false);
                mBinding.tvDiscover.setSelected(false);
                mBinding.ivMessage.setSelected(true);
                mBinding.tvMessage.setSelected(true);
                mBinding.ivMine.setSelected(false);
                mBinding.tvMine.setSelected(false);
                break;
            case 2:
                mBinding.ivDiscover.setSelected(false);
                mBinding.tvDiscover.setSelected(false);
                mBinding.ivMessage.setSelected(false);
                mBinding.tvMessage.setSelected(false);
                mBinding.ivMine.setSelected(true);
                mBinding.tvMine.setSelected(true);
                break;
            default:
                break;
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}