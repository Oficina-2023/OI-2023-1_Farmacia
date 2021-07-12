package br.com.apoo2021.farm.screens;

import br.com.apoo2021.farm.FarmApp;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.util.MD5Cripto;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javax.print.attribute.standard.JobOriginatingUserName;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class LoginScreenController implements Initializable {

    @FXML
    private JFXTextField usernameTextField;

    @FXML
    private JFXPasswordField passwordTextField;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton signUpButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    void loginPressed(ActionEvent event) {
        String hash = MD5Cripto.MD5Converter(usernameTextField.getText()+passwordTextField.getText());
        if (hash != null) {
            List<Object> userData = SQLRunner.executeSQLScript.SQLSelect("login", hash);
            if(userData != null) {
                if (!userData.isEmpty()) {
                    FarmApp.userManager.setFarmData((int)userData.get(0),(String)userData.get(1));
                    System.out.println(FarmApp.userManager.getFarmaceutico().getCrf());
                    System.out.println(FarmApp.userManager.getFarmaceutico().getNome());
                } else {
                    System.out.println("?");
                }
            }
        }
    }

    @FXML
    void signUpPressed(ActionEvent event) {

    }

}
