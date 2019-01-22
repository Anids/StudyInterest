package com.swust.stylezz.studyinteret.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.fragment.IndexFragment;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends BaseAdapter {
    private String token;
    private String fileid;
    private Context mContext;
    private List<String> mList = new ArrayList<> ();
    public MyAdapter(Context context,List<String>list){
        mContext=context;
        mList=list;
    }
    @Override
    public int getCount() {
        return mList.size ();
    }

    @Override
    public Object getItem(int position) {
        return mList.get ( position );
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new ViewHolder ();
            convertView= LayoutInflater.from ( mContext ).inflate ( R.layout.listview_item,null );
            viewHolder.mTextView=(TextView) convertView.findViewById ( R.id.text_itemName );
            viewHolder.mImageButton=(ImageButton)convertView.findViewById ( R.id.icon_fenxiang );
            viewHolder.imageButton=(ImageButton)convertView.findViewById ( R.id.shanchu );
            convertView.setTag ( viewHolder );
        }else{
            viewHolder=(ViewHolder)convertView.getTag ();
        }
        viewHolder.mTextView.setText ( mList.get ( position ) );
        viewHolder.mImageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {

            }
        } );
        viewHolder.imageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                MyAdapter.this.token= IndexFragment.Index_token;
                mOnItemDeleteListener.onDeleteClick ( position );
            }
        } );
        return convertView;
    }
    public interface onItemDeleteListener{
        void onDeleteClick(int i);
    }
    private onItemDeleteListener mOnItemDeleteListener;
    public void setmOnItemDeleteListener(onItemDeleteListener mOnItemDeleteListener){
        this.mOnItemDeleteListener=mOnItemDeleteListener;
    }

    class ViewHolder{
        TextView mTextView;
        ImageButton mImageButton;
        ImageButton imageButton;
    }
}
