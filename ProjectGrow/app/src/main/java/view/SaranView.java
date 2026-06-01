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
import model.KalenderTanam;
import model.Panen;
import service.DeteksiHamaService;
import service.KalenderTanamService;
import service.PanenService;

public class SaranView extends VBox {
    private static final String GREEN_DARK = "#294a20";
    private static final String GREEN_LIGHT = "#3C6630";
    private static final String BACKGROUND = "#f5f7f4";
    private static final String YELLOW_DANGER = "#FFF9E6";
    private static final String ORANGE_WARNING = "#FFE4B5";

    private final DeteksiHamaService deteksiHamaService = new DeteksiHamaService();
    private final KalenderTanamService kalenderTanamService = new KalenderTanamService();
    private final PanenService panenService = new PanenService();

    private VBox contentPanel;
    private Button btnRekomendasi;
    private Button btnKalender;
    private Button btnHama;

    public SaranView() {
        buildView();
    }

    private void buildView() {
        setSpacing(0);
        setBackground(new Background(new BackgroundFill(Color.web(BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(20, 30, 20, 30));

 
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


        HBox submenuPanel = createSubmenuPanel();
        getChildren().add(submenuPanel);


        contentPanel = new VBox();
        contentPanel.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        contentPanel.setPadding(new Insets(30));
        VBox.setVgrow(contentPanel, Priority.ALWAYS);

        getChildren().add(contentPanel);


        showRekomendasiPupuk();
    }


    private HBox createSubmenuPanel() {
        HBox submenu = new HBox(10);
        submenu.setPadding(new Insets(0, 0, 20, 0));
        submenu.setAlignment(Pos.CENTER_LEFT);

        btnRekomendasi = createSubmenuButton(" Rekomendasi Pupuk", true);
        btnKalender = createSubmenuButton(" Kalender Tanam", false);
        btnHama = createSubmenuButton(" Deteksi Hama", false);

        btnRekomendasi.setOnAction(e -> switchMenu(btnRekomendasi, "rekomendasi"));
        btnKalender.setOnAction(e -> switchMenu(btnKalender, "kalender"));
        btnHama.setOnAction(e -> switchMenu(btnHama, "hama"));

        submenu.getChildren().addAll(btnRekomendasi, btnKalender, btnHama);
        return submenu;
    }


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


    private void switchMenu(Button activeBtn, String menu) {
        // Update button states
        btnRekomendasi.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        btnKalender.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-size: 12px; -fx-text-fill: #666;");
        btnHama.setStyle("-fx-background-color: #e0e0e0; -fx-background-radius: 5; " +
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
        }
    }


    private void showRekomendasiPupuk() {
        contentPanel.getChildren().clear();

        VBox headerSection = new VBox(5);
        Label title = new Label("Kalkulator Rekomendasi Pupuk");
        title.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Hitung dosis pupuk berdasarkan jenis komoditas dan luas lahan.");
        subtitle.setFont(Font.font("SansSerif", 12));
        subtitle.setTextFill(Color.GRAY);
        headerSection.getChildren().addAll(title, subtitle);
        contentPanel.getChildren().add(headerSection);

        contentPanel.getChildren().add(createDivider());

        HBox mainLayout = new HBox(30);
        mainLayout.setAlignment(Pos.TOP_LEFT);

        VBox formCard = new VBox(15);
        formCard.setPrefWidth(380);
        formCard.setPadding(new Insets(20));
        formCard.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");

        Label lblJenis = new Label("Jenis Tanaman:");
        lblJenis.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        TextField txtTanaman = new TextField();
        txtTanaman.setPromptText("Masukkan jenis tanaman");
        txtTanaman.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");
        txtTanaman.setMaxWidth(Double.MAX_VALUE);

        Label lblLuas = new Label("Luas Lahan (m²):");
        lblLuas.setFont(Font.font("SansSerif", FontWeight.BOLD, 12));
        TextField txtLuas = new TextField();
        txtLuas.setPromptText("Masukkan luas area (m²)");
        txtLuas.setStyle("-fx-padding: 10px; -fx-font-size: 12px; -fx-border-color: #ddd; -fx-border-radius: 8; -fx-background-radius: 8;");
        txtLuas.setMaxWidth(Double.MAX_VALUE);

        Button btnHitung = new Button("Hitung Sekarang");
        btnHitung.setMaxWidth(Double.MAX_VALUE);
        btnHitung.setCursor(javafx.scene.Cursor.HAND);
        btnHitung.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 12px; -fx-background-radius: 8;");

        formCard.getChildren().addAll(lblJenis, txtTanaman, lblLuas, txtLuas, btnHitung);

        VBox resultCard = new VBox(15);
        resultCard.setPadding(new Insets(20));
        resultCard.setPrefWidth(450);
        resultCard.setStyle("-fx-background-color: #F4F7F4; -fx-background-radius: 12; -fx-border-color: #D2E2D2; -fx-border-radius: 12;");

        Label lblResultTitle = new Label("Hasil Analisis Kebutuhan");
        lblResultTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 16));
        lblResultTitle.setTextFill(Color.web(GREEN_DARK));

        Label lblOutput = new Label("Silakan isi form di samping kiri untuk mengkalkulasi takaran pupuk.");
        lblOutput.setWrapText(true);
        lblOutput.setFont(Font.font("SansSerif", 13));
        lblOutput.setTextFill(Color.web("#555555"));

        resultCard.getChildren().addAll(lblResultTitle, lblOutput);

        btnHitung.setOnAction(e -> {
            try {
                double luas = Double.parseDouble(txtLuas.getText().trim());
                String tanaman = txtTanaman.getText().trim();
                if (tanaman.isEmpty()) {
                    lblOutput.setText("⚠️ Tolong isi jenis tanaman terlebih dahulu.");
                    return;
                }
                double urea = luas * 0.015;
                double npk = luas * 0.020;
                lblOutput.setText(String.format(
                        "Untuk luas area %,.0f m² tanaman %s, dosis optimal fase awal:\n\n" +
                                "• Kebutuhan Urea : %,.1f Kg\n" +
                                "• Kebutuhan NPK  : %,.1f Kg\n\n" +
                                "Saran: Aplikasikan pada pagi hari saat kondisi tanah lembab.",
                        luas, tanaman, urea, npk
                ));
            } catch (NumberFormatException ex) {
                lblOutput.setText("⚠️ Tolong masukkan nilai angka yang valid pada input luas lahan.");
            }
        });

        mainLayout.getChildren().addAll(formCard, resultCard);
        contentPanel.getChildren().add(mainLayout);
    }


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

    private Optional<Panen> getLatestPanen() {
        return panenService.getAllPanen().stream()
                .filter(p -> p.getTanggalPanen() != null && !p.getTanggalPanen().isBlank())
                .max(Comparator.comparing(Panen::getTanggalPanen));
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

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    private javafx.scene.shape.Line createDivider() {
        javafx.scene.shape.Line divider = new javafx.scene.shape.Line();
        divider.setStrokeWidth(1);
        divider.setStroke(Color.web("#e0e0e0"));
        divider.setEndX(600);
        return divider;
    }
}
