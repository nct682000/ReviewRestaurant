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

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<Comment> commentList;

    public CommentAdapter(Context context, int layout, List<Comment> commentList) {
        this.context = context;
        this.layout = layout;
        this.commentList = commentList;
    }


    @Override
    public int getCount() {
        return commentList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(layout, null);

        TextView tv_name = convertView.findViewById(R.id.tv_comName);
        TextView tv_text = convertView.findViewById(R.id.tv_comText);
        ImageView iv_image = convertView.findViewById(R.id.iv_comImage);

        Comment comment = commentList.get(position);

        tv_name.setText(comment.getName());
        tv_text.setText(comment.getText());

//        byte[] image = comment.getImage();
//        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
//        iv_image.setImageBitmap(bitmap);

        return convertView;
    }
}
