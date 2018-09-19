package com.silvergruppen.photoblog.listAdapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.silvergruppen.photoblog.ListViewHolders.AchievementListViewHolder;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.activities.NewPostActivity;
import com.silvergruppen.photoblog.animations.ResizeAnimation;
import com.silvergruppen.photoblog.listItems.Achievement;
import com.silvergruppen.photoblog.listItems.AchievementPost;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;

public class AchievementListAdapter extends ArrayAdapter<Achievement> {
    private ArrayList<Achievement> achievementItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private FloatingActionButton addPostBtn;

    public AchievementListAdapter(Context context, int textViewResourceId,
                                  ArrayList<Achievement> achievementItems) {
        super(context, textViewResourceId, achievementItems);
        this.achievementItems = achievementItems;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();

    }

    @Override
    @SuppressWarnings("deprecation")
    public View getView(final int position, View convertView, ViewGroup parent) {
        AchievementListViewHolder holder = null;
        final Achievement achievementItem = achievementItems.get(position);

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.achievement_list_item, null);

            LinearLayout textViewWrap = (LinearLayout) convertView
                    .findViewById(R.id.text_wrap);
            TextView text = (TextView) convertView.findViewById(R.id.text);

            LinearLayout journalItems = convertView.findViewById(R.id.journal_items);

            addPostBtn = convertView.findViewById(R.id.achievement_item_add_post);

            holder = new AchievementListViewHolder(textViewWrap, text,addPostBtn,journalItems, context);

            // set listener to the list items
            convertView.setClickable(true);
            convertView.setFocusable(true);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    toggle(view, position, achievementItem);
                    MainActivity tmpMainActivity = (MainActivity) getContext();
                    tmpMainActivity.setCurrentAchievement(achievementItem.getName());
                }
            });
        } else
            holder = (AchievementListViewHolder) convertView.getTag();

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                achievementItem.getCurrentHeight());
        holder.getTextViewWrap().setLayoutParams(layoutParams);
        holder.getTextView().setText(achievementItem.getName());

        if( achievementItem.getBlogPostList() != null && holder.getJournalItems().getChildCount() != achievementItem.getBlogPostList().size()){
            holder.getJournalItems().removeAllViews();
            for(int j =0; j<achievementItem.getBlogPostList().size();j++) {

                AchievementPost tmpPost = achievementItem.getBlogPostList().get(j);

                /**
                 * add image view to linear layout
                 */

                ImageView tmpImageView = new ImageView(context);
                android.widget.LinearLayout.LayoutParams params =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1000);
                params.setMargins(0,50,0,50);
                tmpImageView.setLayoutParams(params);
                tmpImageView.setAdjustViewBounds(true);
                tmpImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                holder.getJournalItems().addView(tmpImageView);


                /**
                 * add image to image view
                 */

                RequestOptions placeholderRequest = new RequestOptions();
                placeholderRequest.placeholder(R.drawable.profile_icon);
                Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(tmpPost.getImage()).into(tmpImageView);
                tmpPost.setImageView(tmpImageView);
                achievementItem.setPostHashMap(tmpPost.getTimestamp(),tmpImageView);

                //add onclick listener to the image for removal of a post
                tmpImageView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(final View view) {
                        firebaseFirestore.collection(user_id+"/"+achievementItem.getTopic()+"/"+achievementItem.getName())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Map<String,Object> tmpMap = document.getData();
                                                if(tmpMap.get("timestamp").equals(achievementItem.getBlogListViewHasMap().get(view))) {
                                                    // Delete document from firebase
                                                    firebaseFirestore.collection(user_id+"/"+achievementItem.getTopic()+"/"+achievementItem.getName())
                                                            .document(document.getId())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(context,"Removing post",Toast.LENGTH_LONG).show();
                                                                    notifyDataSetChanged();
                                                                }
                                                            });

                                                }
                                            }
                                        }
                                    }
                                });



                        return true;
                    }
                });


                /**
                 * add text to the image
                 */

                TextView tmpTextView = new TextView(context);
                tmpTextView.setText(tmpPost.getText());

                android.widget.LinearLayout.LayoutParams paramsTextView =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                tmpTextView.setLayoutParams(paramsTextView);
                tmpTextView.setGravity(Gravity.CENTER);
                holder.getJournalItems().addView(tmpTextView);


            }
        }



        convertView.setTag(holder);

        achievementItem.setHolder(holder);

        return convertView;
    }

    private void toggle(View view, final int position, Achievement achievementItem) {
        achievementItem.getHolder().setTextViewWrap((LinearLayout) view);

        int fromHeight = 0;
        int toHeight = 0;

        if (achievementItem.isOpen()) {

            fromHeight = achievementItem.getExpandedHeight();
            toHeight = achievementItem.getCollapsedHeight();
        } else {
            fromHeight = achievementItem.getCollapsedHeight();
            toHeight = achievementItem.getExpandedHeight();

            // This closes all item before the selected one opens
            closeAll();
        }

        toggleAnimation(achievementItem, position, fromHeight, toHeight, true);
    }
    private void closeAll() {
        int i = 0;
        for (Achievement achievementItem : achievementItems) {
            if (achievementItem.isOpen()) {
                toggleAnimation(achievementItem, i, achievementItem.getExpandedHeight(),
                        achievementItem.getCollapsedHeight(), false);
            }
            i++;
        }
    }

    private void toggleAnimation(final Achievement listItem, final int position,
                                 final int fromHeight, final int toHeight, final boolean goToItem) {

        ResizeAnimation resizeAnimation = new ResizeAnimation(this, listItem, 0, fromHeight, 0, toHeight);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listItem.setOpen(!listItem.isOpen());
                listItem.setCurrentHeight(toHeight);
                if(listItem.isOpen())

                    if(goToItem) {

                        //listItem.setCheckBoxStatus(VISIBLE);
                        //listItem.setJournalButtonStatus(VISIBLE);
                    }
                    else {
                        //listItem.setCheckBoxStatus(INVISIBLE);
                        //listItem.setJournalButtonStatus(INVISIBLE);
                    }
                notifyDataSetChanged();
                /*
                if (goToItem)

                    goToItem(position);
            */
            }
        });

        listItem.getHolder().getTextViewWrap().startAnimation(resizeAnimation);
    }


}