package fr.cedricxbg.lilalauncher.ui.panels.pages;

import fr.cedricxbg.lilalauncher.Launcher;
import fr.cedricxbg.lilalauncher.ui.PanelManager;
import fr.cedricxbg.lilalauncher.ui.panel.Panel;
import fr.litarvan.openauth.microsoft.MicrosoftAuthenticator;
import fr.litarvan.openauth.model.AuthProfile;
import fr.theshark34.openlauncherlib.minecraft.AuthInfos;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

public class Login extends Panel {
    GridPane loginCard = new GridPane();

    Saver saver = Launcher.getInstance().getSaver();

    TextField userField = new TextField();
    Button btnLogin = new Button("Connexion");
    Button msLoginBtn = new Button();

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getStylesheetPath() {
        return "css/login.css";
    }

    @Override
    public void init(PanelManager panelManager) {
        super.init(panelManager);

        // Background
        this.layout.getStyleClass().add("login-layout");

        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.LEFT);
        columnConstraints.setMinWidth(350);
        columnConstraints.setMaxWidth(350);
        this.layout.getColumnConstraints().addAll(columnConstraints, new ColumnConstraints());
        this.layout.add(loginCard, 0, 0);

        // Background image
        GridPane bgImage = new GridPane();
        setCanTakeAllSize(bgImage);
        bgImage.getStyleClass().add("bg-image");
        this.layout.add(bgImage, 1, 0);

        // Login card
        setCanTakeAllSize(this.layout);
        loginCard.getStyleClass().add("login-card");
        setLeft(loginCard);
        setCenterH(loginCard);
        setCenterV(loginCard);

        /**
         * Login sidebar
         */
        Label title = new Label("Lilasoutif Launcher");
        title.setFont(Font.font("Consolas", FontWeight.BOLD, FontPosture.REGULAR, 30f));
        title.getStyleClass().add("login-title");
        setCenterH(title);
        setCanTakeAllSize(this.layout);
        setTop(title);
        title.setTextAlignment(TextAlignment.CENTER);
        title.setTranslateY(30d);
        loginCard.getChildren().add(title);

        // Username
        setCanTakeAllSize(userField);
        setCenterV(userField);
        setCenterH(userField);
        userField.setPromptText("Pseudo");
        userField.setMaxWidth(300);
        userField.setTranslateY(-15d);
        userField.getStyleClass().add("login-input");
        userField.textProperty().addListener((_a, oldValue, newValue) -> {
            this.updateLoginBtnState(userField);
        });

        // Login button
        setCanTakeAllWidth(btnLogin);
        setCenterV(btnLogin);
        setCenterH(btnLogin);
        btnLogin.setDisable(true);
        btnLogin.setMaxWidth(300);
        btnLogin.setTranslateY(40d);
        btnLogin.getStyleClass().add("login-log-btn");
        btnLogin.setOnMouseClicked(e -> {
            this.authenticate(userField.getText());
        });

        Separator separator = new Separator();
        setCanTakeAllSize(separator);
        setCenterH(separator);
        setCenterV(separator);
        separator.getStyleClass().add("login-separator");
        separator.setMaxWidth(300);
        separator.setTranslateY(90d);

        // "Login with" label
        Label loginWithLabel = new Label("Ou se connecter avec".toUpperCase());
        setCanTakeAllSize(loginWithLabel);
        setCenterH(loginWithLabel);
        setCenterV(loginWithLabel);
        loginWithLabel.setFont(Font.font(loginWithLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 14f));
        loginWithLabel.getStyleClass().add("login-with-label");
        loginWithLabel.setTranslateY(110d);
        loginWithLabel.setMaxWidth(280d);

        // Microsoft login button
        ImageView view = new ImageView(new Image("images/microsoft.png"));
        view.setPreserveRatio(true);
        view.setFitHeight(30d);
        setCanTakeAllSize(msLoginBtn);
        setCenterH(msLoginBtn);
        setCenterV(msLoginBtn);
        msLoginBtn.getStyleClass().add("ms-login-btn");
        msLoginBtn.setMaxWidth(300);
        msLoginBtn.setTranslateY(145d);
        msLoginBtn.setGraphic(view);
        msLoginBtn.setOnMouseClicked(e -> this.authenticateMS());

        loginCard.getChildren().addAll(userField, btnLogin, separator, loginWithLabel, msLoginBtn);
    }

    public void updateLoginBtnState(TextField textField) {
        btnLogin.setDisable(userField.getText().isEmpty());
    }

    public void authenticate(String user) {
        AuthInfos infos = new AuthInfos(userField.getText(), null, null);
        saver.set("offline-username", infos.getUsername());
        saver.save();
        Launcher.getInstance().setAuthInfos(infos);

        this.logger.info("Connected as " + infos.getUsername());

        panelManager.showPanel(new App());
    }

    public void authenticateMS() {
        MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
        authenticator.loginWithAsyncWebview().whenComplete((response, error) -> {
            if (error != null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erreur");
                alert.setContentText(error.getMessage());
                alert.show();
                return;
            }

            saver.set("msAccessToken", response.getAccessToken());
            saver.set("msRefreshToken", response.getRefreshToken());
            saver.save();

            Launcher.getInstance().setAuthInfos(new AuthInfos(
                    response.getProfile().getName(),
                    response.getAccessToken(),
                    response.getProfile().getId()
            ));
            this.logger.info("Connected as " + response.getProfile().getName());

            Platform.runLater(() -> {
                panelManager.showPanel(new App());
            });
        });
    }
}
