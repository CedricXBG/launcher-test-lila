package fr.cedricxbg.lilalauncher.ui.panel;

import fr.cedricxbg.lilalauncher.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init(PanelManager panelManager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStylesheetPath();
}
