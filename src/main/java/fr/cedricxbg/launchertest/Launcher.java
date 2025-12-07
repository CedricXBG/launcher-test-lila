package fr.cedricxbg.launchertest;

import fr.cedricxbg.launchertest.ui.PanelManager;
import fr.cedricxbg.launchertest.utils.Helpers;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Launcher extends Application {
    private PanelManager panelManager;
    private static Launcher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath("LilasoutifLauncher");

    public Launcher() {
        instance = this;
        this.logger = new Logger("[LilasoutifLauncher]", new File(this.launcherDir, "launcher.log"));
        if (!this.launcherDir.exists()) {
            if (!this.launcherDir.mkdir()) {
                this.logger.err("Unable to create launcher directory");
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.logger.info("Starting launcher");
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();
    }

    public ILogger getLogger() {
        return logger;
    }

    public static Launcher getInstance() {
        return instance;
    }
}
