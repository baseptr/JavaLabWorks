package com.esdc.lab1.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpClientWrapper {
    private final HttpClient cl = HttpClient.newHttpClient();

    public HttpResponse<String> get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        return cl.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> post(String url, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return cl.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> put(String url, String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return cl.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public HttpResponse<String> delete(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();
        return cl.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
