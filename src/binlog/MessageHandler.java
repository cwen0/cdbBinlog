package binlog;

import com.qcloud.dts.message.DataMessage;
import com.qcloud.dts.message.DataMessage.Record;
import com.qcloud.dts.message.DataMessage.Record.Type;
import com.qcloud.dts.message.DataMessage.Record.Field;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.JDBCUtil;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cwen on 17-2-13.
 */
public class MessageHandler {
    private Logger log = LoggerFactory.getLogger(MessageHandler.class);

    public void handleInsert(Record record) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pst = null;
        try {
            conn.setAutoCommit(false);
            FieldData data = getColumnsAndValues(record);
            String sql = "insert into" + data.getDbName()+"."+data.getTableName() + "(";
            for(int i = 0; i < data.getColumns().size(); i++) {
                sql += data.getColumns().get(i) + ",";
            }
            sql = sql.substring(0, sql.length()-1) + " ) values ( ";
            for(int i = 0; i < data.getColumns().size(); i++) {
                sql += "?,";
            }
            sql = sql.substring(0, sql.length()-1) + ")";
            pst = conn.prepareStatement(sql);
            log.info("insert sql: "+sql);
            for(int i = 0; i < data.getValues().size(); i++) {
                pst.setObject(i+1,data.getValues().get(i));
            }
            pst.execute();
            conn.commit();
        }catch (SQLException e) {
            log.error("insert error:" ,e);
            try {
                conn.rollback();
            } catch (SQLException ee){
               ee.printStackTrace();
            }
        }finally {
            JDBCUtil.closeConnection(null,pst,conn);
        }
    }

    public void handleUpdate(Record record) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pst = null;
        try {
            conn.setAutoCommit(false);
            FieldData data = getColumnsAndValues(record);

            String sql = "update " + data.getDbName()+"." + data.getTableName() + " set ";
            for (int i = 0; i < data.getColumnCount(); i++) {
                sql += data.getColumns().get(i) + " = ?,";
            }
            sql = sql.substring(0, sql.length() - 1) + "  where 1=1  ";
            for (int i = 0; i < data.getPrimaryKey().size(); i++) {
                sql += " and " + data.getPrimaryKey().get(i) + " = ? ";
            }
            pst = conn.prepareStatement(sql);

            log.info("update sql: " + sql);

            for (int i = 0; i < data.getValues().size(); i++) {
                pst.setObject(i + 1, data.getValues().get(i));
            }
            for (int i = 0; i < data.getPrimaryValue().size(); i++) {
                pst.setObject(data.getValues().size() + 1, data
                        .getPrimaryValue().get(i));
            }
            pst.execute();
            conn.commit();
        } catch (SQLException e) {
            log.error("update error:",e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtil.closeConnection(null, pst, conn);
        }
    }

    public void handleDelete(Record record) {
        Connection conn = JDBCUtil.getConnection();
        PreparedStatement pst = null;
        try {
            conn.setAutoCommit(false);
            FieldData data = getColumnsAndValues(record);

            String sql = "delete from " + data.getDbName()+"." + data.getTableName() + " where 1=1 ";
            for (int i = 0; i < data.getPrimaryKey().size(); i++) {
                sql += " and " + data.getPrimaryKey().get(i) + " = ? ";
            }
            pst = conn.prepareStatement(sql);

            log.info("delete sql: " + sql);

            for (int i = 0; i < data.getPrimaryValue().size(); i++) {
                pst.setObject(i + 1, data.getPrimaryValue().get(i));
            }
            pst.execute();
            conn.commit();
        } catch (SQLException e) {
            log.error("delete error",e);
            try {
                conn.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        } finally {
            JDBCUtil.closeConnection(null, pst, conn);
        }
    }

    public void handleDdl(Record record) {
        Connection conn = JDBCUtil.getConnection();
        Statement st = null;
        FieldData data = getColumnsAndValues(record);
        if (data.getValues().size() > 0) {
            try {
                conn.setAutoCommit(false);
                st = conn.createStatement();
                st.addBatch(" use " + data.getDbName());
                log.info("ddl sql: "+ " use " + data.getDbName() + "\n");
                for (int i = 0; i < data.getValues().size(); i++) {
                    String sql = (String) data.getValues().get(i);
                    log.info(sql+"\n");
                    st.addBatch(sql);
                }
                st.executeBatch();
                conn.commit();
            } catch (SQLException e) {
                log.error("ddl error", e);
                try {
                    conn.rollback();
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            } finally {
                JDBCUtil.closeConnection(null, st, conn);
            }
        }
    }

    public FieldData getColumnsAndValues(DataMessage.Record record) {
       String dbName = record.getDbName();
       String tableName = record.getTableName();
       List<String> columns = new ArrayList<String>();
       List<Object> values = new ArrayList<Object>();
       List<String> primaryKey = new ArrayList<String>();
       List<Object> primaryValue = new ArrayList<Object>();

       int columCount;
       if (record.getOpt() == Type.UPDATE) {
           columCount = record.getFieldCount() / 2;
       }else {
           columCount = record.getFieldCount();
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
       FieldData data = new FieldData(dbName, tableName,columCount,columns,values,
               primaryKey,primaryValue);
       return data;
    }
}
