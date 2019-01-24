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

import com.baoyz.widget.PullRefreshLayout;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.PdfviewActivity;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.swust.stylezz.studyinteret.ui.MyAdapter;

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

//    private ListView listView;
//    private SimpleAdapter simpleAdapter;//列表
    private View rootView;
    private SharedPreferences sharedPreferences;
    private String IndexToken;
    private JSONObject serverData=null;
    private JSONObject ServerData_cancel=null;
    private String status;
    public static String Url_index;
    private ListView listView_my;
    private List<String> mList=new ArrayList ();
    public static int ans=0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate ( R.layout.fragment_index, container, false );
        //Toast.makeText ( getActivity ().getApplicationContext (),"首页",Toast.LENGTH_LONG ).show ();
        /**
         * 1.显示出收藏列表；
         * 2.请求后端数据，并以列表的形式展出；
         * */
        ReFreshInterface();
        return rootView;
    }

    private void ReFreshInterface() {
        final PullRefreshLayout layout=(PullRefreshLayout)rootView.findViewById ( R.id.swipeRefreshLayout );
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
        RecentlyBrowseFragment.ans=0;
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
        RequireToClientWithData_02();
    }

    private void RequireToClientWithData_02(){
        String UrlStr="http://interestion.xyz:3000/app/getcollect";
        try {
            serverData = HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,IndexToken );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        try {
            final JSONArray data = (JSONArray)serverData.get ( "data" );
            mList.clear ();
            for(int i=0;i<data.length ();i++){
                JSONObject oj=data.getJSONObject ( i );
                mList.add ( (String)oj.get ( "filename" ) );
            }
            this.listView_my=(ListView)rootView.findViewById ( R.id.lv_01 );
            final MyAdapter adapter=new MyAdapter ( getActivity ().getApplicationContext (),mList );
            listView_my.setAdapter ( adapter );
            listView_my.setOnItemClickListener ( this );
            adapter.setmOnItemShareDelListener ( new MyAdapter.OnItemShareDelListener () {
                @Override
                public void onDeleteClick(int i) {
                    String UrlStr="http://interestion.xyz:3000/app/deletecollect";
                    int fileid;
                    try {
                        JSONObject oj=data.getJSONObject ( i );
                        fileid=(int)oj.get ( "fileid" );
                        UrlStr=UrlStr+"?fileid="+fileid;
                        ServerData_cancel=HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,IndexToken );
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    }
                    String oj=null;
                    try {
                        oj=ServerData_cancel.get ( "status" ).toString ();
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
                    if (oj.equals ( "SUCCESS" )){
                        mList.remove ( i );
                        adapter.notifyDataSetChanged ();
                        Toast.makeText ( getActivity ().getApplicationContext (),"删除成功",Toast.LENGTH_SHORT ).show ();
                    }else{
                        Toast.makeText ( getActivity ().getApplicationContext (),"删除失败",Toast.LENGTH_SHORT ).show ();
                    }
                }
                @Override
                public void onShareClick(int i) {
                    String Url = null;
                    try {
                        JSONObject oj=data.getJSONObject ( i );
                        Url="http://interestion.xyz:3000/"+(String) oj.get ( "fileurl" );
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



//    private void RequireToClientWithData() {
//        String UrlStr="http://interestion.xyz:3000/app/getcollect";
//        try {
//            serverData = HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,IndexToken );
//            //Toast.makeText ( getActivity ().getApplicationContext (),"登录出错，请重新登录！",Toast.LENGTH_LONG ).show ();
//        } catch (InterruptedException e) {
//            e.printStackTrace ();
//        }
//        List<Map<String, Object>> datanews=null;
//        try {
//            JSONArray data=(JSONArray)serverData.get("data");
//            listView = rootView.findViewById(R.id.lv_01);
//            datanews = new ArrayList<Map<String, Object>> ();
//            for (int i=0;i<data.length();i++)
//            {
//                JSONObject oj = data.getJSONObject(i);
//                Map<String, Object> item1 = new HashMap<String, Object> ();
//                item1.put("image", R.mipmap.ic_pdf);
//                item1.put("name", (String)oj.get("filename"));
//                datanews.add(item1);
//            }
//            simpleAdapter = new SimpleAdapter(getActivity ().getApplicationContext(), datanews,
//                    R.layout.listview_item, new String[] { "image", "name" },
//                    new int[] { R.id.icon_pdf, R.id.text_itemName });
//            listView.setAdapter(simpleAdapter);
//            listView.setOnItemClickListener ( this );
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        status=sharedPreferences.getString ( "update_state","" );
        IndexToken=sharedPreferences.getString ( "token","" );
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
