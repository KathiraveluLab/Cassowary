package org.cassowary.network.sdn;

import org.cassowary.common.model.ContextData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Interface to the SDN Controller (OpenDaylight MD-SAL).
 * Uses RESTCONF to update the data tree.
 */
public class SdnControllerClient {
    private static final Logger logger = LoggerFactory.getLogger(SdnControllerClient.class);
    private final String controllerUrl;
    private final HttpClient httpClient;

    public SdnControllerClient(String controllerUrl) {
        this.controllerUrl = controllerUrl;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    public void updateDataTree(ContextData data) {
        // MAP the ContextData to MD-SAL Data Tree node
        // Production: PUT to /restconf/config/
        String url = controllerUrl + "/restconf/config/cassowary:context-data/" 
            + data.getDeviceId() + "/" + data.getType();
        
        String jsonPayload = String.format(
            "{\"context-data\": {\"device-id\": \"%s\", \"type\": \"%s\", \"value\": %f}}",
            data.getDeviceId(), data.getType(), data.getValue()
        );

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        logger.debug("Sending RESTCONF PUT to {}: {}", url, jsonPayload);

        httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::statusCode)
                .thenAccept(status -> {
                    if (status >= 200 && status < 300) {
                        logger.info("SDN DATA TREE UPDATE SUCCESS [{}]", status);
                    } else {
                        logger.warn("SDN DATA TREE UPDATE FAILED [{}]", status);
                    }
                })
                .exceptionally(e -> {
                    logger.error("Error updating SDN data tree", e);
                    return null;
                });
    }
}
