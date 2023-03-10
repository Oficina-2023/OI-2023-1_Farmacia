package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.Cliente;
import br.com.apoo2021.farm.util.FarmDialogs;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ProgressIndicator;
import java.net.URL;
import java.util.ResourceBundle;

public class CustomerEditScreenController implements Initializable {

    @FXML
    private JFXTextField clienteNome;

    @FXML
    private JFXButton attButton;

    @FXML
    private JFXTextField clienteCpf;

    @FXML
    private JFXSpinner progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        attButton.disableProperty().bind(clienteNome.textProperty().isEmpty().or(clienteCpf.textProperty().isEmpty()));
        updateClienteData();
    }

    @FXML
    void AttPressed(ActionEvent event) {
        progressIndicator.setVisible(true);
        setLockedData(true);
        new Thread(()-> {
            boolean parseError = false;
            try {
                Cliente editableCliente = EasyFarma.dataManager.getEditableCustomer();
                editableCliente.setNome(clienteNome.getText());
                SQLRunner.ExecuteSQLScript.SQLSet("ClienteUpdate", editableCliente.getNome(), editableCliente.getCpf());
            }catch (Exception e) {
                EasyFarma.logger.error("Erro ao editar o cliente!", e);
                parseError = true;
            }
            boolean finalParseError = parseError;
            Platform.runLater(() -> {
                if (finalParseError) {
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(), "Erro", "Erro ao editar o cliente.\nTente novamente mais tarde!");
                } else {
                    FarmDialogs.showDialog(EasyFarma.dataManager.getMainPane(), "Sucesso", "O cliente foi editado com sucesso!");
                    updateClienteData();
                }
                progressIndicator.setVisible(false);
                setLockedData(false);
            });
        }).start();

    }

    private void updateClienteData(){
        Cliente editableCLiente = EasyFarma.dataManager.getEditableCustomer();
        clienteNome.setText(editableCLiente.getNome());
        clienteCpf.setText(editableCLiente.getCpf());
    }

    private void setLockedData(boolean isProcessing){
        clienteNome.setDisable(isProcessing);
        if(isProcessing){
            attButton.disableProperty().unbind();
            attButton.setDisable(true);
        }else{
            attButton.setDisable(false);
            attButton.disableProperty().bind(clienteNome.textProperty().isEmpty().or(clienteCpf.textProperty().isEmpty()));
        }
    }

}


