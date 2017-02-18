package com.encode.app;

import java.io.*;
import java.lang.reflect.Method;
import java.util.*;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.DataProvider;

public class DataProviders {

    public static String resources = System.getProperty("user.dir") + "\\src\\test\\resources\\";

    @DataProvider
    public static Iterator<Object[]> prepareTestData(Method m) throws IOException {
        SourceFile sourceFile = m.getAnnotation(SourceFile.class);
        List<Object[]> list;
        if (sourceFile != null) {
            switch (sourceFile.format()) {
                case TXT:
                    list = readTxt(sourceFile.path());
                    break;
                case EXCEL:
                    list = readExcel(sourceFile.path());
                    break;
                default:
                    throw new Error("Not supported format " + sourceFile.format());
            }
        } else {
            list = generateRandomFileName();
        }
        return list.iterator();
    }

    public static List<Object[]> generateRandomFileName() {
        List<Object[]> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new Object[]{"tempfile" + new Random().nextInt() + ".txt"});
        }
        return list;
    }

    public static List<Object[]> readTxt(String fileName) throws IOException {
        List<Object[]> list = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                DataProviders.class.getResourceAsStream("/" + fileName)))
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
                FileInputStream fis = new FileInputStream(new File(resources + fileName));
                Workbook workbook = getWorkbook(fis, fileName)
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

    private static Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {
        Workbook workbook = null;
        if (excelFilePath.endsWith("xlsx")) {
            workbook = new XSSFWorkbook(inputStream);
        } else if (excelFilePath.endsWith("xls")) {
            workbook = new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }
}
