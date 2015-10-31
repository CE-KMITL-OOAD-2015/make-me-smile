package com.toocomplicated.mademesmile;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Win8.1 on 31/10/2558.
 */
public class LocateViewHolder extends RecyclerView.ViewHolder {
    protected TextView txtName;
    protected TextView txtAddress;
    public LocateViewHolder(View itemView) {
        super(itemView);
        this.txtName = (TextView)itemView.findViewById(R.id.locationname);
        this.txtAddress = (TextView)itemView.findViewById(R.id.locationaddress);
    }
}
