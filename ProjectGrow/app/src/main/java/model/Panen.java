package model;

public class Panen {
    private int id;
    private int idUser;
    private String namaLahan;
    private String jenisTanaman;
    private double jumlahPanen;
    private String satuan;
    private String tanggalPanen;
    private String kondisiCuaca;

    public Panen(int idUser, String namaLahan, String jenisTanaman, double jumlahPanen, String satuan, String tanggalPanen, String kondisiCuaca) {
        this.idUser = idUser;
        this.namaLahan = namaLahan;
        this.jenisTanaman = jenisTanaman;
        this.jumlahPanen = jumlahPanen;
        this.satuan = satuan;
        this.tanggalPanen = tanggalPanen;
        this.kondisiCuaca = kondisiCuaca;
    }

    public Panen(int id, int idUser, String namaLahan, String jenisTanaman, double jumlahPanen, String satuan, String tanggalPanen, String kondisiCuaca) {
        this.id = id;
        this.idUser = idUser;
        this.namaLahan = namaLahan;
        this.jenisTanaman = jenisTanaman;
        this.jumlahPanen = jumlahPanen;
        this.satuan = satuan;
        this.tanggalPanen = tanggalPanen;
        this.kondisiCuaca = kondisiCuaca;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdUser(){ return idUser; }
    public void setIdUser(int idUser){ this.idUser = idUser; }

    public String getNamaLahan() { return namaLahan; }
    public void setNamaLahan(String namaLahan) { this.namaLahan = namaLahan; }

    public String getJenisTanaman() { return jenisTanaman; }
    public void setJenisTanaman(String jenisTanaman) { this.jenisTanaman = jenisTanaman; }

    public double getJumlahPanen() { return jumlahPanen; }
    public void setJumlahPanen(double jumlahPanen) { this.jumlahPanen = jumlahPanen; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public String getTanggalPanen() { return tanggalPanen; }
    public void setTanggalPanen(String tanggalPanen) { this.tanggalPanen = tanggalPanen; }

    public String getKondisiCuaca() { return kondisiCuaca; }
    public void setKondisiCuaca(String kondisiCuaca) { this.kondisiCuaca = kondisiCuaca; }
}
