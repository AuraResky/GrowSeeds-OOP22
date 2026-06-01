package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.RekomendasiPupuk;

public class RekomendasiPupukService {

    /**
     * Mengambil semua rekomendasi pupuk
     */
    public List<RekomendasiPupuk> getAllRekomendasi() {
        List<RekomendasiPupuk> daftarRekomendasi = new ArrayList<>();
        String query = "SELECT id, jenis_tanaman, luas_lahan, kebutuhan_urea, kebutuhan_npk FROM rekomendasi_pupuk";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                RekomendasiPupuk rekomendasi = new RekomendasiPupuk(
                        rs.getInt("id"),
                        rs.getString("jenis_tanaman"),
                        rs.getDouble("luas_lahan"),
                        rs.getDouble("kebutuhan_urea"),
                        rs.getDouble("kebutuhan_npk")
                );
                daftarRekomendasi.add(rekomendasi);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil rekomendasi pupuk: " + e.getMessage());
        }
        
        return daftarRekomendasi;
    }

    /**
     * Mengambil rekomendasi pupuk berdasarkan ID
     */
    public RekomendasiPupuk getRekomendasiById(int id) {
        String query = "SELECT id, jenis_tanaman, luas_lahan, kebutuhan_urea, kebutuhan_npk FROM rekomendasi_pupuk WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new RekomendasiPupuk(
                            rs.getInt("id"),
                            rs.getString("jenis_tanaman"),
                            rs.getDouble("luas_lahan"),
                            rs.getDouble("kebutuhan_urea"),
                            rs.getDouble("kebutuhan_npk")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil rekomendasi pupuk: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Menambah rekomendasi pupuk baru
     */
    public boolean tambahRekomendasi(String jenisTanaman, double luasLahan, double kebutuhanUrea, double kebutuhanNpk) {
        String query = "INSERT INTO rekomendasi_pupuk (jenis_tanaman, luas_lahan, kebutuhan_urea, kebutuhan_npk) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, jenisTanaman);
            ps.setDouble(2, luasLahan);
            ps.setDouble(3, kebutuhanUrea);
            ps.setDouble(4, kebutuhanNpk);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error menambah rekomendasi pupuk: " + e.getMessage());
            return false;
        }
    }

    public boolean updateRekomendasi(int id, String jenisTanaman, double luasLahan, double kebutuhanUrea, double kebutuhanNpk) {
        String query = "UPDATE rekomendasi_pupuk SET jenis_tanaman = ?, luas_lahan = ?, kebutuhan_urea = ?, kebutuhan_npk = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, jenisTanaman);
            ps.setDouble(2, luasLahan);
            ps.setDouble(3, kebutuhanUrea);
            ps.setDouble(4, kebutuhanNpk);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error mengupdate rekomendasi pupuk: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mencari rekomendasi berdasarkan jenis tanaman
     */
    public List<RekomendasiPupuk> cariRekomendasi(String jenisTanaman) {
        List<RekomendasiPupuk> hasil = new ArrayList<>();
        String query = "SELECT id, jenis_tanaman, luas_lahan, kebutuhan_urea, kebutuhan_npk FROM rekomendasi_pupuk WHERE LOWER(jenis_tanaman) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + jenisTanaman + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    RekomendasiPupuk rekomendasi = new RekomendasiPupuk(
                            rs.getInt("id"),
                            rs.getString("jenis_tanaman"),
                            rs.getDouble("luas_lahan"),
                            rs.getDouble("kebutuhan_urea"),
                            rs.getDouble("kebutuhan_npk")
                    );
                    hasil.add(rekomendasi);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari rekomendasi: " + e.getMessage());
        }
        
        return hasil;
    }
}
