package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetailAjuanActivity extends AppCompatActivity {
    private ActivityDetailAjuanBinding binding;

    String no_reg, file, level, id_layanan, total_waktu, laporan, total_biaya, bukti_bayar;

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
        Locale localeId = new Locale("in", "ID");
        final NumberFormat formatRupiah = NumberFormat.getCurrencyInstance(localeId);

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
        total_waktu = i.getStringExtra("total_waktu");
        total_biaya = i.getStringExtra("total_biaya");
        bukti_bayar = i.getStringExtra("bukti_bayar");

        Picasso.get().load(api.URL_GAMBAR + bukti_bayar).into(binding.imgBuktiBayar);


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
        level = tinyDB.getString("keyLevel");

        if (level.equalsIgnoreCase("Kasi")) {
            binding.spinteknisi.teknisi.setVisibility(View.VISIBLE);
        } else {
            binding.spinteknisi.teknisi.setVisibility(View.GONE);
        }

        if (level.equalsIgnoreCase("Teknisi")) {
            binding.btnTeknisi.setVisibility(View.VISIBLE);
            binding.btnSetujui.setVisibility(View.GONE);
            binding.sisa.setVisibility(View.VISIBLE);
        } else {
            binding.sisa.setVisibility(View.GONE);
            binding.btnTeknisi.setVisibility(View.GONE);
        }

        binding.txtNamaPengaju.setText(i.getStringExtra("nama_pemohon"));
        binding.txtInstument.setText(i.getStringExtra("nama_layanan"));
        binding.txtStatusPengajuan.setText(i.getStringExtra("status_pemohon"));



        binding.txtBiaya.setText(formatRupiah.format(Integer.valueOf(total_biaya)));
        binding.txtNoReg.setText(no_reg);
        binding.txtWaktu.setText(total_waktu);

        binding.btnPending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingTeknisi();
            }
        });
        if (binding.txtStatusPengajuan.getText().toString().equalsIgnoreCase("DI Pending")) {
            binding.txtKeterangan.setVisibility(View.GONE);
            binding.ket.setVisibility(View.GONE);
            binding.btnPengerjaan.setText("Lanjutkan Pengerjaan");
            binding.btnPengerjaan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogLanjutkanKerjaan();
                }
            });
        }
        if (binding.txtStatusPengajuan.getText().toString().equalsIgnoreCase("Di Kerjakan")) {
            binding.btnPending.setVisibility(View.VISIBLE);
            binding.txtKeterangan.setVisibility(View.GONE);
            binding.ket.setVisibility(View.GONE);

            binding.btnPengerjaan.setText("Selesai");
            binding.btnPengerjaan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selesaiTeknisi();
                }
            });

            binding.btnPending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pendingTeknisi();
                }
            });


        } else {
            binding.btnPengerjaan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialogKerjaan();
                }
            });
        }

        getDataTeknisi();

        binding.btnSetujui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (level.equalsIgnoreCase("Admin")) {
                    selesai();
                } else if (level.equalsIgnoreCase("Pimpinan")) {
                    selesaiPimpinan();
                } else if (level.equalsIgnoreCase("Kasi")) {
                    selesaiKasi();
                }


            }
        });


        binding.rvBerita.setHasFixedSize(true);
        binding.rvBerita.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        dataAdmin = new ArrayList<>();
        getDetail();


    }

    private void pendingTeknisi() {
        AndroidNetworking.post(api.URL_SAVE_PENDING_TEKNISI)
                .addBodyParameter("noreg", no_reg)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, "Permohonan Di Pending", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void selesaiTeknisi() {
        AndroidNetworking.post(api.URL_SAVE_SELESAI_TEKNISI)
                .addBodyParameter("noreg", no_reg)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, "Selesai", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, UploadSuratActivity.class);
                                intent.putExtra("NoReg", no_reg);
                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showWaktu() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailAjuanActivity.this);
        alertDialogBuilder.setTitle("Sisa Waktu Pengerjaan");
        alertDialogBuilder.setMessage("Waktu Pengerjaan Tersisa " + total_waktu + " Hari");
        alertDialogBuilder.setPositiveButton("Oke", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialogKerjaan() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailAjuanActivity.this);
        alertDialogBuilder.setTitle("Kerjakan Permohonan?");
        alertDialogBuilder.setMessage("Tekan yes Jika Yakin Memulai permohonan, \nWaktu pengerjaan : " + total_waktu);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        kerjakanTeknisi();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void showDialogLanjutkanKerjaan() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DetailAjuanActivity.this);
        alertDialogBuilder.setTitle("Lanjutkan Kerjakan Permohonan?");
        alertDialogBuilder.setMessage("Tekan yes Jika Yakin Melanjutkan permohonan, \nSisa Waktu pengerjaan : " + total_waktu);
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        kerjakanTeknisi();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", null);
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void kerjakanTeknisi() {
        AndroidNetworking.post(api.URL_SAVE_KERJAAN_TEKNISI)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, " Waktu Tersisa : " + total_waktu, Toast.LENGTH_LONG).show();
                                finish();

                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void getDetail() {
        Log.d("api", api.URL_DETAIL_ADMIN + no_reg);
        AndroidNetworking.get(api.URL_DETAIL_ADMIN + no_reg)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
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
                            } else {

                                Toast.makeText(DetailAjuanActivity.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu", "response:" + anError);
                    }
                });

    }

    private void selesai() {

        Log.d("URL", "selesai  : " + api.URL_SAVE_ADMIN);
        AndroidNetworking.post(api.URL_SAVE_ADMIN)
                .addBodyParameter("noreg", no_reg)
                .addBodyParameter("keterangan", binding.txtKeterangan.getText().toString())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
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

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    public void getDataTeknisi(){
        Log.d("teknisi",api.URL_SPINTEKNISI+id_layanan);
        AndroidNetworking.get(api.URL_SPINTEKNISI+id_layanan)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = 0;
                            Log.d("tampilmenu", "response:" + response);
                            JSONArray res = response.getJSONArray("res");
                            for (int i = 0; i < res.length(); i++) {
                                JSONObject data = res.getJSONObject(i);

                                    String nama = data.getString("nama_teknisi");

                                dataTeknisi.add(nama);

                                id = data.getInt("id_teknisi");
                                Log.d("id Select Teknisi ", "true : " + id);

//                                        String gambar =api.URL_GAMBAR+data.getString("gambar_");

//                                        Item[i] = nama;
                            }
                            setDataSpinner(id);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e("tampil menu", "response:" + anError);
                    }
                });
    }

    private void setDataSpinner(int id) {
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, dataTeknisi);

        //Memasukan Adapter pada Spinner
        binding.spinteknisi.spinnerTeknisi.setAdapter(adapter);

        //Mengeset listener untuk mengetahui event/aksi saat item dipilih
        binding.spinteknisi.spinnerTeknisi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapterView, View view, int i, long l) {
                a = id;
                Log.d("id Select Teknisi ", "false : " + a);
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
                .addBodyParameter("id_teknisi", "" + a)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {

                            if (response.getString("response").equalsIgnoreCase("sukses")) {
                                Toast.makeText(DetailAjuanActivity.this, " Berhasil", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(DetailAjuanActivity.this, MainActivity.class);

                                startActivity(intent);
                            } else {
                                Toast.makeText(DetailAjuanActivity.this, "Upload Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("Upload", "eror : " + anError);
                        Toast.makeText(DetailAjuanActivity.this, "Jaringan Bermasalah", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}