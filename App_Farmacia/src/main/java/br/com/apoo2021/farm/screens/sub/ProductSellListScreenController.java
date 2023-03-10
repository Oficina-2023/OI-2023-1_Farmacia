package br.com.apoo2021.farm.screens.sub;

import br.com.apoo2021.farm.EasyFarma;
import br.com.apoo2021.farm.database.SQLRunner;
import br.com.apoo2021.farm.objects.ProductCart;
import br.com.apoo2021.farm.objects.Produto;
import br.com.apoo2021.farm.objects.Vendas;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ProductSellListScreenController implements Initializable {

    private static class ProductSellCell extends ListCell<ProductCart>{
        HBox box = new HBox();
        Label lote = new Label();
        Label nome = new Label();
        Label quantity = new Label();
        Pane pane = new Pane();

        public ProductSellCell() {
            super();
            setColors();
            setSizes();
            box.getChildren().addAll(lote,nome,quantity,pane);
            HBox.setHgrow(pane, Priority.ALWAYS);
        }

        @Override
        protected void updateItem(ProductCart item, boolean empty) {
            super.updateItem(item, empty);
            setText(null);
            setGraphic(null);

            if(item != null && !empty){
                if(item.getProduto().getLote().length() > 30){
                    lote.setText(item.getProduto().getLote().substring(0,30) + "...");
                }else{
                    lote.setText(item.getProduto().getLote());
                }

                if(item.getProduto().getNome().length() > 50){
                    nome.setText(item.getProduto().getNome().substring(0,50)+"...");
                }else{
                    nome.setText(item.getProduto().getNome());
                }

                quantity.setText(String.valueOf(item.getQuantity()));

                setGraphic(box);
                setPrefHeight(80);
            }
        }

        private void setColors(){
            nome.setStyle("-fx-text-fill: white");
            lote.setStyle("-fx-text-fill: white");
            quantity.setStyle("-fx-text-fill: white");
        }

        private void setSizes(){
            nome.setPrefHeight(50);
            nome.setFont(new Font(16));
            nome.setTranslateX(50);
            lote.setPrefHeight(50);
            lote.setFont(new Font(16));
            lote.setTranslateX(0);
            quantity.setPrefHeight(50);
            quantity.setFont(new Font(16));
            quantity.setTranslateX(100);
        }
    }

    @FXML
    private JFXListView<ProductCart> productVendasListView;

    @FXML
    private JFXTextField nomeTextField;

    @FXML
    private JFXSpinner progressIndicator;

    private final List<ProductCart> produtoList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        progressIndicator.setVisible(true);
        new Thread(() -> {
            Vendas venda = EasyFarma.dataManager.getViewVenda();
            if(venda != null){
                List<Object> productsLotes = SQLRunner.ExecuteSQLScript.SQLSelect("GetProdutosSellByNF", venda.getNf());
                if(productsLotes != null && !productsLotes.isEmpty()){
                    for(Object lote : productsLotes){
                        Produto produto = EasyFarma.dataManager.getProductManager().getProdutoByLote((String) lote);
                        if(produto != null){
                            List<Object> quantityProduct = SQLRunner.ExecuteSQLScript.SQLSelect("GetQuantitySell", venda.getNf(), (String) lote);

                            if(quantityProduct != null && !quantityProduct.isEmpty()){
                                ProductCart productCart = new ProductCart(produto, (int) quantityProduct.get(0));
                                produtoList.add(productCart);
                            }
                        }
                    }
                }
            }
            Platform.runLater(() -> {
                updateList();
                progressIndicator.setVisible(false);
            });
        }).start();
    }

    @FXML
    void searchChanged(KeyEvent event) {
        updateList();
    }

    private void updateList(){
        if(nomeTextField.getText().isEmpty()){
            productVendasListView.setItems(FXCollections.observableList(produtoList));
        }else{
            List<ProductCart> filtredList = new ArrayList<>();

            for(ProductCart productCart : produtoList){
                if(productCart.getProduto().getNome().toLowerCase().contains(nomeTextField.getText().toLowerCase())){
                    filtredList.add(productCart);
                }
            }

            productVendasListView.setItems(FXCollections.observableList(filtredList));
        }
        productVendasListView.setCellFactory(call -> new ProductSellCell());
    }
}
