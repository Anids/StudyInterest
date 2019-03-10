package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.PdfviewActivity;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.swust.stylezz.studyinteret.ui.FileLibraryAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.widget.Toast.LENGTH_LONG;

public class FileLibraryFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    private Spinner spinnerGrade;
    private Spinner spinnerProfessional;
    private Spinner spinnerCourses;
    private Button btn_Determine;
    private ListView listView_my;
    private List<String> mList=new ArrayList ();
//    private ListView listView;
//    private SimpleAdapter simpleAdapter;//列表
    private SharedPreferences sharedPreferences;
    private View rootView;
    private JSONObject ServerData_cancel=null;
    private String gradesData;
    private String preferencesData;
    private String coursesData;
    private String FileLibraryToken;
    private JSONObject ServerData=null;
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
        rootView = inflater.inflate ( R.layout.fragment_filelibrary, container, false );
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
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        btn_Determine.setOnClickListener ( this );
    }

    private void SetFindViewByID() {
        spinnerGrade=rootView.findViewById ( R.id.spinner_grade_01 );
        spinnerProfessional=rootView.findViewById ( R.id.spinner_major_01 );
        spinnerCourses=rootView.findViewById ( R.id.spinner_course_01 );
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
                mList.add ( (String)oj.get ( "filename" ) );
            }
            for (int i=0;i<data.length ();i++){
                int ifcot;
                JSONObject oj=data.getJSONObject ( i );
                ifcot=(int) oj.get ( "ifcollect" );
                mList.add ( String.valueOf ( ifcot ) );
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
