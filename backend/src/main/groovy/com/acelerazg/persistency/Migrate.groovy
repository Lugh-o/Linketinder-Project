package com.acelerazg.persistency

import com.acelerazg.utils.EnvHandler

class Migrate {
    static void main(String[] args) {
        EnvHandler.loadEnv()
        DatabaseHandler.runSql("db/schema.sql")
    }
}