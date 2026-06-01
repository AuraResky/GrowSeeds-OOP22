package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.KalenderTanam;

public class KalenderTanamService {

    /**
     * Mengambil semua kalender tanam
     */
    public List<KalenderTanam> getAllKalenderTanam() {
        List<KalenderTanam> daftarKalender = new ArrayList<>();
        String query = "SELECT id, nama_tanaman, tanggal_semai, estimasi_panen, fase_pertumbuhan FROM kalender_tanam";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                KalenderTanam kalender = new KalenderTanam(
                        rs.getInt("id"),
                        rs.getString("nama_tanaman"),
                        rs.getString("tanggal_semai"),
                        rs.getString("estimasi_panen"),
                        rs.getString("fase_pertumbuhan")
                );
                daftarKalender.add(kalender);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil kalender tanam: " + e.getMessage());
        }
        
        return daftarKalender;
    }

    /**
     * Mengambil kalender tanam berdasarkan ID
     */
    public KalenderTanam getKalenderById(int id) {
        String query = "SELECT id, nama_tanaman, tanggal_semai, estimasi_panen, fase_pertumbuhan FROM kalender_tanam WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new KalenderTanam(
                            rs.getInt("id"),
                            rs.getString("nama_tanaman"),
                            rs.getString("tanggal_semai"),
                            rs.getString("estimasi_panen"),
                            rs.getString("fase_pertumbuhan")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil kalender tanam: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Menambah kalender tanam baru
     */
    public boolean tambahKalenderTanam(String namaTanaman, String tanggalSemai, String estimasiPanen, String fasePertumbuhan) {
        String query = "INSERT INTO kalender_tanam (nama_tanaman, tanggal_semai, estimasi_panen, fase_pertumbuhan) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, namaTanaman);
            ps.setString(2, tanggalSemai);
            ps.setString(3, estimasiPanen);
            ps.setString(4, fasePertumbuhan);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error menambah kalender tanam: " + e.getMessage());
            return false;
        }
    }

    public boolean updateKalender(int id, String namaTanaman, String tanggalSemai, String estimasiPanen, String fasePertumbuhan) {
        String query = "UPDATE kalender_tanam SET nama_tanaman = ?, tanggal_semai = ?, estimasi_panen = ?, fase_pertumbuhan = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, namaTanaman);
            ps.setString(2, tanggalSemai);
            ps.setString(3, estimasiPanen);
            ps.setString(4, fasePertumbuhan);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error mengupdate kalender tanam: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mencari kalender tanam berdasarkan nama tanaman
     */
    public List<KalenderTanam> cariTanaman(String keyword) {
        List<KalenderTanam> hasil = new ArrayList<>();
        String query = "SELECT id, nama_tanaman, tanggal_semai, estimasi_panen, fase_pertumbuhan FROM kalender_tanam WHERE LOWER(nama_tanaman) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    KalenderTanam kalender = new KalenderTanam(
                            rs.getInt("id"),
                            rs.getString("nama_tanaman"),
                            rs.getString("tanggal_semai"),
                            rs.getString("estimasi_panen"),
                            rs.getString("fase_pertumbuhan")
                    );
                    hasil.add(kalender);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari kalender tanam: " + e.getMessage());
        }
        
        return hasil;
    }
}
