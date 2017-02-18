package com.encode.app;

import java.io.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.testng.annotations.DataProvider;

public class DataProviders {

    @DataProvider
    public static Iterator<Object[]> generateRandomFileName() {
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new Object[]{"tempfile" + new Random().nextInt() + ".txt"});
        }
        return list.iterator();
    }

    @DataProvider
    public static Iterator<Object[]> readFileNamesFromFile(Method m) throws IOException {
        SourceFile sourceFile = m.getAnnotation(SourceFile.class);
        List<Object[]> list = new ArrayList<>();
        if (sourceFile != null) {
            switch (sourceFile.format()) {
                case TXT: list = readTxt(sourceFile.path()); break;
                case XLS: list = readExcel(sourceFile.path());
            }
        }
        return list.iterator();
    }

    public static List<Object[]> readTxt(String fileName) throws IOException {
        List<Object[]> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                DataProviders.class.getResourceAsStream(fileName)))
        ) {
            String s = reader.readLine();
            while (s != null) {
                list.add(new Object[]{s});
                s = reader.readLine();
            }
        }
        return list;
    }

    public static List<Object[]> readExcel(String fileName) throws IOException {
        try (
                FileInputStream fis = new FileInputStream(new File(fileName));
                Workbook workbook = new HSSFWorkbook(fis)
        ) {
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            List<Object[]> list = new ArrayList<>();
            while (iterator.hasNext()) {
                Row row = iterator.next();
                String value = row.getCell(0).getStringCellValue();
                list.add(new Object[]{value});
            }
            return list;
        }
    }
}
