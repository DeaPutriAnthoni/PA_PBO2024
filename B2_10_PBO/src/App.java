import javax.swing.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

import Eskrim.*;

public class App {
    private static final String URL = "jdbc:mysql://localhost:3306/toko_icecream";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println("Driver Tidak Ditemukan: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<IceCream> daftarIceCream = new ArrayList<>();
        ArrayList<User> daftarUser = new ArrayList<>();
        Admin admin = new Admin("abc", "123");

        int pilihan;
        do {
            System.out.println("=====================================================");
            System.out.println("====>      SELAMAT DATANG DI ICE CREAM SHOP!    <====");
            System.out.println("=====================================================");
            System.out.println(" >>  Silahkan Registrasi Jika Belum Memiliki Akun << ");
            System.out.println("               1. Login                              ");
            System.out.println("               2. Registrasi                         ");
            System.out.println("               3. Keluar?                            ");
            System.out.println("=====================================================");
            System.out.print(">> Pilihan Anda: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    login(scanner, admin, daftarIceCream, daftarUser);
                    break;
                case 2:
                    userRegistration(scanner, daftarIceCream, daftarUser);
                    break;
                case 3:
                    System.out.println("Terima Kasih!! Selamat Datang Kembali.");
                    break;
                default:
                    System.out.println("Pilihan Tidak Valid. Silakan Coba Lagi :>");
            }
        } while (pilihan != 3);
    }

