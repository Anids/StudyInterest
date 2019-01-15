package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;

public class RecentlyBrowseFragment extends Fragment {
    private static volatile RecentlyBrowseFragment recentlyBrowseFragmentInstance = null;
    @SuppressLint("ValidFragment")
    private RecentlyBrowseFragment(){}
    public static RecentlyBrowseFragment getNewInstance(){//单例模式，防止多项实例化
        if (recentlyBrowseFragmentInstance ==null)
        {
            synchronized (RecentlyBrowseFragment.class){
                if (recentlyBrowseFragmentInstance ==null){
                    recentlyBrowseFragmentInstance =new RecentlyBrowseFragment ();
                }
            }
        }
        return recentlyBrowseFragmentInstance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate( R.layout.fragment_recentlybrowse, container, false);
        Toast.makeText ( getActivity ().getApplicationContext (),"痕迹",Toast.LENGTH_LONG ).show ();
        return rootView;
    }
}
