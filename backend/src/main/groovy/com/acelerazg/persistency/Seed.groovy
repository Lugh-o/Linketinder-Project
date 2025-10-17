package com.acelerazg.persistency

import com.acelerazg.utils.EnvHandler

class Seed {
    static void main(String[] args) {
        EnvHandler.loadEnv()
        DatabaseHandler.runSql("db/data.sql")
    }
}