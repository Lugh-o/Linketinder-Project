package com.acelerazg

import com.acelerazg.persistency.DatabaseHandler
import com.acelerazg.utils.EnvHandler
import com.acelerazg.view.Menu
import groovy.transform.CompileStatic

@CompileStatic
class Main {
    static void main(String[] args) {
        EnvHandler.loadEnv()
        if (!DatabaseHandler.testConnection()) return
        Menu.run()
    }
}