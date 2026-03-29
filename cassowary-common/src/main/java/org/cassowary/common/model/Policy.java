package org.cassowary.common.model;

import java.util.List;

public interface Policy {
    /**
     * Calculates the target value for a property (e.g., Temperature, Light) 
     * based on tenant profiles and their distances.
     * 
     * @param targetProperty The property to calculate (e.g., "temperature")
     * @param profiles List of tenant profiles in proximity
     * @param distances List of distances corresponding to the profiles
     * @return The calculated target value
     */
    Double calculate(String targetProperty, List<Profile> profiles, List<Double> distances);
}