    private static void login(Scanner scanner, Admin admin, ArrayList<IceCream> daftarIceCream, ArrayList<User> daftarUser) {
        System.out.print("Username: ");
        String username = scanner.nextLine();
        String password = getPasswordFromGUI();

        if (admin.login(username, password)) {
            System.out.println("-> Login Berhasil Sebagai Admin.");
            AdminMenu adminMenu = new AdminMenu(admin);
            adminMenu.showMenu(daftarIceCream, daftarUser, scanner);
        } else {
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                String sql = "SELECT * FROM tbcustomer WHERE username = ? AND password = ?";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setString(1, username);
                statement.setString(2, password);

                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    System.out.println("Berhasil Login Sebagai " + username);

                    User user = new User(username, password, "", "", "", "", new ArrayList<>());
                    user.setId(resultSet.getInt("id"));

                    UserMenu userMenu = new UserMenu(user);
                    userMenu.showMenu(daftarIceCream, scanner);
                } else {
                    System.out.println("-> Login Gagal! Username Atau Password Salah.");
                }

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Login Gagal: " + e.getMessage());
            }
        }
    }

    private static void userRegistration(Scanner scanner, ArrayList<IceCream> daftarIceCream, ArrayList<User> daftarUser) {
        String username;
        String nomor_hp;
        String jenis_kelamin;
        int errorCount = 0; // Counter untuk menghitung jumlah kesalahan
    
        while (errorCount < 3) { // Loop akan berhenti setelah 3 kali kesalahan
            while (true) {
                System.out.print("Masukkan Username: ");
                username = scanner.nextLine();
                if (username.matches("[a-zA-Z]+")) {
                    break;
                } else {
                    System.out.println("Username hanya boleh mengandung huruf saja.");
                    errorCount++;
                    if (errorCount == 3) {
                        System.out.println("Anda sudah salah input 3 kali. Registrasi dibatalkan.");
                        return; // Keluar dari metode registrasi jika sudah 3 kali kesalahan
                    }
                }
            }
            
            // Input and validate nomor_hp
            while (true) {
                System.out.print("Masukkan Nomor Hp: ");
                nomor_hp = scanner.nextLine();
                if (nomor_hp.matches("[0-9]+") && nomor_hp.length() == 12) {
                    break;
                } else {
                    System.out.println("Nomor Hp harus berupa angka saja dan harus terdiri dari 12 karakter.");
                    errorCount++;
                    if (errorCount == 3) {
                        System.out.println("Anda sudah salah input 3 kali. Registrasi dibatalkan.");
                        return; // Keluar dari metode registrasi jika sudah 3 kali kesalahan
                    }
                }
            }
            
            System.out.print("Masukkan Email: ");
            String email = scanner.nextLine();
            
            do {
                System.out.print("Masukkan Jenis Kelamin (L/P): ");
                jenis_kelamin = scanner.nextLine().toUpperCase();
                if (!jenis_kelamin.equals("L") && !jenis_kelamin.equals("P")) {
                    System.out.println("Input tidak valid. Silakan masukkan 'L' untuk laki-laki atau 'P' untuk perempuan.");
                    errorCount++;
                    if (errorCount == 3) {
                        System.out.println("Anda sudah salah input 3 kali. Registrasi dibatalkan.");
                        return; // Keluar dari metode registrasi jika sudah 3 kali kesalahan
                    }
                }
            } while (!jenis_kelamin.equals("L") && !jenis_kelamin.equals("P"));
    
            System.out.print("Masukkan Alamat: ");
            String alamat = scanner.nextLine();
            
            String password = getPasswordFromGUI("Masukkan Password: ");
            String confirmPassword = getPasswordFromGUI("Konfirmasi Password: ");
    
            if (isUsernameTaken(daftarUser, username)) {
                System.out.println("Username Sudah Digunakan. Silakan Gunakan Username Lain.");
                errorCount++;
                if (errorCount == 3) {
                    System.out.println("Anda sudah salah input 3 kali. Registrasi dibatalkan.");
                    return; // Keluar dari metode registrasi jika sudah 3 kali kesalahan
                }
                continue; // Lanjut ke iterasi selanjutnya jika username sudah digunakan
            }
    
            if (!password.equals(confirmPassword)) {
                System.out.println("Konfirmasi Password Salah! Registrasi Gagal.");
                errorCount++;
                if (errorCount == 3) {
                    System.out.println("Anda sudah salah input 3 kali. Registrasi dibatalkan.");
                    return; // Keluar dari metode registrasi jika sudah 3 kali kesalahan
                }
                continue; // Lanjut ke iterasi selanjutnya jika konfirmasi password salah
            }
    
            User newUser = new User(username, password, nomor_hp, email, jenis_kelamin, alamat, new ArrayList<>());
            daftarUser.add(newUser);
    
            // Registrasi user ke database
            registerUser(newUser);
            break; // Keluar dari loop registrasi setelah berhasil
        }
    }    

    private static boolean isUsernameTaken(ArrayList<User> daftarUser, String username) {
        for (User user : daftarUser) {
            if (user.getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static void registerUser(User user) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO tbcustomer (username, password, nomor_hp, email, jenis_kelamin, alamat) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getNomorHp());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getJenisKelamin());
            statement.setString(6, user.getAlamat());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Registrasi Berhasil!");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Registrasi Gagal: " + e.getMessage());
        }
    }

    private static boolean checkUserCredentials(String username, String password) {
        boolean validUser = false;
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbcustomer WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            validUser = resultSet.next();

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Login Gagal: " + e.getMessage());
        }
        return validUser;
    }

    private static String getPasswordFromGUI() {
        JPasswordField passwordField = new JPasswordField(20);
        JLabel label = new JLabel("Password: ");
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(passwordField);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, "Enter Password",
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        if (option == 0) { // pressing OK button
            return new String(passwordField.getPassword());
        }
        return "";
    }

    private static String getPasswordFromGUI(String message) {
        JPasswordField passwordField = new JPasswordField(20);
        JLabel label = new JLabel(message);
        JPanel panel = new JPanel();
        panel.add(label);
        panel.add(passwordField);
        String[] options = new String[]{"OK", "Cancel"};
        int option = JOptionPane.showOptionDialog(null, panel, message,
                JOptionPane.NO_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);
        if (option == 0) { // pressing OK button
            return new String(passwordField.getPassword());
        }
        return "";
    }
}
