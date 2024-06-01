package Eskrim;

import java.util.List;

public class User {
    private int Id;
    private String username;
    private String password;
    private String nomorHp;
    private String email;
    private String jenisKelamin;
    private String alamat;

    public User(String username, String password, String nomorHp, String email, String jenisKelamin, String alamat, List<Transaksi> riwayatTransaksi) {
        this.username = username;
        this.password = password;
        this.nomorHp = nomorHp;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
    }

    // Getters and Setters
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

    public String getNomorHp() {
        return nomorHp;
    }

    public void setNomorHp(String nomorHp) {
        this.nomorHp = nomorHp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin(String jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}