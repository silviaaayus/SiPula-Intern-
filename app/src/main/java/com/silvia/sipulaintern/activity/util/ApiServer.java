package com.silvia.sipulaintern.activity.util;

public class ApiServer {


    private String HOST ="http://192.168.100.15/sipula/";

    public String URL_LOGIN = HOST + "login_eks.php";
    public String URL_ADMIN = HOST + "select_data_admin.php";
    public String URL_DETAIL_ADMIN = HOST + "select_detail_admin.php?no_registrasi=";
    public String URL_SAVE_ADMIN = HOST + "save_admin.php";
    public String URL_PIMPINAN = HOST + "select_data_pimpinan.php";
    public String URL_SAVE_PIMPINAN = HOST + "save_pimpinan.php";
    public String URL_KASI = HOST + "select_kasi.php";



}
