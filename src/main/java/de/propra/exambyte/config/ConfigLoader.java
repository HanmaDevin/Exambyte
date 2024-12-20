package de.propra.exambyte.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Component
public class ConfigLoader {

    @Value("${config.roles}")
    private String configRoles;
    private Map<String, Object> configData;

    public Map<String, Object> getConfigData() {
        if (configData == null) {
            loadConfig();
        }
        return configData;
    }

    void loadConfig() {
        try {
            // Load config from file
            String content = Files.readString(Paths.get(configRoles));
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            configData = mapper.readValue(content, new TypeReference<>() {
            });
        } catch (Exception e) {
            // Handle exception
            throw new RuntimeException("Error loading config file", e);
        }
    }

    public String getRole(String username) {
        configData = getConfigData();
        List<Map<String, String>> users = (List<Map<String, String>>) configData.get("users");

        for (Map<String, String> user : users) {
            if (user.get("username").equals(username)) {
                return user.get("role");
            }
        }
        return "STUDENT";
    }


}
