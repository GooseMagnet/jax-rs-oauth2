package com.goosemagnet.oauth2;

import com.goosemagnet.oauth2.util.ApplicationConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;

import java.net.URI;

@Slf4j
public class Main {

    private static final String BASE_URI = "http://localhost:8080";

    public static HttpServer startServer() {
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), new ApplicationConfiguration());
    }

    public static void main(String[] args) throws InterruptedException {
        HttpServer httpServer = startServer();
        Runtime.getRuntime().addShutdownHook(new Thread(httpServer::shutdown));
        Thread.currentThread().join();
    }
}
