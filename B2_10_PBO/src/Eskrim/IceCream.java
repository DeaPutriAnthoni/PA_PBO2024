package Eskrim;

public class IceCream {
    private int id;
    private String rasa;
    private String topping;
    private int stok;
    private double harga;

    public IceCream(String rasa, String topping, int stok, double harga) {
        this.rasa = rasa;
        this.topping = topping;
        this.stok = stok;
        this.harga = harga;
    }

    public String getRasa() {
        return rasa;
    }

    public void setRasa(String rasa) {
        this.rasa = rasa;
    }

    public String getTopping() {
        return topping;
    }

    public void setTopping(String topping) {
        this.topping = topping;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getId() {
        return id;
    }

    public void setId(int Id) {
        this.id = Id;
    }

    @Override
    public String toString() {
        return "Rasa: " + rasa + ", Topping: " + topping + ", Stok: " + stok + ", Harga: " + harga;
    }
}
