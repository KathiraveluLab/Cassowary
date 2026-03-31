package org.cassowary.common.policy;

import org.cassowary.common.model.Policy;
import org.cassowary.common.model.Profile;
import java.util.List;
import java.util.Map;

public class CassowaryPolicy implements Policy {

    @Override
    public Double calculate(String property, List<Profile> profiles, List<Double> distances, Map<String, Double> context) {
        if ("temperature".equalsIgnoreCase(property)) {
            return calculateTemperature(profiles, distances);
        } else if ("light".equalsIgnoreCase(property)) {
            Double Ls = context != null ? context.getOrDefault("Ls", 50.0) : 50.0;
            return calculateIllumination(distances, Ls);
        } else if ("display".equalsIgnoreCase(property)) {
            return calculateDisplayQoS(distances);
        }
        return null;
    }

    /**
     * Equation 1: T = Sum(Ti / xi) / Sum(1 / xi)
     * (HVAC - Tenant Comfort + Energy Efficiency)
     */
    private Double calculateTemperature(List<Profile> profiles, List<Double> distances) {
        double numerator = 0;
        double denominator = 0;
        for (int i = 0; i < profiles.size(); i++) {
            Double Ti = profiles.get(i).getPreferences().getOrDefault("temperature", 22.0);
            Double xi = distances.get(i);
            if (xi > 0) {
                numerator += (Ti / xi);
                denominator += (1.0 / xi);
            }
        }
        return denominator > 0 ? (numerator / denominator) : 22.0;
    }

    /**
     * Equation 3: L = k * (1 / (dmin * Ls))
     * (Light Sources - Energy Efficiency + Tenant Comfort + Pollution Prevention)
     */
    private Double calculateIllumination(List<Double> distances, double Ls) {
        double dmin = distances.stream().min(Double::compare).orElse(Double.MAX_VALUE);
        if (dmin > 10.0) return 0.0; // Hibernation/Dimming for Energy/Pollution
        double k = 5000.0; // system constant
        return k / (dmin * Ls > 0 ? dmin * Ls : 1.0);
    }

    /**
     * Equation 2: L = F(dmin, Ls) logic for Displays
     * (TV/Displays - Energy Efficiency + Pollution Prevention)
     */
    private Double calculateDisplayQoS(List<Double> distances) {
        double dmin = distances.stream().min(Double::compare).orElse(Double.MAX_VALUE);
        if (dmin > 5.0) return 0.0; // HIBERNATE to avoid pollution and save energy
        return 100.0; // Active brightness
    }
}
