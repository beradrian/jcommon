package net.sf.jcommon.logging;

import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * A configurator for java.util.logging to be used inside Spring. This will allow to embed
 * into Spring configuration file the configuration for your own logging system.
 * You can set up the logging levels, handlers and if the logger should use parent handlers.
 */
public class LogConfigurator {

    public void setLogLevels(Map<String, String> logLevels) {
        for (String loggerName : logLevels.keySet()) {
            Logger.getLogger(loggerName).setLevel(Level.parse(logLevels.get(loggerName)));
        }
    }

    public void setLogHandlers(Map<String, Handler> logHandlers) {
        for (String loggerName : logHandlers.keySet()) {
            Logger.getLogger(loggerName).addHandler(logHandlers.get(loggerName));
        }
    }

    public void setLogUseParentHandlers(Map<String, Boolean> logUseParentHandlers) {
        for (String loggerName : logUseParentHandlers.keySet()) {
            Logger.getLogger(loggerName).setUseParentHandlers(logUseParentHandlers.get(loggerName));
        }        
    }
}
