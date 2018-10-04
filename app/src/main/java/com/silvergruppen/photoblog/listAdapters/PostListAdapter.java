package com.silvergruppen.photoblog.listAdapters;


import android.content.Context;
import android.media.Image;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.ListViewHolders.PostListViewHolder;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.listItems.PostItem;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListAdapter extends ArrayAdapter<PostItem> {
    private ArrayList<PostItem> postItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    public PostListAdapter(Context context, int textViewResourceId,
                                  ArrayList<PostItem> postItems) {
        super(context, textViewResourceId, postItems);
        this.postItems = postItems;
        this.context = context;


    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostListViewHolder holder = null;
        final PostItem postItem = postItems.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.post_list_item, null);

            LinearLayout textViewWrap = (LinearLayout) convertView
                    .findViewById(R.id.text_wrap);

            TextView usernameText = (TextView) convertView.findViewById(R.id.post_item_username);

            TextView dateText = (TextView) convertView.findViewById(R.id.post_item_date);

            ImageView image = (ImageView) convertView.findViewById(R.id.post_item_image);

            CircleImageView profileImage = (CircleImageView) convertView.findViewById(R.id.post_item_user_image);

            TextView desc = (TextView) convertView.findViewById(R.id.post_item_desc);


            holder = new PostListViewHolder(textViewWrap, usernameText,dateText,profileImage, image, desc);

            // set listener to the list items
            convertView.setClickable(true);
            convertView.setFocusable(true);

        } else
            holder = (PostListViewHolder) convertView.getTag();

        // set the date text
        Long milliseconds = postItem.getTimeStamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(milliseconds)).toString();
        holder.getDateText().setText(dateString);

        // set the image
        Glide.with(context).load(postItem.getImage_url()).into(holder.getImage());

        // set the user image
        Glide.with(context).load(postItem.getUser_image_url()).into(holder.getUserImage());
        // set the username
        holder.getUsernameText().setText(postItem.getUser_id());
        // set the description
        holder.getDescTextView().setText(postItem.getDesc());




        convertView.setTag(holder);

        return convertView;
    }



}