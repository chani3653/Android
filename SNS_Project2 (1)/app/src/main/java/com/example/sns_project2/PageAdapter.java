package com.example.sns_project2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sns_project2.fragment.Alarm;
import com.example.sns_project2.fragment.Hot;
import com.example.sns_project2.fragment.Main;
import com.example.sns_project2.fragment.Profile;
import com.example.sns_project2.fragment.Userlist;
//프레그먼트 관련
public class PageAdapter extends FragmentStatePagerAdapter {
    int mNumofTabs;
    public PageAdapter(FragmentManager fm,int mNumofTabs) {
        super(fm);
        this.mNumofTabs = mNumofTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                Main main = new Main();
                return main;
            case 1:
                Hot hot = new Hot();
                return hot;
            case 2:
                Profile profile = new Profile();
                return profile;
            case 3:
                Alarm alarm = new Alarm();
                return alarm;
            case 4:
                Userlist userlist = new Userlist();
                return userlist;
                default:
                    return null;
        }

    }

    @Override
    public int getCount() {
        return mNumofTabs;
    }


}
