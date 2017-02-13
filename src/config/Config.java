package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by cwen on 17-2-13.
 */
public class Config {
    private static String configFile = "config.properties";
    public static String  secretID;
    public static String secretKey;
    public static String cdbIP;
    public static int cdbPort;
    public static String guid;
    public static String driverName;
    public static String url = null;
    public static String userName = null;
    public static String password = null;
    public static int initialSize = 5;
    public static int maxSize = 128;
    public static int maxIdle =2;



    private static void readConfig() {
        InputStream is = Config.class.getClassLoader().getResourceAsStream(configFile);
        Properties pro = new Properties();
        try {
            pro.load(is);
            secretID = pro.getProperty("secretID");
            secretKey = pro.getProperty("secretKey");
            cdbIP = pro.getProperty("cdbIP");
            String  cdbPortStr = pro.getProperty("cdbPort");
            cdbPort = Integer.parseInt(cdbPortStr);
            guid = pro.getProperty("guid");
            driverName = pro.getProperty("driverName");
            url = pro.getProperty("url");
            userName = pro.getProperty("userName");
            password = pro.getProperty("password");
            initialSize = Integer.parseInt(pro.getProperty("initialSize"));
            maxSize = Integer.parseInt(pro.getProperty("maxSize"));
            maxIdle = Integer.parseInt(pro.getProperty("maxIdle"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
