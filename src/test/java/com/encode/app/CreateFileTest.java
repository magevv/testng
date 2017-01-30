package com.encode.app;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;

/*
Testing Java function File.createNewFile()

Positive tests
  1. New empty file is created in the right directory
  2. Function returns TRUE if file was successfully created
  3. Function returns FALSE if file with such name already exists

Negative tests
  4. Exception if directory is read-only (works on Linux only)
 */

public class CreateFileTest extends TestBase {

    File dir;

    @BeforeMethod (alwaysRun = true)
    public void setUp() {
        dir = new File("tempDir");
        dir.mkdir();
        System.out.println(dir.getAbsolutePath() + " prepared");
    }

    @AfterMethod (alwaysRun = true)
    public void tearDown() {
        // Cleanup
        if(dir != null) {
            dir.canWrite();
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            dir.delete();
        }
        System.out.println("Cleaned up");
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "generateRandomFileName")
    public void test1(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile();
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(dir.list().length > 0, "New file not found in the directory."); // file created
        soft.assertEquals(f.getName(), dir.list()[0], "File name"); // file created with correct name
        soft.assertAll();
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "generateRandomFileName")
    public void test2(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        Assert.assertTrue(f.createNewFile(), "Function return");
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "readFileNamesFromFile")
    public void test3(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile(); // file exists
        Assert.assertFalse(f.createNewFile(), "Function return");
    }

    @Test (groups = "negative", dataProviderClass = DataProviders.class, dataProvider = "generateRandomFileName")
    public void test4(String fileName) {
        String exception = null;
        String msg = null;
        File f = new File(dir + "/" + fileName);
        dir.setReadOnly(); // Linux only
        try {
            f.createNewFile();
        } catch (IOException e) {
            exception = e.getClass().getName();
            msg = e.getMessage();
        } finally {
            SoftAssert soft = new SoftAssert();
            soft.assertEquals(exception, "java.io.IOException", "Exception");
            soft.assertEquals(msg, "Permission denied", "Exception message");
            soft.assertAll();
        }
    }
}