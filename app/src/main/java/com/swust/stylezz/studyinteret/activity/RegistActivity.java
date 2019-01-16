package com.swust.stylezz.studyinteret.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.http.HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

public class RegistActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout relativeLayoutReturn;
    private EditText editTextNickName;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private String NickName;
    private String Username;
    private String Password;
    private String RepeatPassword;
    private Button btn_Regist;
    private JSONObject RegistObj;
    private HttpClient ServerSquire;
    private boolean Regist_Status=true;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_regist );
        /**
         * 1.实现返回至登录界面；
         * 2.获取文本框中的数据；
         * 3.对文本框中的数据进行格式判断；
         * 4.尝试请求插入新用户信息；
         * */
        InitView();
    }

    private void InitView() {
        RegistFindViewByID();
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        relativeLayoutReturn.setOnClickListener ( this );
        btn_Regist.setOnClickListener ( this );
    }

    private void RegistFindViewByID() {
        relativeLayoutReturn=findViewById ( R.id.regist_return );
        editTextNickName=findViewById ( R.id.et_accountnext_name );
        editTextUsername=findViewById ( R.id.et_accountnext );
        editTextPassword=findViewById ( R.id.et_passwordnext );
        editTextRepeatPassword=findViewById ( R.id.et_repassword );
        btn_Regist=findViewById ( R.id.btn_regist );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.regist_return:
                //返回到登录界面；
                ReturnToLoginInterface();
                break;
            case R.id.btn_regist:
                //尝试注册
                TryToRegist();
                break;
        }
    }

    private void TryToRegist() {
        getData();
        if(NickName.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"昵称不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(Username.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"账号不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(Password.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(RepeatPassword.isEmpty ()){
            Toast.makeText ( RegistActivity.this,"再次输入密码不能为空，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(Password.equals ( RepeatPassword )){

        }else{
            Toast.makeText ( RegistActivity.this,"两次密码不一致，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        String accRegex = "[0-9]{10}";//账号10位数字组成
        String pasRegex = "[0-9a-zA-Z]{6,18}";//密码6-10位数字或字母组成
        if (Username.matches ( accRegex )){

        }else{
            Toast.makeText ( RegistActivity.this,"账号由10位数字组成，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        if(Password.matches ( pasRegex )){

        }else{
            Toast.makeText ( RegistActivity.this,"密码有6~18位数字组成，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        InsertIntoClient();
    }

    private void InsertIntoClient() {
        JSONObject Obj=new JSONObject (  );
        try {
            Obj.put ( "nickname",NickName );
            Obj.put ( "username", Username);
            Obj.put ( "password",Password );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        String status=null;
        try {
            RegistObj=ServerSquire.sendRequestWithHttpClient ( "POST","http://interestion.xyz:3000/app/register",Obj,null );
            status=(String)RegistObj.get ( "status" );
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        if(status.equals ( "SUCCESS" )){

        }else{
            Toast.makeText ( RegistActivity.this,"账号已存在，请重新输入！",Toast.LENGTH_LONG ).show ();
            Regist_Status=false;
            return;
        }
        Toast.makeText ( RegistActivity.this,"注册成功！",Toast.LENGTH_LONG ).show ();
        ReturnToLoginInterface();
    }

    private void getData() {
        NickName=editTextNickName.getText ().toString ();
        Username=editTextUsername.getText ().toString ();
        Password=editTextPassword.getText ().toString ();
        RepeatPassword=editTextRepeatPassword.getText ().toString ();
    }

    private void ReturnToLoginInterface() {
        RegistActivity.this.finish ();
    }
}
