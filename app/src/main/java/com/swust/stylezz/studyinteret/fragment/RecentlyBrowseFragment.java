package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
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
import com.swust.stylezz.studyinteret.http.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentlyBrowseFragment extends Fragment {
    private SharedPreferences sharedPreferences;
    private String RecentToken;
    private JSONObject RBFObj;
    private ListView listView;
    private SimpleAdapter simpleAdapter;//列表
    private static volatile RecentlyBrowseFragment recentlyBrowseFragmentInstance = null;
    private String status;
    private View rootView;

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
        rootView = inflater.inflate( R.layout.fragment_recentlybrowse, container, false);
        //Toast.makeText ( getActivity ().getApplicationContext (),"痕迹",Toast.LENGTH_LONG ).show ();
        /**
         * 1.用token获取后端的数据；
         * 2.用列表展示数据；
         * */
        InitView();
        return rootView;
    }

    private void InitView() {
        getToken();
        if(status.equals ( "1" )){

        }else{
            Toast.makeText ( getActivity ().getApplicationContext (),"您尚未登录，请登录！",Toast.LENGTH_LONG ).show ();
            return;
        }
        if (RecentToken.isEmpty ()){
            Toast.makeText ( getActivity ().getApplicationContext (),"登录出错，请重新登录！",Toast.LENGTH_LONG ).show ();
            return;
        }
        RequireToClientWithData();
    }

    private void RequireToClientWithData() {
        String UrlStr="http://interestion.xyz:3000/app/getrecentlylook";
        try {
            RBFObj= HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,RecentToken );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        List<Map<String, Object>> datanews=null;
        try {
            JSONArray data=(JSONArray)RBFObj.get("data");
            listView = rootView.findViewById(R.id.lv_02);
            datanews = new ArrayList<Map<String, Object>> ();
            for (int i=0;i<data.length();i++)
            {
                JSONObject oj = data.getJSONObject(i);
                Map<String, Object> item1 = new HashMap<String, Object> ();
                item1.put("image", R.mipmap.ic_pdf);
                item1.put("name", (String)oj.get("filename"));
                item1.put ( "date",(String)oj.get("looktime") );
                datanews.add(item1);
            }
            simpleAdapter = new SimpleAdapter (getActivity ().getApplicationContext(), datanews,
                    R.layout.listview_item_recent, new String[] { "image", "name","date" },
                    new int[] { R.id.icon_pdf_recent, R.id.listviewtext_recent ,R.id.listview_date});
            listView.setAdapter(simpleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        RecentToken=sharedPreferences.getString ( "token","" );
        status=sharedPreferences.getString ( "update_state","" );
    }
}
