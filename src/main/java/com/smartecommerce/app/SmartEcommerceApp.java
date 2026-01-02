package com.smartecommerce.app;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.image.Image;

import javafx.stage.Stage;

import java.util.Objects;

import static com.smartecommerce.utils.AppUtils.printE;
import static com.smartecommerce.utils.AppUtils.println;


/**
 * SmartEcommerceApp - Main JavaFX Application
 * Entry point for the Smart E-Commerce System
 */
public class SmartEcommerceApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        // Show landing (public) page first
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/smartecommerce/ui/views/landing.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Make the scene background black
            root.setStyle("-fx-background-color: black;");

            primaryStage.setTitle("Smart E-Commerce System - Home");
            primaryStage.setScene(scene);
            // Landing page is responsive; allow resizing
            primaryStage.setResizable(true);

            // Add application icon (logo) with error handling
            try {
                Image icon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/logo.jpg")));
                if (!icon.isError()) {
                    primaryStage.getIcons().add(icon);
                } else {
                    printE("Warning: Logo image could not be loaded");
                }
            } catch (Exception iconEx) {
                printE("Warning: Could not load application icon: " + iconEx.getMessage());
                // Continue without icon - not a critical error
            }

            primaryStage.show();
        } catch (Exception e) {
            printE("Error loading landing page: " + e.getMessage());
            e.printStackTrace();

            // Fallback to old login screen if landing page fails
            loadFallbackLogin(primaryStage);
        }
    }

    /**
     * Fallback helper to load login page if landing page fails to load.
     */
    private void loadFallbackLogin(Stage primaryStage) {
        try {
            FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/smartecommerce/ui/views/login.fxml")
            );
            Parent root = loader.load();
            Scene scene = new Scene(root);
            primaryStage.setTitle("Smart E-Commerce System - Login");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (Exception ex) {
            printE("Critical error loading login fallback: " + ex.getMessage());
            ex.printStackTrace();
            // As a last resort, load main application layout
        }
    }




    @Override
    public void stop() {
        println("Application closed");
    }

    public static void main(String[] args) {
        launch(args);
    }
}

