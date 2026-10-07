module com.example.hellofx {
    requires javafx.controls;
    exports org.example;
    opens org.example to javafx.base;
}
