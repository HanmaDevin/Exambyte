package de.propra.exambyte.service;

import de.propra.exambyte.config.ConfigLoader;
import org.springframework.stereotype.Service;

@Service
public class RoleAssignmentService {
    private final ConfigLoader configLoader;

    public RoleAssignmentService(ConfigLoader configLoader) {
        this.configLoader = configLoader;
    }

    public String assignRole(String githubHandle) {
        return configLoader.getRole(githubHandle);
    }

}
