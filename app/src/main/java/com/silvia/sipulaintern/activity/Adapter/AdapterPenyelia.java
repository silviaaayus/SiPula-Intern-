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
import com.silvia.sipulaintern.activity.DetailPenyeliaActivity;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;
import com.silvia.sipulaintern.activity.Model.ModelPenyelia;
import com.silvia.sipulaintern.activity.util.TinyDB;

import java.util.List;

public class AdapterPenyelia extends RecyclerView.Adapter<AdapterPenyelia.ViewHolder> {

    Context context;
    List<ModelPenyelia> dataPenyelia;

    public AdapterPenyelia(List<ModelPenyelia> dataPenyelia) {
        this.dataPenyelia = dataPenyelia;
    }

    @NonNull
    @Override
    public AdapterPenyelia.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ajuan,parent,false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterPenyelia.ViewHolder holder, int position) {
        TinyDB tinyDB = new TinyDB(context);
        String level = tinyDB.getString("keyLevel");

        ModelPenyelia data = dataPenyelia.get(position);
        holder.nama.setText(data.getNama_pemohon());
        holder.instrumen.setText(data.getNama_layanan());
        holder.status.setText(data.getStatus_laporan());


        String date = data.getTgl_pengajuan();

        String[] kal = date.split("-");
        String[] day = kal[2].split(" ");
        String mounth = kal[1];
        String year = kal[0];
        holder.tgl.setText(day[0]+"-"+mounth+"-"+year);

        if (level.equalsIgnoreCase("Penyelia")){
            if (data.getStatus_laporan().equalsIgnoreCase("Waiting List Penyelia")){

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, DetailPenyeliaActivity.class);
                        i.putExtra("nama_pemohon",data.getNama_pemohon());
                        i.putExtra("tgl_pemohon",data.getTgl_pengajuan());
                        i.putExtra("nama_layanan",data.getNama_layanan());
                        i.putExtra("no_registrasi",data.getNo_registrasi());
                        i.putExtra("hasil_laporan", data.getHasil_laporan());
                        i.putExtra("status_laporan",data.getStatus_laporan());
                        i.putExtra("komentar_laporan",data.getKomentar_laporan());
                        i.putExtra("id_layanan",data.getId_layanan());

                        context.startActivity(i);

                    }
                });
            } else{
                holder.itemView.setEnabled(false);
            }
        }




    }

    @Override
    public int getItemCount() {
        return dataPenyelia.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nama,instrumen, tgl, status;
        ImageView imgnext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            context = itemView.getContext();
            nama = itemView.findViewById(R.id.txtNamaPengajuan);
            instrumen = itemView.findViewById(R.id.txtInstument);
            tgl = itemView.findViewById(R.id.tglPengajuan);
            imgnext = itemView.findViewById(R.id.next);
            status = itemView.findViewById(R.id.statusPengajuan);
        }
    }
}
