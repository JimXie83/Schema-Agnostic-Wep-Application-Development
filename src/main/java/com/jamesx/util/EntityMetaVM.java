package com.jamesx.util;

import java.util.HashMap;
/**************************************************
 * By JamesXie 2016
 **************************************************/
public class EntityMetaVM {
    public String VmName;
    public String PrimaryKey;
    public HashMap<String, ColumnInfo> Columns;
    public HashMap<String,EntityMetaVM> Child_VMs;
    /**
     * construction to initialize list
     */
    public EntityMetaVM() {
        Columns=new HashMap<String,ColumnInfo>();
        Child_VMs=new HashMap<String, EntityMetaVM>();
    }
}
