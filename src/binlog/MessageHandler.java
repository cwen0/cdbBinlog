package binlog;

import com.qcloud.dts.message.DataMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwen on 17-2-13.
 */
public class MessageHandler {
    private Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public void handleInsert(DataMessage.Record record) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pst = null;
        try {
            conn.setAutoCommit(false);
        }
    }

    public FieldData getColumsAndValues(DataMessage.Record record) {
       String dbName = record.getDbName();
       String tableName = record.getTableName();
       List<String> colums = new ArrayList<String>();
       List<Object> values = new ArrayList<Object>();
       List<String> primaryKey = new ArrayList<String>();
       List<Object> primaryValue = new ArrayList<Object>();

       int columCount;
       if (record.getOpt() == DataMessage.Record.Type.UPDATE) {
           columCount = record.getFieldCount() / 2;
       }else {
           columCount = record.getFieldCount();
       }
       List<DataMessage.Record.Field> fields = record.getFieldList();
       int i = 0;
       while (i < fields.size()) {

       }
    }
}
