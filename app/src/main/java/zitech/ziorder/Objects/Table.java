package zitech.ziorder.Objects;

import ir.mirrajabi.searchdialog.core.Searchable;

public class Table implements Searchable {
    private int id;
    private int areaId;
    private String tableName;
    private int status;

    public Table(int id, int areaId, String tableName, int status) {
        this.id=id;
        this.areaId = areaId;
        this.tableName = tableName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int idArea) {
        this.areaId = areaId;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getTitle() {
        return tableName;
    }
}
