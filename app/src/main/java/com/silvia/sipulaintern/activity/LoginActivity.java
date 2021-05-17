package com.silvia.sipulaintern.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

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
import com.silvia.sipulaintern.R;

import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.activity.util.TinyDB;
import com.silvia.sipulaintern.databinding.ActivityLoginBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    String[] login = {"Admin","Pimpinan","Kasi","Teknisi","Penyelia"};
    String tempLogin;
    TinyDB tinyDB;
    ApiServer api;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        tinyDB = new TinyDB(this);
        api = new ApiServer();

        AndroidNetworking.initialize(this);

        ArrayAdapter<String> Adokter= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,login);
        Adokter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(Adokter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tempLogin = login[i];
                Log.e("spinner",tempLogin);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getLogin();


            }
        });


    }

    private void getLogin() {
//        alertDialog.show();
        Log.e("api", api.URL_LOGIN);
        AndroidNetworking.post(api.URL_LOGIN)
                .addBodyParameter("username", binding.edUsername.getText().toString())
                .addBodyParameter("password", binding.edPassword.getText().toString())
                .addBodyParameter("level_admin",tempLogin )
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
//                            alertDialog.hide();
                            int stat = response.getInt("status");
                            String message = response.getString("message");
                            Log.d("sukses", "code" + response);
                            if (stat == 1) {

                                JSONObject data = response.getJSONObject("data");
                                String id = data.getString("id");

                                String nama = data.getString("nama_admin");
                                String email = data.getString("email_admin");
                                String level = data.getString("level_admin");

                                tinyDB.putString("keyId",id);
                                tinyDB.putString("keyNama",nama);
                                tinyDB.putString("keyEmail",email);
                                tinyDB.putString("keyLevel",level);


                                tinyDB.putBoolean("keyLogin", true);



                                if (level.equalsIgnoreCase("Admin")) {
                                    Toast.makeText(LoginActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(i);
                                } else if (level.equalsIgnoreCase("Pimpinan")){
                                    Toast.makeText(LoginActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(i);
                                } else if(level.equalsIgnoreCase("Kasi")){
                                    Toast.makeText(LoginActivity.this, "Login Sukses", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(i);
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {

                        Log.d("eror", "code :" + anError);
                        Toast.makeText(LoginActivity.this, "" + anError, Toast.LENGTH_SHORT).show();
                    }
                });


        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

    }
}