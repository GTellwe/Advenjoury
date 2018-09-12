package com.silvergruppen.photoblog.listAdapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.listItems.Achievement;

import java.util.List;

public class AchievementRecyclerAdapter extends RecyclerView.Adapter<AchievementRecyclerAdapter.ViewHolder> {

    public List<Achievement> achievementList;
    public Context context;
    public AchievementRecyclerAdapter(List<Achievement> achievementList){

        this.achievementList = achievementList;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.achievement_list_item, viewGroup, false);

        context = viewGroup.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String headline = achievementList.get(i).getTopicName();
        viewHolder.setTopicHeadline(headline);

        String points = achievementList.get(i).getPoints();
        viewHolder.setProgressText(points);

    }

    @Override
    public int getItemCount() {
        return achievementList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;
        private TextView topicHeadline;
        private TextView topicProgress;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTopicHeadline(String headline){

            topicHeadline = mView.findViewById(R.id.topic_item_text);
            topicHeadline.setText(headline);

        }

        public void setProgressText(String numberOfItems){

            topicProgress = mView.findViewById(R.id.topic_item_progress);
            topicProgress.setText(numberOfItems);

        }
    }
}