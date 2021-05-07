package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

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
import com.silvia.sipulaintern.activity.Adapter.AdapterAdmin;
import com.silvia.sipulaintern.activity.Adapter.Adapter_Detail_Admin;
import com.silvia.sipulaintern.activity.Model.ModelAdmin;
import com.silvia.sipulaintern.activity.Model.ModelDetailAdmin;
import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.databinding.ActivityDetailAjuanBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailAjuanActivity extends AppCompatActivity {
    private ActivityDetailAjuanBinding binding;
    String no_reg;
    List<ModelDetailAdmin> dataAdmin;

    ApiServer api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailAjuanBinding.inflate(getLayoutInflater());
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
        binding.txtNamaPengaju.setText(i.getStringExtra("nama_pemohon"));
        binding.txtInstument.setText(i.getStringExtra("nama_layanan"));
        binding.txtStatusPengajuan.setText(i.getStringExtra("status_pemohon"));

        binding.rvBerita.setHasFixedSize(true);
        binding.rvBerita.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        dataAdmin = new ArrayList<>();
        getDetail();

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
}