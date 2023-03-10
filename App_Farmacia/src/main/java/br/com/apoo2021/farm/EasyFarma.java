package br.com.apoo2021.farm;

import br.com.apoo2021.farm.managers.DataManager;
import br.com.apoo2021.farm.util.ScreenAdjusts;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Objects;

public class EasyFarma extends Application {

    public static final Logger logger = LogManager.getLogger(EasyFarma.class.getName());

    public static final DataManager dataManager = new DataManager();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/LoginScreen.fxml")));
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("images/icon.png"))));
        primaryStage.setTitle("EasyFarma - Facilitando sua vida!");
        primaryStage.setScene(new Scene(root));
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.show();
        ScreenAdjusts.centerScreen(primaryStage);
        ScreenAdjusts.setDraggable(root, primaryStage);
    }
}
