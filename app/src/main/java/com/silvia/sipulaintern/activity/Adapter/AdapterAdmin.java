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
import com.silvia.sipulaintern.activity.util.TinyDB;

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
        TinyDB tinyDB = new TinyDB(context);
        String level = tinyDB.getString("keyLevel");

        ModelAdmin data = dataAdmin.get(position);
        holder.nama.setText(data.getNama_pemohon());
        holder.instrumen.setText(data.getNama_layanan());
        holder.status.setText(data.getStatus_pemohon());

        String date = data.getTgl_pengajuan();

        String[] kal = date.split("-");
        String[] day = kal[2].split(" ");
        String mounth = kal[1];
        String year = kal[0];
        holder.tgl.setText(day[0]+"-"+mounth+"-"+year);

        if (level.equalsIgnoreCase("Admin")) {
            if (data.getStatus_pemohon().equalsIgnoreCase("Pending")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, DetailAjuanActivity.class);
                        i.putExtra("nama_pemohon", data.getNama_pemohon());
                        i.putExtra("tgl_pemohon", data.getTgl_pengajuan());
                        i.putExtra("nama_layanan", data.getNama_layanan());
                        i.putExtra("no_registrasi", data.getNo_registrasi());
                        i.putExtra("pdf_file", data.getFile_pemohon());
                        i.putExtra("status_pemohon", data.getStatus_pemohon());
                        i.putExtra("file_pemohon", data.getFile_pemohon());
                        i.putExtra("id_layanan", data.getId_layanan());
                        i.putExtra("total_waktu", data.getTotal_waktu());
                        i.putExtra("total_biaya", data.getTotal_biaya());
                        i.putExtra("bukti_bayar", data.getBukti_pembayaran());
                        context.startActivity(i);

                    }
                });

            } else {
                holder.itemView.setEnabled(false);

            }
        }

        if (level.equalsIgnoreCase("Pimpinan")) {
            if (data.getStatus_pemohon().equalsIgnoreCase("Waiting List Pimpinan")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, DetailAjuanActivity.class);
                        i.putExtra("nama_pemohon", data.getNama_pemohon());
                        i.putExtra("tgl_pemohon", data.getTgl_pengajuan());
                        i.putExtra("nama_layanan", data.getNama_layanan());
                        i.putExtra("no_registrasi", data.getNo_registrasi());
                        i.putExtra("pdf_file", data.getFile_pemohon());
                        i.putExtra("status_pemohon", data.getStatus_pemohon());
                        i.putExtra("file_pemohon", data.getFile_pemohon());
                        i.putExtra("id_layanan", data.getId_layanan());
                        i.putExtra("total_waktu", data.getTotal_waktu());
                        i.putExtra("total_biaya", data.getTotal_biaya());
                        i.putExtra("bukti_bayar", data.getBukti_pembayaran());
                        context.startActivity(i);

                    }
                });

            } else {
                holder.itemView.setEnabled(false);

            }
        }

        if (level.equalsIgnoreCase("Kasi")) {
            if (data.getStatus_pemohon().equalsIgnoreCase("Waiting List Kasi")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, DetailAjuanActivity.class);
                        i.putExtra("nama_pemohon", data.getNama_pemohon());
                        i.putExtra("tgl_pemohon", data.getTgl_pengajuan());
                        i.putExtra("nama_layanan", data.getNama_layanan());
                        i.putExtra("no_registrasi", data.getNo_registrasi());
                        i.putExtra("pdf_file", data.getFile_pemohon());
                        i.putExtra("status_pemohon", data.getStatus_pemohon());
                        i.putExtra("file_pemohon", data.getFile_pemohon());
                        i.putExtra("id_layanan", data.getId_layanan());
                        i.putExtra("total_waktu", data.getTotal_waktu());
                        i.putExtra("total_biaya", data.getTotal_biaya());
                        i.putExtra("bukti_bayar", data.getBukti_pembayaran());
                        context.startActivity(i);

                    }
                });

            } else {
                holder.itemView.setEnabled(false);

            }
        }

        if (level.equalsIgnoreCase("Teknisi")) {
            if (data.getStatus_pemohon().equalsIgnoreCase("Waiting List Teknisi") || data.getStatus_pemohon().equalsIgnoreCase("Di Kerjakan")
                || data.getStatus_pemohon().equalsIgnoreCase("Di Pending")) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent i = new Intent(context, DetailAjuanActivity.class);
                        i.putExtra("nama_pemohon", data.getNama_pemohon());
                        i.putExtra("tgl_pemohon", data.getTgl_pengajuan());
                        i.putExtra("nama_layanan", data.getNama_layanan());
                        i.putExtra("no_registrasi", data.getNo_registrasi());
                        i.putExtra("pdf_file", data.getFile_pemohon());
                        i.putExtra("status_pemohon", data.getStatus_pemohon());
                        i.putExtra("file_pemohon", data.getFile_pemohon());
                        i.putExtra("id_layanan", data.getId_layanan());
                        i.putExtra("total_waktu", data.getTotal_waktu());
                        i.putExtra("total_biaya", data.getTotal_biaya());
                        i.putExtra("bukti_bayar", data.getBukti_pembayaran());
                        context.startActivity(i);

                    }
                });

            } else {
                holder.itemView.setEnabled(false);

            }
        }




    }

    @Override
    public int getItemCount() {
        return dataAdmin.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nama,instrumen, tgl, status;
        ImageView imgnext;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            nama = itemView.findViewById(R.id.txtNamaPengajuan);
            status = itemView.findViewById(R.id.statusPengajuan);
            instrumen = itemView.findViewById(R.id.txtInstument);
            tgl = itemView.findViewById(R.id.tglPengajuan);
            imgnext = itemView.findViewById(R.id.next);

        }
    }
}
