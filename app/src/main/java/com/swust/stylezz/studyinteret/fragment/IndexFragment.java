package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterViewAnimator;
import android.widget.AdapterViewFlipper;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.PdfviewActivity;
import com.swust.stylezz.studyinteret.http.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IndexFragment extends Fragment implements AdapterView.OnItemClickListener{
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
    private String IndexToken;
    public static String Index_token;
    private JSONObject serverData=null;
    private String status;
    public static String Url_index;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate ( R.layout.fragment_index, container, false );
        //Toast.makeText ( getActivity ().getApplicationContext (),"首页",Toast.LENGTH_LONG ).show ();
        /**
         * 1.显示出收藏列表；
         * 2.请求后端数据，并以列表的形式展出；
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
        if (IndexToken.isEmpty ()){
            Toast.makeText ( getActivity ().getApplicationContext (),"登录出错，请重新登录！",Toast.LENGTH_LONG ).show ();
            return;
        }
        RequireToClientWithData();
    }



    private void RequireToClientWithData() {
        String UrlStr="http://interestion.xyz:3000/app/getcollect";
        try {
            serverData = HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,IndexToken );
            //Toast.makeText ( getActivity ().getApplicationContext (),"登录出错，请重新登录！",Toast.LENGTH_LONG ).show ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        List<Map<String, Object>> datanews=null;
        try {
            JSONArray data=(JSONArray)serverData.get("data");
            listView = rootView.findViewById(R.id.lv_01);
            datanews = new ArrayList<Map<String, Object>> ();
            for (int i=0;i<data.length();i++)
            {
                JSONObject oj = data.getJSONObject(i);
                Map<String, Object> item1 = new HashMap<String, Object> ();
                item1.put("image", R.mipmap.ic_pdf);
                item1.put("name", (String)oj.get("filename"));
                datanews.add(item1);
            }
            simpleAdapter = new SimpleAdapter(getActivity ().getApplicationContext(), datanews,
                    R.layout.listview_item, new String[] { "image", "name" },
                    new int[] { R.id.icon_pdf, R.id.text_itemName });
            listView.setAdapter(simpleAdapter);
            listView.setOnItemClickListener ( this );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        status=sharedPreferences.getString ( "update_state","" );
        IndexToken=sharedPreferences.getString ( "token","" );
        Index_token=IndexToken;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String UrlPDF="http://interestion.xyz:3000";
        try {
            JSONArray data=(JSONArray)serverData.get ( "data" );
            JSONObject oj=data.getJSONObject ( position );
            UrlPDF=UrlPDF+oj.get ( "fileurl" );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        this.Url_index=UrlPDF;
        PdfviewActivity.ans=1;
        //Toast.makeText ( getActivity ().getApplicationContext (),UrlPDF,Toast.LENGTH_LONG ).show ();
        startActivity ( new Intent ( getActivity ().getApplicationContext (), PdfviewActivity.class ) );
    }

}
