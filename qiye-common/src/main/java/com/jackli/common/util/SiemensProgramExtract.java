package com.jackli.common.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 读取西门子加工程序的刀单
 * @Author:luoweicheng
 **/
public class SiemensProgramExtract {

    public static List<String> toolsNameList(String filePath){
        List<String> toolNameList = new ArrayList<>();
        List<String> extractedValues = readLinesWithRAtFourthOrFifthChar(filePath);
        for (String name : extractedValues) {
            toolNameList.add(name);
        }
        return toolNameList;
    }

    private static List<String> readLinesWithRAtFourthOrFifthChar(String filePath) {
        List<String> extractedValues = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (hasRAtFourthOrFifthChar(line)) {
                    String extractedValue = extractValueAfterSemicolon(line);
                    if (extractedValue != null) {
                        extractedValues.add(extractedValue);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return extractedValues;
    }

    private static boolean hasRAtFourthOrFifthChar(String line) {
        return line.length() >= 5 && (line.charAt(3) == 'R' || line.charAt(4) == 'R');
    }

    private static String extractValueAfterSemicolon(String line) {
        int semicolonIndex = line.indexOf(';');
        if (semicolonIndex != -1) {
            return line.substring(semicolonIndex + 1).trim();
        } else {
            return null;
        }
    }
}
