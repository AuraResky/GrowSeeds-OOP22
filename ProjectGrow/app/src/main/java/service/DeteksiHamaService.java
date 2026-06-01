package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import database.DatabaseConnection;
import model.DeteksiHama;

public class DeteksiHamaService {


    public List<DeteksiHama> getAllHama() {
        List<DeteksiHama> daftarHama = new ArrayList<>();
        String query = "SELECT id, nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya FROM deteksi_hama";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                DeteksiHama hama = new DeteksiHama(
                        rs.getInt("id"),
                        rs.getString("nama_hama_penyakit"),
                        rs.getString("gejala"),
                        rs.getString("solusi_penanganan"),
                        rs.getString("tingkat_bahaya")
                );
                daftarHama.add(hama);
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data hama: " + e.getMessage());
        }
        
        return daftarHama;
    }


    public DeteksiHama getHamaById(int id) {
        String query = "SELECT id, nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya FROM deteksi_hama WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, id);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DeteksiHama(
                            rs.getInt("id"),
                            rs.getString("nama_hama_penyakit"),
                            rs.getString("gejala"),
                            rs.getString("solusi_penanganan"),
                            rs.getString("tingkat_bahaya")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data hama: " + e.getMessage());
        }
        
        return null;
    }


    public DeteksiHama getHamaByNama(String nama) {
        String query = "SELECT id, nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya FROM deteksi_hama WHERE LOWER(nama_hama_penyakit) = LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nama);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new DeteksiHama(
                            rs.getInt("id"),
                            rs.getString("nama_hama_penyakit"),
                            rs.getString("gejala"),
                            rs.getString("solusi_penanganan"),
                            rs.getString("tingkat_bahaya")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil data hama: " + e.getMessage());
        }
        
        return null;
    }


    public boolean tambahHama(String nama, String gejala, String solusi, String tingkatBahaya) {
        String query = "INSERT INTO deteksi_hama (nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, nama);
            ps.setString(2, gejala);
            ps.setString(3, solusi);
            ps.setString(4, tingkatBahaya);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error menambah hama: " + e.getMessage());
            return false;
        }
    }


    public List<DeteksiHama> cariHamaBerdasarkanGejala(String keyword) {
        List<DeteksiHama> hasil = new ArrayList<>();
        String query = "SELECT id, nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya FROM deteksi_hama WHERE LOWER(gejala) LIKE LOWER(?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, "%" + keyword + "%");
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    DeteksiHama hama = new DeteksiHama(
                            rs.getInt("id"),
                            rs.getString("nama_hama_penyakit"),
                            rs.getString("gejala"),
                            rs.getString("solusi_penanganan"),
                            rs.getString("tingkat_bahaya")
                    );
                    hasil.add(hama);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mencari hama: " + e.getMessage());
        }
        
        return hasil;
    }
}
