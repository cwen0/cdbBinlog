package binlog;

import com.alibaba.fastjson.JSON;
import com.qcloud.dts.message.DataMessage;
import com.qcloud.dts.message.DataMessage.Record;
import com.qcloud.dts.message.DataMessage.Record.Type;
import com.qcloud.dts.message.DataMessage.Record.Field;

import config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwen on 17-2-13.
 */
public class MessageHandler {
    private Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public void handle(Record record) {
       try {
           FieldData data = getColumnsAndValues(record);
           String jsonString = JSON.toJSONString(data);
           FileUtil.writeToFile(Config.binlogFile, jsonString);
       } catch (Exception e) {

       }
    }

    public FieldData getColumnsAndValues(DataMessage.Record record) {
       String dbName = record.getDbName();
       String tableName = record.getTableName();
       List<String> columns = new ArrayList<String>();
       List<Object> values = new ArrayList<Object>();
       List<String> primaryKey = new ArrayList<String>();
       List<Object> primaryValue = new ArrayList<Object>();

       int columnCount = 0;
       if (record.getOpt() == Type.UPDATE) {
           columnCount = record.getFieldCount() / 2;
       }else {
           columnCount = record.getFieldCount();
       }
       List<Field> fields = record.getFieldList();
       int i = 0;
       while (i < fields.size()) {
           Field f = null;
           if(record.getOpt() == Type.UPDATE) {
                f = fields.get(i + 1);
           }else {
                f = fields.get(i);
           }

           try {
                String key = f.getFieldname();
                Object value = f.getValue();
                columns.add(key);
                values.add(value);
                if(f.isPrimary()) {
                    primaryKey.add(key);
                    primaryValue.add(value);
                }
           } catch (Exception e) {
               e.printStackTrace();
           }

           if(record.getOpt() == Type.UPDATE) {
               i += 2;
           }else {
               i++;
           }
       }
       FieldData data = new FieldData(dbName, tableName,record.getOpt().toString(),columnCount,columns,values,
               primaryKey,primaryValue);
       return data;
    }
}
