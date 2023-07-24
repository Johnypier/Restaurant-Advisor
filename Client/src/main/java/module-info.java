module restaurant.advisor.client {
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.annotation;
    requires restaurant.information;
    requires java.sql;
    requires spring.core;
    requires spring.web;
    requires spring.webflux;
    requires javafx.controls;
    requires javafx.web;
    requires transitive javafx.graphics;
    requires reactor.core;

    opens client.view to javafx.graphics, javafx.web;
    opens client.app to javafx.graphics;
}
