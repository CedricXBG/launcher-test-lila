package fr.cedricxbg.lilalauncher.ui;

import com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import fr.cedricxbg.lilalauncher.Launcher;
import fr.cedricxbg.lilalauncher.ui.panel.IPanel;
import fr.cedricxbg.lilalauncher.ui.panels.partials.TopBar;
import fr.flowarg.flowcompat.Platform;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Objects;

public class PanelManager {
    private final Launcher launcher;
    private final Stage stage;
    private GridPane layout;
    private final GridPane contentPane = new GridPane();

    public PanelManager(Launcher launcher, Stage stage) {
        this.launcher = launcher;
        this.stage = stage;
    }

    public void init() {
        this.stage.setTitle("Lilasoutif Launcher");
        this.stage.setMinWidth(854);
        this.stage.setMinHeight(480);
        this.stage.setWidth(1280);
        this.stage.setHeight(720);
        this.stage.centerOnScreen();
        this.stage.getIcons().add(new Image("images/icon.png"));

        this.layout = new GridPane();

        if (Platform.isOnLinux()) {
            Scene scene = new Scene(this.layout);
            this.stage.setScene(scene);
        } else {
            this.stage.initStyle(StageStyle.UNDECORATED);

            TopBar topBar = new TopBar();
            BorderlessScene scene = new BorderlessScene(this.stage, StageStyle.UNDECORATED, this.layout);
            scene.setResizable(true);
            scene.setMoveControl(topBar.getLayout());
            scene.removeDefaultCSS();

            this.stage.setScene(scene);

            RowConstraints topPaneConstraints = new RowConstraints();
            topPaneConstraints.setValignment(VPos.TOP);
            topPaneConstraints.setMinHeight(25);
            topPaneConstraints.setMaxHeight(25);
            this.layout.getRowConstraints().addAll(topPaneConstraints, new RowConstraints());
            this.layout.add(topBar.getLayout(), 0, 0);
            topBar.init(this);
        }

        this.layout.add(this.contentPane, 0, 1);
        GridPane.setVgrow(this.contentPane, Priority.ALWAYS);
        GridPane.setHgrow(this.contentPane, Priority.ALWAYS);

        contentPane.getStyleClass().add("login-layout");

        this.stage.show();
    }

    public void showPanel(IPanel panel) {
        this.contentPane.getChildren().clear();
        this.contentPane.getChildren().add(panel.getLayout());

        if (panel.getStylesheetPath() != null) {
            this.stage.getScene().getStylesheets().clear();

            String css = Objects.requireNonNull(
                    getClass().getClassLoader().getResource(panel.getStylesheetPath()),
                    "CSS non trouvée : " + panel.getStylesheetPath()
            ).toExternalForm();

            this.stage.getScene().getStylesheets().add(css);
        }

        panel.init(this);
        panel.onShow();
    }



    public Stage getStage() {
        return stage;
    }

    public Launcher getLauncher() {
        return launcher;
    }
}
