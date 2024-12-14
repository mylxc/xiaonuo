package com.jackli.common.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @description: 文件比对工具
 * @author: luoxingcheng
 * @created: 2023/09/08 09:15
 */
public class FileComparison {

    public static void compareFiles(String file1Path, String file2Path, String outputFilePath) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));

        List<String> lines1 = new ArrayList<>();
        List<String> lines2 = new ArrayList<>();

        String line;
        while ((line = reader1.readLine()) != null) {
            lines1.add(line);
        }

        while ((line = reader2.readLine()) != null) {
            lines2.add(line);
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        writer.write("<html><head><style> .container { display: flex; } .left, .right { flex: 1; padding: 10px; } .highlight1 { background-color: #add8e6; } .highlight2 { background-color: #ffffe0; }</style></head><body>");

        writer.write("<div class='container'>");

        writer.write("<div class='left'>");
        writer.write("<h2>File 1</h2>");
        writer.write("<pre>");
        for (String line1 : lines1) {
            writer.write("<div>" + line1 + "</div>");
        }
        writer.write("</pre>");
        writer.write("</div>");

        writer.write("<div class='right'>");
        writer.write("<h2>File 2</h2>");
        writer.write("<pre>");
        for (String line2 : lines2) {
            writer.write("<div>" + line2 + "</div>");
        }
        writer.write("</pre>");
        writer.write("</div>");

        writer.write("</div>");

        writer.write("<script>");
        writer.write("var leftLines = document.querySelectorAll('.left pre div');");
        writer.write("var rightLines = document.querySelectorAll('.right pre div');");

        writer.write("for (var i = 0; i < leftLines.length; i++) {");
        writer.write("  if (leftLines[i].textContent !== rightLines[i].textContent) {");
        writer.write("    leftLines[i].classList.add('highlight1');");
        writer.write("    rightLines[i].classList.add('highlight2');");
        writer.write("  }");
        writer.write("}");
        writer.write("</script>");

        writer.write("</body></html>");
        writer.close();
    }

    public static void compareFilesFirst(String file1Path, String file2Path, String outputFilePath) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));

        List<String> lines1 = new ArrayList<>();
        List<String> lines2 = new ArrayList<>();
        List<Integer> diffLines = new ArrayList<>();

        String line;
        int lineNum = 1;

        while ((line = reader1.readLine()) != null) {
            lines1.add(line);
            lines2.add(reader2.readLine());
            if (!lines1.get(lineNum - 1).equals(lines2.get(lineNum - 1))) {
                diffLines.add(lineNum);
            }
            lineNum++;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        writer.write("<html><head><style> .container { display: flex; } .left, .right { flex: 1; padding: 10px; } .highlight { background-color: #add8e6; }</style></head><body>");

        writer.write("<div class='container'>");

        writer.write("<div class='left'>");
        writer.write("<h2>File 1</h2>");
        writer.write("<pre>");
        for (int i = 0; i < lines1.size(); i++) {
            if (diffLines.contains(i + 1)) {
                writer.write("<div class='highlight'>" + lines1.get(i) + "</div>");
            } else {
                writer.write("<div>" + lines1.get(i) + "</div>");
            }
        }
        writer.write("</pre>");
        writer.write("</div>");

        /*writer.write("<div class='right'>");
        writer.write("<h2>File 2</h2>");
        writer.write("<pre>");
        writer.write("<!-- File 2 content intentionally omitted -->");
        writer.write("</pre>");
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</body></html>");*/
        writer.close();
    }

    public static void compareFilesRight(String file1Path, String file2Path, String outputFilePath) throws IOException {
        BufferedReader reader1 = new BufferedReader(new FileReader(file1Path));
        BufferedReader reader2 = new BufferedReader(new FileReader(file2Path));

        List<String> lines1 = new ArrayList<>();
        List<String> lines2 = new ArrayList<>();
        List<Integer> diffLines = new ArrayList<>();

        String line;
        int lineNum = 1;

        while ((line = reader1.readLine()) != null) {
            lines1.add(line);
            lines2.add(reader2.readLine());
            if (!lines1.get(lineNum - 1).equals(lines2.get(lineNum - 1))) {
                diffLines.add(lineNum);
            }
            lineNum++;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath));
        writer.write("<html><head><style> .container { display: flex; } .right { flex: 1; padding: 10px; } .highlight { background-color: #add8e6; }</style></head><body>");
        writer.write("<div class='container'>");
        writer.write("<div class='right'>");
        writer.write("<h2>File 2</h2>");
        writer.write("<pre>");
        for (int i = 0; i < lines2.size(); i++) {
            if (diffLines.contains(i + 1)) {
                writer.write("<div class='highlight'>" + lines2.get(i) + "</div>");
            } else {
                writer.write("<div>" + lines2.get(i) + "</div>");
            }
        }
        writer.write("</pre>");
        writer.write("</div>");

        writer.write("</div>");
        writer.write("</body></html>");
        writer.close();
    }

    public static void main(String[] args) {
       // String file1Path = "f:/ceshi1.txt";
        String file1Path = "f:/1680054474018320386.NC";
        String file2Path = "f:/ceshi2.txt";
        String outputFilePath = "f:/output.html";

        try {
            compareFiles(file1Path, file2Path, outputFilePath);
           // compareFilesFirst(file1Path, file2Path, outputFilePath);
           // compareFilesRight(file1Path, file2Path, outputFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
