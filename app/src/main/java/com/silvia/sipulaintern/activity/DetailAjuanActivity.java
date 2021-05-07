package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.silvia.sipulaintern.databinding.ActivityDetailAjuanBinding;

public class DetailAjuanActivity extends AppCompatActivity {
    private ActivityDetailAjuanBinding binding;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailAjuanBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent i = new Intent(getIntent());
        binding.txtNamaPengaju.setText(i.getStringExtra("nama_pemohon"));

        binding.txtInstument.setText(i.getStringExtra("nama_layanan"));

        date = i.getStringExtra("tgl_pemohon");
        Log.e("tgl",date);


        String[] kal = date.split("-");
        String[] day = kal[2].split(" ");
        String mounth = kal[1];
        String year = kal[0];
        binding.tglPengajuan.setText(day[0]+"-"+mounth+"-"+year);




    }
}