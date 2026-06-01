package view;

import java.util.Optional;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import model.Profile;
import service.ProfileService;
import session.UserSession;

public class ProfileView extends VBox {
    private static final String GREEN_DARK = "#294a20";
    private static final String GREEN_LIGHT = "#3C6630";
    private static final String BACKGROUND = "#f5f7f4";
    
    private final ProfileService profileService;
    private Profile currentProfile;
    private Label lblNamaPengguna;
    private Label lblEmailKontak;
    private Label lblStatusLahan;
    private int clickCount = 0;
    private long lastClickTime = 0;
    private static final long DOUBLE_CLICK_DELAY = 300;

    public ProfileView() {
        this.profileService = new ProfileService();
        buildView();
        loadProfile();
    }

    private void buildView() {
        setSpacing(0);
        setBackground(new Background(new BackgroundFill(Color.web(BACKGROUND), CornerRadii.EMPTY, Insets.EMPTY)));
        setPadding(new Insets(20, 30, 20, 30));


        BorderPane headerPanel = new BorderPane();
        headerPanel.setPadding(new Insets(0, 0, 30, 0));

        VBox titleBlock = new VBox(5);
        Label title = new Label("Profil Pengguna");
        title.setFont(Font.font("Georgia", FontWeight.BOLD, 28));
        title.setTextFill(Color.web(GREEN_DARK));

        Label subtitle = new Label("Kelola informasi pribadi dan pengaturan akun Anda");
        subtitle.setFont(Font.font("SansSerif", 13));
        subtitle.setTextFill(Color.GRAY);
        
        titleBlock.getChildren().addAll(title, subtitle);
        headerPanel.setLeft(titleBlock);

        getChildren().add(headerPanel);


        VBox profileCard = createProfileCard();
        getChildren().add(profileCard);


        HBox actionPanel = createActionPanel();
        getChildren().add(actionPanel);
    }


    private VBox createProfileCard() {
        VBox card = new VBox(20);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 5);");
        card.setPadding(new Insets(40));
        card.setMaxWidth(600);


        VBox avatarSection = createAvatarSection();
        card.getChildren().add(avatarSection);


        addDivider(card);

        VBox infoSection = createInfoSection();
        card.getChildren().add(infoSection);

