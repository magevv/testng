package com.encode.app;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

/**
 * Global fixture for test classes.
 */
public class TestBase {

    @BeforeTest
    public void systemStart() {
        System.out.println("----- System started -----");
    }

    @AfterTest
    public void systemStop() {
        System.out.println("----- System stopped -----");
    }

}
