package com.silvergruppen.photoblog.adapters;


import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.holders.PostListViewHolder;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.DispayUserProfileActivity;
import com.silvergruppen.photoblog.items.PostItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostListAdapter extends BaseAdapter {
    private ArrayList<PostItem> postItems = new ArrayList<PostItem>();
    private LayoutInflater mInflater;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private static final int TYPE_WITH_IMAGE = 0;
    private static final int TYPE_WITHOUT_IMAGE = 1;
    private static final int TYPE_MAX_COUNT = 2;

    private HashMap<Integer, Integer> hashmap = new HashMap<Integer, Integer>();

    public PostListAdapter(Context context, ArrayList<PostItem> postItems) {
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       clonePostList(postItems);
        this.context = context;

    }

    public void clonePostList(ArrayList<PostItem> postItems){

        this.postItems.clear();

        for(PostItem tmpPostItem : postItems)
            addPostItem(tmpPostItem);

    }



    public void addPostItem(final PostItem item) {
        postItems.add(item);

        if(item.getImage_url() != null)
            hashmap.put(postItems.size()-1, TYPE_WITH_IMAGE);
        else {
            hashmap.put(postItems.size() - 1, TYPE_WITHOUT_IMAGE);
            Log.d("\n \n \n \n", "yess");
        }

        notifyDataSetChanged();
    }
    @Override
    public int getItemViewType(int position) {

        return hashmap.get(position);
    }





    @Override
    public Object getItem(int i) {
        return postItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getCount() {
        return postItems.size();
    }

    @Override
    public int getViewTypeCount() {
        return TYPE_MAX_COUNT;
    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {
        PostListViewHolder holder = null;
         final PostItem postItem = postItems.get(position);
        int type = getItemViewType(position);
        if (convertView == null) {

            switch (type) {
                case TYPE_WITH_IMAGE:
                    convertView = mInflater.inflate(R.layout.post_list_item_with_image, null);
                    break;
                case TYPE_WITHOUT_IMAGE:
                    convertView = mInflater.inflate(R.layout.post_list_item_without_image,null);
                     break;
            }


            TextView usernameText = (TextView) convertView.findViewById(R.id.post_item_username);

            TextView dateText = (TextView) convertView.findViewById(R.id.post_item_date);

            //ImageView image = (ImageView) convertView.findViewById(R.id.post_item_image);

            CircleImageView profileImage = (CircleImageView) convertView.findViewById(R.id.post_item_user_image);

            TextView desc = (TextView) convertView.findViewById(R.id.post_item_desc);

            TextView headline = (TextView) convertView.findViewById(R.id.post_item_headline);

            //LinearLayout rootLayout = (LinearLayout) convertView.findViewById(R.id.post_item_root_layout);

            ImageView image = null;

            if(type == TYPE_WITH_IMAGE)
                image = (ImageView) convertView.findViewById(R.id.post_item_image);





            usernameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent displayAccountIntent = new Intent(context, DispayUserProfileActivity.class);
                    displayAccountIntent.putExtra("user_id",postItem.getUser_id());
                    context.startActivity(displayAccountIntent);
                }
            });


            holder = new PostListViewHolder( usernameText,dateText,profileImage, desc, headline, image);



        } else
            holder = (PostListViewHolder) convertView.getTag();

        // set the date text
        Long milliseconds = postItem.getTimeStamp().getTime();
        String dateString = DateFormat.format("MM/dd/yyyy", new Date(milliseconds)).toString();
        holder.getDateText().setText(dateString);

        // if image exist add image

        if(type == TYPE_WITH_IMAGE) {

            Glide.with(context).load(postItem.getImage_url()).into(holder.getImage());
        }

       // Log.d("glide"+postItem.getTimeStamp().toString()+"\n "+ postItem.getImage_url(),"\n \n \n \n");
//        Glide.with(context).load(postItem.getImage_url()).into(holder.getImage());
        // set the user image
        Glide.with(context).load(postItem.getUser_image_url()).into(holder.getUserImage());
        // set the username
        holder.getUsernameText().setText(postItem.getUserName());
        // set the description
        holder.getDescTextView().setText(postItem.getDesc());
        // set the headline
        holder.getHeadline().setText(postItem.getHeadline());




        convertView.setTag(holder);

        return convertView;
    }

    public void clearPostItems(){

        postItems.clear();
    }



}