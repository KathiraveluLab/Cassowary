package org.cassowary.appliance.grid;

import org.infinispan.configuration.cache.ConfigurationBuilder;
import org.infinispan.configuration.global.GlobalConfigurationBuilder;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InfinispanGrid {
    private static final Logger logger = LoggerFactory.getLogger(InfinispanGrid.class);
    private EmbeddedCacheManager cacheManager;

    public InfinispanGrid() {
        initialize();
    }

    private void initialize() {
        logger.info("Initializing Cassowary Infinispan Grid...");
        GlobalConfigurationBuilder global = GlobalConfigurationBuilder.defaultClusteredBuilder();
        global.transport().clusterName("CassowaryCluster");
        
        cacheManager = new DefaultCacheManager(global.build());
        
        // Default configuration for caches
        ConfigurationBuilder builder = new ConfigurationBuilder();
        builder.clustering().cacheMode(org.infinispan.configuration.cache.CacheMode.DIST_SYNC);
        
        cacheManager.defineConfiguration("profiles", builder.build());
        cacheManager.defineConfiguration("policies", builder.build());
    }

    public <K, V> org.infinispan.Cache<K, V> getCache(String name) {
        return cacheManager.getCache(name);
    }

    public <K, V> org.infinispan.Cache<K, V> getBuildingCache(String buildingId, String cacheType) {
        String cacheName = buildingId + "_" + cacheType;
        if (!cacheManager.cacheExists(cacheName)) {
            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.clustering().cacheMode(org.infinispan.configuration.cache.CacheMode.DIST_SYNC);
            cacheManager.defineConfiguration(cacheName, builder.build());
        }
        return cacheManager.getCache(cacheName);
    }

    public void stop() {
        if (cacheManager != null) {
            cacheManager.stop();
        }
    }
}
