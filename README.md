# GrowSeeds-OOP22

## Deskripsi Program
GrowSeeds adalah aplikasi manajemen pertanian berbasis Java yang membantu petani mengelola data stok, panen, kalender tanam, dan saran pertanian dalam satu platform. Aplikasi ini dikembangkan untuk mempermudah pencatatan aktivitas pertanian sehari-hari dan memberikan rekomendasi yang relevan dengan kondisi lahan.

## Fitur Utama
- Dashboard ringkas untuk menampilkan kondisi sistem dan metrik penting.
- Manajemen profil pengguna untuk menyimpan data dasar petani.
- Pencatatan stok bahan, termasuk jumlah dan status ketersediaan.
- Pencatatan hasil panen lengkap dengan informasi lahan, tanaman, jumlah, satuan, dan kondisi.
- Fitur Saran Tani yang mencakup:
  - Rekomendasi pupuk berdasarkan jenis tanaman dan luas lahan.
  - Kalender tanam untuk jadwal semai, estimasi panen, dan fase pertumbuhan.
  - Deteksi hama dengan informasi gejala dan solusi penanganan.
- Integrasi database SQLite untuk penyimpanan data lokal.

## Tujuan Program
1. Membantu petani mencatat dan mengelola data pertanian secara digital.
2. Menyediakan rekomendasi pertanian yang lebih terstruktur dan mudah diakses.
3. Mengurangi ketergantungan pada catatan fisik dengan menyimpan data secara terpusat.
4. Meningkatkan efisiensi pengambilan keputusan dalam perawatan tanaman dan penanganan hama.
5. Menyediakan alat bantu sederhana untuk merencanakan jadwal tanam dan penggunaan pupuk.

## Struktur Projek
- `ProjectGrow/app/src/main/java/view` - tampilan antarmuka pengguna (UI).
- `ProjectGrow/app/src/main/java/service` - logika bisnis dan interaksi dengan database.
- `ProjectGrow/app/src/main/java/database` - koneksi dan inisialisasi database.
- `ProjectGrow/app/src/main/java/model` - model data yang digunakan dalam aplikasi.

## Cara Menjalankan
1. Buka folder `ProjectGrow` di IDE Java atau terminal.
2. Jalankan perintah Gradle: `./gradlew.bat :app:run`.
3. Aplikasi akan diluncurkan dengan antarmuka GUI untuk pengelolaan pertanian.
