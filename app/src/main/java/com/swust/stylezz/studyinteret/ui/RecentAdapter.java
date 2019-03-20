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
import java.util.Map;

public class RecentAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>> ();
    public RecentAdapter(Context context,List<Map<String,Object>>list){
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
        RecentAdapter.ViewHolder viewHolder=null;
        if(convertView==null){
            viewHolder=new RecentAdapter.ViewHolder ();
            convertView= LayoutInflater.from ( mContext ).inflate ( R.layout.listview_item_recent,null );
            viewHolder.mTextView=(TextView) convertView.findViewById ( R.id.listviewtext_recent );
            viewHolder.textView=(TextView)convertView.findViewById ( R.id.listview_date ) ;
            viewHolder.mImageButton=(ImageButton)convertView.findViewById ( R.id.icon_fenxiang_recent );
            convertView.setTag ( viewHolder );
        }else{
            viewHolder=(RecentAdapter.ViewHolder)convertView.getTag ();
        }
        Map<String,Object>map=mList.get ( position );
        viewHolder.mTextView.setText ( (String)map.get ( "text" ) );
        viewHolder.textView.setText ( (String)map.get ( "date" ) );
        viewHolder.mImageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mOnItemShareDelListener.onShareClick ( position );
            }
        } );
        return convertView;
    }
    public interface OnItemShareDelListener {
        void onShareClick(int i);
    }
    private OnItemShareDelListener mOnItemShareDelListener;
    public void setmOnItemShareDelListener(OnItemShareDelListener mOnItemShareDelListener){
        this.mOnItemShareDelListener=mOnItemShareDelListener;
    }

    class ViewHolder{
        TextView mTextView;
        TextView textView;
        ImageButton mImageButton;
    }
}
