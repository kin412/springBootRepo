package com.kin.springbootproject1.config;

import org.hibernate.dialect.OracleDialect;

//잘 안되서 오라글 19c설치함
public class Oracle12cCustomDialect extends OracleDialect {
    public Oracle12cCustomDialect() {
        super();
    }
}
