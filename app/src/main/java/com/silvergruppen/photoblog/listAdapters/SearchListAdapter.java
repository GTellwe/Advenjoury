package com.silvergruppen.photoblog.listAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.silvergruppen.photoblog.R;
import com.silvergruppen.photoblog.activities.DispayUserProfileActivity;
import com.silvergruppen.photoblog.activities.SearchActivity;
import com.silvergruppen.photoblog.listItems.SearchItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchListAdapter extends BaseAdapter {

    // Declare Variables
    Context mContext;
    LayoutInflater inflater;
    private List<SearchItem> searchList = null;
    private ArrayList<SearchItem> arraylist;

    public SearchListAdapter(Context context,
                           List<SearchItem> searchList) {
        mContext = context;
        this.searchList = searchList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<SearchItem>();
        this.arraylist.addAll(searchList);
    }

    public class ViewHolder {
        TextView name;
        CircleImageView image;
    }

    @Override
    public int getCount() {
        return searchList.size();
    }

    @Override
    public SearchItem getItem(int position) {
        return searchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.search_list_item, null);
            // Locate the TextViews in listview_item.xml
            holder.name = (TextView) view.findViewById(R.id.search_list_item_name);
            holder.image = (CircleImageView) view.findViewById(R.id.search_list_Item_image);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Set the results into TextViews
        holder.name.setText(searchList.get(position).getName());
        // Set the results into ImageView
        String tmpImage = searchList.get(position).getImage();
        if(tmpImage != null){

            RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.profile_icon);
            Glide.with(mContext).setDefaultRequestOptions(placeholderRequest).load(tmpImage).into(holder.image);

        }

        // Listen for ListView Item Click
        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent displayAccountIntent = new Intent(mContext, DispayUserProfileActivity.class);
                displayAccountIntent.putExtra("user_id", searchList.get(position).getUser_id());
                mContext.startActivity(displayAccountIntent);

            }
        });

        return view;
    }

    public void setArraylist(ArrayList<SearchItem> arraylist){

        this.arraylist.addAll(arraylist);

    }

    // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        searchList.clear();
        if (charText.length() == 0) {
            searchList.addAll(arraylist);
        } else {
            for (SearchItem wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    searchList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}