package service;

import database.DatabaseConnection;
import model.Panen;
import session.UserSession;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PanenService {
    
    public void tambahPanen(Panen panen) {
        try {
            int idUser = UserSession.requireUserId();
            String sql = "INSERT INTO panen (id_user, nama_lahan, jenis_tanaman, jumlah_panen, satuan, tanggal_panen, kondisi_cuaca) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?)";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, idUser);
                pstmt.setString(2, panen.getNamaLahan());
                pstmt.setString(3, panen.getJenisTanaman());
                pstmt.setDouble(4, panen.getJumlahPanen());
                pstmt.setString(5, panen.getSatuan());
                pstmt.setString(6, panen.getTanggalPanen());
                pstmt.setString(7, panen.getKondisiCuaca());

                pstmt.executeUpdate();
                System.out.println("Data panen berhasil disimpan!");

            } catch (SQLException e) {
                System.out.println("Gagal menyimpan data: " + e.getMessage());
            }
        } catch (IllegalStateException e) {
            System.out.println("Akses ditolak: " + e.getMessage());
        }
    }

    public List<Panen> getAllPanen() {
        List<Panen> listPanen = new ArrayList<>();
        try {
            int idUser = UserSession.requireUserId();
            String sql = "SELECT * FROM panen WHERE id_user = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                 
                pstmt.setInt(1, idUser);
                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Panen p = new Panen(
                                rs.getInt("id"),
                                rs.getInt("id_user"),
                                rs.getString("nama_lahan"),
                                rs.getString("jenis_tanaman"),
                                rs.getDouble("jumlah_panen"),
                                rs.getString("satuan"),
                                rs.getString("tanggal_panen"),
                                rs.getString("kondisi_cuaca")
                        );
                        listPanen.add(p);
                    }
                }
            } catch (SQLException e) {
                System.out.println("Gagal mengambil data: " + e.getMessage());
            }
        } catch (IllegalStateException e) {
            System.out.println("Akses ditolak: " + e.getMessage());
        }
        return listPanen;
    }

    public void updatePanen(Panen panen) {
        try {
            int idUser = UserSession.requireUserId();
            String sql = "UPDATE panen SET nama_lahan = ?, jenis_tanaman = ?, jumlah_panen = ?, " +
                         "satuan = ?, tanggal_panen = ?, kondisi_cuaca = ? WHERE id = ? AND id_user = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, panen.getNamaLahan());
                pstmt.setString(2, panen.getJenisTanaman());
                pstmt.setDouble(3, panen.getJumlahPanen());
                pstmt.setString(4, panen.getSatuan());
                pstmt.setString(5, panen.getTanggalPanen());
                pstmt.setString(6, panen.getKondisiCuaca());
                pstmt.setInt(7, panen.getId()); 
                pstmt.setInt(8, idUser); 
                
                pstmt.executeUpdate();
                System.out.println("Data panen berhasil diupdate!");

            } catch (SQLException e) {
                System.out.println("Gagal mengupdate data: " + e.getMessage());
            }
        } catch (IllegalStateException e) {
            System.out.println("Akses ditolak: " + e.getMessage());
        }
    }

    public void hapusPanen(int id) {
        try {
            int idUser = UserSession.requireUserId();
            String sql = "DELETE FROM panen WHERE id = ? AND id_user = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, id);
                pstmt.setInt(2, idUser);
                pstmt.executeUpdate();
                System.out.println("Data panen berhasil dihapus!");

            } catch (SQLException e) {
                System.out.println("Gagal menghapus data: " + e.getMessage());
            }
        } catch (IllegalStateException e) {
            System.out.println("Akses ditolak: " + e.getMessage());
        }
    }

    public int hitungTotalLahanAktif() {
        int total = 0;
        try {
            int idUser = UserSession.requireUserId();
            String sql = "SELECT COUNT(DISTINCT nama_lahan) AS total FROM panen WHERE id_user = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idUser);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) total = rs.getInt("total");
                }
            } catch (SQLException e) {
                System.out.println("Error hitung lahan: " + e.getMessage());
            }
        } catch (IllegalStateException e) {}
        return total;
    }

    public double hitungTotalHasilPanen() {
        double total = 0;
        try {
            int idUser = UserSession.requireUserId();
            String sql = "SELECT SUM(jumlah_panen) AS total FROM panen WHERE id_user = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setInt(1, idUser);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) total = rs.getDouble("total");
                }
            } catch (SQLException e) {
                System.out.println("Error hitung hasil: " + e.getMessage());
            }
        } catch (IllegalStateException e) {}
        return total;
    }
}