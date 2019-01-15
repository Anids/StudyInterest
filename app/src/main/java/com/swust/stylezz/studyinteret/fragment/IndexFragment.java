package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;

public class IndexFragment extends Fragment {
    //实例化对象,单例模式
    private static volatile IndexFragment indexFragmentInstance = null;
    @SuppressLint("ValidFragment")
    private IndexFragment(){}
    public static IndexFragment getNewInstance(){//单例模式，防止多项实例化
        if (indexFragmentInstance ==null)
        {
            synchronized (IndexFragment.class){
                if (indexFragmentInstance ==null){
                    indexFragmentInstance =new IndexFragment ();
                }
            }
        }
        return indexFragmentInstance;
    }

    private ListView listView;
    private SimpleAdapter simpleAdapter;//列表
    private View rootView;
    private SharedPreferences sharedPreferences;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate ( R.layout.fragment_index, container, false );
        Toast.makeText ( getActivity ().getApplicationContext (),"首页",Toast.LENGTH_LONG ).show ();
        return rootView;
    }
}
