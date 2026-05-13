package com.doarmais.model.bo;

import com.doarmais.model.infra.exception.DoarmaisException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class NavigationBO {

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    public static void navegar(String fxml, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(NavigationBO.class.getResource("/view/" + fxml));
            Parent root = loader.load();
            
            if (primaryStage == null) {
                throw new DoarmaisException("Stage principal não configurado no NavigationBO.");
            }

            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle(titulo);
            primaryStage.show();

        } catch (IOException e) {
            throw new DoarmaisException("Erro ao carregar a tela: " + fxml, e);
        }
    }
}
