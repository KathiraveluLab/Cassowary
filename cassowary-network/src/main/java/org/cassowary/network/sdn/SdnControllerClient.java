package org.cassowary.network.sdn;

import org.cassowary.common.model.ContextData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Interface to the SDN Controller (OpenDaylight MD-SAL).
 * In a real deployment, this would use RESTCONF to update the data tree.
 */
public class SdnControllerClient {
    private static final Logger logger = LoggerFactory.getLogger(SdnControllerClient.class);
    private final String controllerUrl;

    public SdnControllerClient(String controllerUrl) {
        this.controllerUrl = controllerUrl;
    }

    public void updateDataTree(ContextData data) {
        // MAP the ContextData to MD-SAL Data Tree node
        // In prototype, we log the update. In production, this would be a PUT to /restconf/config/...
        logger.info("SDN DATA TREE UPDATE [{}] [{}]: {} = {}", 
            controllerUrl, data.getDeviceId(), data.getType(), data.getValue());
    }
}
