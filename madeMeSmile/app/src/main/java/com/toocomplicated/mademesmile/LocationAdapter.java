package com.toocomplicated.mademesmile;

import android.content.ComponentName;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Win8.1 on 31/10/2558.
 */
public class LocationAdapter extends RecyclerView.Adapter<LocateViewHolder> {
    private List<Location> locateItemList;
    private Context mContext;

    public LocationAdapter(Context context,List<Location> locateItemList){
        this.locateItemList = locateItemList;
        this.mContext = context;
    }
    @Override
    public LocateViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listlocation, null);

        LocateViewHolder locateHolder = new LocateViewHolder(view);
        return  locateHolder;
    }

    @Override
    public void onBindViewHolder(final LocateViewHolder holder, final int position) {
        final Location locateItem = locateItemList.get(position);

        holder.txtName.setText(locateItem.getName());
        holder.txtAddress.setText(locateItem.getAddress());
    }

    @Override
    public int getItemCount() {
        return (null != locateItemList ? locateItemList.size() : 0);
    }
}
