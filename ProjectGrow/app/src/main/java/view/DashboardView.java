package view;

import service.PanenService;
import service.StokService;
import model.Stok;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class DashboardView extends Application {

    private BorderPane mainContainer;
    private ScrollPane panelBeranda;
    private StokView stokView;
    private PanenView panenView;
    private Stage primaryStage;
    private Label lblStokRendahValue; 
    private PanenService panenService;
    private HBox menuBeranda;
    private HBox menuPanen;
    private HBox menuStok;
    private HBox menuSaran;
    private HBox menuProfil;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("GrowSeeds - Aplikasi Manajemen Pertanian");

        mainContainer = new BorderPane();
        panenService = new PanenService();
        stokView = new StokView(() -> updateStokRendahCounter());
        panenView = new PanenView(); 
        VBox sidebar = createSidebar();
        mainContainer.setLeft(sidebar);

        panelBeranda = createBerandaPanel();
        mainContainer.setCenter(panelBeranda);
        updateStokRendahCounter();

        Scene scene = new Scene(mainContainer, 1100, 700);
        primaryStage.setScene(scene);

        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox();
        sidebar.setStyle("-fx-background-color: #294A20;");
        sidebar.setPrefWidth(250);
        sidebar.setPadding(new Insets(30, 0, 20, 0)); 

        Label lblSidebarTitle = new Label("GrowSeeds");
        lblSidebarTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        lblSidebarTitle.setTextFill(Color.WHITE);
        HBox headerBox = new HBox(lblSidebarTitle);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(0, 0, 40, 25));

        VBox menuBox = new VBox(10); 
        menuBox.setPadding(new Insets(0, 15, 0, 15));


        menuBeranda = createMenuItem("Beranda", true);
        menuPanen = createMenuItem("Manajemen Panen", false);
        menuStok = createMenuItem("Pelacakan Stok", false);
        menuSaran = createMenuItem("Saran Tani", false);
        menuProfil = createMenuItem("Profil", false);

        menuBox.getChildren().addAll(menuBeranda, menuPanen, menuStok, menuSaran, menuProfil);

        menuBeranda.setOnMouseClicked(e -> {
            setMenuActive(menuBeranda);
            mainContainer.setCenter(panelBeranda);
            updateStokRendahCounter();
        });

        menuStok.setOnMouseClicked(e -> {
            setMenuActive(menuStok);
            stokView.refreshData();
            mainContainer.setCenter(stokView);
        });
        

        menuPanen.setOnMouseClicked(e -> {
            setMenuActive(menuPanen);
            mainContainer.setCenter(panenView);
        });
        menuSaran.setOnMouseClicked(e -> setMenuActive(menuSaran));
        menuProfil.setOnMouseClicked(e -> setMenuActive(menuProfil));

        Label lblKeluar = new Label("Keluar");
        lblKeluar.setFont(Font.font("SansSerif", 18));
        lblKeluar.setTextFill(Color.rgb(200, 220, 190));
        HBox footerBox = new HBox(lblKeluar);
        footerBox.setPadding(new Insets(0, 0, 30, 25));
        footerBox.setCursor(Cursor.HAND);
        footerBox.setOnMouseClicked(e -> {
            primaryStage.close();
        });

        VBox.setVgrow(menuBox, Priority.ALWAYS); 

        sidebar.getChildren().addAll(headerBox, menuBox, footerBox);
        return sidebar;
    }

    private void setMenuActive(HBox selectedMenu) {
        HBox[] allMenus = {menuBeranda, menuPanen, menuStok, menuSaran, menuProfil};
        
        for (HBox menu : allMenus) {
            Label lbl = (Label) menu.getChildren().get(0);
            if (menu == selectedMenu) {
                menu.setStyle("-fx-background-color: #3C6630; -fx-background-radius: 8;");
                lbl.setFont(Font.font("SansSerif", FontWeight.BOLD, 15));
                lbl.setTextFill(Color.WHITE);
            } else {
                menu.setStyle("-fx-background-color: transparent; -fx-background-radius: 0;");
                lbl.setFont(Font.font("SansSerif", FontWeight.NORMAL, 15));
                lbl.setTextFill(Color.web("#C8DCBE"));
            }
        }
    }

    private ScrollPane createBerandaPanel() {
        VBox mainContent = new VBox();
        mainContent.setStyle("-fx-background-color: #F5F7F4;");

        BorderPane topBar = new BorderPane();
        topBar.setPadding(new Insets(25, 35, 20, 35));

        VBox topBarLeft = new VBox(5);
        Label lblBeranda = new Label("Dashboard Beranda");
        lblBeranda.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        lblBeranda.setTextFill(Color.web("#294A20"));
        
        Label lblWelcome = new Label("Selamat datang, pantau data pertanian Anda hari ini.");
        lblWelcome.setFont(Font.font("SansSerif", 14));
        lblWelcome.setTextFill(Color.GRAY);
        topBarLeft.getChildren().addAll(lblBeranda, lblWelcome);
        topBar.setLeft(topBarLeft);

        Circle avatar = new Circle(25);
        avatar.setFill(Color.web("#DCEBD7"));
        topBar.setRight(avatar);
        BorderPane.setAlignment(avatar, Pos.CENTER_RIGHT);

        VBox bodyPanel = new VBox(25);
        bodyPanel.setPadding(new Insets(10, 35, 30, 35));

        VBox banner = new VBox(8);
        banner.setStyle("-fx-background-color: #3C6630; -fx-background-radius: 12;");
        banner.setPadding(new Insets(20, 25, 20, 25));
        banner.setAlignment(Pos.CENTER_LEFT);

        Label lblBannerTitle = new Label("Sistem Pertanian Berjalan Lancar");
        lblBannerTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 20));
        lblBannerTitle.setStyle("-fx-text-fill: white;");
        
        Label lblBannerSub = new Label("Data cuaca dan kalender tanam siap diakses. Pastikan Anda memperbarui log panen harian.");
        lblBannerSub.setFont(Font.font("SansSerif", 14));
        lblBannerSub.setStyle(("-fx-text-fill: #DCEBD7;"));
        banner.getChildren().addAll(lblBannerTitle, lblBannerSub);

        int totalLahan = panenService.hitungTotalLahanAktif();
        double totalPanen = panenService.hitungTotalHasilPanen();

        VBox cardLahan = createMetricCard("Total Lahan Aktif", String.valueOf(totalLahan), "Lahan", "#FFFFFF");
        VBox cardEstimasi = createMetricCard("Total Hasil Panen", String.format("%.2f", totalPanen), "Ton", "#FFFFFF");

        VBox cardStokRendah = new VBox(8);
        cardStokRendah.setStyle("-fx-background-color: #FFF9E6; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        cardStokRendah.setPadding(new Insets(20));
        HBox.setHgrow(cardStokRendah, Priority.ALWAYS);

        Label lblStokTitle = new Label("Peringatan Stok Rendah");
        lblStokTitle.setFont(Font.font("SansSerif", 14));
        lblStokTitle.setTextFill(Color.GRAY);

        lblStokRendahValue = new Label("0 Item");
        lblStokRendahValue.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        lblStokRendahValue.setTextFill(Color.web("#294A20"));

        cardStokRendah.getChildren().addAll(lblStokTitle, lblStokRendahValue);


        cardStokRendah.setCursor(Cursor.HAND);
        cardStokRendah.setOnMouseClicked(e -> {
            setMenuActive(menuStok);
            stokView.refreshData();
            mainContainer.setCenter(stokView);
        });

        HBox metricRow = new HBox(20);
        metricRow.getChildren().addAll(cardLahan, cardEstimasi, cardStokRendah);

        HBox bottomRow = new HBox(20);
        
        VBox aktivitasPanel = createSectionPanel("Aktivitas Terakhir");
        aktivitasPanel.getChildren().add(createEmptyRowPlaceholder("Belum ada aktivitas yang dicatat."));
        HBox.setHgrow(aktivitasPanel, Priority.ALWAYS); 

        VBox tipsPanel = createSectionPanel("Saran Tani Cerdas");
        tipsPanel.getChildren().addAll(
            createTipsRow("Jadwal Irigasi", "Suhu hari ini mencapai 32°C. Disarankan melakukan penyiraman lahan padi pada sore hari."),
            createTipsRow("Pemupukan", "Waktunya memberikan pupuk urea untuk Lahan B (Jagung).")
        );
        HBox.setHgrow(tipsPanel, Priority.ALWAYS); 

        bottomRow.getChildren().addAll(aktivitasPanel, tipsPanel);

        bodyPanel.getChildren().addAll(banner, metricRow, bottomRow);
        mainContent.getChildren().addAll(topBar, bodyPanel);

        ScrollPane scrollPane = new ScrollPane(mainContent);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-background: #F5F7F4;");
        scrollPane.setBorder(Border.EMPTY);

        return scrollPane;
    }

    
    private void updateStokRendahCounter() {
        if (stokView != null && lblStokRendahValue != null) {
            int jumlahRendah = stokView.getJumlahStokRendah();
            lblStokRendahValue.setText(jumlahRendah + " Item");
        }
    }

    private HBox createMenuItem(String text, boolean isActive) {
        HBox item = new HBox();
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(12, 15, 12, 15));
        item.setCursor(Cursor.HAND); 

        if (isActive) {
            item.setStyle("-fx-background-color: #3C6630; -fx-background-radius: 8;");
        }

        Label label = new Label(text);
        label.setFont(Font.font("SansSerif", isActive ? FontWeight.BOLD : FontWeight.NORMAL, 15));
        label.setTextFill(isActive ? Color.WHITE : Color.web("#C8DCBE"));
        
        item.getChildren().add(label);
        return item;
    }

    private VBox createMetricCard(String title, String value, String unit, String hexColor) {
        VBox card = new VBox(8);
        card.setStyle("-fx-background-color: " + hexColor + "; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        card.setPadding(new Insets(20));
        HBox.setHgrow(card, Priority.ALWAYS); 

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("SansSerif", 14));
        lblTitle.setTextFill(Color.GRAY);

        Label lblValue = new Label(value + " " + unit);
        lblValue.setFont(Font.font("Georgia", FontWeight.BOLD, 26));
        lblValue.setTextFill(Color.web("#294A20"));

        card.getChildren().addAll(lblTitle, lblValue);
        return card;
    }

    private VBox createSectionPanel(String sectionTitle) {
        VBox panel = new VBox(15);
        panel.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        panel.setPadding(new Insets(25));

        Label lblTitle = new Label(sectionTitle);
        lblTitle.setFont(Font.font("Georgia", FontWeight.BOLD, 18));
        lblTitle.setTextFill(Color.web("#294A20"));
        
        panel.getChildren().add(lblTitle);
        return panel;
    }

    private Label createEmptyRowPlaceholder(String msg) {
        Label lblMsg = new Label(msg);
        lblMsg.setFont(Font.font("SansSerif", 14));
        lblMsg.setStyle("-fx-font-style: italic;");
        lblMsg.setTextFill(Color.LIGHTGRAY);
        lblMsg.setMaxWidth(Double.MAX_VALUE);
        lblMsg.setAlignment(Pos.CENTER);
        lblMsg.setPadding(new Insets(40, 0, 40, 0));
        return lblMsg;
    }

    private VBox createTipsRow(String title, String desc) {
        VBox textBlock = new VBox(5);
        textBlock.setStyle("-fx-background-color: #F8FBF6; -fx-background-radius: 8; -fx-border-color: #E2EFE0; -fx-border-radius: 8;");
        textBlock.setPadding(new Insets(12, 15, 12, 15));

        Label lblTitle = new Label(title);
        lblTitle.setFont(Font.font("SansSerif", FontWeight.BOLD, 14));
        lblTitle.setTextFill(Color.web("#294A20"));
        
        Label lblDesc = new Label(desc);
        lblDesc.setFont(Font.font("SansSerif", 13));
        lblDesc.setTextFill(Color.web("#555555"));
        lblDesc.setWrapText(true); 
        
        textBlock.getChildren().addAll(lblTitle, lblDesc);
        return textBlock;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
