package com.silvia.sipulaintern.activity.Model;


public class ModelTeknisi  {

        private String id_teknisi;
        private String id_layanan;
        private String nama_teknisi;

    public ModelTeknisi(String id_teknisi, String id_layanan, String nama_teknisi) {
        this.id_teknisi = id_teknisi;
        this.id_layanan = id_layanan;
        this.nama_teknisi = nama_teknisi;
    }

    public String getId_teknisi() {
        return id_teknisi;
    }

    public void setId_teknisi(String id_teknisi) {
        this.id_teknisi = id_teknisi;
    }

    public String getId_layanan() {
        return id_layanan;
    }

    public void setId_layanan(String id_layanan) {
        this.id_layanan = id_layanan;
    }

    public String getNama_teknisi() {
        return nama_teknisi;
    }

    public void setNama_teknisi(String nama_teknisi) {
        this.nama_teknisi = nama_teknisi;
    }
}
