package com.silvergruppen.photoblog.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.MainActivity;
import com.silvergruppen.photoblog.items.Achievement;
import com.silvergruppen.photoblog.items.Catagorie;
import com.silvergruppen.photoblog.items.RecycleListItem;

import java.util.ArrayList;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.MyViewHolder> {
    private ArrayList<? extends RecycleListItem> catagoriesItems;
    private ArrayList<Achievement> achievemetItems;
    private Context context;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;
    private String user_id;
    private MainActivity mainActivity;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public CardView parentLayout;
        public MyViewHolder(CardView v) {
            super(v);
            parentLayout = v;
        }
    }

    public RecycleViewAdapter(Context context,
                              ArrayList<? extends RecycleListItem> catagoriesItems) {
        this.catagoriesItems = catagoriesItems;
        this.context = context;
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
       if(context.getClass() == MainActivity.class)
            mainActivity = (MainActivity) context;


    }


    @Override
    public RecycleViewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {
        // create a new view
        CardView
                v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.catagorie_list_item, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        final RecycleListItem tmpItem = catagoriesItems.get(position);
        Achievement tmpAchievement = null;

        //holder.parentLayout.setTranslationZ(cardView.getElevation()+10*position);
        ConstraintLayout constraintLayout = (ConstraintLayout) holder.parentLayout.getChildAt(0);
        constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(tmpItem instanceof Catagorie) {
                    mainActivity.startAchievementFragment((Catagorie) tmpItem);
                    Toast.makeText(context,tmpItem.getName(), Toast.LENGTH_LONG).show();
                }else if (tmpItem instanceof Achievement){

                    mainActivity.startAchievementActivity((Achievement) tmpItem);
                }
            }
        });
        TextView textView = (TextView) constraintLayout.getChildAt(0);
        textView.setText(catagoriesItems.get(position).getName());

    }

    @Override
    public int getItemCount() {

        return catagoriesItems.size();

    }





}