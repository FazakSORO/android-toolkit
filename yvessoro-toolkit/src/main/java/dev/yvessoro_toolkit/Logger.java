package dev.yvessoro_toolkit;

/**
 * @author FOUNGNIGUE YVES SORO
 * @since 12/08/2016
 **/

public class Logger {
    public void error(Object val) {
        System.err.println(CommonKeys.FATAL_ERROR + " " + val);
    }

    public void debug(Object val) {
        System.err.println(CommonKeys.DEBUG + " " + val);
    }

    public void info(Object val) {
        System.out.println(CommonKeys.INFO + " " + val);
    }
}
