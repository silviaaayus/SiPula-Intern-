package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.silvia.sipulaintern.R;
import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.activity.util.TinyDB;
import com.silvia.sipulaintern.databinding.ActivityDetailAjuanBinding;
import com.silvia.sipulaintern.databinding.ActivityDetailPenyeliaBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailPenyeliaActivity extends AppCompatActivity {
    private ActivityDetailPenyeliaBinding binding;

    ApiServer api;
    TinyDB tinyDB;

    String no_reg,laporan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailPenyeliaBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        api = new ApiServer();

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = new Intent(getIntent());
        no_reg = i.getStringExtra("no_registrasi");
        laporan = i.getStringExtra("hasil_laporan");

        binding.txtNamaPengaju.setText(i.getStringExtra("nama_pemohon"));
        binding.txtInstument.setText(i.getStringExtra("nama_layanan"));
        binding.txtStatusPengajuan.setText(i.getStringExtra("status_laporan"));
        binding.txtKeterangan.setText(i.getStringExtra("komentar_laporan"));
        binding.txtNoReg.setText(no_reg);



        binding.btnPdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(DetailPenyeliaActivity.this, PdfActivity.class);
                intent.putExtra("PDF", laporan);
                intent.putExtra("nama", "Document");
                startActivity(intent);
            }


        });
        binding.btnSetujui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selesaiPenyelia();
            }
        });

    }

    private void selesaiPenyelia() {

        AndroidNetworking.post(api.URL_SAVE_PENYELIA)
                .addBodyParameter("no_registrasi", no_reg)
                .addBodyParameter("komentar_laporan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailPenyeliaActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailPenyeliaActivity.this, PenyeliaActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailPenyeliaActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailPenyeliaActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}