module com.ecommerce.smartecommercesystem {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;

    opens com.ecommerce.smartecommercesystem to javafx.fxml;
    exports com.ecommerce.smartecommercesystem;
}