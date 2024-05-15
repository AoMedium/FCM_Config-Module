package fspm.config;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import fspm.config.tests.HierarchyTestSuite;
import fspm.config.tests.ParamAccessTestSuite;

@RunWith(Suite.class)
@SuiteClasses({
        ParamAccessTestSuite.class, HierarchyTestSuite.class })

public class ConfigTestSuite {
    public static final Config CONFIG = Config.getInstance();

    public static void println(Object o) {
        System.out.println(o.toString());
    }
}