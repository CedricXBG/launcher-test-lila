package fr.cedricxbg.lilalauncher;

import fr.cedricxbg.lilalauncher.ui.PanelManager;
import fr.cedricxbg.lilalauncher.ui.panels.pages.Login;
import fr.cedricxbg.lilalauncher.utils.Helpers;
import fr.flowarg.flowlogger.ILogger;
import fr.flowarg.flowlogger.Logger;
import fr.theshark34.openlauncherlib.util.Saver;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.File;

public class Launcher extends Application {
    private PanelManager panelManager;
    private static Launcher instance;
    private final ILogger logger;
    private final File launcherDir = Helpers.generateGamePath("LilasoutifLauncher");
    private final Saver saver;

    public Launcher() {
        instance = this;
        this.logger = new Logger("[LilasoutifLauncher]", new File(this.launcherDir, "launcher.log"));
        if (!this.launcherDir.exists()) {
            if (!this.launcherDir.mkdir()) {
                this.logger.err("Unable to create launcher directory");
            }
        }

        saver = new Saver(new File(launcherDir, "config.properites"));
        saver.load();
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.logger.info("Starting launcher");
        this.panelManager = new PanelManager(this, stage);
        this.panelManager.init();

        this.panelManager.showPanel(new Login());
    }

    public ILogger getLogger() {
        return logger;
    }

    public static Launcher getInstance() {
        return instance;
    }

    public Saver getSaver() {
        return saver;
    }
}
