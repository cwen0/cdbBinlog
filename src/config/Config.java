package config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by cwen on 17-2-13.
 */
public class Config {
    private static String configFile = "config/config.properties";
    public static String  secretID;
    public static String secretKey;
    public static String cdbIP;
    public static int cdbPort;
    public static String guid;
    public static String binlogPath;
    public static String binlogFilePre;
    public static int maxSize;

    static {
        readConfig();
    }

    private static void readConfig() {
        InputStream input = null;
        Properties pro = new Properties();
        try {
            input = new FileInputStream(configFile);
            pro.load(input);
            secretID = pro.getProperty("secretID");
            secretKey = pro.getProperty("secretKey");
            cdbIP = pro.getProperty("cdbIP");
            String  cdbPortStr = pro.getProperty("cdbPort");
            cdbPort = Integer.parseInt(cdbPortStr);
            guid = pro.getProperty("guid");
            binlogPath = pro.getProperty("binlogPath");
            binlogFilePre = pro.getProperty("binlogFilePre");
            String maxSizeStr = pro.getProperty("maxSize");
            maxSize = Integer.parseInt(maxSizeStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
