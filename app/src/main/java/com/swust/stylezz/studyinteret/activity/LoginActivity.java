package com.swust.stylezz.studyinteret.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.fragment.InSetFragment;
import com.swust.stylezz.studyinteret.fragment.IndexFragment;
import com.swust.stylezz.studyinteret.http.HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private JSONObject Login_ObjData=null;
    private EditText GetUserNameStr;
    private EditText GetPassWordStr;
    private Button btn_Button;
    private TextView RegisteredNumber;
    private ImageView PassWordStatus;
    private HttpClient ServerSquire;
    private String loginToken;
    private String NickName;
    private String UsernameHash=null;
    private String PasswordHash=null;
    private int state = 1;

    private boolean is_show = true;

    private SharedPreferences sharedPreferencesLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_login );
        /**
         * 1.实现输入账号密码的登录；
         * 2.实现成功登录后的跳转；
         * 3.实现登录失败的提示；
         * 4.实现后端返回数据的存储；
         * 5.实现注册页面的跳转；
         * 6.历史登录数据登录；
         * */
        InitView();
    }

    private void InitView() {
        //初始化logindata文件中的update_state值
        ChangeStatus();
        //
        LocalDataLogin();
        UpdateLoginStatus();
        SetFindViewByID();
        SetOnClickListener();
    }

    private void ChangeStatus() {
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferencesLogin.edit ();
        editor.putString ( "update_state","0" );
        editor.commit ();
    }

    private void LocalDataLogin() {
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        String username = sharedPreferencesLogin.getString ( "username","" );
        String password = sharedPreferencesLogin.getString ( "password","" );
        String token = sharedPreferencesLogin.getString ( "token","" );
        if(token.isEmpty ()){
            state=0;
            return;
        }
        if (username.isEmpty ()){
            state=0;
            return;
        }
        if(password.isEmpty ()){
            state=0;
            return;
        }
        UpdateThisData(username,password);
        CompareDataWithServerClient();
        SaveDataInLocal();
        CloseTheLoginInterface();
    }

    private void UpdateLoginStatus() {
        this.state=1;
    }


    private void UpdateThisData(String username,String password) {
        this.UsernameHash=username;
        this.PasswordHash=password;
    }

    private void SetOnClickListener() {
        PassWordStatus.setOnClickListener ( this );
        btn_Button.setOnClickListener ( this );
        RegisteredNumber.setOnClickListener ( this );
    }

    private void SetFindViewByID() {
        GetUserNameStr=findViewById ( R.id.et_account );
        GetPassWordStr=findViewById ( R.id.et_password );
        PassWordStatus=findViewById ( R.id.iv_see_password );
        btn_Button=findViewById ( R.id.btn_login );
        RegisteredNumber=findViewById ( R.id.regist_account );
        ChangePwdStatus();
    }

    private void ChangePwdStatus() {
        PassWordStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_noseepass));
        GetPassWordStr.setTransformationMethod( PasswordTransformationMethod.getInstance());
        GetPassWordStr.setSelection(GetPassWordStr.getText().toString().length());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ()){
            case R.id.iv_see_password:
                //改变密码的展示形态；
                setPasswordVisibility();
                break;
            case R.id.btn_login:
                //比较数据判断是否登录成功；
                UpdateLoginStatus();
                setLogin();
                break;
            case R.id.regist_account:
                //跳转到注册界面；
                JumpToRegistInterface();
                break;
        }
    }

    private void JumpToRegistInterface() {
        startActivity ( new Intent ( this,RegistActivity.class  ) );
    }

    private void setLogin() {
        /**
         * 1.获取输入框中的数据；
         * 2.向后端请求比较数据；
         * 3.登录成功后保存返回数据，登录失败则提示；
         * */
        getUsernameAndPassword();
        SaveDataInLocal();
        CloseTheLoginInterface();
    }

    private void CloseTheLoginInterface() {
        if(state==0)
        {
            Toast.makeText ( LoginActivity.this,"无法登录",Toast.LENGTH_SHORT ).show ();
            return;
        }
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferencesLogin.edit ();
        editor.putString ( "update_state","1" );
        editor.commit ();
        LoginActivity.this.finish ();
    }

    private void SaveDataInLocal() {
        if(state==0)
        {
            Toast.makeText ( LoginActivity.this,"无法保存",Toast.LENGTH_SHORT ).show ();
            return;
        }
        sharedPreferencesLogin=getSharedPreferences ( "logindata",MODE_PRIVATE );
        SharedPreferences.Editor editor = sharedPreferencesLogin.edit ();
        editor.clear ();
        editor.putString ( "username",UsernameHash );
        editor.putString ( "password",PasswordHash );
        editor.putString ( "token",loginToken );
        editor.putString ( "nickname",NickName );
        editor.commit ();
    }


    private void getUsernameAndPassword() {
        if(getUsername ().isEmpty ())
        {
            Toast.makeText ( LoginActivity.this,"登录账号不能为空，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        if (getPassword ().isEmpty ()){
            Toast.makeText ( LoginActivity.this,"登录密码不能为空，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        String accRegex = "[0-9]{10}";//账号10位数字组成
        String pasRegex = "[0-9a-zA-Z]{6,18}";//密码6-10位数字或字母组成
        if(!getUsername ().matches ( accRegex )){
            Toast.makeText ( LoginActivity.this,"登录账号格式为10位数字，请重新输入！",Toast.LENGTH_LONG ).show ();
            state=0;
            return;
        }
        if(!getPassword ().matches ( pasRegex )){
            Toast.makeText ( LoginActivity.this,"登录密码格式为6~18位中英文字符，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        SaveUsernameIn();
        SavePasswordIn();
        CompareDataWithServerClient();
    }

    private void SavePasswordIn() {
        this.PasswordHash=getPassword ();
    }

    private void CompareDataWithServerClient() {
        JSONObject obj = new JSONObject (  );
        try {
            obj.put ( "username",UsernameHash );
            obj.put ( "password",PasswordHash );
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        JSONArray objdata;
        String status=null;
        String token=null;
        String nick_name=null;
        try {
            //请求登录验证；
            Login_ObjData=ServerSquire.sendRequestWithHttpClient("POST","http://interestion.xyz:3000/app/login",obj,null);
            status=(String) Login_ObjData.get ( "status" );
            objdata=(JSONArray)Login_ObjData.get ( "data" );
            obj=objdata.getJSONObject ( 0 );
            token=(String)obj.get ( "token" );
            nick_name=(String)obj.get("nickname");
        } catch (InterruptedException e) {
            e.printStackTrace ();
        } catch (JSONException e) {
            e.printStackTrace ();
        }
        if(status.equals ( "SUCCESS" )){

        }
        else{
            Toast.makeText ( LoginActivity.this,"登录失败，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        if(token.isEmpty ()){
            Toast.makeText ( LoginActivity.this,"登录失败，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        if(nick_name.isEmpty ()){
            Toast.makeText ( LoginActivity.this,"登录失败，请重新输入！",Toast.LENGTH_SHORT ).show ();
            state=0;
            return;
        }
        NickName=nick_name;
        loginToken=token;
        obj=null;
        objdata=null;
    }

    private void SaveUsernameIn() {
        this.UsernameHash=getUsername ();
    }

    /**
     * 获取登录信息
     * */
    public JSONObject getLoginData()
    {
        return Login_ObjData;
    }


    /**
     * 获取账号
     */
    public String getUsername() {
        return GetUserNameStr.getText().toString();
    }
    /**
     * 获取密码
     */
    public String getPassword() {
        return GetPassWordStr.getText().toString();
    }


    private void setPasswordVisibility() {
        if (is_show) {
            PassWordStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_seepass));
            GetPassWordStr.setTransformationMethod( HideReturnsTransformationMethod.getInstance());
            GetPassWordStr.setSelection(GetPassWordStr.getText().toString().length());
            is_show = !is_show;

        } else {
            PassWordStatus.setImageDrawable(getResources().getDrawable(R.mipmap.ic_noseepass));
            GetPassWordStr.setTransformationMethod( PasswordTransformationMethod.getInstance());
            GetPassWordStr.setSelection(GetPassWordStr.getText().toString().length());
            is_show = !is_show;
        }
    }
}
