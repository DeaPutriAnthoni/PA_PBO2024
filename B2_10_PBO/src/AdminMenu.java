import Eskrim.Admin;
import Eskrim.IceCream;
import Eskrim.User;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Scanner;

public class AdminMenu {
    private Admin admin;

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

    public AdminMenu(Admin admin) {
        this.admin = admin;
    }

    public void showMenu(ArrayList<IceCream> daftarIceCream, ArrayList<User> daftarUser, Scanner scanner) {
        int pilihan;
        do {
            System.out.println("======================================");
            System.out.println("====>    SELAMAT DATANG ADMIN!   <====");
            System.out.println("======================================");
            System.out.println("         1.| Tambah Ice Cream         ");
            System.out.println("         2.| Ubah Ice Cream           ");
            System.out.println("         3.| Hapus Ice Cream          ");
            System.out.println("         4.| Lihat Semua Ice Cream    ");
            System.out.println("         5.| Cek Transaksi Customer   ");
            System.out.println("         6.| Hapus Customer           ");
            System.out.println("         7.| Kembali                  ");
            System.out.println("======================================");
            System.out.print(">> Pilihan Anda: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    tambahIceCream(scanner, daftarIceCream);
                    break;
                case 2:
                    ubahIceCream(scanner, daftarIceCream);
                    break;
                case 3:
                    hapusIceCream(scanner, daftarIceCream);
                    break;
                case 4:
                    lihatIceCream(daftarIceCream);
                    break;
                case 5:
                    cekTransaksiPengguna(daftarUser);
                    break;
                case 6:
                    hapusCustomer(scanner); // Ubah pemanggilan untuk memasukkan Scanner scanner
                    break;
                case 7:
                    System.out.println("Kembali Ke Menu Utama.");
                    break;
                default:
                    System.out.println("Pilihan Tidak Valid. Silakan Coba Lagi :>");
            }
        } while (pilihan != 7);
    }
   

