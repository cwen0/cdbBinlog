package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

/**
 * Created by cwen on 17-2-15.
 */
public class FileUtil {

    public static void writeToFile(String filePath, String data){
        try {
            File file = new File(filePath);
            if(!file.exists()) {
                file.createNewFile();
            }

            FileWriter fileWriter = new FileWriter(file.getName(), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(data);
            bufferedWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
