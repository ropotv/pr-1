package services;

import java.util.HashMap;
import java.util.Map;

public class ConfigService {
    private static final Map<String, String> _config = new HashMap<String, String>() {{
        put("serverUrl", "http://localhost:5000");
        put("serverRegisterURL", "http://localhost:5000/register");
        put("serverHomeURL", "http://localhost:5000/home");
    }};

    public static String Get(String key) {
        return ConfigService._config.get(key);
    }
}
