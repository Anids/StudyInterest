package com.swust.stylezz.studyinteret.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.http.HttpClient;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdatePwdActivity extends AppCompatActivity implements View.OnClickListener {
    private JSONObject updateObj;
    private ImageButton imageButton_return;
    private EditText UsedPassword;
    private EditText NewsPassword;
    private EditText RepeatPassword;
    private Button ConfirmButton;
    private String usedPassword;
    private String newsPassword;
    private String repeatPassword;
    private boolean status=true;
    private HttpClient ServerSquire;
    private SharedPreferences sharedPreferencesLogin;
    private String UPAtoken;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_updatepwd );
        /**
         * 1.获取文本框中的数据；
         * 2.旧密码与后端比较；
         * 3.新密码与确认新密码比较；
         * 4.修改数据库对应账号密码；
         * */
        InitView();
    }

    private void InitView() {
        UPA_FindViewByID();
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        imageButton_return.setOnClickListener ( this );
        ConfirmButton.setOnClickListener ( this );
    }

    private void UPA_FindViewByID() {
        imageButton_return=findViewById ( R.id.left_btn );
        UsedPassword=findViewById ( R.id.et_oldpass );
        NewsPassword=findViewById ( R.id.et_newpass1 );
        RepeatPassword=findViewById ( R.id.et_newpass2 );
        ConfirmButton=findViewById ( R.id.btn_sure );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.left_btn:
                //返回到设置页面；
                ReturnToInset();
                break;
            case R.id.btn_sure:
                //确认修改密码；
                ConfirmChangePassword();
                break;
        }
    }

    private void ConfirmChangePassword() {
        getUsedPassword();
        getNewsPassword();
        getRepeatPassword();
        if(usedPassword.isEmpty ()){
            Toast.makeText ( UpdatePwdActivity.this,"原密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        if(newsPassword.isEmpty ()){
            Toast.makeText ( UpdatePwdActivity.this,"新密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        if (repeatPassword.isEmpty ()){
            Toast.makeText ( UpdatePwdActivity.this,"确认新密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        if(newsPassword.equals ( repeatPassword )){

        }else{
            Toast.makeText ( UpdatePwdActivity.this,"确认新密码与新密码不一致，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        String pasRegex= "[0-9a-zA-Z]{6,18}";
        if(usedPassword.matches ( pasRegex )){

        }else{
            Toast.makeText ( UpdatePwdActivity.this,"原密码格式不正确，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        if (newsPassword.matches ( pasRegex )){

        }else{
            Toast.makeText ( UpdatePwdActivity.this,"新密码格式不正确，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        if(repeatPassword.matches ( pasRegex )){

        }else{
            Toast.makeText ( UpdatePwdActivity.this,"确认新密码格式不正确，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        CompareWithClient();
    }

    private void CompareWithClient() {
        JSONObject obj=new JSONObject (  );
        getToken();
        try {
            //obj.put ( "Authorization",UPAtoken );
            obj.put("orginalpassword",usedPassword);
            obj.put ( "newpassword",newsPassword );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        try {
            //请求修改密码接口；
            updateObj= ServerSquire.sendRequestWithHttpClient ( "POST","http://interestion.xyz:3000/app/changepassword",obj,UPAtoken );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
        String UPAstatus=null;
        try {
            UPAstatus=(String)updateObj.get ( "status" );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        if (UPAstatus.equals ( "SUCCESS" )){

        }else{
            Toast.makeText ( UpdatePwdActivity.this,"修改失败，请重新输入！",Toast.LENGTH_LONG ).show ();
            status=false;
            return;
        }
        UpdateSharePrefencesData();
    }

    private void UpdateSharePrefencesData() {
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferencesLogin.edit ();
        editor.putString ( "password",newsPassword );
        editor.commit ();
        Toast.makeText ( UpdatePwdActivity.this,"修改成功",Toast.LENGTH_SHORT ).show ();
        ReturnToInset();
    }

    private void getToken() {
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        UPAtoken=sharedPreferencesLogin.getString ( "token","" );
    }

    private void getRepeatPassword() {
        this.repeatPassword=RepeatPassword.getText ().toString ();
    }

    private void getNewsPassword() {
        this.newsPassword=NewsPassword.getText ().toString ();
    }

    private void getUsedPassword() {
        this.usedPassword=UsedPassword.getText ().toString ();
    }

    private void ReturnToInset() {
        UpdatePwdActivity.this.finish ();
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
