package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.InfoCuacaTani;

public class InfoCuacaService {

    /**
     * Mengambil semua data cuaca
     */
    public List<InfoCuacaTani> getAllInfoCuaca() {
        List<InfoCuacaTani> daftarCuaca = new ArrayList<>();
        String query = "SELECT id, wilayah_lahan, suhu, kelembaban, curah_hujan FROM info_cuaca_tani";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                InfoCuacaTani cuaca = new InfoCuacaTani(
                        rs.getInt("id"),
                        rs.getString("wilayah_lahan"),
                        rs.getDouble("suhu"),
                        rs.getDouble("kelembaban"),
                        rs.getDouble("curah_hujan")
                );
                daftarCuaca.add(cuaca);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil info cuaca: " + e.getMessage());
        }
        
        return daftarCuaca;
    }

    /**
     * Mengambil info cuaca berdasarkan ID
     */
    public InfoCuacaTani getInfoCuacaById(int id) {
        String query = "SELECT id, wilayah_lahan, suhu, kelembaban, curah_hujan FROM info_cuaca_tani WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InfoCuacaTani(
                            rs.getInt("id"),
                            rs.getString("wilayah_lahan"),
                            rs.getDouble("suhu"),
                            rs.getDouble("kelembaban"),
                            rs.getDouble("curah_hujan")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil info cuaca: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Mengambil info cuaca berdasarkan wilayah
     */
    public InfoCuacaTani getInfoCuacaByWilayah(String wilayah) {
        String query = "SELECT id, wilayah_lahan, suhu, kelembaban, curah_hujan FROM info_cuaca_tani WHERE LOWER(wilayah_lahan) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, wilayah);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new InfoCuacaTani(
                            rs.getInt("id"),
                            rs.getString("wilayah_lahan"),
                            rs.getDouble("suhu"),
                            rs.getDouble("kelembaban"),
                            rs.getDouble("curah_hujan")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil info cuaca: " + e.getMessage());
        }
        
        return null;
    }

    /**
     * Menambah data cuaca baru
     */
    public boolean tambahInfoCuaca(String wilayah, double suhu, double kelembaban, double curahHujan) {
        String query = "INSERT INTO info_cuaca_tani (wilayah_lahan, suhu, kelembaban, curah_hujan) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, wilayah);
            ps.setDouble(2, suhu);
            ps.setDouble(3, kelembaban);
            ps.setDouble(4, curahHujan);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error menambah info cuaca: " + e.getMessage());
            return false;
        }
    }

    /**
     * Update info cuaca
     */
    public boolean updateInfoCuaca(int id, String wilayah, double suhu, double kelembaban, double curahHujan) {
        String query = "UPDATE info_cuaca_tani SET wilayah_lahan = ?, suhu = ?, kelembaban = ?, curah_hujan = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, wilayah);
            ps.setDouble(2, suhu);
            ps.setDouble(3, kelembaban);
            ps.setDouble(4, curahHujan);
            ps.setInt(5, id);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error update info cuaca: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mencari info cuaca berdasarkan wilayah
     */
    public List<InfoCuacaTani> cariCuacaByWilayah(String keyword) {
        List<InfoCuacaTani> hasil = new ArrayList<>();
        String query = "SELECT id, wilayah_lahan, suhu, kelembaban, curah_hujan FROM info_cuaca_tani WHERE LOWER(wilayah_lahan) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    InfoCuacaTani cuaca = new InfoCuacaTani(
                            rs.getInt("id"),
                            rs.getString("wilayah_lahan"),
                            rs.getDouble("suhu"),
                            rs.getDouble("kelembaban"),
                            rs.getDouble("curah_hujan")
                    );
                    hasil.add(cuaca);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari info cuaca: " + e.getMessage());
        }
        
        return hasil;
    }
}
