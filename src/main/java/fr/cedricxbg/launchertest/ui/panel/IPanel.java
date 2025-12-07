package fr.cedricxbg.launchertest.ui.panel;

import fr.cedricxbg.launchertest.ui.PanelManager;
import javafx.scene.layout.GridPane;

public interface IPanel {
    void init(PanelManager panelManager);
    GridPane getLayout();
    void onShow();
    String getName();
    String getStyleSheetPath();
}
