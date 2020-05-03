package com.example.interntask.adapter;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.interntask.NowPlayingFragment;
import com.example.interntask.R;
import com.example.interntask.UpcomingFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {
    @StringRes
    private static final int[] TAB_TITLES = new int[] { R.string.now_playinng, R.string.upcoming};
    private final Context mContext;
    public TabsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:

                return new NowPlayingFragment();

            case 1:
                return new UpcomingFragment();
            default:
                return null;
        }
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 2;
    }
}