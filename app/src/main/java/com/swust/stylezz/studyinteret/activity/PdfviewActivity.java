package com.swust.stylezz.studyinteret.activity;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.fragment.FileLibraryFragment;
import com.swust.stylezz.studyinteret.fragment.IndexFragment;
import com.swust.stylezz.studyinteret.fragment.RecentlyBrowseFragment;
import com.umeng.analytics.MobclickAgent;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;


public class PdfviewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_returnToIndex;
    private TextView textView_returnToIndex;
    private PDFView pdfView;
    private String UrlPDF=null;
    public static int ans;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.index_pdfview_item );
        /**
         * 1.打开PDF文件；
         * 2.button返回；
         * */
        InitView();
    }

    private void InitView() {
        if(ans==1){
            UrlPDF=IndexFragment.Url_index;
        }
        if(ans==2){
            UrlPDF=FileLibraryFragment.pdf_Url;
        }
        if(ans==3){
            UrlPDF=RecentlyBrowseFragment.Url_RBF;
        }
        if(UrlPDF.isEmpty ()){
            return;
        }
        btn_returnToIndex=findViewById ( R.id.btn_return_pdf );
        textView_returnToIndex=findViewById ( R.id.textview_pdf_tt );
        pdfView=(PDFView)findViewById ( R.id.pdfview ) ;
        downloadOkHttpFile();
        btn_returnToIndex.setOnClickListener ( this );
        textView_returnToIndex.setOnClickListener ( this );
    }



    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.btn_return_pdf:
                PdfviewActivity.this.finish ();
                break;
            case R.id.textview_pdf_tt:
                PdfviewActivity.this.finish ();
                break;
        }
    }
    public void downloadOkHttpFile(){
        String destfilename = UrlPDF.replace ( "http://interestion.xyz:3000/","" );
        String a="/";
        final String destfilename2=a.concat ( destfilename );
        File file = new File ( Environment.getExternalStorageDirectory ().getAbsolutePath ()+destfilename2 );
        if (file.exists ())
        {
            pdfView.fromFile ( file).load ();
        }
        else{
            new Thread (  ){
                @Override
                public void run() {
                    super.run ();
                    String destfilename = UrlPDF.replace ( "http://interestion.xyz:3000/","" );
                    OkHttpUtils
                            .get ()
                            .url ( UrlPDF )
                            .build ()
                            .execute ( new FileCallBack (Environment.getExternalStorageDirectory ().getAbsolutePath (),destfilename) {
                                @Override
                                public void inProgress(float v, long l) {

                                }

                                @Override
                                public void onError(Call call, Exception e) {

                                }

                                @Override
                                public void onResponse(File file) {
                                    pdfView.fromFile (
                                            new File ( Environment.getExternalStorageDirectory ().getAbsolutePath ()+destfilename2 ) )
                                            .load ();
                                }
                            } );
                }

            }.start ();

        }
    }
    // Activity页面onResume函数重载
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏

    }

    // Activity页面onResume函数重载
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); // 不能遗漏
    }
}
