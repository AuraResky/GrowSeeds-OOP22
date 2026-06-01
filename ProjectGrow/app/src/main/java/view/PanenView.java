package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import model.Panen;
import service.PanenService;
import session.UserSession;

public class PanenView extends VBox {
    
    private TextField txtLahan, txtTanaman, txtHasil, txtSatuan, txtCuaca;
    private DatePicker dpTanggal;
    private TableView<Panen> tablePanen;
    private Button btnSimpan, btnHapus;
    
    private PanenService panenService;
    private ObservableList<Panen> panenList;

    public PanenView() {
        panenService = new PanenService();
        panenList = FXCollections.observableArrayList();

        this.setSpacing(20);
        this.setPadding(new Insets(30));
        this.setStyle("-fx-background-color: #F8FAF8;");

        Label lblJudul = new Label("Manajemen Data Panen");
        lblJudul.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));
        lblJudul.setTextFill(Color.web("#294A20"));

        GridPane formGrid = new GridPane();
        formGrid.setHgap(15);
        formGrid.setVgap(15);

        txtLahan = new TextField(); txtLahan.setPromptText("Contoh: Sawah A");
        txtTanaman = new TextField(); txtTanaman.setPromptText("Contoh: Padi");
        txtHasil = new TextField(); txtHasil.setPromptText("Contoh: 1.5");
        txtSatuan = new TextField(); txtSatuan.setPromptText("Contoh: Ton");
        dpTanggal = new DatePicker(); dpTanggal.setPromptText("Pilih Tanggal");
        txtCuaca = new TextField(); txtCuaca.setPromptText("Contoh: Cerah");

        formGrid.add(new Label("Nama Lahan:"), 0, 0); formGrid.add(txtLahan, 1, 0);
        formGrid.add(new Label("Jenis Tanaman:"), 0, 1); formGrid.add(txtTanaman, 1, 1);
        formGrid.add(new Label("Hasil Panen:"), 2, 0); formGrid.add(txtHasil, 3, 0);
        formGrid.add(new Label("Satuan:"), 2, 1); formGrid.add(txtSatuan, 3, 1);
        formGrid.add(new Label("Tanggal Panen:"), 4, 0); formGrid.add(dpTanggal, 5, 0);
        formGrid.add(new Label("Kondisi Cuaca:"), 4, 1); formGrid.add(txtCuaca, 5, 1);

        btnSimpan = new Button("Simpan Data");
        btnSimpan.setStyle("-fx-background-color: #294A20; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        
        btnHapus = new Button("Hapus Terpilih");
        btnHapus.setStyle("-fx-background-color: #C0392B; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");

        HBox boxTombol = new HBox(15, btnSimpan, btnHapus);
        boxTombol.setAlignment(Pos.CENTER_LEFT);

        tablePanen = new TableView<>();
        
        TableColumn<Panen, String> colLahan = new TableColumn<>("Lahan");
        colLahan.setCellValueFactory(new PropertyValueFactory<>("namaLahan"));
        
        TableColumn<Panen, String> colTanaman = new TableColumn<>("Tanaman");
        colTanaman.setCellValueFactory(new PropertyValueFactory<>("jenisTanaman"));
        
        TableColumn<Panen, Double> colHasil = new TableColumn<>("Hasil");
        colHasil.setCellValueFactory(new PropertyValueFactory<>("jumlahPanen"));

        TableColumn<Panen, String> colSatuan = new TableColumn<>("Satuan");
        colSatuan.setCellValueFactory(new PropertyValueFactory<>("satuan"));
        
        TableColumn<Panen, String> colTanggal = new TableColumn<>("Tanggal");
        colTanggal.setCellValueFactory(new PropertyValueFactory<>("tanggalPanen"));

        TableColumn<Panen, String> colCuaca = new TableColumn<>("Cuaca");
        colCuaca.setCellValueFactory(new PropertyValueFactory<>("kondisiCuaca"));

        tablePanen.getColumns().addAll(colLahan, colTanaman, colHasil, colSatuan, colTanggal, colCuaca);
        tablePanen.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        VBox.setVgrow(tablePanen, Priority.ALWAYS);

        this.getChildren().addAll(lblJudul, formGrid, boxTombol, tablePanen);

        aturLogikaTombol();

        muatDataTabel();
    }

    private void aturLogikaTombol() {
        btnSimpan.setOnAction(e -> {
            try {
                int idUser = UserSession.requireUserId();
                String lahan = txtLahan.getText();
                String tanaman = txtTanaman.getText();
                double hasil = Double.parseDouble(txtHasil.getText());
                String satuan = txtSatuan.getText();
                String tanggal = (dpTanggal.getValue() != null) ? dpTanggal.getValue().toString() : "";
                String cuaca = txtCuaca.getText();

                Panen panenBaru = new Panen(idUser, lahan, tanaman, hasil, satuan, tanggal, cuaca);
                panenService.tambahPanen(panenBaru);

                kosongkanForm();
                muatDataTabel();

            } catch (NumberFormatException ex) {
                System.out.println("Error: Pastikan Hasil Panen diisi dengan angka!");
            }
        });

        btnHapus.setOnAction(e -> {
            Panen panenTerpilih = tablePanen.getSelectionModel().getSelectedItem();
            if (panenTerpilih != null) {
                panenService.hapusPanen(panenTerpilih.getId());
                muatDataTabel();
            }
        });
    }

    private void muatDataTabel() {
        panenList.clear();
        panenList.addAll(panenService.getAllPanen());
        tablePanen.setItems(panenList);
    }

    private void kosongkanForm() {
        txtLahan.clear();
        txtTanaman.clear();
        txtHasil.clear();
        txtSatuan.clear();
        dpTanggal.setValue(null);
        txtCuaca.clear();
    }
}