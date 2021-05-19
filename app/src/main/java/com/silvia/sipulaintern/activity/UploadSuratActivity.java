package com.silvia.sipulaintern.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.silvia.sipulaintern.activity.util.ApiServer;
import com.silvia.sipulaintern.databinding.ActivityUploadSuratBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class UploadSuratActivity extends AppCompatActivity {
    private ActivityUploadSuratBinding binding;

    private static final String IMAGE_DIRECTORY = "/pdf";
    private String url = "https://www.google.com";
    private static final int BUFFER_SIZE = 1024 * 2;
    String path, komentar;
    AlertDialog dialog;
    ApiServer apiServer;
    String noReg, total, waktu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadSuratBinding.inflate(getLayoutInflater());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        dialog = new SpotsDialog.Builder().setContext(UploadSuratActivity.this).setMessage("Sedang Proses ....").setCancelable(false).build();
        apiServer = new ApiServer();
        setContentView(binding.getRoot());
        requestMultiplePermissions();
        AndroidNetworking.initialize(this);

        Intent intent = new Intent(getIntent());
        noReg = intent.getStringExtra("NoReg");
        binding.noreg.setText(noReg);

//        binding.totalBiaya.setText(total);

        binding.uploadSurat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                startActivityForResult(intent,1);
            }
        });
        binding.lanjutBayar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKomen();
            }
        });
        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showDialogKomen() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambahkan Komentar Pada File");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                komentar = input.getText().toString();
                uploadPDF(path, komentar);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void uploadPDF(final String path, String komentar) {
        dialog.show();
        final File file = new File(path);
        Log.d("Upload", "URL  : "+apiServer.URL_UPLOAD_PDF+noReg);
        AndroidNetworking.upload(apiServer.URL_UPLOAD_PDF+noReg+"&komentar="+ komentar)
                .addMultipartFile("filename", file)
                .addMultipartParameter("filename", "value")
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("upload","respon : "+response);
                        try {
                            int cut = String.valueOf(file).lastIndexOf('/');
                            String sample = path.substring(cut+1);
                            Log.d("Upload io", "fil :"+sample);
//                            simpanData(sample);

                            dialog.hide();
                            JSONObject jsonObject = new JSONObject(response.toString());
                            Toast.makeText(getApplicationContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                            jsonObject.toString().replace("\\\\", "");

                            if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                                Toast.makeText(UploadSuratActivity.this, "Sukses", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(UploadSuratActivity.this, MainActivity.class);
                                startActivity(intent1);
                            }else {
                                Toast.makeText(UploadSuratActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onError(ANError anError) {
                        dialog.hide();
                        Log.d("io Upload", "code :"+anError);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
            String uriString = uri.toString();
            File myFile = new File(uriString);

            path = getFilePathFromURI(UploadSuratActivity.this,uri);
            Log.d("ioooo", myFile.getName()+"| "+uri.getPath()+"|"+uri);
            int cut = path.lastIndexOf('/');

            binding.fileTxt.setText(path.substring(cut + 1));
            binding.lanjutBayar.setVisibility(View.VISIBLE);

        }

        super.onActivityResult(requestCode, resultCode, data);

    }


    public static String getFilePathFromURI(Context context, Uri contentUri) {
        //copy file and send new file path
        String fileName = getFileName(contentUri);
        Log.d("ioo", "nah :"+fileName);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        Log.d("ioo", "nih :"+wallpaperDirectory);
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        if (!TextUtils.isEmpty(fileName)) {
            File copyFile = new File(wallpaperDirectory + File.separator + fileName);
            // create folder if not exists

            copy(context, contentUri, copyFile);
            return copyFile.getAbsolutePath();
        }
        return null;
    }

    public static String getFileName(Uri uri) {
        if (uri == null) return null;
        String path = uri.getPath();
        int cut = path.lastIndexOf('/');
        String fileName = null;
        if (cut != -1) {
            String sample = path.substring(cut+1);
            String last = sample.substring(sample.length()-3);
            Log.d("io:o", "ini :"+last);
            if (last.equalsIgnoreCase("pdf")) {
                fileName = path.substring(cut + 1);
            }else {
                fileName = path.substring(cut + 1)+".pdf";
            }
        }
        return fileName;
    }

    public static void copy(Context context, Uri srcUri, File dstFile) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(srcUri);
            if (inputStream == null) return;
            OutputStream outputStream = new FileOutputStream(dstFile);
            copystream(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int copystream(InputStream input, OutputStream output) throws Exception, IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        BufferedInputStream in = new BufferedInputStream(input, BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(output, BUFFER_SIZE);
        int count = 0, n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
                count += n;
            }
            out.flush();
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
            try {
                in.close();
            } catch (IOException e) {
                Log.e(e.getMessage(), String.valueOf(e));
            }
        }
        return count;
    }

    private void  requestMultiplePermissions(){
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplicationContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<com.karumi.dexter.listener.PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }
}