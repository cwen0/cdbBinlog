package binlog;

import java.util.List;

/**
 * Created by cwen on 17-2-13.
 */
public class FieldData {
    private String dbName;
    private String tableName;
    private String type;
    private int columnCount;
    private List<String> columns;
    private List<Object> values;
    private List<String> primaryKey;
    private List<Object> primaryValue;

    public FieldData() {
        super();
    }

    public FieldData(String dbName, String tableName,String type, int columnCount, List<String> columns,
                     List<Object> values, List<String> primaryKey, List<Object> primaryValue) {
        super();
        this.dbName = dbName;
        this.tableName = tableName;
        this.type = type;
        this.columnCount = columnCount;
        this.columns = columns;
        this.values = values;
        this.primaryKey = primaryKey;
        this.primaryValue = primaryValue;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public List<String> getColumns() {
        return this.columns;
    }

    public void setColumns(List<String> columns) {
       this.columns = columns;
    }

    public List<Object> getValues() {
        return this.values;
    }

    public void setValues(List<Object> values) {
       this.values = values;
    }

    public List<String> getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(List<String> primaryKey) {
       this.primaryKey = primaryKey;
    }

    public List<Object> getPrimaryValue() {
        return primaryValue;
    }

    public void setPrimaryValue(List<Object> primaryValue) {
        this.primaryValue = primaryValue;
    }
}
