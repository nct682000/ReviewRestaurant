package com.example.foodreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ResAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Res> resList;

    public ResAdapter(Context context, int layout, List<Res> resList) {
        this.context = context;
        this.layout = layout;
        this.resList = resList;
    }

    public ResAdapter() {
    }

    @Override
    public int getCount() {
        return resList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private class ViewHolder{
        ImageView imgView;
        TextView textView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder.imgView = convertView.findViewById(R.id.iv_img);
            holder.textView = convertView.findViewById(R.id.tv_text);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        Res res = resList.get(position);
        holder.textView.setText(res.getName());

//        String src = res.getImg();

//        chuyển byte[] -> bitmap
        byte[] img = res.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        holder.imgView.setImageBitmap(bitmap);

        return convertView;
    }
}
