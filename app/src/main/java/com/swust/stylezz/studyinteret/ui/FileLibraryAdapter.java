package com.swust.stylezz.studyinteret.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.swust.stylezz.studyinteret.MainActivity;
import com.swust.stylezz.studyinteret.R;
import com.swust.stylezz.studyinteret.fragment.FileLibraryFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileLibraryAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String,Object>> mList = new ArrayList<Map<String,Object>> ();
    public FileLibraryAdapter(Context context, List<Map<String, Object>> list){
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
        Map<String,Object>map=mList.get ( position );
        viewHolder.mTextView.setText ( (String)map.get ( "filename" ) );
        if((String)map.get ( "ifcollect" )=="1")
        {
            viewHolder.imageButton.setImageDrawable ( convertView.getContext ().getResources ().getDrawable ( R.mipmap.btn_heart_2 ) );
        }else{
            viewHolder.imageButton.setImageDrawable ( convertView.getContext ().getResources ().getDrawable ( R.mipmap.btn_heart_1 ) );
        }
        viewHolder.mImageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                mOnItemShareColListener.onShareClick ( position );
            }
        } );
        //final ViewHolder finalViewHolder = viewHolder;
        //final View finalConvertView = convertView;
        viewHolder.imageButton.setOnClickListener ( new View.OnClickListener () {
            @Override
            public void onClick(View v) {
                Map<String,Object>map=mList.get ( position );
                String msc=(String)map.get ( "ifcollect" );
                if(msc=="1")
                {
                    mOnItemShareColListener.onDeleteCollectClick ( position );
                    //finalViewHolder.imageButton.setImageDrawable ( finalConvertView.getContext ().getResources ().getDrawable ( R.mipmap.btn_heart_1 ) );
                    map.put ( "ifcollect","0" );
                    FileLibraryFragment.getNewInstance ();
                }
                else{
                    mOnItemShareColListener.onCollectClick ( position );
                    map.put ( "ifcollect","1" );
                    FileLibraryFragment.getNewInstance ();
                    //finalViewHolder.imageButton.setImageDrawable ( finalConvertView.getContext ().getResources ().getDrawable ( R.mipmap.btn_heart_2 ) );
                }
            }
        } );
        return convertView;
    }
    public interface OnItemShareColListener {
        void onCollectClick(int i);
        void onShareClick(int i);
        void onDeleteCollectClick(int i);
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
