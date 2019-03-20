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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.baoyz.widget.PullRefreshLayout;
import com.swust.stylezz.studyinteret.MainActivity;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.PdfviewActivity;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.swust.stylezz.studyinteret.ui.RecentAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecentlyBrowseFragment extends Fragment implements AdapterView.OnItemClickListener {
    private SharedPreferences sharedPreferences;
    private String RecentToken;
    private JSONObject RBFObj;
    private ListView listView;
    private SimpleAdapter simpleAdapter;//列表
    private ListView listView_my;
    private JSONObject ServerData_cancel=null;
    private List<Map<String,Object>> mList=new ArrayList<Map<String,Object>> ();
    private static volatile RecentlyBrowseFragment recentlyBrowseFragmentInstance = null;
    private String status;
    private View rootView;
    public static String Url_RBF;
    public static int ans=0;

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
        if (rootView==null){
            rootView = inflater.inflate( R.layout.fragment_recentlybrowse, container, false);
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        //Toast.makeText ( getActivity ().getApplicationContext (),"痕迹",Toast.LENGTH_LONG ).show ();
        /**
         * 1.用token获取后端的数据；
         * 2.用列表展示数据；
         * */
        PullRefreshInterface();
        return rootView;
    }

    private void PullRefreshInterface() {
        final PullRefreshLayout layout=(PullRefreshLayout)rootView.findViewById ( R.id.recent_pullrefresh );
        layout.setOnRefreshListener ( new PullRefreshLayout.OnRefreshListener () {
            @Override
            public void onRefresh() {
                layout.postDelayed ( new Runnable () {
                    @Override
                    public void run() {
                        InitView ();
                        layout.setRefreshing ( false );
                    }
                },3000 );
                ans=1;
            }
        } );
        if(ans==0){
            InitView ();
        }
    }

    private void InitView() {
        IndexFragment.ans=0;
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
        RequireToClientWithData_02();
    }
    private void RequireToClientWithData_02(){
        String UrlStr="http://interestion.xyz:3000/app/getrecentlylook";
        try {
            RBFObj= HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,RecentToken );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        try {
            final JSONArray data=(JSONArray)RBFObj.get ( "data" );
            mList.clear ();
            for (int i=0;i<data.length ();i++){
                JSONObject oj=data.getJSONObject ( i );
                Map<String,Object>item=new HashMap<> (  );
                item.put ( "text",(String)oj.get ( "filename" ) );
                item.put ( "date",(String)oj.get ( "looktime" ) );
                mList.add ( item );
            }
            this.listView_my=(ListView)rootView.findViewById ( R.id.lv_02 );
            final RecentAdapter adapter=new RecentAdapter ( getActivity ().getApplicationContext (),mList );
            listView_my.setAdapter ( adapter );
            listView_my.setOnItemClickListener ( this );
            adapter.setmOnItemShareDelListener ( new RecentAdapter.OnItemShareDelListener () {
                @Override
                public void onShareClick(int i) {
                    String Url = null;
                    try {
                        JSONObject oj=data.getJSONObject ( i );
                        Url="http://interestion.xyz:3000"+(String) oj.get ( "fileurl" );
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                    Intent textIntent = new Intent ( Intent.ACTION_SEND );
                    textIntent.setType ( "text/plain" );
                    textIntent.putExtra ( Intent.EXTRA_TEXT ,Url);
                    startActivity ( Intent.createChooser ( textIntent,"分享" ) );
                }
            } );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
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
            listView.setOnItemClickListener ( this );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        RecentToken=sharedPreferences.getString ( "token","" );
        status=sharedPreferences.getString ( "update_state","" );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String UrlPDF="http://interestion.xyz:3000";
        try {
            JSONArray data=(JSONArray)RBFObj.get ( "data" );
            JSONObject oj=data.getJSONObject ( position );
            UrlPDF=UrlPDF+oj.get ( "fileurl" );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        this.Url_RBF=UrlPDF;
        PdfviewActivity.ans=3;
        //Toast.makeText ( getActivity ().getApplicationContext (),UrlPDF,Toast.LENGTH_LONG ).show ();
        startActivity ( new Intent ( getActivity ().getApplicationContext (), PdfviewActivity.class ) );
    }
}
