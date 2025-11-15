package com.acelerazg.web

import com.acelerazg.utils.EnvHandler

import javax.servlet.ServletContextEvent
import javax.servlet.ServletContextListener
import javax.servlet.annotation.WebListener

@WebListener
class AppInitializer implements ServletContextListener {
    @Override
    void contextInitialized(ServletContextEvent sce) {
        EnvHandler.loadEnv("/opt/tomcat/webapps/linketinder/.env")
    }

    @Override
    void contextDestroyed(ServletContextEvent sce) {}
}
