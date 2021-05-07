package com.silvia.sipulaintern.activity.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.sipulaintern.R;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;
import com.silvia.sipulaintern.activity.Model.ModelDetailAdmin;

import java.util.List;

public class Adapter_Detail_Admin extends RecyclerView.Adapter<Adapter_Detail_Admin.ViewHolder> {

    Context context;
    List<ModelDetailAdmin> dataAdmin;

    public Adapter_Detail_Admin(List<ModelDetailAdmin> dataAdmin) {
        this.dataAdmin = dataAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_detail_admin,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter_Detail_Admin.ViewHolder holder, int position) {
        ModelDetailAdmin data = dataAdmin.get(position);
        holder.nama.setText(data.getNama_pemohon());
        holder.instrumen.setText(data.getUraian());


    }

    @Override
    public int getItemCount() {
        return dataAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,instrumen;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.txtNamaPengajuan);
            instrumen = itemView.findViewById(R.id.txtInstument);
                    }
    }
}
