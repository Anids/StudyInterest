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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.activity.LoginActivity;
import com.swust.stylezz.studyinteret.activity.UpdatePwdActivity;

public class InSetFragment extends Fragment implements View.OnClickListener {
    private View rootView;
    private TextView lbelName;
    private TextView lbelUserName;
    private TextView lbelStatus;
    private SharedPreferences sharedPreferencesLogin;
    private String InsetUserName;
    private String InsetPassWord;
    private String InsetToken;

    private static volatile InSetFragment inSetFragmentInstance = null;
    @SuppressLint("ValidFragment")
    private InSetFragment(){}
    public static InSetFragment getNewInstance(){//单例模式，防止多项实例化
        if (inSetFragmentInstance ==null)
        {
            synchronized (InSetFragment.class){
                if (inSetFragmentInstance ==null){
                    inSetFragmentInstance =new InSetFragment ();
                }
            }
        }
        return inSetFragmentInstance;
    }

    private ImageButton imageButtonHead;
    private LinearLayout linearLayoutUpdatePwd;
    private Button buttonCancellationLogin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate ( R.layout.fragment_inset, container, false );
        Toast.makeText ( getActivity ().getApplicationContext (),"设置",Toast.LENGTH_LONG ).show ();
        /**
         * 1.头像框登录；
         * 2.登录后更新信息；
         * 3.修改密码；
         * 4.注销登录；
         * */
        InitView();
        return rootView;
    }

    private void InitView() {
        SetFindViewByID();
        UpdateData();
        SetOnClickListener();
    }

    private void SetOnClickListener() {
        imageButtonHead.setOnClickListener ( this );
        linearLayoutUpdatePwd.setOnClickListener ( this );
        buttonCancellationLogin.setOnClickListener ( this );
    }

    private void SetFindViewByID() {
        imageButtonHead=rootView.findViewById ( R.id.myPhoto );
        linearLayoutUpdatePwd=rootView.findViewById ( R.id.changePassWord );
        buttonCancellationLogin=rootView.findViewById ( R.id.button_Logout_login );
        lbelName = rootView.findViewById ( R.id.truthname );
        lbelUserName = rootView.findViewById ( R.id.inset_username ) ;
        lbelStatus = rootView.findViewById ( R.id.static_user ) ;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId ())
        {
            case R.id.myPhoto:
                //实现登录界面的跳转或历史登录；
                JumpToLoginInterface();
                break;
            case R.id.changePassWord:
                //实现修改密码界面的跳转；
                JumpToUpdatePwdInterface();
                break;
            case R.id.button_Logout_login:
                //实现注销登录;
                CancellationLogin();
                break;
        }
    }

    private void CancellationLogin() {
        //进行缓存登录信息的清理；
        sharedPreferencesLogin=getActivity ().getSharedPreferences ( "logindata",Context.MODE_PRIVATE );
        SharedPreferences.Editor editor=sharedPreferencesLogin.edit ();
        editor.clear ();
        editor.commit ();
        startActivity ( new Intent ( getActivity ().getApplicationContext (),LoginActivity.class ) );
    }

    private void JumpToUpdatePwdInterface() {
        startActivity ( new Intent ( getActivity ().getApplicationContext (), UpdatePwdActivity.class ) );
    }

    private void JumpToLoginInterface() {
        startActivity ( new Intent ( getActivity ().getApplicationContext (), LoginActivity.class ) );
        UpdateData();
    }

    private void UpdateData() {
        sharedPreferencesLogin=getActivity ().getSharedPreferences ( "logindata", Context.MODE_PRIVATE );
        String token = sharedPreferencesLogin.getString ( "token","" );
        String username=sharedPreferencesLogin.getString ( "username","" );
        String password=sharedPreferencesLogin.getString ( "password","" );
        String status=sharedPreferencesLogin.getString ( "update_state","" );
        getInsetToken(token);
        getInsetUserName(username);
        getInsetPassWord(password);
        if (status.equals ( "1" )){
            SetData ( username );
        }
    }

    private void getInsetPassWord(String password) {
        this.InsetPassWord=password;
    }

    private void getInsetUserName(String username) {
        this.InsetUserName=username;
    }

    private void getInsetToken(String token) {
        this.InsetToken=token;
    }


    public void SetData(String username) {
        lbelName.setText ( username );
        lbelUserName.setText ( username );
        lbelStatus.setText ( "已登录" );
    }
}