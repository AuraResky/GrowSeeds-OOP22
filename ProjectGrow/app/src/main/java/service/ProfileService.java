package service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.DatabaseConnection;
import model.Profile;

public class ProfileService {

    /**
     * Mengambil profil pengguna berdasarkan ID
     */
    public Profile getProfileByUserId(int userId) {
        String query = "SELECT id, nama_pengguna, email_kontak, status_lahan FROM profiles WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Profile(
                            rs.getInt("id"),
                            rs.getString("nama_pengguna"),
                            rs.getString("email_kontak"),
                            rs.getString("status_lahan")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error mengambil profil: " + e.getMessage());
        }
        return null;
    }

    /**
     * Membuat profil baru untuk pengguna
     */
    public boolean buatProfileBaru(int userId, String namaPengguna, String emailKontak, String statusLahan) {
        String query = "INSERT INTO profiles (id_user, nama_pengguna, email_kontak, status_lahan) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            ps.setString(2, namaPengguna);
            ps.setString(3, emailKontak);
            ps.setString(4, statusLahan);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Error membuat profil: " + e.getMessage());
            return false;
        }
    }

    /**
     * Memperbarui nama pengguna
     */
    public boolean updateNamaPengguna(int userId, String namaPengguna) {
        String query = "UPDATE profiles SET nama_pengguna = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, namaPengguna);
            ps.setInt(2, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate nama: " + e.getMessage());
            return false;
        }
    }

    /**
     * Memperbarui email kontak
     */
    public boolean updateEmailKontak(int userId, String emailKontak) {
        String query = "UPDATE profiles SET email_kontak = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, emailKontak);
            ps.setInt(2, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate email: " + e.getMessage());
            return false;
        }
    }

    /**
     * Memperbarui status lahan
     */
    public boolean updateStatusLahan(int userId, String statusLahan) {
        String query = "UPDATE profiles SET status_lahan = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, statusLahan);
            ps.setInt(2, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate status lahan: " + e.getMessage());
            return false;
        }
    }

    /**
     * Memperbarui seluruh profil
     */
    public boolean updateProfileLengkap(int userId, String namaPengguna, String emailKontak, String statusLahan) {
        String query = "UPDATE profiles SET nama_pengguna = ?, email_kontak = ?, status_lahan = ? WHERE id_user = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, namaPengguna);
            ps.setString(2, emailKontak);
            ps.setString(3, statusLahan);
            ps.setInt(4, userId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.err.println("Error mengupdate profil: " + e.getMessage());
            return false;
        }
    }

    /**
     * Mengecek apakah profil sudah ada untuk user
     */
    public boolean profileSudahAda(int userId) {
        String query = "SELECT 1 FROM profiles WHERE id_user = ? LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.err.println("Error mengecek profil: " + e.getMessage());
            return false;
        }
    }
}
