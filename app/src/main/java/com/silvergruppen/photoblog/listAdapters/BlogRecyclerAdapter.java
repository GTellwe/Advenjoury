package com.silvergruppen.photoblog.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.listItems.PostItem;


import java.util.Date;
import java.util.List;

public class BlogRecyclerAdapter extends RecyclerView.Adapter<BlogRecyclerAdapter.ViewHolder> {

    public List<PostItem> blogList;
    public Context context;

    public BlogRecyclerAdapter(List<PostItem> blogList){

        this.blogList = blogList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.post_list_item, viewGroup, false);

        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String desc_data = blogList.get(i).getDesc();
        viewHolder.setDescText(desc_data);

        String image_url = blogList.get(i).getImage_url();

        viewHolder.setBlogImage(image_url);

        if(blogList.get(i).getTimeStamp() != null) {

            Long milliseconds = blogList.get(i).getTimeStamp().getTime();
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(milliseconds)).toString();
            viewHolder.setDate(dateString);

        }else {

            viewHolder.setDate(DateFormat.format("MM/dd/yyyy", new Date(0)).toString());

        }

    }

    @Override
    public int getItemCount() {
        return blogList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView descView;
        private ImageView blogImageView;
        private TextView blogDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDescText(String descText){

                descView = mView.findViewById(R.id.post_item_username);
                descView.setText(descText);

        }

        public void setBlogImage(String downloadUri){

            blogImageView = mView.findViewById(R.id.post_item_image);
            Glide.with(context).load(downloadUri).into(blogImageView);
        }

        public void setDate(String date){

            blogDate = mView.findViewById(R.id.post_item_date);
            blogDate.setText(date);

        }
    }
}
