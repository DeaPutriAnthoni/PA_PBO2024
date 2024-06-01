package Eskrim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class Transaksi {
    private String idTransaksi;
    private LocalDateTime tanggal;
    private String username;
    private String nomorHp;
    private String email;
    private String jenisKelamin;
    private String alamat;
    private int totalHarga;
    private List<IceCream> daftarIceCream;


    // Method to save transaction to database
    public void saveToDatabase() {
        String url = "jdbc:mysql://localhost:3306/toko_icecream";
        String username = "root";
        String password = ""; // Ubah sesuai dengan password database Anda

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            String sql = "INSERT INTO tbtransaksi (id, tanggal, username, nomor_hp, email, jenis_kelamin, alamat, total_harga) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            
            // Set values for PreparedStatement
            statement.setString(1, idTransaksi);
            statement.setObject(2, tanggal);
            statement.setString(3, username);
            statement.setString(4, nomorHp);
            statement.setString(5, email);
            statement.setString(6, jenisKelamin);
            statement.setString(7, alamat);
            statement.setInt(8, totalHarga);
            
            // Execute the PreparedStatement
            statement.executeUpdate();
            
            System.out.println("Transaksi Berhasil Disimpan Ke Database.");
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Constructor
    public Transaksi(String idTransaksi, LocalDateTime tanggal, String username, String nomorHp, String email, String jenisKelamin, String alamat, int totalHarga, List<IceCream> daftarIceCream){
        this.idTransaksi = generateIdTransaksi(); // Generate unique transaction ID
        this.tanggal = LocalDateTime.now(); // Set transaction date and time to current date and time
        this.username = username;
        this.nomorHp = nomorHp;
        this.email = email;
        this.jenisKelamin = jenisKelamin;
        this.alamat = alamat;
        this.totalHarga = totalHarga;
        this.daftarIceCream = daftarIceCream;
    }

    // Method to generate unique transaction ID (You can customize this method according to your requirements)
    private String generateIdTransaksi() {
        // Implementation to generate unique transaction ID
        // You can implement your own logic here, such as combining username with current timestamp
        // For simplicity, I'll return a random string for now
        return "TRX-" + LocalDateTime.now().toString();
    }

    // Getters and Setters
    public String getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(String idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public LocalDateTime getTanggal() {
        return tanggal;
    }

    public void setTanggal(LocalDateTime tanggal) {
        this.tanggal = tanggal;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public int getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(int totalHarga) {
        this.totalHarga = totalHarga;
    }

    public List<IceCream> getDaftarIceCream() {
        return daftarIceCream;
    }

    public void setDaftarIceCream(List<IceCream> daftarIceCream) {
        this.daftarIceCream = daftarIceCream;
    }

    // Method to print list of ice creams in the transaction
    public void printDaftarIceCream() {
        if (daftarIceCream != null && !daftarIceCream.isEmpty()) {
            System.out.println("============================================"); 
            System.out.println(" ||   Daftar Ice Cream Dalam Transaksi   ||:");
            System.out.println("============================================"); 
            for (IceCream iceCream : daftarIceCream) {
                System.out.println(iceCream);
            }
        } else {
            System.out.println("Daftar Ice Cream Dalam Transaksi Kosong.");
        }
    }

    @Override
    public String toString() {
        return "ID Transaksi: " + idTransaksi + "\n" +
               "Tanggal: " + tanggal.toString() + "\n" +
               "Username: " + username + "\n" +
               "Nomor HP: " + nomorHp + "\n" +
               "Email: " + email + "\n" +
               "Jenis Kelamin: " + jenisKelamin + "\n" +
               "Alamat: " + alamat + "\n" +
               "Total Harga: Rp.  " + totalHarga;
    }
}
