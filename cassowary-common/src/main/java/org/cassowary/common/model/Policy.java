package org.cassowary.common.model;

import java.util.List;
import java.util.Map;

public interface Policy {
    /**
     * Calculates the target value for a property (e.g., Temperature, Light) 
     * based on tenant profiles, their distances, and optional context.
     * 
     * @param targetProperty The property to calculate (e.g., "temperature")
     * @param profiles List of tenant profiles in proximity
     * @param distances List of distances corresponding to the profiles
     * @param context Additional context like natural light level (Ls)
     * @return The calculated target value
     */
    Double calculate(String targetProperty, List<Profile> profiles, List<Double> distances, Map<String, Double> context);
}
