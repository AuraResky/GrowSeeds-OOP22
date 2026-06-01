package database;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
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
                ")";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate(queryTabelUser);
            stmt.executeUpdate(queryTabelStok);
            stmt.executeUpdate(queryTabelPanen);

            File databaseFile = new File(URL.replace("jdbc:sqlite:", ""));
            System.out.println("Database GrowSeeds siap digunakan.");
            System.out.println("Lokasi database: " + databaseFile.getAbsolutePath());

        } catch (SQLException e) {
            throw new IllegalStateException(
                    "Gagal inisialisasi database: " + e.getMessage(),
                    e
            );
        }
    }
}
