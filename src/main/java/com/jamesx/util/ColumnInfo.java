package com.jamesx.util;

/**************************************************
 * By JamesXie 2016
 **************************************************/
public class ColumnInfo {
    public String ColName;      //Entity's Column/field Name, NOT DB's column name
    public String ColDesc;      //Description of Column
    public String ColType;      // Column Type: Number/Date/String
    public Integer ColLength;   // Column Length
    public Integer MinLength;   // field minimum length
    public Integer MaxLength;   // field maximum length
    public Object MinValue;     // fidld Min Value
    public Object MaxValue;     // field Max Value
    public Integer Precision;   // precision
    public Integer Scale;       // Scale of the column
    public Boolean Nullable;    // true if field is nullable
    public Boolean isKey;       // true if this is Primary Key Field
    public String ColRegex;     // Regular Expression for validating column value
}
