module ru.kpfu.itis.selyantsev.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;

    opens ru.kpfu.itis.selyantsev.controller to javafx.fxml;
    exports ru.kpfu.itis.selyantsev.app to javafx.graphics;
}