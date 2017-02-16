package util;

import binlog.FieldData;
import config.Config;
import protocol.BinlogProto.Binlog;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by cwen on 17-2-15.
 */
public class FileUtil {
    public static final String binglogFilePre = "binlog-";
    public static String curentBinlogFile;
    public static String postion = curentBinlogFile+"-0";

    static {
       initCurrentBinlogFile();
    }

    public static void writeToFile(Binlog.Builder binlog){
        try {
            File file = new File(Config.binlogPath + "/" + curentBinlogFile);
            if(!file.exists()) {
                file.createNewFile();
            }
            if(file.length()+binlog.toString().length() >= Config.maxSize) {
                String fileName = file.getName();
                String numStr = fileName.replaceFirst(binglogFilePre, "");
                int nextNum = Integer.parseInt(numStr) + 1;
                String newFileName = binglogFilePre+String.format("%08d", nextNum);
                file = new File(Config.binlogPath + "/" + newFileName);
                if(!file.exists()) {
                    file.createNewFile();
                }
                curentBinlogFile = newFileName;
                FileOutputStream output = new FileOutputStream(file);
                try {
                    binlog.build().writeTo(output);
                } finally {
                    postion = curentBinlogFile+file.length();
                    output.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getPostion() {
        return postion;
    }

    private static void initCurrentBinlogFile() {
        File file = new File(Config.binlogPath);
        File[] array = file.listFiles();
        int currentNum = 1;
        if (array != null) {
            for(int i = 0; i < array.length; i++) {
                if(array[i].isFile()) {
                    String fileName = array[i].getName();
                    String numStr = fileName.replaceFirst(binglogFilePre, "");
                    int tmpNum = Integer.parseInt(numStr);
                    if (tmpNum > currentNum) {
                        currentNum = tmpNum;
                    }
                }
            }
        }
       curentBinlogFile = binglogFilePre+String.format("%08d", currentNum);
    }
}
