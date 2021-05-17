package com.silvia.sipulaintern.activity.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.sipulaintern.activity.Model.ModelTeknisi;

import java.util.List;

public class AdapterTeknisi extends RecyclerView.Adapter<AdapterTeknisi.ViewHolder> {
    Context context;
    List<ModelTeknisi> dataTeknisi;

    public AdapterTeknisi(List<ModelTeknisi> dataTeknisi) {
        this.dataTeknisi = dataTeknisi;
    }

    @NonNull
    @Override
    public AdapterTeknisi.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterTeknisi.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataTeknisi.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
