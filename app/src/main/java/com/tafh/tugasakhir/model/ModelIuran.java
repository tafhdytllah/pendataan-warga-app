package com.tafh.tugasakhir.model;

public class ModelIuran {
    private String iuranId, keluargaId, noRumah, noKK, namaKK, judulIuran, jumlahIuran, pembayaranIuran, tanggalIuran;

    public ModelIuran() {

    }

    public ModelIuran(String iuranId, String keluargaId, String noRumah, String noKK, String namaKK, String judulIuran, String jumlahIuran, String pembayaranIuran, String tanggalIuran) {

        this.iuranId = iuranId;
        this.keluargaId = keluargaId;
        this.noRumah = noRumah;
        this.noKK = noKK;
        this.namaKK = namaKK;
        this.judulIuran = judulIuran;
        this.jumlahIuran = jumlahIuran;
        this.pembayaranIuran = pembayaranIuran;
        this.tanggalIuran = tanggalIuran;
    }

    public String getIuranId() {
        return iuranId;
    }

    public void setIuranId(String iuranId) {
        this.iuranId = iuranId;
    }

    public String getKeluargaId() {
        return keluargaId;
    }

    public void setKeluargaId(String keluargaId) {
        this.keluargaId = keluargaId;
    }

    public String getNoRumah() {
        return noRumah;
    }

    public void setNoRumah(String noRumah) {
        this.noRumah = noRumah;
    }

    public String getNoKK() {
        return noKK;
    }

    public void setNoKK(String noKK) {
        this.noKK = noKK;
    }

    public String getNamaKK() {
        return namaKK;
    }

    public void setNamaKK(String namaKK) {
        this.namaKK = namaKK;
    }

    public String getJudulIuran() {
        return judulIuran;
    }

    public void setJudulIuran(String judulIuran) {
        this.judulIuran = judulIuran;
    }

    public String getJumlahIuran() {
        return jumlahIuran;
    }

    public void setJumlahIuran(String jumlahIuran) {
        this.jumlahIuran = jumlahIuran;
    }

    public String getPembayaranIuran() {
        return pembayaranIuran;
    }

    public void setPembayaranIuran(String pembayaranIuran) {
        this.pembayaranIuran = pembayaranIuran;
    }

    public String getTanggalIuran() {
        return tanggalIuran;
    }

    public void setTanggalIuran(String tanggalIuran) {
        this.tanggalIuran = tanggalIuran;
    }
}
