package com.silvergruppen.photoblog.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.holders.AchievementListViewHolder;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.items.Achievement;

import java.util.ArrayList;

public class AchievementListAdapter extends ArrayAdapter<Achievement> {
    private ArrayList<Achievement> achievementItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private String topic;
    private boolean editable;
    private MainActivity mainActivity;

    public AchievementListAdapter(Context context, int textViewResourceId,
                                  ArrayList<Achievement> achievementItems, String topic, boolean editable) {
        super(context, textViewResourceId, achievementItems);
        this.achievementItems = achievementItems;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        this.topic = topic;
        this.editable = editable;
        if(context.getClass() == MainActivity.class)
            mainActivity = (MainActivity) context;


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
/*
            LinearLayout textViewWrap = (LinearLayout) convertView
                    .findViewById(R.id.text_wrap);
            TextView text = (TextView) convertView.findViewById(R.id.text);

            LinearLayout journalItems = convertView.findViewById(R.id.journal_items);

            FloatingActionButton addPostBtn = convertView.findViewById(R.id.achievement_item_add_post);

            Button markAsDone = convertView.findViewById(R.id.achievement_list_item_done_button);

            ImageView checkImage = convertView.findViewById(R.id.image_view_check_mark);

            TextView pointsText = convertView.findViewById(R.id.pointsText);
*/       // if the list is not editable then hide the add and remove post buttons
/*
           if(!editable){

                markAsDone.setVisibility(View.INVISIBLE);
                addPostBtn.setVisibility(View.INVISIBLE);
            }

            addPostBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent newPostIntent = new Intent(context, NewPostActivity.class);
                    Bundle b = new Bundle();
                    MainActivity tmpActivity = (MainActivity) context;
                    b.putString("topic",tmpActivity.getTopic());
                    b.putString("achievement", tmpActivity.getCurrentAchievement());
                    newPostIntent.putExtras(b);
                    context.startActivity(newPostIntent);
                }
            });

            markAsDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    firebaseAuth = FirebaseAuth.getInstance();
                    final String currentUserID= firebaseAuth.getCurrentUser().getUid();
                    firebaseFirestore = FirebaseFirestore.getInstance();
                    // if the achievement is not done by the current user then add the user name to the achievments list of
                    // users otherwise remove it
                    if(!achievementItem.isDone()){

                        final HashMap<String, Object> tmpHashMap = new HashMap<>();
                        tmpHashMap.put("timestamp", FieldValue.serverTimestamp());
                        firebaseFirestore.collection("Achievements/"+achievementItem.getName()+"/Users").document(currentUserID).set(tmpHashMap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // add to Users/achievments as well
                                        firebaseFirestore.collection("Users/"+currentUserID+"/Achievements").document(achievementItem.getName()).set(tmpHashMap)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {

                                                        Toast.makeText(context,"Achievement marked as done",Toast.LENGTH_LONG).show();
                                                        achievementItem.setDone(true);
                                                    }
                                                });
                                    }
                                });

                    } else {

                        firebaseFirestore.collection("Achievements/"+achievementItem.getName()+"/Users").document(currentUserID).delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        firebaseFirestore.collection("Users/"+currentUserID+"/Achievements").document(achievementItem.getName()).delete()
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Toast.makeText(context,"Achievement marked as not done",Toast.LENGTH_LONG).show();
                                                        achievementItem.setDone(false);
                                                    }
                                                });

                                    }
                                });


                    }

                }
            });
*/
            holder = new AchievementListViewHolder( context);
/*
            // set listener to the list items
            convertView.setClickable(true);
            convertView.setFocusable(true);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    toggle(view, position, achievementItem);
                    MainActivity tmpMainActivity;
                    if(getContext().getClass() == MainActivity.class) {

                        tmpMainActivity = (MainActivity) getContext();
                        tmpMainActivity.setCurrentAchievement(achievementItem.getName());

                    }
                }
            });
            */
        } else
            holder = (AchievementListViewHolder) convertView.getTag();

        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(AbsListView.LayoutParams.FILL_PARENT,
                achievementItem.getCurrentHeight());
        /*holder.getTextViewWrap().setLayoutParams(layoutParams);
        holder.getTextView().setText(achievementItem.getName());
        holder.getPointsText().setText(achievementItem.getPoints());
        // check if the checkmark should be present
        if(achievementItem.isDone())
            holder.setChecImageVisibility(View.VISIBLE);
        else
            holder.setChecImageVisibility(View.INVISIBLE);
*/
        // check if there is missing any lisitem in the achievement list and add if ther is
/*
        if( achievementItem.getBlogPostList() != null && holder.getJournalItems().getChildCount() != achievementItem.getBlogPostList().size()){
            holder.getJournalItems().removeAllViews();
            achievementItem.setExpandedHeight(1100);
            for(int j =0; j<achievementItem.getBlogPostList().size();j++) {

                AchievementPost tmpPost = achievementItem.getBlogPostList().get(j);

                //add a text view with a time stamp to the linear layout

                TextView tmpDateTextView = new TextView(context);
                Long milliseconds = tmpPost.getTimestamp().getTime();
                String dateString = DateFormat.format("MM/dd/yyyy HH:mm", new Date(milliseconds)).toString();
                tmpDateTextView.setText(dateString);

                android.widget.LinearLayout.LayoutParams paramsDateTextView =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsDateTextView.gravity = Gravity.LEFT;
                paramsDateTextView.setMargins(0, 50 , 0 ,0);
                tmpDateTextView.setLayoutParams(paramsDateTextView);
                tmpDateTextView.setGravity(Gravity.LEFT);

                holder.getJournalItems().addView(tmpDateTextView);



                // if image exists add image view to linear layout

                if(tmpPost.getImage() != null) {

                    ImageView tmpImageView = new ImageView(context);
                    android.widget.LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 1200);
                    params.setMargins(0, 50, 0, 50);
                    tmpImageView.setLayoutParams(params);
                    tmpImageView.setAdjustViewBounds(true);
                    tmpImageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    holder.getJournalItems().addView(tmpImageView);



                     // add image to image view


                    RequestOptions placeholderRequest = new RequestOptions();
                    placeholderRequest.placeholder(R.drawable.profile_icon);
                    Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(tmpPost.getImage()).into(tmpImageView);
                    tmpPost.setImageView(tmpImageView);




                }
                //add text to the image


                TextView tmpTextView = new TextView(context);
                tmpTextView.setText(tmpPost.getText());

                android.widget.LinearLayout.LayoutParams paramsTextView =new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                paramsTextView.gravity = Gravity.CENTER;
                tmpTextView.setLayoutParams(paramsTextView);
                tmpTextView.setGravity(Gravity.CENTER_HORIZONTAL);
                tmpTextView.setPadding(5,0,5,0);
                holder.getJournalItems().addView(tmpTextView);
                achievementItem.setPostHashMap(tmpPost.getTimestamp(), tmpTextView);

                //add onclick listener to the text for removal of a post
                tmpTextView.setOnLongClickListener(new View.OnLongClickListener() {



                    @Override
                    public boolean onLongClick(final View view) {
                        Toast.makeText(context, "here", Toast.LENGTH_LONG).show();
                        firebaseFirestore.collection(user_id)
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            for (DocumentSnapshot document : task.getResult()) {
                                                Map<String, Object> tmpMap = document.getData();
                                                if (tmpMap.get("timestamp").equals(achievementItem.getBlogListViewHasMap().get(view))) {
                                                    // Delete document from firebase
                                                    firebaseFirestore.collection(user_id)
                                                            .document(document.getId())
                                                            .delete()
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(context, "Removing post", Toast.LENGTH_LONG).show();
                                                                    //notifyDataSetChanged();

                                                                    mainActivity.loadAchievements();
                                                                    mainActivity.loadPostItems();

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

                // add the height to the echievement item
                if(tmpPost.getImage() != null)
                    achievementItem.setExpandedHeight(achievementItem.getExpandedHeight()+1200+(int)(tmpPost.getText().length()));
                else
                    achievementItem.setExpandedHeight(achievementItem.getExpandedHeight()+(int)(tmpPost.getText().length()));


            }
        }

*/

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

       /* ResizeAnimation resizeAnimation = new ResizeAnimation(this, listItem, 0, fromHeight, 0, toHeight);
        resizeAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listItem.setOpen(!listItem.isOpen(),editable);
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
       /*
            }
        });

        listItem.getHolder().getTextViewWrap().startAnimation(resizeAnimation);
    */
    }


}