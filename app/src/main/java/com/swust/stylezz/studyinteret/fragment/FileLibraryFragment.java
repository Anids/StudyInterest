package com.swust.stylezz.studyinteret.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.http.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileLibraryFragment extends Fragment implements View.OnClickListener {
    private Spinner spinnerGrade;
    private Spinner spinnerProfessional;
    private Spinner spinnerCourses;
    private Button btn_Determine;
    private ListView listView;
    private SimpleAdapter simpleAdapter;//列表
    private SharedPreferences sharedPreferences;
    private View rootView;
    private String gradesData;
    private String preferencesData;
    private String coursesData;
    private String FileLibraryToken;
    private JSONObject ServerData=null;
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
        RequireToClient();
    }

    private void RequireToClient() {
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
        List<Map<String, Object>> datanews=null;
        try {
            JSONArray data=(JSONArray)ServerData.get("data");
            listView = rootView.findViewById(R.id.lv_03);
            datanews = new ArrayList<Map<String, Object>> ();
            for (int i=0;i<data.length();i++)
            {
                JSONObject oj = data.getJSONObject(i);
                //步骤1 一个列表项的内容，就是一个item
                Map<String, Object> item1 = new HashMap<String, Object> ();
                item1.put("image", R.mipmap.ic_pdf);
                item1.put("name", (String)oj.get("filename"));
                //步骤2：把这些Map放到List当中
                datanews.add(item1);
            }
            //注意：第四个参数和第五个参数要一一对应
            simpleAdapter = new SimpleAdapter (getActivity ().getApplicationContext(), datanews,
                    R.layout.listview_item_file, new String[] { "image", "name" },
                    new int[] { R.id.icon_pdf_database, R.id.listviewtext_database });
            //步骤3：将List中的内容填充到listView里面去
            listView.setAdapter(simpleAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void getToken() {
        sharedPreferences=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        FileLibraryToken=sharedPreferences.getString ( "token","" );
    }
}