    private static void tambahIceCream(Scanner scanner, ArrayList<IceCream> daftarIceCream) {
        String rasa = null;
        String topping = null;
        double harga = 0;
        int stok = -1;
    
        // Input and validate rasa
        int attempts = 0;
        while (attempts < 3) {
            System.out.print("Masukkan Rasa: ");
            rasa = scanner.nextLine();
            if (rasa.matches("[a-zA-Z ]+")) {
                break;
            } else {
                System.out.println("Silahkan masukkan huruf.");
                attempts++;
            }
        }
        if (attempts == 3) {
            System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
            return;
        }
    
        // Input and validate topping
        attempts = 0;
        while (attempts < 3) {
            System.out.print("Masukkan Topping: ");
            topping = scanner.nextLine();
            if (topping.matches("[a-zA-Z ]+")) {
                break;
            } else {
                System.out.println("Silahkan masukkan huruf.");
                attempts++;
            }
        }
        if (attempts == 3) {
            System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
            return;
        }
    
        // Input and validate harga
        attempts = 0;
        while (attempts < 3) {
            try {
                System.out.print("Masukkan Harga: Rp. ");
                harga = Double.parseDouble(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Silahkan masukkan angka.");
                attempts++;
            }
        }
        if (attempts == 3) {
            System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
            return;
        }
    
        // Input and validate stok
        attempts = 0;
        while (attempts < 3) {
            try {
                System.out.print("Masukkan Stok: ");
                stok = Integer.parseInt(scanner.nextLine());
                if (stok >= 0) {
                    break;
                } else {
                    System.out.println("Stok Tidak Boleh Negatif.");
                    attempts++;
                }
            } catch (NumberFormatException e) {
                System.out.println("Silahkan Masukkan Angka.");
                attempts++;
            }
        }
        if (attempts == 3) {
            System.out.println("Percobaan Maksimal Tercapai. Kembali Ke Menu Utama.");
            return;
        }
    
        // Buat objek IceCream tanpa ID
        IceCream newIceCream = new IceCream(rasa, topping, stok, harga);
    
        // Simpan ice cream ke database dan dapatkan ID-nya
        int iceCreamId = saveCreamToDatabase(newIceCream);
    
        // Setel ID ice cream yang diperoleh ke objek ice cream
        newIceCream.setId(iceCreamId);
    
        // Tambahkan ice cream ke daftar ice cream
        daftarIceCream.add(newIceCream);
    
        System.out.println("Ice Cream Berhasil Ditambahkan!");
    }

    private static int saveCreamToDatabase(IceCream iceCream) {
        int iceCreamId = -1; // Default ID jika penyimpanan gagal
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "INSERT INTO tbicecream (rasa, topping, stok, harga) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, iceCream.getRasa());
            statement.setString(2, iceCream.getTopping());
            statement.setInt(3, iceCream.getStok());
            statement.setDouble(4, iceCream.getHarga());

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Ice Cream Berhasil Disimpan ke Database!!");
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    iceCreamId = generatedKeys.getInt(1); // Dapatkan ID yang baru saja di-generate
                    System.out.println("ID Ice Cream yang baru: " + iceCreamId); // Tambahkan pesan log untuk menampilkan ID ice cream baru
                }
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Ice Cream Gagal Disimpan ke Database!: " + e.getMessage());
        }
        return iceCreamId;
    }

    private static void lihatIceCream(ArrayList<IceCream> daftarIceCream) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbicecream";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
    
            System.out.println("==========================================================================");
            System.out.println("|                            DAFTAR SEMUA ICE CREAM                      |");
            System.out.println("==========================================================================");
            System.out.printf("| %-3s | %-20s | %-20s | %-5s | %-10s |\n", "No", "Rasa", "Topping", "Stok", "Harga");
            System.out.println("--------------------------------------------------------------------------");
    
            daftarIceCream.clear(); // Clear the list before repopulating it
            int nomor = 1; // Inisialisasi variabel nomor di sini
    
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int stok = resultSet.getInt("stok");
                double harga = resultSet.getDouble("harga");
    
                IceCream iceCream = new IceCream(rasa, topping, stok, harga);
                iceCream.setId(id); // Set ID dari database
                daftarIceCream.add(iceCream);
    
                System.out.printf("| %-3d | %-20s | %-20s | %-5d | %-10.2f |\n", nomor, rasa, topping, stok, harga);
                System.out.println("--------------------------------------------------------------------------");
                nomor++;
            }
    
            if (daftarIceCream.isEmpty()) {
                System.out.println("|                   Tidak Ada Ice Cream Yang Tersedia.                    |");
                System.out.println("==========================================================================");
            }
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal Mengambil Data dari Database!: " + e.getMessage());
        }
    }
    
    private static void ubahIceCream(Scanner scanner, ArrayList<IceCream> daftarIceCream) {
        lihatIceCream(daftarIceCream); // Tampilkan daftar Ice Cream dengan nomor
    
        System.out.print("Masukkan Nomor Ice Cream yang Ingin Diubah: ");
        int nomor = scanner.nextInt();
        scanner.nextLine(); // Membersihkan newline dari buffer
    
        if (nomor > 0 && nomor <= daftarIceCream.size()) {
            IceCream iceCream = daftarIceCream.get(nomor - 1); // Ambil item dari ArrayList menggunakan nomor - 1 sebagai indeks
            int id = iceCream.getId(); // Dapatkan ID ice cream
            String rasa = iceCream.getRasa();
    
            System.out.println("Data Ice Cream yang akan diubah:");
            System.out.println("ID: " + id);
            System.out.println("Rasa: " + rasa);
    
            // Meminta konfirmasi pengguna
            System.out.print("Apakah Anda ingin mengubah ice cream ini? (y/n): ");
            String konfirmasi = scanner.nextLine();
    
            if (konfirmasi.equalsIgnoreCase("y")) {
                String newRasa = null, newTopping = null;
                double newHarga = 0;
                int newStok = -1;
    
                // Input and validate new rasa
                int attempts = 0;
                while (attempts < 3) {
                    System.out.print("Masukkan Rasa Baru: ");
                    newRasa = scanner.nextLine();
                    if (newRasa.matches("[a-zA-Z ]+")) {
                        break;
                    } else {
                        System.out.println("Silahkan masukkan huruf.");
                        attempts++;
                    }
                }
                if (attempts == 3) {
                    System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
                    return;
                }
    
                // Input and validate new topping
                attempts = 0;
                while (attempts < 3) {
                    System.out.print("Masukkan Topping Baru: ");
                    newTopping = scanner.nextLine();
                    if (newTopping.matches("[a-zA-Z ]+")) {
                        break;
                    } else {
                        System.out.println("Silahkan masukkan huruf.");
                        attempts++;
                    }
                }
                if (attempts == 3) {
                    System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
                    return;
                }
    
                // Input and validate new harga
                attempts = 0;
                while (attempts < 3) {
                    try {
                        System.out.print("Masukkan Harga Baru: Rp. ");
                        newHarga = Double.parseDouble(scanner.nextLine());
                        if (newHarga >= 0) {
                            break;
                        } else {
                            System.out.println("Harga tidak boleh negatif.");
                            attempts++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Silahkan masukkan angka.");
                        attempts++;
                    }
                }
                if (attempts == 3) {
                    System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
                    return;
                }
    
                // Input and validate new stok
                attempts = 0;
                while (attempts < 3) {
                    try {
                        System.out.print("Masukkan Stok Baru: ");
                        newStok = Integer.parseInt(scanner.nextLine());
                        if (newStok >= 0) {
                            break;
                        } else {
                            System.out.println("Stok tidak boleh negatif.");
                            attempts++;
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Silahkan masukkan angka.");
                        attempts++;
                    }
                }
                if (attempts == 3) {
                    System.out.println("Percobaan maksimal tercapai. Kembali ke menu utama.");
                    return;
                }
    
                // Update data ice cream
                iceCream.setRasa(newRasa);
                iceCream.setTopping(newTopping);
                iceCream.setHarga(newHarga);
                iceCream.setStok(newStok);
    
                // Update ice cream di database
                if (updateIceCreamInDatabase(iceCream)) {
                    System.out.println("Ice Cream Berhasil Diubah!");
                } else {
                    System.out.println("Gagal mengubah Ice Cream.");
                }
            } else if (konfirmasi.equalsIgnoreCase("n")) {
                System.out.println("Pengubahan Ice Cream Dibatalkan.");
            } else {
                System.out.println("Masukan tidak valid. Pengubahan dibatalkan.");
            }
        } else {
            System.out.println("Nomor Ice Cream Tidak Valid.");
        }
    }
    
    private static boolean updateIceCreamInDatabase(IceCream iceCream) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "UPDATE tbicecream SET rasa = ?, topping = ?, harga = ?, stok = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, iceCream.getRasa());
            statement.setString(2, iceCream.getTopping());
            statement.setDouble(3, iceCream.getHarga());
            statement.setInt(4, iceCream.getStok());
            statement.setInt(5, iceCream.getId()); // Menggunakan id untuk WHERE clause
    
            int rowsUpdated = statement.executeUpdate();
            statement.close();
            connection.close();
    
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Gagal mengubah Ice Cream di Database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    private static void hapusIceCream(Scanner scanner, ArrayList<IceCream> daftarIceCream) {
        lihatIceCream(daftarIceCream); // Tampilkan daftar Ice Cream dengan nomor
        
        System.out.print("Masukkan Nomor Ice Cream yang Ingin Dihapus: ");
        int nomor = scanner.nextInt();
        scanner.nextLine(); // Membersihkan newline dari buffer
        
        if (nomor > 0 && nomor <= daftarIceCream.size()) {
            IceCream iceCream = daftarIceCream.get(nomor - 1);
            String rasa = iceCream.getRasa();
        
            // Meminta konfirmasi pengguna
            System.out.print("Apakah Anda yakin ingin menghapus ice cream dengan rasa " + rasa + "? (y/n): ");
            String konfirmasi = scanner.nextLine();
        
            if (konfirmasi.equalsIgnoreCase("y")) {
                try {
                    Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                    String sql = "DELETE FROM tbicecream WHERE rasa = ?";
                    PreparedStatement statement = connection.prepareStatement(sql);
                    statement.setString(1, rasa);
        
                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Ice Cream dengan rasa " + rasa + " berhasil dihapus dari Database!!");
                        // Hapus juga dari daftarIceCream
                        daftarIceCream.remove(nomor - 1);
                    } else {
                        System.out.println("Ice Cream dengan Rasa " + rasa + " Tidak Ditemukan di Database.");
                    }
        
                    statement.close();
                    connection.close();
                } catch (SQLException e) {
                    System.out.println("Ice Cream Gagal Dihapus dari Database!: " + e.getMessage());
                }
            } else if (konfirmasi.equalsIgnoreCase("n")) {
                System.out.println("Penghapusan Ice Cream Dibatalkan.");
            } else {
                System.out.println("Masukan tidak valid. Penghapusan dibatalkan.");
            }
        
        } else {
            System.out.println("Nomor Ice Cream Tidak Valid.");
        }
    }
         
        
        private static void cekTransaksiPengguna(ArrayList<User> daftarUser) {
            try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbtransaksi";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("======================================================================================================================================================================");
            System.out.println("|                                                                       DAFTAR SEMUA TRANSAKSI                                                                       |");
            System.out.println("======================================================================================================================================================================");
            System.out.printf("| %-3s | %-36s | %-10s | %-20s | %-20s | %-6s | %-10s | %-12s | %-19s |\n",
                    "No", "ID Transaksi", "ID Pelanggan", "Rasa", "Topping", "Jumlah", "Harga", "Total Harga", "Waktu Transaksi");
            System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            
            int nomor = 1;
            while (resultSet.next()) {
                String idTransaksi = resultSet.getString("id");
                int idCustomer = resultSet.getInt("idcustomer");
                int idIceCream = resultSet.getInt("idicecream");
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int jumlah = resultSet.getInt("jumlah");
                double harga = resultSet.getDouble("harga");
                double totalHarga = resultSet.getDouble("total_harga");
                LocalDateTime waktuTransaksi = resultSet.getTimestamp("waktu_transaksi").toLocalDateTime();
                
                System.out.printf("| %-3d | %-10s | %-12d | %-20s | %-20s | %-6d | %-10.2f | %-12.2f | %-19s |\n",
                nomor, idTransaksi, idCustomer, rasa, topping, jumlah, harga, totalHarga, waktuTransaksi);
                System.out.println("----------------------------------------------------------------------------------------------------------------------------------------------------------------------");
                nomor++;
            }

            if (nomor == 1) {
                System.out.println("Belum ada transaksi.");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan transaksi: " + e.getMessage());
        }
    }

    public static void tampilkanDataCustomer() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT `username`, `password`, `nomor_hp`, `email`, `jenis_kelamin`, `alamat`, `id` FROM `tbcustomer`";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("=====================================================================================================================================");
            System.out.println("|                                                            DATA CUSTOMER                                                          |");
            System.out.println("=====================================================================================================================================");
            System.out.printf("| %-4s | %-15s | %-15s | %-25s | %-25s | %-15s | %-12s |\n",
                    "ID", "Username", "Password", "Nomor HP", "Email", "Jenis Kelamin", "Alamat");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            
            while (resultSet.next()) {
                String username = resultSet.getString("username");
                String password = resultSet.getString("password");
                String nomorHp = resultSet.getString("nomor_hp");
                String email = resultSet.getString("email");
                String jenisKelamin = resultSet.getString("jenis_kelamin");
                String alamat = resultSet.getString("alamat");
                int id = resultSet.getInt("id");
                
                System.out.printf("| %-4d | %-15s | %-15s | %-25s | %-25s | %-15s | %-12s |\n",
                id, username, password, nomorHp, email, jenisKelamin, alamat);
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------");
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan data customer: " + e.getMessage());
        }
    }

    public static void hapusCustomer(Scanner scanner) {
        tampilkanDataCustomer(); // Tampilkan data customer terlebih dahulu
    
        System.out.print("Masukkan ID Customer yang ingin dihapus: ");
        int idCustomer = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbcustomer WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idCustomer);
            ResultSet resultSet = statement.executeQuery();
    
            if (resultSet.next()) {
                // Menampilkan data customer yang akan dihapus
                System.out.println("Data Customer yang akan dihapus:");
                System.out.println("ID: " + resultSet.getInt("id"));
                System.out.println("Username: " + resultSet.getString("username"));
                System.out.println("Password: " + resultSet.getString("password"));
                System.out.println("Nomor HP: " + resultSet.getString("nomor_hp"));
                System.out.println("Email: " + resultSet.getString("email"));
                System.out.println("Jenis Kelamin: " + resultSet.getString("jenis_kelamin"));
                System.out.println("Alamat: " + resultSet.getString("alamat"));
    
                // Meminta konfirmasi untuk menghapus
                System.out.print("Apakah Anda yakin ingin menghapus data customer ini? (y/n): ");
                String konfirmasi = scanner.nextLine();
                if (konfirmasi.equalsIgnoreCase("y")) {
                    // Melakukan penghapusan data
                    sql = "DELETE FROM tbcustomer WHERE id = ?";
                    statement = connection.prepareStatement(sql);
                    statement.setInt(1, idCustomer);
                    int rowsDeleted = statement.executeUpdate();
                    if (rowsDeleted > 0) {
                        System.out.println("Customer dengan ID " + idCustomer + " berhasil dihapus.");
                    } else {
                        System.out.println("Gagal menghapus customer dengan ID " + idCustomer + ".");
                    }
                } else {
                    System.out.println("Penghapusan data dibatalkan.");
                    return; // Keluar dari metode hapusCustomer
                }
            } else {
                System.out.println("Customer dengan ID " + idCustomer + " tidak ditemukan.");
            }
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menghapus customer: " + e.getMessage());
        }
        System.out.print("Apakah Anda ingin kembali ke menu sebelumnya? (y/n): ");
        String kembali = scanner.nextLine();
        if (!kembali.equalsIgnoreCase("y")) {
            hapusCustomer(scanner); // Rekursif jika tidak ingin kembali ke menu sebelumnya
        }
    }
}
        