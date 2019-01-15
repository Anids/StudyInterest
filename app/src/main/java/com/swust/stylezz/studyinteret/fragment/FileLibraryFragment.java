package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;

public class FileLibraryFragment extends Fragment {
    //单例模式，实例化对象
    private static volatile FileLibraryFragment fileLibraryFragmentInstance = null;
    @SuppressLint("ValidFragment")
    private FileLibraryFragment(){}
    public static FileLibraryFragment getNewInstance(){//单例模式，防止多项实例化
        if (fileLibraryFragmentInstance ==null)
        {
            synchronized (FileLibraryFragment.class){
                if (fileLibraryFragmentInstance ==null){
                    fileLibraryFragmentInstance =new FileLibraryFragment ();
                }
            }
        }
        return fileLibraryFragmentInstance;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate ( R.layout.fragment_filelibrary, container, false );
        Toast.makeText ( getActivity ().getApplicationContext (),"资料",Toast.LENGTH_LONG ).show ();
        return rootView;
    }
}