        return card;
    }


    private VBox createAvatarSection() {
        VBox section = new VBox(15);
        section.setAlignment(Pos.CENTER);


        Circle avatar = new Circle(60);
        avatar.setFill(Color.web(GREEN_LIGHT));
        

        Label lblInisial = new Label("GS");
        lblInisial.setFont(Font.font("SansSerif", FontWeight.BOLD, 24));
        lblInisial.setTextFill(Color.WHITE);
        lblInisial.setAlignment(Pos.CENTER);


        javafx.scene.layout.StackPane avatarStack = new javafx.scene.layout.StackPane();
        avatarStack.getChildren().addAll(avatar, lblInisial);
        avatarStack.setPrefSize(120, 120);
        avatarStack.setAlignment(Pos.CENTER);


        lblNamaPengguna = new Label("Nama Pengguna");
        lblNamaPengguna.setFont(Font.font("SansSerif", FontWeight.BOLD, 22));
        lblNamaPengguna.setTextFill(Color.web(GREEN_DARK));
        

        setupDoubleClickNameEdit(lblNamaPengguna);


        Label lblHint = new Label("(Double-click untuk mengubah nama)");
        lblHint.setFont(Font.font("SansSerif", 11));
        lblHint.setTextFill(Color.GRAY);

        section.getChildren().addAll(avatarStack, lblNamaPengguna, lblHint);
        return section;
    }


    private VBox createInfoSection() {
        VBox section = new VBox(20);


        VBox emailGroup = createInfoGroup(
                "Alamat Email",
                lblEmailKontak = new Label("email@growseeds.id")
        );
        section.getChildren().add(emailGroup);


        VBox statusGroup = createInfoGroup(
                "Status Lahan",
                lblStatusLahan = new Label("Lahan Pribadi")
        );
        section.getChildren().add(statusGroup);


        VBox userIdGroup = createInfoGroup(
                "ID Pengguna",
                new Label(String.valueOf(UserSession.requireUserId()))
        );
        section.getChildren().add(userIdGroup);

        return section;
    }


    private VBox createInfoGroup(String label, Label valueLabel) {
        VBox group = new VBox(8);
        
        Label lblLabel = new Label(label);
        lblLabel.setFont(Font.font("SansSerif", 12));
        lblLabel.setTextFill(Color.GRAY);

        valueLabel.setFont(Font.font("SansSerif", 14));
        valueLabel.setTextFill(Color.web("#323232"));

        group.getChildren().addAll(lblLabel, valueLabel);
        return group;
    }

 
    private HBox createActionPanel() {
        HBox panel = new HBox(15);
        panel.setAlignment(Pos.CENTER);
        panel.setPadding(new Insets(20, 0, 0, 0));

        Button btnEditProfile = new Button("Ubah Email & Status Lahan");
        btnEditProfile.setStyle("-fx-background-color: " + GREEN_LIGHT + "; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: white; " +
                "-fx-padding: 12px 30px;");
        btnEditProfile.setCursor(javafx.scene.Cursor.HAND);
        btnEditProfile.setOnAction(e -> openEditProfileDialog());

        Button btnRefresh = new Button("Refresh");
        btnRefresh.setStyle("-fx-background-color: #757575; -fx-background-radius: 5; " +
                "-fx-font-family: 'SansSerif'; -fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: white; " +
                "-fx-padding: 12px 30px;");
        btnRefresh.setCursor(javafx.scene.Cursor.HAND);
        btnRefresh.setOnAction(e -> loadProfile());

        panel.getChildren().addAll(btnEditProfile, btnRefresh);
        return panel;
    }

 
    private void setupDoubleClickNameEdit(Label label) {
        label.setOnMouseClicked(this::handleNameClick);
    }

    private void handleNameClick(MouseEvent event) {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastClickTime <= DOUBLE_CLICK_DELAY) {
            clickCount++;
        } else {
            clickCount = 1;
        }
        
        lastClickTime = currentTime;

        if (clickCount == 2) {
            clickCount = 0;
            openEditNameDialog();
        }
    }


    private void openEditNameDialog() {
        javafx.scene.control.Dialog<String> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Ubah Nama");
        dialog.setHeaderText("Masukkan nama pengguna baru");

        ButtonType okButton = new ButtonType("Simpan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        VBox content = new VBox(10);
        content.setPadding(new Insets(10));

        Label lblInfo = new Label("Nama Baru:");
        lblInfo.setFont(Font.font("SansSerif", 12));

        TextField txtNamaBaru = new TextField();
        txtNamaBaru.setText(lblNamaPengguna.getText());
        txtNamaBaru.setStyle("-fx-padding: 10px; -fx-font-size: 13px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");

        content.getChildren().addAll(lblInfo, txtNamaBaru);
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return txtNamaBaru.getText().trim();
            }
            return null;
        });

        Optional<String> result = dialog.showAndWait();
        
        if (result.isPresent() && !result.get().isEmpty()) {
            String namaBaru = result.get();
            updateNamaPengguna(namaBaru);
        }
    }

 
    private void openEditProfileDialog() {
        javafx.scene.control.Dialog<javafx.util.Pair<String, String>> dialog = new javafx.scene.control.Dialog<>();
        dialog.setTitle("Ubah Profil");
        dialog.setHeaderText("Perbarui informasi profil Anda");

        ButtonType okButton = new ButtonType("Simpan", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = ButtonType.CANCEL;
        dialog.getDialogPane().getButtonTypes().addAll(okButton, cancelButton);

        VBox content = new VBox(15);
        content.setPadding(new Insets(15));


        Label lblEmailLabel = new Label("Alamat Email:");
        lblEmailLabel.setFont(Font.font("SansSerif", 12));
        TextField txtEmail = new TextField();
        txtEmail.setText(lblEmailKontak.getText());
        txtEmail.setStyle("-fx-padding: 10px; -fx-font-size: 13px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");


        Label lblStatusLabel = new Label("Status Lahan:");
        lblStatusLabel.setFont(Font.font("SansSerif", 12));
        
        javafx.scene.control.ComboBox<String> cmbStatus = new javafx.scene.control.ComboBox<>();
        cmbStatus.getItems().addAll("Lahan Pribadi", "Lahan Sewa", "Lahan Komunal", "Lahan Percobaan");
        cmbStatus.setValue(lblStatusLahan.getText());
        cmbStatus.setStyle("-fx-padding: 10px; -fx-font-size: 13px; -fx-border-color: #ddd; " +
                "-fx-border-radius: 5; -fx-background-radius: 5;");

        content.getChildren().addAll(
                lblEmailLabel, txtEmail,
                lblStatusLabel, cmbStatus
        );
        dialog.getDialogPane().setContent(content);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButton) {
                return new javafx.util.Pair<>(txtEmail.getText().trim(), cmbStatus.getValue());
            }
            return null;
        });

        Optional<javafx.util.Pair<String, String>> result = dialog.showAndWait();
        
        if (result.isPresent()) {
            String emailBaru = result.get().getKey();
            String statusBaru = result.get().getValue();
            updateProfileLengkap(emailBaru, statusBaru);
        }
    }


    private void loadProfile() {
        try {
            int userId = UserSession.requireUserId();
            

            if (!profileService.profileSudahAda(userId)) {

                profileService.buatProfileBaru(userId, "Nama Pengguna", UserSession.getCurrentUserEmail(), "Lahan Pribadi");
            }
            
            currentProfile = profileService.getProfileByUserId(userId);
            
            if (currentProfile != null) {
                lblNamaPengguna.setText(currentProfile.getNamaPengguna());
                lblEmailKontak.setText(currentProfile.getEmailKontak());
                lblStatusLahan.setText(currentProfile.getStatusLahan());
            } else {
                showAlert("Error", "Gagal memuat profil pengguna", Alert.AlertType.ERROR);
            }
        } catch (IllegalStateException e) {
            showAlert("Error", "Anda harus login terlebih dahulu", Alert.AlertType.ERROR);
        }
    }


    private void updateNamaPengguna(String namaBaru) {
        try {
            int userId = UserSession.requireUserId();
            
            if (profileService.updateNamaPengguna(userId, namaBaru)) {
                lblNamaPengguna.setText(namaBaru);
                showAlert("Sukses", "Nama pengguna berhasil diperbarui", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Gagal memperbarui nama pengguna", Alert.AlertType.ERROR);
            }
        } catch (IllegalStateException e) {
            showAlert("Error", "Anda harus login terlebih dahulu", Alert.AlertType.ERROR);
        }
    }


    private void updateProfileLengkap(String emailBaru, String statusBaru) {
        try {
            int userId = UserSession.requireUserId();
            
            if (profileService.updateProfileLengkap(userId, 
                    currentProfile.getNamaPengguna(), 
                    emailBaru, 
                    statusBaru)) {
                lblEmailKontak.setText(emailBaru);
                lblStatusLahan.setText(statusBaru);
                showAlert("Sukses", "Profil berhasil diperbarui", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Error", "Gagal memperbarui profil", Alert.AlertType.ERROR);
            }
        } catch (IllegalStateException e) {
            showAlert("Error", "Anda harus login terlebih dahulu", Alert.AlertType.ERROR);
        }
    }


    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void addDivider(VBox container) {
        javafx.scene.shape.Line divider = new javafx.scene.shape.Line();
        divider.setStrokeWidth(1);
        divider.setStroke(Color.web("#e0e0e0"));
        divider.setEndX(450);
        container.getChildren().add(divider);
    }
}
