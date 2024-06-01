import Eskrim.IceCream;
import java.time.LocalDateTime;
import java.util.UUID;


public class Transaksi {
    private UUID idTransaksi;
    private IceCream iceCream;
    private int jumlah;
    private double totalHarga;
    private LocalDateTime waktuTransaksi;

    public Transaksi(IceCream iceCream, int jumlah, double totalHarga, LocalDateTime waktuTransaksi) {
        this.idTransaksi = UUID.randomUUID(); // Generate random UUID
        this.iceCream = iceCream;
        this.jumlah = jumlah;
        this.totalHarga = totalHarga;
        this.waktuTransaksi = waktuTransaksi;
    }

    public UUID getIdTransaksi() {
        return idTransaksi;
    }

    public IceCream getIceCream() {
        return iceCream;
    }

    public int getJumlah() {
        return jumlah;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public LocalDateTime getWaktuTransaksi() {
        return waktuTransaksi;
    }
}
