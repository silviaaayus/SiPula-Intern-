package com.silvia.sipulaintern.activity.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.silvia.sipulaintern.R;
import com.silvia.sipulaintern.activity.DetailAjuanActivity;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;

import java.util.List;

public class AdapterAdmin extends RecyclerView.Adapter<AdapterAdmin.ViewHolder> {

    Context context;
    List<ModelAdmin> dataAdmin;

    public AdapterAdmin(List<ModelAdmin> dataAdmin) {
        this.dataAdmin = dataAdmin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ajuan,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelAdmin data = dataAdmin.get(position);
        holder.nama.setText(data.getNama_pemohon());
        holder.instrumen.setText(data.getNama_layanan());


        String date = data.getTgl_pengajuan();

        String[] kal = date.split("-");
        String[] day = kal[2].split(" ");
        String mounth = kal[1];
        String year = kal[0];
        holder.tgl.setText(day[0]+"-"+mounth+"-"+year);

        holder.imgnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, DetailAjuanActivity.class);
                i.putExtra("nama_pemohon",data.getNama_pemohon());
                i.putExtra("tgl_pemohon",data.getTgl_pengajuan());
                i.putExtra("nama_layanan",data.getNama_layanan());
                i.putExtra("no_registrasi",data.getNo_registrasi());
                i.putExtra("status_pemohon",data.getStatus_pemohon());
                i.putExtra("file_pemohon",data.getFile_pemohon());
                context.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return dataAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,instrumen, tgl;
        ImageView imgnext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.txtNamaPengajuan);
            instrumen = itemView.findViewById(R.id.txtInstument);
            tgl = itemView.findViewById(R.id.tglPengajuan);
            imgnext = itemView.findViewById(R.id.next);

        }
    }
}
