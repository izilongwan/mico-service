package com.common.entity;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class AutoGeneratorEntity {
    String url = "127.0.0.1:3306";
    String databse;
    String username = "root";
    String password = "root";
    String driverName = "com.mysql.cj.jdbc.Driver";
    String moduleName;
    String packageName;
    String author = "izilong";
    String parentName = "com";
    String tableName;
}
