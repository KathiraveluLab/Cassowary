package org.cassowary.common.model;

import java.util.Map;
import java.util.HashMap;

public class Profile {
    private String id;
    private String name;
    private Map<String, Double> preferences = new HashMap<>();

    public Profile() {}

    public Profile(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Map<String, Double> getPreferences() { return preferences; }
    public void setPreferences(Map<String, Double> preferences) { this.preferences = preferences; }

    public void addPreference(String key, Double value) {
        this.preferences.put(key, value);
    }
}
