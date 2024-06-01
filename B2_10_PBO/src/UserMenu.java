import Eskrim.User;
import Eskrim.IceCream;
import Eskrim.Transaksi;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Timestamp;
import java.util.UUID;


public class UserMenu {
    private User user;
    private List<Transaksi> transaksiList;

    // Database connection details
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

    // Constructor that accepts a User parameter
    public UserMenu(User user) {
        this.user = user;
        this.transaksiList = new ArrayList<>(); // Initialize transaksiList
    }

    private void cariIceCream(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        int pilihanCari;
        do {
            System.out.println("=========================================");
            System.out.println("          MENU PENCARIAN ICE CREAM       ");
            System.out.println("=========================================");
            System.out.println("[1] Cari berdasarkan rasa");
            System.out.println("[2] Cari berdasarkan topping");
            System.out.println("[3] Keluar");
            System.out.print(">> Pilihan Anda: ");
            pilihanCari = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character
    
            switch (pilihanCari) {
                case 1:
                    cariIceCreamBerdasarkanRasa(daftarIceCream, scanner);
                    break;
                case 2:
                    cariIceCreamBerdasarkanTopping(daftarIceCream, scanner);
                    break;
                case 3:
                    System.out.println("Keluar dari menu pencarian.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid! Silakan coba lagi.");
            }
        } while (pilihanCari != 3);
    }
    
    private void cariIceCreamBerdasarkanRasa(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        System.out.print("Masukkan Rasa Ice Cream yang ingin dicari: ");
        String rasaCari = scanner.nextLine();
        System.out.println("================================================");
        System.out.println("        HASIL PENCARIAN BERDASARKAN RASA        ");
        System.out.println("================================================");
    
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbicecream WHERE rasa LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + rasaCari + "%");
            ResultSet resultSet = statement.executeQuery();
    
            boolean ditemukan = false;
            while (resultSet.next()) {
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int stok = resultSet.getInt("stok");
                double harga = resultSet.getDouble("harga");
    
                IceCream iceCream = new IceCream(rasa, topping, stok, harga);
                System.out.println("Rasa: " + rasa + ", Topping: " + topping + ", Stok: " + stok + ", Harga: Rp.  " + harga);
                ditemukan = true;
            }
    
