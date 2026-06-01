package view;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.DeteksiHama;
import model.InfoCuacaTani;
import model.KalenderTanam;
import model.Panen;
import model.RekomendasiPupuk;
import service.DeteksiHamaService;
import service.InfoCuacaService;
import service.KalenderTanamService;
import service.PanenService;
import service.RekomendasiPupukService;

public class SaranView extends VBox {
    private static final String GREEN_DARK = "#294a20";
    private static final String GREEN_LIGHT = "#3C6630";
    private static final String BACKGROUND = "#f5f7f4";
    private static final String YELLOW_DANGER = "#FFF9E6";
    private static final String ORANGE_WARNING = "#FFE4B5";

    private final DeteksiHamaService deteksiHamaService = new DeteksiHamaService();
    private final KalenderTanamService kalenderTanamService = new KalenderTanamService();
    private final RekomendasiPupukService rekomendasiPupukService = new RekomendasiPupukService();
    private final InfoCuacaService infoCuacaService = new InfoCuacaService();
    private final PanenService panenService = new PanenService();

    private VBox contentPanel;
    private Button btnRekomendasi;
    private Button btnKalender;
    private Button btnHama;
    private Button btnCuaca;

    public SaranView() {
        buildView();
    }

    private void buildView() {
        setSpacing(0);
        setBackground(new Background(new BackgroundFill(Color.web(BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(20, 30, 20, 30));

        // Header Section
        BorderPane headerPanel = new BorderPane();
        headerPanel.setPadding(new Insets(0, 0, 30, 0));

        VBox titleBlock = new VBox(5);
        Label title = new Label("Saran Pertanian Terpadu");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Dapatkan rekomendasi lengkap untuk pertanian Anda");
        subtitle.setFont(Font.font("SansSerif", 13));
        subtitle.setTextFill(Color.GRAY);

        titleBlock.getChildren().addAll(title, subtitle);
        headerPanel.setLeft(titleBlock);
        getChildren().add(headerPanel);

        // Submenu Section
        HBox submenuPanel = createSubmenuPanel();
        getChildren().add(submenuPanel);

        // Content Panel
        contentPanel = new VBox();
        contentPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        contentPanel.setPadding(new Insets(30));
        VBox.setVgrow(contentPanel, Priority.ALWAYS);

        getChildren().add(contentPanel);

        // Default: Tampilkan Rekomendasi Pupuk
        showRekomendasiPupuk();
    }

    /**
     * Membuat panel submenu
     */
    private HBox createSubmenuPanel() {
        HBox submenu = new HBox(10);
        submenu.setPadding(new Insets(0, 0, 20, 0));
        submenu.setAlignment(Pos.CENTER_LEFT);

        btnRekomendasi = createSubmenuButton("💧 Rekomendasi Pupuk", true);
        btnKalender = createSubmenuButton("📅 Kalender Tanam", false);
        btnHama = createSubmenuButton("🐛 Deteksi Hama", false);
        btnCuaca = createSubmenuButton("🌤️ Info Cuaca", false);

        btnRekomendasi.setOnAction(e -> switchMenu(btnRekomendasi, "rekomendasi"));
        btnKalender.setOnAction(e -> switchMenu(btnKalender, "kalender"));
        btnHama.setOnAction(e -> switchMenu(btnHama, "hama"));
        btnCuaca.setOnAction(e -> switchMenu(btnCuaca, "cuaca"));

        submenu.getChildren().addAll(btnRekomendasi, btnKalender, btnHama, btnCuaca);
        return submenu;
    }

    /**
     * Membuat button submenu
     */
    private Button createSubmenuButton(String text, boolean isActive) {
        Button btn = new Button(text);
        btn.setPrefWidth(150);
        btn.setPrefHeight(40);
        if (isActive) {
            btn.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                    "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 12px; " +
                    "-fx-text-fill: white;");
        } else {
            btn.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                    "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        }
        btn.setCursor(javafx.scene.Cursor.HAND);
        return btn;
    }

    /**
     * Switch menu
     */
    private void switchMenu(Button activeBtn, String menu) {
        // Update button states
        btnRekomendasi.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        btnKalender.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        btnHama.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        btnCuaca.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");

        activeBtn.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 12px; " +
                "-fx-text-fill: white;");

        // Show content
        switch (menu) {
            case "rekomendasi":
                showRekomendasiPupuk();
                break;
            case "kalender":
                showKalenderTanam();
                break;
            case "hama":
                showDeteksiHama();
                break;
            case "cuaca":
                showInfoCuaca();
                break;
        }
    }

    /**
     * Menampilkan Rekomendasi Pupuk
     */
    private void showRekomendasiPupuk() {
        contentPanel.getChildren().clear();

        VBox headerSection = new VBox(5);
        Label title = new Label("Rekomendasi Pupuk");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Panduan pemberian pupuk berdasarkan jenis tanaman dan luas lahan");
        subtitle.setFont(Font.font("SansSerif", 12));
        subtitle.setTextFill(Color.GRAY);
        headerSection.getChildren().addAll(title, subtitle);
        contentPanel.getChildren().add(headerSection);

        contentPanel.getChildren().add(createDivider());

        Optional<Panen> panenTerakhir = getLatestPanen();
        Label lblIntegrasi = new Label();
        if (panenTerakhir.isPresent()) {
            lblIntegrasi.setText("Tanaman panen terakhir: " + panenTerakhir.get().getJenisTanaman() + ". Gunakan tombol di bawah untuk mengisi rekomendasi berdasarkan tanaman tersebut.");
        } else {
            lblIntegrasi.setText("Tidak ada data panen terbaru. Tambahkan data panen agar rekomendasi pupuk lebih relevan.");
        }
        lblIntegrasi.setFont(Font.font("SansSerif", 12));
        lblIntegrasi.setTextFill(Color.web("#5B5B5B"));
        contentPanel.getChildren().add(lblIntegrasi);

        // Search section
        HBox searchBox = new HBox(10);
        searchBox.setPadding(new Insets(15, 0, 15, 0));
        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Cari jenis tanaman...");
        txtSearch.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");

        Button btnSearch = new Button("Cari");
        btnSearch.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: white; -fx-padding: 8px 20px;");
        btnSearch.setCursor(javafx.scene.Cursor.HAND);
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnTambah = new Button("Tambah");
        btnTambah.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnTambah.setCursor(javafx.scene.Cursor.HAND);

        Button btnUbah = new Button("Ubah Terpilih");
        btnUbah.setStyle("-fx-background-color: #F3D9D3; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #8B3A3A; -fx-padding: 8px 20px;");
        btnUbah.setCursor(javafx.scene.Cursor.HAND);

        Button btnRefresh = new Button("Segarkan");
        btnRefresh.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnRefresh.setCursor(javafx.scene.Cursor.HAND);

        Button btnGunakanPanenTerakhir = new Button("Pakai Tanaman Panen Terakhir");
        btnGunakanPanenTerakhir.setStyle("-fx-background-color: #E7F4E7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnGunakanPanenTerakhir.setCursor(javafx.scene.Cursor.HAND);

        searchBox.getChildren().addAll(txtSearch, btnSearch, btnGunakanPanenTerakhir, btnTambah, btnUbah, btnRefresh);
        contentPanel.getChildren().add(searchBox);

        // Table
        TableView<RekomendasiPupuk> table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<RekomendasiPupuk, String> colTanaman = new TableColumn<>("Jenis Tanaman");
        colTanaman.setCellValueFactory(new PropertyValueFactory<>("jenisTanaman"));
        colTanaman.setPrefWidth(150);

        TableColumn<RekomendasiPupuk, Double> colLahan = new TableColumn<>("Luas Lahan (m²)");
        colLahan.setCellValueFactory(new PropertyValueFactory<>("luasLahan"));
        colLahan.setPrefWidth(120);

        TableColumn<RekomendasiPupuk, Double> colUrea = new TableColumn<>("Urea (Kg)");
        colUrea.setCellValueFactory(new PropertyValueFactory<>("kebutuhanUrea"));
        colUrea.setPrefWidth(100);

        TableColumn<RekomendasiPupuk, Double> colNpk = new TableColumn<>("NPK (Kg)");
        colNpk.setCellValueFactory(new PropertyValueFactory<>("kebutuhanNpk"));
        colNpk.setPrefWidth(100);

        table.getColumns().addAll(colTanaman, colLahan, colUrea, colNpk);
        table.setPlaceholder(new Label("Tidak ada rekomendasi pupuk ditemukan. Silakan perbarui data di database."));

        Runnable refreshRekomendasi = () -> {
            table.getItems().clear();
            String keyword = txtSearch.getText().trim();
            List<RekomendasiPupuk> hasil;
            if (keyword.isEmpty()) {
                hasil = rekomendasiPupukService.getAllRekomendasi();
            } else {
                hasil = rekomendasiPupukService.cariRekomendasi(keyword);
            }
            table.getItems().addAll(hasil);
        };

        btnSearch.setOnAction(e -> refreshRekomendasi.run());
        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            refreshRekomendasi.run();
        });

        btnGunakanPanenTerakhir.setOnAction(e -> {
            Optional<Panen> currentLast = getLatestPanen();
            if (currentLast.isPresent()) {
                txtSearch.setText(currentLast.get().getJenisTanaman());
                refreshRekomendasi.run();
            } else {
                showAlert("Data Panen Kosong", "Tidak ada data panen terbaru untuk digunakan.", Alert.AlertType.INFORMATION);
            }
        });

        btnTambah.setOnAction(e -> {
            Optional<RekomendasiPupuk> result = showRekomendasiPupukDialog(null);
            result.ifPresent(item -> {
                rekomendasiPupukService.tambahRekomendasi(
                        item.getJenisTanaman(), item.getLuasLahan(), item.getKebutuhanUrea(), item.getKebutuhanNpk());
                refreshRekomendasi.run();
            });
        });

        btnUbah.setOnAction(e -> {
            RekomendasiPupuk selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Pilih data", "Pilih rekomendasi pupuk terlebih dahulu untuk diubah.", Alert.AlertType.WARNING);
                return;
            }
            Optional<RekomendasiPupuk> result = showRekomendasiPupukDialog(selected);
            result.ifPresent(item -> {
                rekomendasiPupukService.updateRekomendasi(
                        selected.getId(), item.getJenisTanaman(), item.getLuasLahan(), item.getKebutuhanUrea(), item.getKebutuhanNpk());
                refreshRekomendasi.run();
            });
        });

        refreshRekomendasi.run();
        contentPanel.getChildren().add(table);
    }

