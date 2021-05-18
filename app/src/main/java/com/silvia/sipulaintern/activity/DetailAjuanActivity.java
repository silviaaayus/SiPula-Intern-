package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.silvia.sipulaintern.activity.Adapter.AdapterAdmin;
import com.silvia.sipulaintern.activity.Adapter.Adapter_Detail_Admin;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;
import com.silvia.sipulaintern.activity.Model.ModelDetailAdmin;
import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.activity.util.TinyDB;
import com.silvia.sipulaintern.databinding.ActivityDetailAjuanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailAjuanActivity extends AppCompatActivity {
    private ActivityDetailAjuanBinding binding;
    String no_reg,file,level,id_layanan;
    List<ModelDetailAdmin> dataAdmin;

    ArrayList<String> dataTeknisi = new ArrayList<>();
    ArrayAdapter<String> adapter;
    int a;

    ApiServer api;
    TinyDB tinyDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailAjuanBinding.inflate(getLayoutInflater());
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
        file = i.getStringExtra("file_pemohon");
        id_layanan = i.getStringExtra("id_layanan");

        binding.btnPdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailAjuanActivity.this, PdfActivity.class);
                intent.putExtra("PDF", file);
                intent.putExtra("nama", "Document");
                startActivity(intent);
            }
        });

        tinyDB = new TinyDB(this);
        level =  tinyDB.getString("keyLevel");

        if (level.equalsIgnoreCase("Kasi")){
            binding.spinteknisi.teknisi.setVisibility(View.VISIBLE);
        }else {
            binding.spinteknisi.teknisi.setVisibility(View.GONE);
        }



        binding.txtNamaPengaju.setText(i.getStringExtra("nama_pemohon"));
        binding.txtInstument.setText(i.getStringExtra("nama_layanan"));
        binding.txtStatusPengajuan.setText(i.getStringExtra("status_pemohon"));

       getDataTeknisi();



        binding.btnSetujui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.equalsIgnoreCase("Admin")) {
                    selesai();
                } else if (level.equalsIgnoreCase("Pimpinan")){
                    selesaiPimpinan();
                } else if (level.equalsIgnoreCase("Kasi")){
                    selesaiKasi();
                }else if (level.equalsIgnoreCase("Penyelia")){
                    selesaiPenyelia();
                }else if(level.equalsIgnoreCase("Teknisi")){
                    selesaiTeknisi();
                }


            }
        });

        binding.rvBerita.setHasFixedSize(true);
        binding.rvBerita.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        dataAdmin = new ArrayList<>();
        getDetail();



    }

    private void selesaiTeknisi() {
        AndroidNetworking.post(api.URL_SAVE_TEKNISI)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getDetail(){
        Log.d("api",api.URL_DETAIL_ADMIN+no_reg);
        AndroidNetworking.get(api.URL_DETAIL_ADMIN+no_reg)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {

                                JSONArray res = response.getJSONArray("res");
                                Gson gson = new Gson();
                                dataAdmin.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelDetailAdmin Isi = gson.fromJson(data + "", ModelDetailAdmin.class);
                                    dataAdmin.add(Isi);
                                }
                                Adapter_Detail_Admin adapter = new Adapter_Detail_Admin(dataAdmin);
                                binding.rvBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {

                                Toast.makeText(DetailAjuanActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });

    }

    private void selesai() {

        Log.d("URL", "selesai  : "+api.URL_SAVE_ADMIN);
        AndroidNetworking.post(api.URL_SAVE_ADMIN)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selesaiPimpinan() {

        AndroidNetworking.post(api.URL_SAVE_PIMPINAN)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getDataTeknisi(){
        AndroidNetworking.get(api.URL_SPINTEKNISI+id_layanan)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            Log.d("tampilmenu","response:"+response);
                            JSONArray res = response.getJSONArray("res");
                            for(int i =0; i <res.length();i++){
                                JSONObject data = res.getJSONObject(i);
                                    String nama = data.getString("nama_teknisi");
                                dataTeknisi.add(nama);

                                int id = data.getInt("id_teknisi");

//                                        String gambar =api.URL_GAMBAR+data.getString("gambar_");

//                                        Item[i] = nama;
                            }
                            setDataSpinner();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu","response:"+anError);
                    }
                });
    }

    private void setDataSpinner (){
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dataTeknisi);

        //Memasukan Adapter pada Spinner
        binding.spinteknisi.spinnerTeknisi.setAdapter(adapter);

        //Mengeset listener untuk mengetahui event/aksi saat item dipilih
        binding.spinteknisi.spinnerTeknisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                a  = (int) adapter.getItemId(i)+1;

            }

            @Override
            public void onNothingSelected(AdapterView adapterView) {

            }
        });
    }

    private void selesaiKasi() {

        AndroidNetworking.post(api.URL_SAVE_KASI)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .addBodyParameter("id_teknisi", ""+a)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selesaiPenyelia() {

        AndroidNetworking.post(api.URL_SAVE_KASI)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .addBodyParameter("id_teknisi", ""+a)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")){
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            }else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : "+ anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}