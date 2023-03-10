package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.objects.Farmaceutico;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable {

    @FXML
    private JFXButton productButton;

    @FXML
    private JFXButton vendaButton;

    @FXML
    private JFXButton clienteButton;

    @FXML
    private JFXButton closeButton;

    @FXML
    private Text usernameField;

    @FXML
    private ImageView settingsButton;

    @FXML
    private ImageView logoutButton;

    @FXML
    private StackPane mainPane;

    @FXML
    private JFXButton cartButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateUsername();
        EasyFarma.dataManager.setMainPane(mainPane);
    }

    @FXML
    void clientesPressed(ActionEvent event) {
        openSubScreen("CustomerListScreen");
    }

    @FXML
    void logoutPressed(MouseEvent event) {
        FarmDialogs.showSoftwareLogoutDialog(mainPane, closeButton, closeButton, productButton, vendaButton, clienteButton, cartButton, settingsButton, logoutButton);
    }

    @FXML
    void produtosPressed(ActionEvent event) {
        openSubScreen("ProductListScreen");
    }

    @FXML
    void settingsPressed(MouseEvent event) {
        openSubScreen("UserEditScreen");
    }

    @FXML
    void vendaPressed(ActionEvent event) {
        openSubScreen("SellListScreen");
    }

    @FXML
    void cartPressed(ActionEvent event) {
        openSubScreen("SellScreen");
    }


    @FXML
    void closePressed(ActionEvent event) {
        FarmDialogs.showSoftwareCloseDialog(mainPane,closeButton, productButton, vendaButton, clienteButton, cartButton, settingsButton, logoutButton);
    }

    private void openSubScreen(@NotNull String screenName){
        try{
            mainPane.getChildren().clear();
            mainPane.getChildren().add(FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("screens/sub/" + screenName + ".fxml"))));
        }catch (IOException e){
            EasyFarma.logger.error("Erro ao abrir a janela " + screenName +"!", e);
        }
    }

    private void updateUsername(){
        Farmaceutico farmaceutico = EasyFarma.dataManager.getFarmManager().getFarmaceutico();
        if(farmaceutico.getNome().length() > 25){
            usernameField.setText(farmaceutico.getNome().substring(0,25) + "...");
        }else{
            usernameField.setText(farmaceutico.getNome());
        }
    }
}
