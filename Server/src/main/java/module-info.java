module restaurant.advisor.server {
    requires restaurant.information;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires org.json;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;
    requires spring.context;
    requires spring.beans;
    requires org.jsoup;

    opens server to spring.boot, spring.boot.autoconfigure, spring.web, spring.context, spring.beans, spring.core, spring.webflux;
    opens server.controller to spring.boot, spring.boot.autoconfigure, spring.web, spring.context, spring.beans, spring.core, spring.webflux;
    opens server.service to spring.boot, spring.boot.autoconfigure, spring.web, spring.context, spring.beans, spring.core, spring.webflux;
}
