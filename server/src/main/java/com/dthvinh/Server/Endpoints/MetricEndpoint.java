package com.dthvinh.Server.Endpoints;

import com.dthvinh.Server.DTOs.MetricDto;
import com.dthvinh.Server.Endpoints.Base.BaseEndpoint;
import com.dthvinh.Server.Lib.SummerBoot.Anotations.Endpoint;
import com.dthvinh.Server.Service.MetricService;
import com.sun.net.httpserver.HttpExchange;
import lombok.AllArgsConstructor;

import java.time.Instant;

@Endpoint(route = "metrics")
@AllArgsConstructor
public class MetricEndpoint extends BaseEndpoint {
    private final MetricService metricService;

    @Override
    public void handle(HttpExchange exchange) {
        try {
            if (isPost(exchange))
                handlePostMetric(exchange);
            else
                throw new Exception("");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void handlePostMetric(HttpExchange ex) throws Exception {
        MetricDto dto = parseBody(ex, MetricDto.class);

        metricService.sendMetric(dto.metric, Instant.now().getEpochSecond(), dto.value, dto.tags);
    }
}
