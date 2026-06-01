package database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = resolveDatabaseUrl();

    static {
        inisialisasiDatabase();
    }
    private DatabaseConnection() {
    }

    private static String resolveDatabaseUrl() {
        Path workingDirectory = Paths.get(System.getProperty("user.dir"));
        Path databasePath;

        if (Files.isDirectory(workingDirectory.resolve("app"))) {
            databasePath = workingDirectory.resolve("app").resolve("growseeds.db");
        } else {
            databasePath = workingDirectory.resolve("growseeds.db");
        }

        return "jdbc:sqlite:" + databasePath.toAbsolutePath();
    }

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Driver SQLite tidak ditemukan.", e);
        }

        Connection connection = DriverManager.getConnection(URL);
        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }
        return connection;
    }

    public static void inisialisasiDatabase() {
        String queryTabelUser = "CREATE TABLE IF NOT EXISTS users (" +
                "id_user INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "email TEXT NOT NULL UNIQUE COLLATE NOCASE, " +
                "password TEXT NOT NULL" +
                ");";

        String queryTabelProfile = "CREATE TABLE IF NOT EXISTS profiles (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_user INTEGER UNIQUE, " +
                "nama_pengguna TEXT NOT NULL, " +
                "email_kontak TEXT NOT NULL, " +
                "status_lahan TEXT NOT NULL DEFAULT 'Lahan Pribadi', " +
                "FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE" +
                ");";

        String queryTabelStok = "CREATE TABLE IF NOT EXISTS stok (" +
                "id_stok INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_user INTEGER, " +
                "nama_barang TEXT NOT NULL, " +
                "kategori TEXT NOT NULL, " +
                "jumlah REAL NOT NULL DEFAULT 0, " +
                "terjual REAL NOT NULL DEFAULT 0, " +
                "status TEXT NOT NULL DEFAULT 'Tersedia', " +
                "FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE" +
                ");";

        String queryTabelPanen = "CREATE TABLE IF NOT EXISTS panen (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_user INTEGER, " +
                "nama_lahan TEXT NOT NULL, " +
                "jenis_tanaman TEXT NOT NULL, " +
                "jumlah_panen REAL NOT NULL, " +
                "satuan TEXT NOT NULL, " +
                "tanggal_panen TEXT NOT NULL, " +
                "kondisi_cuaca TEXT," +
                "FOREIGN KEY(id_user) REFERENCES users(id_user)" +
                ");";

        String queryTabelDeteksiHama = "CREATE TABLE IF NOT EXISTS deteksi_hama (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama_hama_penyakit TEXT NOT NULL, " +
                "gejala TEXT NOT NULL, " +
                "solusi_penanganan TEXT NOT NULL, " +
                "tingkat_bahaya TEXT NOT NULL" +
                ");";

        String queryTabelKalenderTanam = "CREATE TABLE IF NOT EXISTS kalender_tanam (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nama_tanaman TEXT NOT NULL, " +
                "tanggal_semai TEXT NOT NULL, " +
                "estimasi_panen TEXT NOT NULL, " +
                "fase_pertumbuhan TEXT NOT NULL" +
                ");";

        String queryTabelRekomendasiPupuk = "CREATE TABLE IF NOT EXISTS rekomendasi_pupuk (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "jenis_tanaman TEXT NOT NULL, " +
                "luas_lahan REAL NOT NULL, " +
                "kebutuhan_urea REAL NOT NULL, " +
                "kebutuhan_npk REAL NOT NULL" +
                ");";

        String queryTabelInfoCuaca = "CREATE TABLE IF NOT EXISTS info_cuaca_tani (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "wilayah_lahan TEXT NOT NULL, " +
                "suhu REAL NOT NULL, " +
                "kelembaban REAL NOT NULL, " +
                "curah_hujan REAL NOT NULL" +
                ");";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(queryTabelUser);
            stmt.executeUpdate(queryTabelProfile);
            stmt.executeUpdate(queryTabelStok);
            stmt.executeUpdate(queryTabelPanen);
            stmt.executeUpdate(queryTabelDeteksiHama);
            stmt.executeUpdate(queryTabelKalenderTanam);
            stmt.executeUpdate(queryTabelRekomendasiPupuk);
            stmt.executeUpdate(queryTabelInfoCuaca);

            File databaseFile = new File(URL.replace("jdbc:sqlite:", ""));
            System.out.println("Database GrowSeeds siap digunakan.");
            System.out.println("Lokasi database: " + databaseFile.getAbsolutePath());

            // Inisialisasi data sample jika database kosong
            inisialisasiDataSample();

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal inisialisasi database: " + e.getMessage(),
                    e
            );
        }
    }

    /**
     * Menginisialisasi data sample untuk tabel-tabel saran
     */
    private static void inisialisasiDataSample() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Cek dan insert data hama
            try (ResultSet rs = stmt.executeQuery("SELECT COUNT(*) as count FROM deteksi_hama")) {
                if (rs.next() && rs.getInt("count") == 0) {
                    String[] inserts = {
                            "INSERT INTO deteksi_hama (nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya) VALUES " +
                                    "('Wereng Coklat', 'Daun menguning, tanaman tampak layu, ada bintik kuning pada daun', " +
                                    "'Semprotkan pestisida organik atau kimiawi, gunakan perangkap perekat kuning, tanam varietas tahan', 'Tinggi')",
                            "INSERT INTO deteksi_hama (nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya) VALUES " +
                                    "('Belalang', 'Daun berlubang-lubang, area daun yang hilang, kerusakan parah pada tunas muda', " +
                                    "'Panen manual, semprot dengan pestisida, tanam tanaman pengusir, gunakan jaring', 'Sedang')",
                            "INSERT INTO deteksi_hama (nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya) VALUES " +
                                    "('Ulat Grayak', 'Daun terlihat berlubang dengan tepi yang tidak teratur, kotoran pada tanaman', " +
                                    "'Ambil ulat secara manual, gunakan Bacillus thuringiensis, semprotkan insektisida piretroid', 'Sedang')",
                            "INSERT INTO deteksi_hama (nama_hama_penyakit, gejala, solusi_penanganan, tingkat_bahaya) VALUES " +
                                    "('Penyakit Karat', 'Bintik coklat pada daun dengan spora berwarna coklat, daun menguning', " +
                                    "'Pangkas bagian yang terinfeksi, semprotkan fungisida, tingkatkan drainase, hindari kelembaban tinggi', 'Sedang')"
                    };
                    for (String insert : inserts) {
                        stmt.executeUpdate(insert);
                    }
                    System.out.println("Data hama sample berhasil diinisialisasi.");
                }
            }

            // Tidak ada data sample otomatis untuk kalender tanam, rekomendasi pupuk, dan info cuaca.
            // Data tersebut akan diisi oleh pengguna melalui UI.
            // Namun jika masih ada baris template sample lama, hapus saja entry khusus ini.
            String cleanupQuery = "DELETE FROM kalender_tanam WHERE nama_tanaman = ? " +
                    "AND tanggal_semai = ? AND estimasi_panen = ? AND fase_pertumbuhan = ?";
            try (PreparedStatement cleanupStmt = conn.prepareStatement(cleanupQuery)) {
                cleanupStmt.setString(1, "Tomat");
                cleanupStmt.setString(2, "15-08-2024");
                cleanupStmt.setString(3, "30-11-2024");
                cleanupStmt.setString(4, "Bibit - Vegetatif - Berbuah");
                int deleted = cleanupStmt.executeUpdate();
                if (deleted > 0) {
                    System.out.println("Baris sample kalender tanam template keempat berhasil dihapus.");
                }
            }

        } catch (SQLException e) {
            System.err.println("Gagal menginisialisasi data sample: " + e.getMessage());
        }
    }
}
