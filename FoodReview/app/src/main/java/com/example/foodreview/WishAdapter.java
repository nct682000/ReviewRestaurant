package com.example.foodreview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class WishAdapter extends BaseAdapter {

    Context context;
    int layout;
    List<WishRes> arrayWish;

    public WishAdapter(Context context, int layout, List<WishRes> arrayWish) {
        this.context = context;
        this.layout = layout;
        this.arrayWish = arrayWish;
    }

    @Override
    public int getCount() {
        return arrayWish.size();
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
        TextView tv_wishName;
        TextView tv_wishAddress;
        TextView tv_wishProvince;
        TextView tv_wishDesciption;
        RatingBar ratingBar_wishRating;
        ImageView iv_wishImage;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);

            holder.tv_wishName = convertView.findViewById(R.id.tv_wishName);
            holder.tv_wishAddress = convertView.findViewById(R.id.tv_wishAddress);
            holder.tv_wishProvince = convertView.findViewById(R.id.tv_wishProvince);
            holder.tv_wishDesciption = convertView.findViewById(R.id.tv_wishDescription);
            holder.ratingBar_wishRating = convertView.findViewById(R.id.ratingBar_wishRating);
            holder.iv_wishImage = convertView.findViewById(R.id.iv_wishImage);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        WishRes wish = arrayWish.get(position);
        holder.tv_wishName.setText(wish.getName());
        holder.tv_wishAddress.setText(wish.getAddress());
        holder.tv_wishProvince.setText(wish.getProvince());
        holder.tv_wishDesciption.setText(wish.getDes());
        holder.ratingBar_wishRating.setRating(wish.getRate());

        byte[] img = wish.getImg();
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);
        holder.iv_wishImage.setImageBitmap(bitmap);

        return convertView;
    }
}
