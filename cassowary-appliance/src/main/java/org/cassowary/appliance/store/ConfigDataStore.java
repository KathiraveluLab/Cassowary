package org.cassowary.appliance.store;

import org.cassowary.appliance.grid.InfinispanGrid;
import org.cassowary.common.model.Profile;
import org.infinispan.Cache;

public class ConfigDataStore {
    private final InfinispanGrid grid;

    public ConfigDataStore(InfinispanGrid grid) {
        this.grid = grid;
    }

    public void saveProfile(String buildingId, Profile profile) {
        Cache<String, Profile> profileCache = grid.getBuildingCache(buildingId, "profiles");
        profileCache.put(profile.getId(), profile);
    }

    public Profile getProfile(String buildingId, String id) {
        Cache<String, Profile> profileCache = grid.getBuildingCache(buildingId, "profiles");
        return profileCache.get(id);
    }
}
