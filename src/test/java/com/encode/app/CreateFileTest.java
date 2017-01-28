package com.encode.app;

import org.testng.annotations.*;

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

    @BeforeMethod
    public void setUp() {
        dir = new File("tempDir");
        dir.mkdir();
        System.out.println(dir.getAbsolutePath() + " prepared");
    }

    @AfterMethod
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

    @Test
    public void test1() throws IOException {
        File f = new File(dir + "/temp.txt");
        Boolean res = f.createNewFile();
        System.out.println(f.getAbsolutePath() + " created");
    }

    @Test
    public void test2() throws IOException {
        File f = new File(dir + "/temp.txt");
        Boolean res = f.createNewFile();
        System.out.println("Function returns " + res);
    }

    @Test
    public void test3() throws IOException {
        File f = new File(dir + "/temp.txt");
        f.createNewFile();
        Boolean res = f.createNewFile();
        System.out.println("Function returns " + res + ", file already exists");
    }

    @Test
    public void test4() {
        File f = new File(dir + "/temp.txt");
        dir.setReadOnly(); // Linux only
        try {
            f.createNewFile();
        } catch (IOException e) {
            System.out.println("cannot write file to read-only directory");
        }
    }
}