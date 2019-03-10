package com.swust.stylezz.studyinteret.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.swust.stylezz.studyinteret.R;

import java.util.ArrayList;
import java.util.List;

public class FileLibraryAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> mList = new ArrayList<> ();
    public FileLibraryAdapter(Context context,List<String>list){
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
        FileLibraryAdapter.ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new FileLibraryAdapter.ViewHolder ();
            convertView= LayoutInflater.from ( mContext ).inflate ( R.layout.listview_item_file,null );
            viewHolder.mTextView=(TextView) convertView.findViewById ( R.id.listviewtext_database );
            viewHolder.mImageButton=(ImageButton)convertView.findViewById ( R.id.icon_fenxiang_database );
            viewHolder.imageButton=(ImageButton)convertView.findViewById ( R.id.imageview_collection );
            convertView.setTag ( viewHolder );
        }else{
            viewHolder=(FileLibraryAdapter.ViewHolder)convertView.getTag ();
        }
        viewHolder.mTextView.setText ( mList.get ( position ) );
        int sum=mList.size ()/2+position;
        if(mList.get ( sum )=="1")
        {

        }
        viewHolder.mImageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mOnItemShareColListener.onShareClick ( position );
            }
        } );
        viewHolder.imageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mOnItemShareColListener.onCollectClick ( position );
            }
        } );
        return convertView;
    }
    public interface OnItemShareColListener {
        void onCollectClick(int i);
        void onShareClick(int i);
    }
    private OnItemShareColListener mOnItemShareColListener;
    public void setmOnItemShareColListener(OnItemShareColListener mOnItemShareColListener){
        this.mOnItemShareColListener=mOnItemShareColListener;
    }

    class ViewHolder{
        TextView mTextView;
        ImageButton mImageButton;
        ImageButton imageButton;
    }
}
