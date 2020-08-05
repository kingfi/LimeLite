package com.example.limelite.adapters;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.limelite.fragments.tabs.FriendListFragment;
import com.example.limelite.fragments.tabs.FriendRequestsFragment;

public class FriendTabsAdapter extends FragmentStateAdapter {
    Fragment fragment;

    public FriendTabsAdapter(Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
//        // Return a NEW fragment instance in createFragment(int)
//        Fragment fragment = new DemoObjectFragment();
//        Bundle args = new Bundle();
//        // Our object is just an integer :-P
//        args.putInt(DemoObjectFragment.ARG_OBJECT, position + 1);
//        fragment.setArguments(args);

        if (position == 1) {
            fragment = new FriendRequestsFragment();
        }else {
            fragment = new FriendListFragment();
        }

        return fragment;
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