    /**
     * Menampilkan Kalender Tanam
     */
    private void showKalenderTanam() {
        contentPanel.getChildren().clear();

        VBox headerSection = new VBox(5);
        Label title = new Label("Kalender Tanam");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Pantau jadwal tanam dan estimasi panen tanaman Anda");
        subtitle.setFont(Font.font("SansSerif", 12));
        subtitle.setTextFill(Color.GRAY);
        headerSection.getChildren().addAll(title, subtitle);
        contentPanel.getChildren().add(headerSection);

        contentPanel.getChildren().add(createDivider());

        Label lblIntegrasi = new Label("Kalender tanam akan terlihat lebih akurat jika data panen dan tanaman di-update secara berkala.");
        lblIntegrasi.setFont(Font.font("SansSerif", 12));
        lblIntegrasi.setTextFill(Color.web("#5B5B5B"));
        contentPanel.getChildren().add(lblIntegrasi);

        // Table
        TableView<KalenderTanam> table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<KalenderTanam, String> colTanaman = new TableColumn<>("Nama Tanaman");
        colTanaman.setCellValueFactory(new PropertyValueFactory<>("namaTanaman"));
        colTanaman.setPrefWidth(150);

        TableColumn<KalenderTanam, String> colSemai = new TableColumn<>("Tanggal Semai");
        colSemai.setCellValueFactory(new PropertyValueFactory<>("tanggalSemai"));
        colSemai.setPrefWidth(120);

        TableColumn<KalenderTanam, String> colPanen = new TableColumn<>("Estimasi Panen");
        colPanen.setCellValueFactory(new PropertyValueFactory<>("estimasiPanen"));
        colPanen.setPrefWidth(120);

        TableColumn<KalenderTanam, String> colFase = new TableColumn<>("Fase Pertumbuhan");
        colFase.setCellValueFactory(new PropertyValueFactory<>("fasePertumbuhan"));
        colFase.setPrefWidth(150);

        table.getColumns().addAll(colTanaman, colSemai, colPanen, colFase);
        table.setPlaceholder(new Label("Tidak ada jadwal kalender tanam yang tersedia. Silakan perbarui database."));

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Cari nama tanaman...");
        txtSearch.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Cari");
        btnSearch.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: white; -fx-padding: 8px 20px;");
        btnSearch.setCursor(javafx.scene.Cursor.HAND);

        Button btnTambah = new Button("Tambah");
        btnTambah.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnTambah.setCursor(javafx.scene.Cursor.HAND);

        Button btnUbah = new Button("Ubah Terpilih");
        btnUbah.setStyle("-fx-background-color: #F3D9D3; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #8B3A3A; -fx-padding: 8px 20px;");
        btnUbah.setCursor(javafx.scene.Cursor.HAND);

        Button btnRefresh = new Button("Segarkan");
        btnRefresh.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnRefresh.setCursor(javafx.scene.Cursor.HAND);

        HBox searchBox = new HBox(10, txtSearch, btnSearch, btnTambah, btnUbah, btnRefresh);
        searchBox.setPadding(new Insets(15, 0, 15, 0));
        contentPanel.getChildren().add(searchBox);

        Runnable refreshKalender = () -> {
            table.getItems().clear();
            String keyword = txtSearch.getText().trim();
            List<KalenderTanam> hasil;
            if (keyword.isEmpty()) {
                hasil = kalenderTanamService.getAllKalenderTanam();
            } else {
                hasil = kalenderTanamService.cariTanaman(keyword);
            }
            table.getItems().addAll(hasil);
        };

        btnSearch.setOnAction(e -> refreshKalender.run());
        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            refreshKalender.run();
        });

        btnTambah.setOnAction(e -> {
            Optional<KalenderTanam> result = showKalenderTanamDialog(null);
            result.ifPresent(item -> {
                kalenderTanamService.tambahKalenderTanam(item.getNamaTanaman(), item.getTanggalSemai(), item.getEstimasiPanen(), item.getFasePertumbuhan());
                refreshKalender.run();
            });
        });

        btnUbah.setOnAction(e -> {
            KalenderTanam selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Pilih data", "Pilih entry kalender tanam terlebih dahulu untuk diubah.", Alert.AlertType.WARNING);
                return;
            }
            Optional<KalenderTanam> result = showKalenderTanamDialog(selected);
            result.ifPresent(item -> {
                kalenderTanamService.updateKalender(selected.getId(), item.getNamaTanaman(), item.getTanggalSemai(), item.getEstimasiPanen(), item.getFasePertumbuhan());
                refreshKalender.run();
            });
        });

        refreshKalender.run();
        contentPanel.getChildren().add(table);
    }

    /**
     * Menampilkan Deteksi Hama (dengan detail dan opsi lihat hama lain)
     */
    private void showDeteksiHama() {
        contentPanel.getChildren().clear();

        VBox headerSection = new VBox(5);
        Label title = new Label("Deteksi dan Pencegahan Hama");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Identifikasi hama dan pelajari cara mengatasinya");
        subtitle.setFont(Font.font("SansSerif", 12));
        subtitle.setTextFill(Color.GRAY);
        headerSection.getChildren().addAll(title, subtitle);
        contentPanel.getChildren().add(headerSection);

        contentPanel.getChildren().add(createDivider());

        // Select Hama Section
        HBox selectSection = new HBox(10);
        selectSection.setPadding(new Insets(15, 0, 15, 0));
        selectSection.setAlignment(Pos.CENTER_LEFT);

        Label lblSelect = new Label("Pilih Hama:");
        lblSelect.setFont(Font.font("SansSerif", 12));

        ComboBox<String> cmbHama = new ComboBox<>();
        List<DeteksiHama> daftarHama = deteksiHamaService.getAllHama();
        for (DeteksiHama hama : daftarHama) {
            cmbHama.getItems().add(hama.getNamaHamaPenyakit());
        }
        cmbHama.setStyle("-fx-padding: 8px; -fx-font-size: 12px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        cmbHama.setPrefWidth(250);

        if (!cmbHama.getItems().isEmpty()) {
            cmbHama.getSelectionModel().selectFirst();
        }

        selectSection.getChildren().addAll(lblSelect, cmbHama);
        contentPanel.getChildren().add(selectSection);

        // Detail Hama Section
        VBox detailSection = createHamaDetailPanel(cmbHama);
        VBox.setVgrow(detailSection, Priority.ALWAYS);
        contentPanel.getChildren().add(detailSection);

        // Update detail ketika hama dipilih
        cmbHama.setOnAction(e -> {
            detailSection.getChildren().clear();
            detailSection.getChildren().addAll(createHamaDetailPanel(cmbHama).getChildren());
        });
    }

    /**
     * Membuat panel detail hama
     */
    private VBox createHamaDetailPanel(ComboBox<String> cmbHama) {
        VBox panel = new VBox(15);

        String selectedHama = cmbHama.getValue();
        if (selectedHama == null || selectedHama.isEmpty()) {
            Label lbl = new Label("Pilih hama terlebih dahulu");
            lbl.setFont(Font.font("SansSerif", 12));
            lbl.setTextFill(Color.GRAY);
            panel.getChildren().add(lbl);
            return panel;
        }

        DeteksiHama hama = deteksiHamaService.getHamaByNama(selectedHama);
        if (hama == null) {
            Label lbl = new Label("Data hama tidak ditemukan");
            lbl.setFont(Font.font("SansSerif", 12));
            lbl.setTextFill(Color.GRAY);
            panel.getChildren().add(lbl);
            return panel;
        }

        // Tingkat Bahaya
        HBox tingkatBox = new HBox(10);
        tingkatBox.setStyle("-fx-background-color: " + YELLOW_DANGER + "; -fx-background-radius: 8; " +
                "-fx-padding: 15;");
        Label lblTingkat = new Label("Tingkat Bahaya:");
        lblTingkat.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        Label lblTingkatValue = new Label(hama.getTingkatBahaya());
        lblTingkatValue.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        lblTingkatValue.setTextFill(Color.web("#FF6B6B"));
        tingkatBox.getChildren().addAll(lblTingkat, lblTingkatValue);
        panel.getChildren().add(tingkatBox);

        // Gejala
        VBox gejalaSection = new VBox(8);
        Label lblGejalTitle = new Label("Gejala pada Tanaman:");
        lblGejalTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        TextArea txtGejala = new TextArea(hama.getGejala());
        txtGejala.setWrapText(true);
        txtGejala.setEditable(false);
        txtGejala.setPrefHeight(100);
        txtGejala.setStyle("-fx-control-inner-background: #f9f9f9; -fx-font-family: 'SansSerif'; " +
                "-fx-font-size: 11px;");
        gejalaSection.getChildren().addAll(lblGejalTitle, txtGejala);
        panel.getChildren().add(gejalaSection);

        // Solusi Penanganan
        VBox solusiSection = new VBox(8);
        Label lblSolusiTitle = new Label("Cara Mengatasi:");
        lblSolusiTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        TextArea txtSolusi = new TextArea(hama.getSolusiPenanganan());
        txtSolusi.setWrapText(true);
        txtSolusi.setEditable(false);
        txtSolusi.setPrefHeight(150);
        txtSolusi.setStyle("-fx-control-inner-background: #f9f9f9; -fx-font-family: 'SansSerif'; " +
                "-fx-font-size: 11px;");
        solusiSection.getChildren().addAll(lblSolusiTitle, txtSolusi);
        panel.getChildren().add(solusiSection);

        return panel;
    }

    /**
     * Menampilkan Info Cuaca
     */
    private void showInfoCuaca() {
        contentPanel.getChildren().clear();

        VBox headerSection = new VBox(5);
        Label title = new Label("Informasi Cuaca Pertanian");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Pantau kondisi cuaca untuk perencanaan pertanian yang lebih baik");
        subtitle.setFont(Font.font("SansSerif", 12));
        subtitle.setTextFill(Color.GRAY);
        headerSection.getChildren().addAll(title, subtitle);
        contentPanel.getChildren().add(headerSection);

        contentPanel.getChildren().add(createDivider());

        Label lblIntegrasi = new Label("Data cuaca dapat dipadukan dengan hasil panen untuk membuat keputusan pemupukan dan penanaman lebih tepat.");
        lblIntegrasi.setFont(Font.font("SansSerif", 12));
        lblIntegrasi.setTextFill(Color.web("#5B5B5B"));
        contentPanel.getChildren().add(lblIntegrasi);

        // Table
        TableView<InfoCuacaTani> table = new TableView<>();
        table.setPrefHeight(300);

        TableColumn<InfoCuacaTani, String> colWilayah = new TableColumn<>("Wilayah Lahan");
        colWilayah.setCellValueFactory(new PropertyValueFactory<>("wilayahLahan"));
        colWilayah.setPrefWidth(150);

        TableColumn<InfoCuacaTani, Double> colSuhu = new TableColumn<>("Suhu (°C)");
        colSuhu.setCellValueFactory(new PropertyValueFactory<>("suhu"));
        colSuhu.setPrefWidth(100);

        TableColumn<InfoCuacaTani, Double> colKelembaban = new TableColumn<>("Kelembaban (%)");
        colKelembaban.setCellValueFactory(new PropertyValueFactory<>("kelembaban"));
        colKelembaban.setPrefWidth(120);

        TableColumn<InfoCuacaTani, Double> colHujan = new TableColumn<>("Curah Hujan (mm)");
        colHujan.setCellValueFactory(new PropertyValueFactory<>("curahHujan"));
        colHujan.setPrefWidth(130);

        table.getColumns().addAll(colWilayah, colSuhu, colKelembaban, colHujan);
        table.setPlaceholder(new Label("Tidak ada informasi cuaca yang tersedia. Silakan perbarui data cuaca di database."));

        TextField txtSearch = new TextField();
        txtSearch.setPromptText("Cari wilayah lahan...");
        txtSearch.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");
        HBox.setHgrow(txtSearch, Priority.ALWAYS);

        Button btnSearch = new Button("Cari");
        btnSearch.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: white; -fx-padding: 8px 20px;");
        btnSearch.setCursor(javafx.scene.Cursor.HAND);

        Button btnTambah = new Button("Tambah");
        btnTambah.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnTambah.setCursor(javafx.scene.Cursor.HAND);

        Button btnUbah = new Button("Ubah Terpilih");
        btnUbah.setStyle("-fx-background-color: #F3D9D3; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #8B3A3A; -fx-padding: 8px 20px;");
        btnUbah.setCursor(javafx.scene.Cursor.HAND);

        Button btnRefresh = new Button("Segarkan");
        btnRefresh.setStyle("-fx-background-color: #DCEBD7; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 11px; " +
                "-fx-text-fill: #294A20; -fx-padding: 8px 20px;");
        btnRefresh.setCursor(javafx.scene.Cursor.HAND);

        HBox searchBox = new HBox(10, txtSearch, btnSearch, btnTambah, btnUbah, btnRefresh);
        searchBox.setPadding(new Insets(15, 0, 15, 0));
        contentPanel.getChildren().add(searchBox);

        Runnable refreshCuaca = () -> {
            table.getItems().clear();
            String keyword = txtSearch.getText().trim();
            List<InfoCuacaTani> hasil;
            if (keyword.isEmpty()) {
                hasil = infoCuacaService.getAllInfoCuaca();
            } else {
                hasil = infoCuacaService.cariCuacaByWilayah(keyword);
            }
            table.getItems().addAll(hasil);
        };

        btnSearch.setOnAction(e -> refreshCuaca.run());
        btnRefresh.setOnAction(e -> {
            txtSearch.clear();
            refreshCuaca.run();
        });

        btnTambah.setOnAction(e -> {
            Optional<InfoCuacaTani> result = showInfoCuacaDialog(null);
            result.ifPresent(item -> {
                infoCuacaService.tambahInfoCuaca(item.getWilayahLahan(), item.getSuhu(), item.getKelembaban(), item.getCurahHujan());
                refreshCuaca.run();
            });
        });

        btnUbah.setOnAction(e -> {
            InfoCuacaTani selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Pilih data", "Pilih data cuaca terlebih dahulu untuk diubah.", Alert.AlertType.WARNING);
                return;
            }
            Optional<InfoCuacaTani> result = showInfoCuacaDialog(selected);
            result.ifPresent(item -> {
                infoCuacaService.updateInfoCuaca(selected.getId(), item.getWilayahLahan(), item.getSuhu(), item.getKelembaban(), item.getCurahHujan());
                refreshCuaca.run();
            });
        });

        refreshCuaca.run();
        contentPanel.getChildren().add(table);
    }

    private Optional<Panen> getLatestPanen() {
        return panenService.getAllPanen().stream()
                .filter(p -> p.getTanggalPanen() != null && !p.getTanggalPanen().isBlank())
                .max(Comparator.comparing(Panen::getTanggalPanen));
    }

    private Optional<RekomendasiPupuk> showRekomendasiPupukDialog(RekomendasiPupuk rekomendasi) {
        Dialog<RekomendasiPupuk> dialog = new Dialog<>();
        dialog.setTitle(rekomendasi == null ? "Tambah Rekomendasi Pupuk" : "Ubah Rekomendasi Pupuk");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField txtTanaman = new TextField();
        txtTanaman.setPromptText("Jenis tanaman");
        TextField txtLahan = new TextField();
        txtLahan.setPromptText("Luas lahan (m²)");
        TextField txtUrea = new TextField();
        txtUrea.setPromptText("Kebutuhan Urea");
        TextField txtNpk = new TextField();
        txtNpk.setPromptText("Kebutuhan NPK");

        if (rekomendasi != null) {
            txtTanaman.setText(rekomendasi.getJenisTanaman());
            txtLahan.setText(String.valueOf(rekomendasi.getLuasLahan()));
            txtUrea.setText(String.valueOf(rekomendasi.getKebutuhanUrea()));
            txtNpk.setText(String.valueOf(rekomendasi.getKebutuhanNpk()));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));
        grid.add(new Label("Jenis Tanaman:"), 0, 0);
        grid.add(txtTanaman, 1, 0);
        grid.add(new Label("Luas Lahan (m²):"), 0, 1);
        grid.add(txtLahan, 1, 1);
        grid.add(new Label("Urea (Kg):"), 0, 2);
        grid.add(txtUrea, 1, 2);
        grid.add(new Label("NPK (Kg):"), 0, 3);
        grid.add(txtNpk, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String jenisTanaman = txtTanaman.getText().trim();
                    double luasLahan = Double.parseDouble(txtLahan.getText().trim());
                    double kebutuhanUrea = Double.parseDouble(txtUrea.getText().trim());
                    double kebutuhanNpk = Double.parseDouble(txtNpk.getText().trim());
                    if (jenisTanaman.isEmpty()) {
                        throw new IllegalArgumentException("Jenis tanaman tidak boleh kosong.");
                    }
                    return new RekomendasiPupuk(jenisTanaman, luasLahan, kebutuhanUrea, kebutuhanNpk);
                } catch (NumberFormatException ex) {
                    showAlert("Input tidak valid", "Luas lahan, Urea, dan NPK harus berupa angka.", Alert.AlertType.ERROR);
                } catch (IllegalArgumentException ex) {
                    showAlert("Input tidak valid", ex.getMessage(), Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<KalenderTanam> showKalenderTanamDialog(KalenderTanam kalender) {
        Dialog<KalenderTanam> dialog = new Dialog<>();
        dialog.setTitle(kalender == null ? "Tambah Kalender Tanam" : "Ubah Kalender Tanam");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField txtTanaman = new TextField();
        txtTanaman.setPromptText("Nama tanaman");
        TextField txtSemai = new TextField();
        txtSemai.setPromptText("Tanggal semai");
        TextField txtPanen = new TextField();
        txtPanen.setPromptText("Estimasi panen");
        TextField txtFase = new TextField();
        txtFase.setPromptText("Fase pertumbuhan");

        if (kalender != null) {
            txtTanaman.setText(kalender.getNamaTanaman());
            txtSemai.setText(kalender.getTanggalSemai());
            txtPanen.setText(kalender.getEstimasiPanen());
            txtFase.setText(kalender.getFasePertumbuhan());
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));
        grid.add(new Label("Nama Tanaman:"), 0, 0);
        grid.add(txtTanaman, 1, 0);
        grid.add(new Label("Tanggal Semai:"), 0, 1);
        grid.add(txtSemai, 1, 1);
        grid.add(new Label("Estimasi Panen:"), 0, 2);
        grid.add(txtPanen, 1, 2);
        grid.add(new Label("Fase Pertumbuhan:"), 0, 3);
        grid.add(txtFase, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                String namaTanaman = txtTanaman.getText().trim();
                String tanggalSemai = txtSemai.getText().trim();
                String estimasiPanen = txtPanen.getText().trim();
                String fasePertumbuhan = txtFase.getText().trim();
                if (namaTanaman.isEmpty() || tanggalSemai.isEmpty() || estimasiPanen.isEmpty() || fasePertumbuhan.isEmpty()) {
                    showAlert("Input tidak valid", "Semua field kalender tanam harus diisi.", Alert.AlertType.ERROR);
                    return null;
                }
                return new KalenderTanam(namaTanaman, tanggalSemai, estimasiPanen, fasePertumbuhan);
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private Optional<InfoCuacaTani> showInfoCuacaDialog(InfoCuacaTani info) {
        Dialog<InfoCuacaTani> dialog = new Dialog<>();
        dialog.setTitle(info == null ? "Tambah Info Cuaca" : "Ubah Info Cuaca");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        TextField txtWilayah = new TextField();
        txtWilayah.setPromptText("Wilayah lahan");
        TextField txtSuhu = new TextField();
        txtSuhu.setPromptText("Suhu (°C)");
        TextField txtKelembaban = new TextField();
        txtKelembaban.setPromptText("Kelembaban (%)");
        TextField txtHujan = new TextField();
        txtHujan.setPromptText("Curah hujan (mm)");

        if (info != null) {
            txtWilayah.setText(info.getWilayahLahan());
            txtSuhu.setText(String.valueOf(info.getSuhu()));
            txtKelembaban.setText(String.valueOf(info.getKelembaban()));
            txtHujan.setText(String.valueOf(info.getCurahHujan()));
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 10, 20));
        grid.add(new Label("Wilayah Lahan:"), 0, 0);
        grid.add(txtWilayah, 1, 0);
        grid.add(new Label("Suhu (°C):"), 0, 1);
        grid.add(txtSuhu, 1, 1);
        grid.add(new Label("Kelembaban (%):"), 0, 2);
        grid.add(txtKelembaban, 1, 2);
        grid.add(new Label("Curah Hujan (mm):"), 0, 3);
        grid.add(txtHujan, 1, 3);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(buttonType -> {
            if (buttonType == ButtonType.OK) {
                try {
                    String wilayah = txtWilayah.getText().trim();
                    double suhu = Double.parseDouble(txtSuhu.getText().trim());
                    double kelembaban = Double.parseDouble(txtKelembaban.getText().trim());
                    double curahHujan = Double.parseDouble(txtHujan.getText().trim());
                    if (wilayah.isEmpty()) {
                        showAlert("Input tidak valid", "Wilayah lahan harus diisi.", Alert.AlertType.ERROR);
                        return null;
                    }
                    return new InfoCuacaTani(wilayah, suhu, kelembaban, curahHujan);
                } catch (NumberFormatException ex) {
                    showAlert("Input tidak valid", "Suhu, kelembaban, dan curah hujan harus berupa angka.", Alert.AlertType.ERROR);
                }
            }
            return null;
        });

        return dialog.showAndWait();
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Helper untuk membuat divider
     */
    private javafx.scene.shape.Line createDivider() {
        javafx.scene.shape.Line divider = new javafx.scene.shape.Line();
        divider.setStrokeWidth(1);
        divider.setStroke(Color.web("#e0e0e0"));
        divider.setEndX(600);
        return divider;
    }
}