            if (!ditemukan) {
                System.out.println("Ice cream dengan rasa '" + rasaCari + "' tidak ditemukan.");
            }
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal melakukan pencarian berdasarkan rasa: " + e.getMessage());
        }
    }
    
    private void cariIceCreamBerdasarkanTopping(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        System.out.print("Masukkan topping ice cream yang ingin dicari: ");
        String toppingCari = scanner.nextLine();
        System.out.println("=================================================");
        System.out.println("       HASIL PENCARIAN BERDASARKAN TOPPING       ");
        System.out.println("=================================================");
    
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbicecream WHERE topping LIKE ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, "%" + toppingCari + "%");
            ResultSet resultSet = statement.executeQuery();
    
            boolean ditemukan = false;
            while (resultSet.next()) {
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int stok = resultSet.getInt("stok");
                double harga = resultSet.getDouble("harga");
    
                IceCream iceCream = new IceCream(rasa, topping, stok, harga);
                System.out.println("Rasa: " + rasa + ", Topping: " + topping + ", Stok: " + stok + ", Harga: Rp.  " + harga);
                ditemukan = true;
            }
    
            if (!ditemukan) {
                System.out.println("Ice cream dengan topping '" + toppingCari + "' tidak ditemukan.");
            }
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal melakukan pencarian berdasarkan topping: " + e.getMessage());
        }
    }
    
    
    public void showMenu(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        int pilihan;
        do {
            System.out.println("==============================================");
            System.out.println("====>       SELAMAT DATANG CUSTOMER!     <====");
            System.out.println("==============================================");
            System.out.println("           1.| Lihat Semua Ice Cream          ");
            System.out.println("           2.| Cari Ice Cream                 ");
            System.out.println("           3.| Beli Ice Cream                 ");
            System.out.println("           4.| Lihat Keranjang                ");
            System.out.println("           5.| Lihat Transaksi                ");
            System.out.println("           6.| Kembali                        ");
            System.out.println("==============================================");
            System.out.print(">> Pilihan Anda: ");
            pilihan = scanner.nextInt();
            scanner.nextLine();

            switch (pilihan) {
                case 1:
                    lihatIceCream(daftarIceCream);
                    break;
                case 2:
                    cariIceCream(daftarIceCream, scanner);
                    break;
                case 3:
                    beliIceCream(daftarIceCream, scanner);
                    break;
                case 4:
                    lihatKeranjang(daftarIceCream, scanner);
                    break;
                case 5:
                    lihatTransaksi();
                    break;
                case 6:
                    System.out.println("Kembali Ke Menu Utama.");
                    break;
                default:
                    System.out.println("-> Pilihan Tidak Valid! Silakan Coba Lagi :>");
            }
        } while (pilihan != 6);
    }

    private static void lihatIceCream(ArrayList<IceCream> daftarIceCream) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "SELECT * FROM tbicecream";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            System.out.println("=============================================================================");
            System.out.println("|                           DAFTAR SEMUA ICE CREAM                          |");
            System.out.println("=============================================================================");
            System.out.printf("| %-4s | %-17s | %-17s | %-6s | %-17s |\n", "No", "Rasa", "Topping", "Stok", "Harga");
            System.out.println("-----------------------------------------------------------------------------");
            
            daftarIceCream.clear(); // Clear the list before repopulating it
            int nomor = 1;
            while (resultSet.next()) {
                int id = resultSet.getInt("id"); // Dapatkan ID dari kolom "id"
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int stok = resultSet.getInt("stok");
                double harga = resultSet.getDouble("harga");
                
                IceCream iceCream = new IceCream(rasa, topping, stok, harga);
                iceCream.setId(id); // Set ID ke objek IceCream
                daftarIceCream.add(iceCream);
                
                System.out.printf("| %-4s | %-17s | %-17s | %-6s | Rp. %-13.2f |\n", nomor, rasa, topping, stok, harga);
                System.out.println("--------------------------------------------------------------------------------------------");
                nomor++;
            }

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal Mengambil Data dari Database!: " + e.getMessage());
        }
    }


    private void updateStokIceCream(IceCream iceCream, int jumlah) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "UPDATE tbicecream SET stok = stok - ? WHERE rasa = ? AND topping = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, jumlah); // Mengurangkan stok dengan jumlah yang dibeli
            statement.setString(2, iceCream.getRasa());
            statement.setString(3, iceCream.getTopping());

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal mengupdate stok Ice Cream di database: " + e.getMessage());
        }
    }

    private void beliIceCream(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        // Menampilkan data ice cream dari database
        lihatIceCream(daftarIceCream);

        System.out.println("======================================");
        System.out.println("            PEMBELIAN ICE CREAM        ");
        System.out.println("======================================");

        System.out.print("Masukkan nomor Ice Cream yang ingin dibeli: ");
        int nomor = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character

        if (nomor >= 1 && nomor <= daftarIceCream.size()) {
            IceCream iceCream = daftarIceCream.get(nomor - 1);

            System.out.println("Anda memilih: " + iceCream.getRasa() + " - " + iceCream.getTopping());
            System.out.print("Masukkan jumlah yang ingin dibeli: ");
            int jumlah = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character

            if (jumlah <= iceCream.getStok()) {
                double totalHarga = jumlah * iceCream.getHarga();
                System.out.println("Total Harga: Rp.  " + totalHarga);

                System.out.println("     Pilih tindakan selanjutnya:  ");
                System.out.println("[1] Tambahkan ke keranjang");
                System.out.println("[2] Bayar langsung");
                System.out.println("[3] Keluar");
                System.out.print(">> Pilihan Anda: ");
                int tindakan = scanner.nextInt();
                scanner.nextLine(); // Clear the newline character

                switch (tindakan) {
                    case 1:
                        // Tambahkan item ke keranjang
                        tambahKeKeranjang(iceCream, jumlah, iceCream.getRasa(), iceCream.getTopping());
                        break;
                    case 2:
                        // Bayar langsung
                        LocalDateTime waktuTransaksi = LocalDateTime.now();
                        UUID idTransaksi = UUID.randomUUID();

                        try {
                            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                            String sql = "INSERT INTO tbtransaksi (id, idcustomer, idicecream, rasa, topping, jumlah, harga, total_harga, waktu_transaksi) " +
                                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                            PreparedStatement transaksiStatement = connection.prepareStatement(sql);
                            transaksiStatement.setString(1, idTransaksi.toString());
                            transaksiStatement.setInt(2, user.getId());
                            transaksiStatement.setInt(3, iceCream.getId());
                            transaksiStatement.setString(4, iceCream.getRasa());
                            transaksiStatement.setString(5, iceCream.getTopping());
                            transaksiStatement.setInt(6, jumlah);
                            transaksiStatement.setDouble(7, iceCream.getHarga());
                            transaksiStatement.setDouble(8, totalHarga);
                            transaksiStatement.setTimestamp(9, Timestamp.valueOf(waktuTransaksi));

                            int rowsInserted = transaksiStatement.executeUpdate();
                            if (rowsInserted > 0) {
                                System.out.println("Pembelian berhasil. Transaksi telah dicatat.");
                            }

                            updateStokIceCream(iceCream, jumlah); // Update stok ice cream di database

                            transaksiStatement.close();
                            connection.close();
                        } catch (SQLException e) {
                            System.out.println("Gagal melakukan pembelian: " + e.getMessage());
                        }
                        break;
                    case 3:
                        System.out.println("Keluar.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            } else {
                System.out.println("Stok tidak mencukupi untuk pembelian ini.");
            }
        } else {
            System.out.println("Nomor Ice Cream tidak valid.");
        }
    }
    private void tambahKeKeranjang(IceCream iceCream, int jumlah, String rasa, String topping) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    
            // Mendapatkan ID Pelanggan dari tabel tbcustomer berdasarkan pengguna yang sedang login
            int idPelanggan = user.getId();
    
            // Menambahkan item ke keranjang dengan id baru
            String sql = "INSERT INTO tbkeranjang (idcustomer, idicecream, rasa, topping, jumlah, total_harga, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idPelanggan); // Menggunakan id pengguna yang sedang login
            statement.setInt(2, iceCream.getId()); // Menggunakan id ice cream yang dipesan
            statement.setString(3, rasa); // Menambahkan rasa ke keranjang
            statement.setString(4, topping); // Menambahkan topping ke keranjang
            statement.setInt(5, jumlah);
            double totalHarga = jumlah * iceCream.getHarga();
            statement.setDouble(6, totalHarga);
            statement.setString(7, "Belum dibeli"); // Status awal
    
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Item berhasil ditambahkan ke keranjang.");
            }
    
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menambahkan item ke keranjang: " + e.getMessage());
        }
    }
    
    
    
    private void lihatKeranjang(ArrayList<IceCream> daftarIceCream, Scanner scanner) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            int idPelanggan = user.getId(); // Mendapatkan ID pelanggan dari objek User yang sedang login
            String sql = "SELECT k.id, k.idcustomer, i.id as idicecream, i.rasa, i.topping, k.jumlah, k.total_harga, k.status " +
                         "FROM tbkeranjang k " +
                         "JOIN tbicecream i ON k.idicecream = i.id " +
                         "WHERE k.idcustomer = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idPelanggan);
            ResultSet resultSet = statement.executeQuery();
    
            System.out.println("===========================================================================================================================================");
            System.out.println("|                                                           KERANJANG BELANJA                                                             |");
            System.out.println("===========================================================================================================================================");
            System.out.printf("| %-4s | %-12s | %-12s | %-12s | %-17s | %-17s | %-7s | %-15s | %-15s |\n",
                    "No", "ID Keranjang", "ID Customer", "ID Ice Cream", "Rasa", "Topping", "Jumlah", "Total Harga", "Status");
            System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
            
            double totalHargaKeranjang = 0;
            int nomor = 1;
            while (resultSet.next()) {
                int idKeranjang = resultSet.getInt("id");
                int idCustomer = resultSet.getInt("idcustomer");
                int idIceCream = resultSet.getInt("idicecream");
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                int jumlah = resultSet.getInt("jumlah");
                double totalHargaItem = resultSet.getDouble("total_harga");
                String status = resultSet.getString("status");
                
                System.out.printf("| %-4s | %-12s | %-12s | %-12s | %-17s | %-17s | %-7s | %-15s | %-15s |\n",
                nomor, idKeranjang, idCustomer, idIceCream, rasa, topping, jumlah, totalHargaItem, status);
                System.out.println("-------------------------------------------------------------------------------------------------------------------------------------------");
                totalHargaKeranjang += totalHargaItem;
                nomor++;
            }
    
            if (nomor == 1) {
                System.out.println("Keranjang belanja kosong.");
            } else {
                System.out.println("-----------------------------------------------");
                System.out.println("Total Harga Keranjang: Rp.  " + totalHargaKeranjang);
                System.out.println("Pilihan:");
                System.out.println("[1] Tambahkan item baru ke keranjang");
                System.out.println("[2] Hapus item dari keranjang");
                System.out.println("[3] Checkout");
                System.out.println("[4] Kembali ke menu utama");
                System.out.print(">> Pilihan Anda: ");
                int pilihan = scanner.nextInt();
                scanner.nextLine(); // Clear the newline character
    
                switch (pilihan) {
                    case 1:
                        tambahItemBaruKeKeranjang(daftarIceCream, scanner, idPelanggan);
                        break;
                    case 2:
                        hapusItemKeranjang(scanner);
                        break;
                    case 3:
                        checkoutKeranjang();
                        break;
                    case 4:
                        System.out.println("Kembali ke menu utama.");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid.");
                }
            }
    
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menampilkan keranjang belanja: " + e.getMessage());
        }
    }
    
    private void tambahItemBaruKeKeranjang(ArrayList<IceCream> daftarIceCream, Scanner scanner, int idPelanggan) {
        // Menampilkan data ice cream dari database
        lihatIceCream(daftarIceCream);
    
        System.out.println("=================================================");
        System.out.println("                 PEMBELIAN ICE CREAM             ");
        System.out.println("=================================================");
    
        System.out.print("Masukkan nomor Ice Cream yang ingin dibeli: ");
        int nomor = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        if (nomor >= 1 && nomor <= daftarIceCream.size()) {
            IceCream iceCream = daftarIceCream.get(nomor - 1);
    
            System.out.println("Anda memilih: " + iceCream.getRasa() + " - " + iceCream.getTopping());
            System.out.print("Masukkan jumlah yang ingin dibeli: ");
            int jumlah = scanner.nextInt();
            scanner.nextLine(); // Clear the newline character
    
            try {
                Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    
                // Menambahkan item ke keranjang
                String sql = "INSERT INTO tbkeranjang (idcustomer, idicecream, rasa, topping, jumlah, total_harga, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.setInt(1, idPelanggan);
                statement.setInt(2, iceCream.getId());
                statement.setString(3, iceCream.getRasa());
                statement.setString(4, iceCream.getTopping());
                statement.setInt(5, jumlah);
                double totalHarga = jumlah * iceCream.getHarga();
                statement.setDouble(6, totalHarga);
                statement.setString(7, "Belum dibeli");
    
                int rowsInserted = statement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Item berhasil ditambahkan ke keranjang.");
                }
    
                statement.close();
                connection.close();
            } catch (SQLException e) {
                System.out.println("Gagal menambahkan item ke keranjang: " + e.getMessage());
            }
        } else {
            System.out.println("Nomor Ice Cream tidak valid.");
        }
    }
    
    private void hapusItemKeranjang(Scanner scanner) {
        System.out.print("Masukkan ID Keranjang yang ingin dihapus: ");
        int idKeranjang = scanner.nextInt();
        scanner.nextLine(); // Clear the newline character
    
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "DELETE FROM tbkeranjang WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idKeranjang);
    
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Item berhasil dihapus dari keranjang.");
            } else {
                System.out.println("Tidak ada item dengan ID Keranjang tersebut dalam keranjang.");
            }
    
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menghapus item dari keranjang: " + e.getMessage());
        }
    }
    
    private void checkoutKeranjang() {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            int idPelanggan = user.getId(); // Mendapatkan ID pelanggan dari objek User yang sedang login
            String sql = "SELECT k.id, k.idcustomer, k.idicecream, k.jumlah, k.total_harga, i.rasa, i.topping, i.harga, i.stok " +
                         "FROM tbkeranjang k " +
                         "JOIN tbicecream i ON k.idicecream = i.id " +
                         "WHERE k.idcustomer = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, idPelanggan);
            ResultSet resultSet = statement.executeQuery();

            double totalHargaKeranjang = 0;
            List<String> receiptItems = new ArrayList<>();

            while (resultSet.next()) {
                int idIceCream = resultSet.getInt("idicecream");
                int jumlah = resultSet.getInt("jumlah");
                double totalHargaItem = resultSet.getDouble("total_harga");
                String rasa = resultSet.getString("rasa");
                String topping = resultSet.getString("topping");
                double harga = resultSet.getDouble("harga");
                int stok = resultSet.getInt("stok");

                totalHargaKeranjang += totalHargaItem;

                // Generate receipt item
                String receiptItem = String.format("%-15s %-10s %-5d %-10.2f %-10.2f", rasa, topping, jumlah, harga, totalHargaItem);
                receiptItems.add(receiptItem);

                // Masukkan data transaksi ke dalam tabel tbtransaksi
                LocalDateTime waktuTransaksi = LocalDateTime.now();
                UUID idTransaksi = UUID.randomUUID();

                sql = "INSERT INTO tbtransaksi (id, idcustomer, idicecream, rasa, topping, jumlah, harga, total_harga, waktu_transaksi) " +
                      "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement transaksiStatement = connection.prepareStatement(sql);
                transaksiStatement.setString(1, idTransaksi.toString()); // Menggunakan UUID sebagai string
                transaksiStatement.setInt(2, idPelanggan);
                transaksiStatement.setInt(3, idIceCream);
                transaksiStatement.setString(4, rasa);
                transaksiStatement.setString(5, topping);
                transaksiStatement.setInt(6, jumlah);
                transaksiStatement.setDouble(7, harga);
                transaksiStatement.setDouble(8, totalHargaItem);
                transaksiStatement.setTimestamp(9, Timestamp.valueOf(waktuTransaksi));

                int rowsInserted = transaksiStatement.executeUpdate();
                if (rowsInserted > 0) {
                    System.out.println("Transaction successful for item: " + rasa);
                }

                transaksiStatement.close();

                // Update stok ice cream di database
                updateStokIceCream(idIceCream, jumlah, stok);
            }

            statement.close();

            // Setelah checkout, hapus semua item dari keranjang
            sql = "DELETE FROM tbkeranjang WHERE idcustomer = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, idPelanggan);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Checkout berhasil");

            } else {
                System.out.println("Checkout gagal.");
            }

            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal melakukan proses checkout: " + e.getMessage());
        }
    }

    
    
    
    private void updateStokIceCream(int idIceCream, int jumlah, int stok) {
        try {
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            String sql = "UPDATE tbicecream SET stok = ? WHERE id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, stok - jumlah); // Mengurangkan stok dengan jumlah yang dibeli
            statement.setInt(2, idIceCream);
    
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("");
            }
    
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal mengupdate stok Ice Cream di database: " + e.getMessage());
        }
    }
    
    
    private void lihatTransaksi() {
    try {
        Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        int idPelanggan = user.getId(); // Mendapatkan ID pelanggan dari objek User yang sedang login
        String sql = "SELECT `id`, `idcustomer`, `idicecream`, `rasa`, `topping`, `jumlah`, `harga`, `total_harga`, `waktu_transaksi` FROM `tbtransaksi` WHERE `idcustomer` = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, idPelanggan);
        ResultSet resultSet = statement.executeQuery();

        System.out.println("======================================================================================================================================================");
        System.out.println("|                                                         DAFTAR TRANSAKSI                                                                           |");
        System.out.println("======================================================================================================================================================");
        System.out.printf("| %-4s | %-36s | %-12s | %-12s | %-15s | %-15s | %-6s | %-10s | %-12s |\n",
                "NO", "ID Transaksi", "ID Pelanggan", "ID Ice Cream", "Rasa", "Topping", "Jumlah", "Harga", "Total Harga");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

        boolean adaTransaksi = false;
        int nomor = 1;
        while (resultSet.next()) {
            adaTransaksi = true;
            String idTransaksi = resultSet.getString("id");
            int idCustomer = resultSet.getInt("idcustomer");
            int idIceCream = resultSet.getInt("idicecream");
            String rasa = resultSet.getString("rasa");
            String topping = resultSet.getString("topping");
            int jumlah = resultSet.getInt("jumlah");
            double harga = resultSet.getDouble("harga");
            double totalHarga = resultSet.getDouble("total_harga");
            LocalDateTime waktuTransaksi = resultSet.getTimestamp("waktu_transaksi").toLocalDateTime();

            // Format waktu transaksi
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedWaktuTransaksi = waktuTransaksi.format(formatter);

            System.out.printf("| %-4d | %-36s | %-12d | %-12d | %-15s | %-15s | %-6d | %-10.2f | %-12.2f |\n",
                    nomor, idTransaksi, idCustomer, idIceCream, rasa, topping, jumlah, harga, totalHarga);
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");

            // Print waktu transaksi pada baris kedua setelah setiap entri transaksi
            System.out.printf("| %-4s | %-104s |\n", "", "Waktu Transaksi: " + formattedWaktuTransaksi);
            System.out.println("------------------------------------------------------------------------------------------------------------------------------------------------------");
            
            nomor++;
        }

        if (!adaTransaksi) {
            System.out.println("|                                                           Belum ada transaksi.                                                                     |");
            System.out.println("======================================================================================================================================================");
        } else {
            System.out.println("======================================================================================================================================================");
        }

        try {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            System.out.println("Gagal menutup sumber daya database: " + e.getMessage());
        }
    } catch (SQLException e) {
        System.out.println("Gagal menampilkan transaksi: " + e.getMessage());
    }
}
}