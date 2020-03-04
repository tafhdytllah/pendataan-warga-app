package com.tafh.tugasakhir.model;

public class ModelUser {
    //Deklarasi variabel

    private String userType, noRumah  ,noKK,namaKepalaKeluarga, noHp ,gmail ,password ,userid
    , img, search, cover, onlineStatus;

    public ModelUser() {
    }

    public ModelUser(String userType, String noRumah, String noKK, String namaKepalaKeluarga, String noHp, String gmail, String password, String img, String search, String cover, String onlineStatus) {
        this.userType = userType;
        this.noRumah = noRumah;
        this.noKK = noKK;
        this.namaKepalaKeluarga = namaKepalaKeluarga;
        this.noHp = noHp;

        this.gmail = gmail;
        this.password = password;
        this.img = img;
        this.search = search;
        this.cover = cover;
        this.onlineStatus = onlineStatus;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getNamaKepalaKeluarga() {
        return namaKepalaKeluarga;
    }

    public void setNamaKepalaKeluarga(String namaKepalaKeluarga) {
        this.namaKepalaKeluarga = namaKepalaKeluarga;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }
}
