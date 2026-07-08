package com.doarmais;

import com.doarmais.model.bo.NavigationBO;
import com.doarmais.util.AuditLogger;
import com.doarmais.util.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

public class DoarmaisApplication extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Logger.reset();
        AuditLogger.reset();

        NavigationBO.setPrimaryStage(primaryStage);
        primaryStage.setResizable(false);
        NavigationBO.navegar("login.fxml", "Login");
    }
}
