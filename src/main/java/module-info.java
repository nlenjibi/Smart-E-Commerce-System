module com.ecommerce.smartecommercesystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires io.github.cdimascio.dotenv.java;
    requires org.slf4j;


    // Open packages to JavaFX for reflection
    opens com.smartecommerce.app to javafx.fxml;
    opens com.smartecommerce.controllers to javafx.fxml;
    opens com.smartecommerce.models to javafx.base;

    // Export packages
    exports com.smartecommerce.app;
    exports com.smartecommerce.controllers;
    exports com.smartecommerce.models;
    exports com.smartecommerce.service;
    exports com.smartecommerce.dao;
}
