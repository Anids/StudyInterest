package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.PdfviewActivity;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.swust.stylezz.studyinteret.ui.FileLibraryAdapter;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class FileLibraryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Spinner spinnerGrade=null;
    private Spinner spinnerProfessional=null;
    private Spinner spinnerCourses=null;
    private Button btn_Determine;
    private ListView listView_my;
    private List<Map<String,Object>> mList=new ArrayList<Map<String,Object>> ();
    private SharedPreferences sharedPreferences;
    private View rootView;
    private JSONObject ServerData_cancel=null;
    private String gradesData;
    private String preferencesData;
    private String coursesData;
    private String FileLibraryToken;
    private JSONObject ServerData=null;
    private JSONObject Data=null;
    public static String pdf_Url;

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
        if (rootView==null){
            rootView = inflater.inflate ( R.layout.fragment_filelibrary, container, false );
        }
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        //Toast.makeText ( getActivity ().getApplicationContext (),"资料",Toast.LENGTH_LONG ).show ();
        /**
         * 1.获取spinner中的数据；
         * 2.向后端请求数据；
         * 3.用得到的数据生成列表；
         * */
        InitView();
        return rootView;
    }

    private void InitView() {
        IndexFragment.ans=0;
        RecentlyBrowseFragment.ans=0;
        SetFindViewByID();
        work();
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        btn_Determine.setOnClickListener ( this );
    }

    private void SetFindViewByID() {
        btn_Determine=rootView.findViewById ( R.id.bata_button );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.bata_button:
                //获取筛选框中的数据并尝试登录；
                getDataAndRequireClient();
                break;
        }
    }

    // 主要的函数
    private void work() {
        spinnerGrade = rootView.findViewById(R.id.spinner_grade_01);
        // 请求数据
        try {
            Data=HttpClient.sendRequestWithHttpClient("GET","http://interestion.xyz:3000/app/getcourseclass",null,null);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        JSONObject data1=null;
        try {
            // 返回数据中data的数据
            data1=(JSONObject) Data.get("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // 获取年级
        Iterator<String> it1 = data1.keys();
        int len=0;
        while(it1.hasNext()){
            String key = it1.next();
            len++;
        }
        String[] mItems=new String[len];
        int i=0;
        Iterator<String> it = data1.keys();
        while(it.hasNext()){
            // 获得key
            String key = it.next();
            String value = null;
            try {
                value = Data.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mItems[i++]=key;
        }
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGrade.setAdapter(adapter);

//        设置监听
        final JSONObject data1t=data1;
        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mTemp = (String) parent.getAdapter().getItem(position);
                // 设置专业
                JSONObject data2=null;
                try {
                    data2 = (JSONObject) data1t.get(mTemp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Iterator<String> it2 = data2.keys();
                int len2=0;
                while(it2.hasNext()){
                    String key = it2.next();
                    len2++;
                }
                String[] mItems;
                if(len2==0)
                {
                    mItems=new String[1];
                    mItems[0]="无数据";
                }
                else
                {
                    mItems=new String[len2];
                    int i=0;
                    Iterator<String> it = data2.keys();
                    while(it.hasNext()){
                        // 获得key
                        String key = it.next();
                        mItems[i++]=key;
                    }
                }


                spinnerProfessional = rootView.findViewById(R.id.spinner_major_01);
                ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerProfessional.setAdapter(adapter);

                final JSONObject data2t=data2;
                spinnerProfessional.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String mTemp = (String) parent.getAdapter().getItem(position);
                        JSONArray data3=null;
                        String[] mItems=null;
                        try {
                            data3 = (JSONArray) data2t.get(mTemp);
                            mItems=new String[data3.length()];
                            for(int i=0;i<data3.length();i++)
                            {
                                try {
                                    mItems[i]=data3.getString(i);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            mItems=new String[1];
                            mItems[0]="无数据";
                            e.printStackTrace();
                        }

                        spinnerCourses = rootView.findViewById(R.id.spinner_course_01);
                        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, mItems);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinnerCourses.setAdapter(adapter);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    @Override
    public void onResume() {
        super.onResume ();
    }

    private void getDataAndRequireClient() {
        gradesData=(String) spinnerGrade.getSelectedItem ();
        preferencesData=(String)spinnerProfessional.getSelectedItem ();
        coursesData=(String)spinnerCourses.getSelectedItem ();
        RequireToClient_02();
    }

    private void RequireToClient_02(){
        getToken();
        String UrlStr="";
        try {
            String Grades = URLEncoder.encode ( gradesData,"UTF-8" );
            String Preference=URLEncoder.encode ( preferencesData,"UTF-8" );
            String Courses=URLEncoder.encode ( coursesData,"UTF-8" );
            UrlStr="http://interestion.xyz:3000/app/getpdf?grade="+Grades+"&major="+Preference+"&course="+Courses;
            ServerData= HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,FileLibraryToken );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace ();
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        try {
            final JSONArray data=(JSONArray)ServerData.get ( "data" );
            mList.clear ();
            for (int i=0;i<data.length ();i++){
                JSONObject oj=data.getJSONObject ( i );
                Map<String,Object>item=new HashMap<> (  );
                item.put ( "filename",(String)oj.get ( "filename" ) );
                item.put ( "ifcollect",oj.get ( "ifcollect" ).toString () );
                mList.add ( item );
            }
            this.listView_my=(ListView)rootView.findViewById ( R.id.lv_03 );
            final FileLibraryAdapter adapter=new FileLibraryAdapter ( getActivity ().getApplicationContext (),mList );
            listView_my.setAdapter ( adapter );
            listView_my.setOnItemClickListener ( this );
            adapter.setmOnItemShareColListener ( new FileLibraryAdapter.OnItemShareColListener () {
                @Override
                public void onCollectClick(int i) {
                    String UrlStr="http://interestion.xyz:3000/app/addcollect";
                    int fileid;
                    try {
                        JSONObject oj=data.getJSONObject ( i );
                        fileid=(int)oj.get ( "fileid" );
                        UrlStr=UrlStr+"?fileid="+fileid;
                        ServerData_cancel=HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,FileLibraryToken );
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
                        adapter.notifyDataSetChanged ();
                        Toast.makeText ( getActivity ().getApplicationContext (),"添加成功",Toast.LENGTH_SHORT ).show ();
                    }else{
                        Toast.makeText ( getActivity ().getApplicationContext (),"添加失败",Toast.LENGTH_SHORT ).show ();
                    }
                }

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

                @Override
                public void onDeleteCollectClick(int i) {
                    String UrlStr="http://interestion.xyz:3000/app/deletecollect";
                    int fileid;
                    try {
                        JSONObject oj=data.getJSONObject ( i );
                        fileid=(int)oj.get ( "fileid" );
                        UrlStr=UrlStr+"?fileid="+fileid;
                        ServerData_cancel=HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,FileLibraryToken );
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
                        //mList.remove ( i );
                        adapter.notifyDataSetChanged ();
                        Toast.makeText ( getActivity ().getApplicationContext (),"已取消收藏",Toast.LENGTH_SHORT ).show ();
                    }else{
                        Toast.makeText ( getActivity ().getApplicationContext (),"取消失败",Toast.LENGTH_SHORT ).show ();
                    }
                }
            } );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
    }

//    private void RequireToClient() {
//        getToken();
//        String UrlStr="";
//        try {
//            String Grades = URLEncoder.encode ( gradesData,"UTF-8" );
//            String Preference=URLEncoder.encode ( preferencesData,"UTF-8" );
//            String Courses=URLEncoder.encode ( coursesData,"UTF-8" );
//            UrlStr="http://interestion.xyz:3000/app/getpdf?grade="+Grades+"&major="+Preference+"&course="+Courses;
//            ServerData= HttpClient.sendRequestWithHttpClient ( "GET",UrlStr,null,FileLibraryToken );
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace ();
//        } catch (InterruptedException e) {
//            e.printStackTrace ();
//        }
//        List<Map<String, Object>> datanews=null;
//        try {
//            JSONArray data=(JSONArray)ServerData.get("data");
//            listView = rootView.findViewById(R.id.lv_03);
//            datanews = new ArrayList<Map<String, Object>> ();
//            for (int i=0;i<data.length();i++)
//            {
//                JSONObject oj = data.getJSONObject(i);
//                //步骤1 一个列表项的内容，就是一个item
//                Map<String, Object> item1 = new HashMap<String, Object> ();
//                item1.put("image", R.mipmap.ic_pdf);
//                item1.put("name", (String)oj.get("filename"));
//                //步骤2：把这些Map放到List当中
//                datanews.add(item1);
//            }
//            //注意：第四个参数和第五个参数要一一对应
//            simpleAdapter = new SimpleAdapter (getActivity ().getApplicationContext(), datanews,
//                    R.layout.listview_item_file, new String[] { "image", "name" },
//                    new int[] { R.id.icon_pdf_database, R.id.listviewtext_database });
//            //步骤3：将List中的内容填充到listView里面去
//            listView.setAdapter(simpleAdapter);
//            listView.setOnItemClickListener ( this );
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        FileLibraryToken=sharedPreferences.getString ( "token","" );
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String UrlPDF="http://interestion.xyz:3000";
        try {
            JSONArray data=(JSONArray)ServerData.get ( "data" );
            JSONObject oj=data.getJSONObject ( position );
            UrlPDF=UrlPDF+oj.get ( "fileurl" );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        this.pdf_Url=UrlPDF;
        PdfviewActivity.ans=2;
        startActivity ( new Intent ( getActivity ().getApplicationContext (), PdfviewActivity.class ) );
    }
}
