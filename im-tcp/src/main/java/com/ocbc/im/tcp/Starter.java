package com.ocbc.im.tcp;

import com.ocbc.im.cdec.config.BootstrapConfig;
import com.ocbc.im.tcp.server.LimServer;
import com.ocbc.im.tcp.utils.TcpUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;

public class Starter {

    public static void main(String[] args) {
        start();
    }



    private static void start() {
        try {
            new LimServer(TcpUtil.getBootstrapConfig().getLim()).start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
