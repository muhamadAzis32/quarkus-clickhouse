package org.iconpln.example.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MasterUnitupi {

    private String unitupi;
    private String alamat;
    private String kota;
    private String manager;
    private String nama;

    @JsonProperty("nama_singkat")
    private String namaSingkat;

    private String satuan;
    private String telepon;

    @JsonProperty("__ts_ms")
    private Long tsMs;

    @JsonProperty("__deleted")
    private Integer deleted;

    // Constructor kosong
    public MasterUnitupi() {
    }

    // Constructor lengkap
    public MasterUnitupi(String unitupi, String alamat, String kota, String manager,
                         String nama, String namaSingkat, String satuan, String telepon,
                         Long tsMs, Integer deleted) {
        this.unitupi = unitupi;
        this.alamat = alamat;
        this.kota = kota;
        this.manager = manager;
        this.nama = nama;
        this.namaSingkat = namaSingkat;
        this.satuan = satuan;
        this.telepon = telepon;
        this.tsMs = tsMs;
        this.deleted = deleted;
    }

    // Getters dan Setters
    public String getUnitupi() {
        return unitupi;
    }

    public void setUnitupi(String unitupi) {
        this.unitupi = unitupi;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNamaSingkat() {
        return namaSingkat;
    }

    public void setNamaSingkat(String namaSingkat) {
        this.namaSingkat = namaSingkat;
    }

    public String getSatuan() {
        return satuan;
    }

    public void setSatuan(String satuan) {
        this.satuan = satuan;
    }

    public String getTelepon() {
        return telepon;
    }

    public void setTelepon(String telepon) {
        this.telepon = telepon;
    }

    public Long getTsMs() {
        return tsMs;
    }

    public void setTsMs(Long tsMs) {
        this.tsMs = tsMs;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "MasterUnitupi{" +
                "unitupi='" + unitupi + '\'' +
                ", nama='" + nama + '\'' +
                ", kota='" + kota + '\'' +
                ", deleted=" + deleted +
                '}';
    }


}
