package com.encode.app;

import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import static com.encode.app.SourceFile.FileFormat.*;

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

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method m) {

        dir = new File("tempDir");
        dir.mkdir();

        TempDir tempDir = m.getAnnotation(TempDir.class);
        if (tempDir != null) {
            dir.setReadable(tempDir.read());
            dir.setWritable(tempDir.write());
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        // Cleanup
        if(dir != null) {
            dir.canWrite();
            File[] files = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                files[i].delete();
            }
            dir.delete();
            dir = null;
        }
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "prepareTestData")
    @SourceFile(format = EXCEL, path = "filenames.xlsx")
    public void test1(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile();
        SoftAssert soft = new SoftAssert();
        soft.assertTrue(dir.list().length > 0, "New file not found in the directory."); // file created
        soft.assertEquals(f.getName(), dir.list()[0], "File name"); // file created with correct name
        soft.assertAll();
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "prepareTestData")
    @SourceFile(format = EXCEL, path = "filenames.xls")
    public void test2(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        Assert.assertTrue(f.createNewFile(), "Function return");
    }

    @Test (groups = "positive", dataProviderClass = DataProviders.class, dataProvider = "prepareTestData")
    @SourceFile(format = TXT, path = "filenames.txt")
    public void test3(String fileName) throws IOException {
        File f = new File(dir + "/" + fileName);
        f.createNewFile(); // file exists
        Assert.assertFalse(f.createNewFile(), "Function return");
    }

    @Test (groups = "negative", dataProviderClass = DataProviders.class, dataProvider = "prepareTestData")
    @TempDir(write = false)
    public void test4(String fileName) {
        String exception = null;
        String msg = null;
        File f = new File(dir + "/" + fileName);
        try {
            f.createNewFile();
            throw new IOException("Permission denied");
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