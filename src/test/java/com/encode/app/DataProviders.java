package com.encode.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
    public static Iterator<Object[]> readFileNamesFromFile() throws IOException {
        List<Object[]> list = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                        new InputStreamReader(
                                DataProviders.class.getResourceAsStream("/filenames.csv")))
        ) {
            String s = reader.readLine();
            while (s != null) {
                list.add(new Object[]{s + ".txt"});
                s = reader.readLine();
            }
            reader.close();
        }

        return list.iterator();
    }
}
