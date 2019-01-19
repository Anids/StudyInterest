package com.swust.stylezz.studyinteret.activity;


import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.fragment.FileLibraryFragment;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;


public class PdfviewActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btn_returnToIndex;
    private TextView textView_returnToIndex;
    private ProgressBar pb_bar;
    private PDFView pdfView;
    private String UrlPDF;
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
        deletefile("mas.pdf");
        btn_returnToIndex=findViewById ( R.id.btn_return_pdf );
        textView_returnToIndex=findViewById ( R.id.textview_pdf_tt );
        pdfView=(PDFView)findViewById ( R.id.pdfview ) ;
        pb_bar=findViewById ( R.id.pb_bar );
        UrlPDF=FileLibraryFragment.pdf_Url;
        downloadOkHttpFile();
        pdfView.fromFile ( new File ( Environment.getExternalStorageDirectory ().getAbsolutePath ()+"/mas.pdf" ) ).load ();
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
        OkHttpUtils
                .get ()
                .url ( UrlPDF )
                .build ()
                .execute ( new FileCallBack (Environment.getExternalStorageDirectory ().getAbsolutePath (),"mas.pdf") {
                    @Override
                    public void inProgress(float v, long l) {

                    }

                    @Override
                    public void onError(Call call, Exception e) {

                    }

                    @Override
                    public void onResponse(File file) {

                    }
                } );
    }
    public static void deletefile(String fileName) {
        try {
            // 找到文件所在的路径并删除该文件
            File file = new File(Environment.getExternalStorageDirectory(), fileName);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
