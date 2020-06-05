package me.ifling;

import me.ifling.bean.ConfigAdapter;
import me.ifling.bean.ConfigDB;
import me.ifling.bean.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

public class Config {
    public ConfigDB db;
    public Map<String, ConfigAdapter> adapters;
    public Map<String, ConfigService> services;
    private static String resourcePath;
    static Logger logger = LoggerFactory.getLogger(Config.class);

    public void setDB(ConfigDB db) {
        this.db = db;
    }

    public String getResourcePath() {
        return resourcePath;
    }

    private static class ConfigHolder {
        private static final Config instance = parse();

        static Config parse() {
            String path = Config.class.getClassLoader().getResource("application.yml").getPath();
            resourcePath = new File(path).getParent();
            Yaml yaml = new Yaml();
            try (InputStream fr = Config.class.getClassLoader().getResource("application.yml").openStream()) {
                Config config = yaml.loadAs(fr, Config.class);
                return config;
            } catch (Exception e) {
                logger.error(Util.getStackTraceString(e));
                return null;
            }
        }
    }

    public static void main(String[] args) {
        String path = Config.class.getClassLoader().getResource("application.yml").getPath();
        Config.getInstance();
        System.out.println(path);
    }

    private Config() {
    }


    public static Config getInstance() {
        return ConfigHolder.instance;
    }


}
