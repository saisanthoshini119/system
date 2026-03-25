package com.example.system.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * Loads KEY=VALUE from .env in current or project directory and sets System properties.
 * No external dependency; works regardless of classpath.
 */
public final class EnvLoader {

    private EnvLoader() {}

    public static void loadEnv() {
        Path cwd = Paths.get("").toAbsolutePath();
        Path[] candidates = {
            cwd.resolve(".env"),
            cwd.resolve("system").resolve(".env")
        };
        for (Path envFile : candidates) {
            if (Files.isRegularFile(envFile)) {
                try {
                    List<String> lines = Files.readAllLines(envFile);
                    for (String line : lines) {
                        line = line.trim();
                        if (line.isEmpty() || line.startsWith("#")) continue;
                        int eq = line.indexOf('=');
                        if (eq <= 0) continue;
                        String key = line.substring(0, eq).trim();
                        String value = line.substring(eq + 1).trim();
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        } else if (value.startsWith("'") && value.endsWith("'")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        if (!key.isEmpty() && System.getProperty(key) == null) {
                            System.setProperty(key, value);
                        }
                    }
                    break;
                } catch (IOException ignored) {
                    // try next candidate
                }
            }
        }
    }
}
