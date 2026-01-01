package com.dthvinh.Server.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class MetricService {
    private final HttpClient httpClient;
    private final String opentsdbUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public MetricService(String opentsdbUrl) {
        this.opentsdbUrl = Objects.requireNonNull(opentsdbUrl, "OpenTSDB URL cannot be null");
        this.httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public CompletableFuture<Boolean> sendMetric(
            String metric,
            long timestamp,
            Number value,
            Map<String, String> tags) {

        MetricPoint point = new MetricPoint(metric, timestamp, value, tags);
        return sendBatch(List.of(point));
    }

    public CompletableFuture<Boolean> sendBatch(List<MetricPoint> points) {
        if (points == null || points.isEmpty()) {
            return CompletableFuture.completedFuture(true);
        }

        try {
            String jsonPayload = objectMapper.writeValueAsString(points);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://" + opentsdbUrl + "/api/put"))
                    .timeout(Duration.ofSeconds(10))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                    .build();

            return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(response -> {
                        return response.statusCode() == 204;
                    })
                    .exceptionally(throwable -> {
                        return false;
                    });

        } catch (Exception e) {
            return CompletableFuture.completedFuture(false);
        }
    }

    public void sendMetricAsync(String metric, long timestamp, Number value, Map<String, String> tags) {
        sendMetric(metric, timestamp, value, tags);
    }

    public record MetricPoint(String metric, long timestamp, Number value, Map<String, String> tags) {
        public MetricPoint(String metric, long timestamp, Number value, Map<String, String> tags) {
            this.metric = Objects.requireNonNull(metric, "metric is required");
            this.timestamp = timestamp;
            this.value = Objects.requireNonNull(value, "value is required");
            this.tags = Objects.requireNonNullElse(tags, Map.of());
        }
    }
}
