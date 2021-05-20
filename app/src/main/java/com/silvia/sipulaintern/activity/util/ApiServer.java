package com.silvia.sipulaintern.activity.util;

public class ApiServer {



  private String HOST ="http://192.168.100.15/sipula/";

//    private String HOST ="http://192.168.43.22/sipula/";

    public String URL_LOGIN = HOST + "login_eks.php";
    public String URL_ADMIN = HOST + "select_data_admin.php";
    public String URL_PDF = HOST + "pdf/";
    public String URL_GAMBAR = HOST + "gambar/";
    public String URL_DETAIL_ADMIN = HOST + "select_detail_admin.php?no_registrasi=";
    public String URL_SAVE_ADMIN = HOST + "save_admin.php";
    public String URL_PIMPINAN = HOST + "select_data_pimpinan.php";
    public String URL_SAVE_PIMPINAN = HOST + "save_pimpinan.php";
    public String URL_SAVE_KERJAAN_TEKNISI = HOST + "save_kerjakan_teknisi.php";
    public String URL_SAVE_SELESAI_TEKNISI = HOST + "save_selesai_teknisi.php";
    public String URL_SAVE_PENDING_TEKNISI = HOST + "save_pending_teknisi.php";
    public String URL_UPLOAD_PDF = HOST+"upload_laporan.php?noreg=";
    public String URL_KASI = HOST + "select_kasi.php";
    public String URL_SPINTEKNISI = HOST + "select_teknisi.php?id_layanan=";
    public String URL_SAVE_KASI = HOST + "save_kasi.php";
    public String URL_TEKNISI = HOST + "select_data_teknisi.php?id_admin=";

    public String URL_PENYELIA = HOST + "select_data_penyelia.php";
    public String URL_SAVE_PENYELIA = HOST + "save_laporan.php";



}
