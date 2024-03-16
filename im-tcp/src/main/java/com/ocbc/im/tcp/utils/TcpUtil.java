package com.ocbc.im.tcp.utils;

import com.ocbc.im.cdec.config.BootstrapConfig;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class TcpUtil {

    private final static String CONFIG_PATH = "/config";



    public static BootstrapConfig getBootstrapConfig() {
        Yaml yaml = new Yaml();
        InputStream inputStream = TcpUtil.class.getResourceAsStream(CONFIG_PATH + File.separator + "config.yml");
        return yaml.loadAs(inputStream, BootstrapConfig.class);
    }

}
