package com.silvia.sipulaintern.activity.Model;


public class ModelAdmin  {


        private String id_pembayaran;
        private String no_registrasi;
        private String id_pemohon;
        private String id_layanan;
        private String total_biaya;
        private String total_waktu;
        private Object id_teknisi;
        private String file_pemohon;
        private String bukti_pembayaran;
        private String status_pembayaran;
        private String status_pemohon;
        private Object keterangan;
        private String tgl_pengajuan;
        private String username;
        private String password;
        private String nama_pemohon;
        private String notelp_pemohon;
        private String email_pemohon;
        private String nama_perusahaan;
        private String pemilik_perusahaan;
        private String alamat_perusahaan;
        private String nama_layanan;
        private String img_layanan;

    public ModelAdmin(String id_pembayaran, String no_registrasi, String id_pemohon, String id_layanan, String total_biaya, String total_waktu, Object id_teknisi, String file_pemohon, String bukti_pembayaran, String status_pembayaran, String status_pemohon, Object keterangan, String tgl_pengajuan, String username, String password, String nama_pemohon, String notelp_pemohon, String email_pemohon, String nama_perusahaan, String pemilik_perusahaan, String alamat_perusahaan, String nama_layanan, String img_layanan) {
        this.id_pembayaran = id_pembayaran;
        this.no_registrasi = no_registrasi;
        this.id_pemohon = id_pemohon;
        this.id_layanan = id_layanan;
        this.total_biaya = total_biaya;
        this.total_waktu = total_waktu;
        this.id_teknisi = id_teknisi;
        this.file_pemohon = file_pemohon;
        this.bukti_pembayaran = bukti_pembayaran;
        this.status_pembayaran = status_pembayaran;
        this.status_pemohon = status_pemohon;
        this.keterangan = keterangan;
        this.tgl_pengajuan = tgl_pengajuan;
        this.username = username;
        this.password = password;
        this.nama_pemohon = nama_pemohon;
        this.notelp_pemohon = notelp_pemohon;
        this.email_pemohon = email_pemohon;
        this.nama_perusahaan = nama_perusahaan;
        this.pemilik_perusahaan = pemilik_perusahaan;
        this.alamat_perusahaan = alamat_perusahaan;
        this.nama_layanan = nama_layanan;
        this.img_layanan = img_layanan;
    }

    public String getId_pembayaran() {
        return id_pembayaran;
    }

    public void setId_pembayaran(String id_pembayaran) {
        this.id_pembayaran = id_pembayaran;
    }

    public String getNo_registrasi() {
        return no_registrasi;
    }

    public void setNo_registrasi(String no_registrasi) {
        this.no_registrasi = no_registrasi;
    }

    public String getId_pemohon() {
        return id_pemohon;
    }

    public void setId_pemohon(String id_pemohon) {
        this.id_pemohon = id_pemohon;
    }

    public String getId_layanan() {
        return id_layanan;
    }

    public void setId_layanan(String id_layanan) {
        this.id_layanan = id_layanan;
    }

    public String getTotal_biaya() {
        return total_biaya;
    }

    public void setTotal_biaya(String total_biaya) {
        this.total_biaya = total_biaya;
    }

    public String getTotal_waktu() {
        return total_waktu;
    }

    public void setTotal_waktu(String total_waktu) {
        this.total_waktu = total_waktu;
    }

    public Object getId_teknisi() {
        return id_teknisi;
    }

    public void setId_teknisi(Object id_teknisi) {
        this.id_teknisi = id_teknisi;
    }

    public String getFile_pemohon() {
        return file_pemohon;
    }

    public void setFile_pemohon(String file_pemohon) {
        this.file_pemohon = file_pemohon;
    }

    public String getBukti_pembayaran() {
        return bukti_pembayaran;
    }

    public void setBukti_pembayaran(String bukti_pembayaran) {
        this.bukti_pembayaran = bukti_pembayaran;
    }

    public String getStatus_pembayaran() {
        return status_pembayaran;
    }

    public void setStatus_pembayaran(String status_pembayaran) {
        this.status_pembayaran = status_pembayaran;
    }

    public String getStatus_pemohon() {
        return status_pemohon;
    }

    public void setStatus_pemohon(String status_pemohon) {
        this.status_pemohon = status_pemohon;
    }

    public Object getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(Object keterangan) {
        this.keterangan = keterangan;
    }

    public String getTgl_pengajuan() {
        return tgl_pengajuan;
    }

    public void setTgl_pengajuan(String tgl_pengajuan) {
        this.tgl_pengajuan = tgl_pengajuan;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNama_pemohon() {
        return nama_pemohon;
    }

    public void setNama_pemohon(String nama_pemohon) {
        this.nama_pemohon = nama_pemohon;
    }

    public String getNotelp_pemohon() {
        return notelp_pemohon;
    }

    public void setNotelp_pemohon(String notelp_pemohon) {
        this.notelp_pemohon = notelp_pemohon;
    }

    public String getEmail_pemohon() {
        return email_pemohon;
    }

    public void setEmail_pemohon(String email_pemohon) {
        this.email_pemohon = email_pemohon;
    }

    public String getNama_perusahaan() {
        return nama_perusahaan;
    }

    public void setNama_perusahaan(String nama_perusahaan) {
        this.nama_perusahaan = nama_perusahaan;
    }

    public String getPemilik_perusahaan() {
        return pemilik_perusahaan;
    }

    public void setPemilik_perusahaan(String pemilik_perusahaan) {
        this.pemilik_perusahaan = pemilik_perusahaan;
    }

    public String getAlamat_perusahaan() {
        return alamat_perusahaan;
    }

    public void setAlamat_perusahaan(String alamat_perusahaan) {
        this.alamat_perusahaan = alamat_perusahaan;
    }

    public String getNama_layanan() {
        return nama_layanan;
    }

    public void setNama_layanan(String nama_layanan) {
        this.nama_layanan = nama_layanan;
    }

    public String getImg_layanan() {
        return img_layanan;
    }

    public void setImg_layanan(String img_layanan) {
        this.img_layanan = img_layanan;
    }
}
