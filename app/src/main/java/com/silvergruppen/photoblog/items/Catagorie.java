package com.silvergruppen.photoblog.items;

import com.silvergruppen.photoblog.holders.CatagoriesListViewHolder;

public class Catagorie extends RecycleListItem {

    CatagoriesListViewHolder holder;

    public Catagorie(String name){
        super(name);


    }

    public CatagoriesListViewHolder getHolder() {
        return holder;
    }

    public void setHolder(CatagoriesListViewHolder holder) {
        this.holder = holder;
    }

}
