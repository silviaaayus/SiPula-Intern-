package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.gson.Gson;
import com.silvia.sipulaintern.R;
import com.silvia.sipulaintern.activity.Adapter.AdapterPenyelia;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;
import com.silvia.sipulaintern.activity.Model.ModelPenyelia;
import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.activity.util.TinyDB;
import com.silvia.sipulaintern.databinding.ActivityMainBinding;
import com.silvia.sipulaintern.databinding.ActivityPenyeliaBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PenyeliaActivity extends AppCompatActivity {
    private ActivityPenyeliaBinding binding;

    ApiServer api;
    List<ModelPenyelia> dataPenyelia;

    TinyDB tinyDB;
    String level,teknisi,nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPenyeliaBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(binding.getRoot());

        AndroidNetworking.initialize(this);
        tinyDB = new TinyDB(this);
        level =  tinyDB.getString("keyLevel");
        teknisi =  tinyDB.getString("keyId");
        nama =  tinyDB.getString("keyNama");

        api = new ApiServer();

        binding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tinyDB.clear();
                Intent i = new Intent(PenyeliaActivity.this ,LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });
        binding.namaUser.setText(level);



        binding.rvBerita.setHasFixedSize(true);
        binding.rvBerita.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        dataPenyelia = new ArrayList<>();

        binding.swHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPenyelia();
            }
        });

        getPenyelia();

    }

    public void getPenyelia(){
        binding.swHome.setRefreshing(false);
        Log.d("api",api.URL_PENYELIA);
        AndroidNetworking.get(api.URL_PENYELIA)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if (response.getString("status").equalsIgnoreCase("sukses")) {

                                JSONArray res = response.getJSONArray("res");
                                Gson gson = new Gson();
                                dataPenyelia.clear();
                                for (int i = 0; i < res.length(); i++) {
                                    JSONObject data = res.getJSONObject(i);
                                    ModelPenyelia Isi = gson.fromJson(data + "", ModelPenyelia.class);
                                    dataPenyelia.add(Isi);
                                }
                                AdapterPenyelia adapter = new AdapterPenyelia(dataPenyelia);
                                binding.rvBerita.setAdapter(adapter);
                                adapter.notifyDataSetChanged();
                            }else {

                                Toast.makeText(PenyeliaActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
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
}