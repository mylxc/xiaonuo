package com.jackli.web.core.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  获取cpu序列号
 */
public class HardwareInfoUtil {

    /**
     *  获取cpu处理器ID
     * @return
     */
    public static String getHardwareSerialNumbers() {
        String cpuSerialNumber = null;
        String mainboardSerialNumber = null;
        String osName = System.getProperty("os.name").toLowerCase();

        try {
            if (osName.contains("windows")) {
                cpuSerialNumber = executeCommand(new String[]{"wmic", "cpu", "get", "ProcessorId"});
                mainboardSerialNumber = executeCommand(new String[]{"wmic", "baseboard", "get", "SerialNumber"});
                if(StringUtils.isBlank(mainboardSerialNumber) || mainboardSerialNumber.contains("Default") || mainboardSerialNumber.contains("string")){
                    mainboardSerialNumber = getWindowsHardwareID();
                }
            } else if (osName.contains("linux")) {
                cpuSerialNumber = executeCommand(new String[]{"bash", "-c", "dmidecode -t processor | grep 'ID' | sed 's/.*ID://' | tr -d ' '"});
                mainboardSerialNumber = executeCommand(new String[]{"bash", "-c", "dmidecode -s baseboard-serial-number"});
                if(StringUtils.isBlank(mainboardSerialNumber) || mainboardSerialNumber.contains("Default") || mainboardSerialNumber.contains("string")){
                    mainboardSerialNumber = getLinuxHardwareID();
                }
            } else if (osName.contains("mac")) {
                cpuSerialNumber = executeCommand(new String[]{"system_profiler", "SPHardwareDataType"});
                mainboardSerialNumber = extractMacMainboardSerial(mainboardSerialNumber);
                if(StringUtils.isBlank(mainboardSerialNumber) || mainboardSerialNumber.contains("Default") || mainboardSerialNumber.contains("string")){
                    mainboardSerialNumber = getMacHardwareID();
                }
            } else {
                throw new UnsupportedOperationException("Unsupported operating system: " + osName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cpuSerialNumber = cpuSerialNumber.replace("-","_");
        mainboardSerialNumber = mainboardSerialNumber.replace("-","_");
        return  (cpuSerialNumber != null ? cpuSerialNumber : "") + ";"
                + (mainboardSerialNumber != null ? mainboardSerialNumber : "");
    }


    private static String getWindowsHardwareID() throws Exception {
        String command = "wmic csproduct get uuid";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String hardwareId = null;

        while ((line = reader.readLine()) != null) {
            if (line != null && !line.trim().isEmpty() && !line.startsWith("UUID")) {
                hardwareId = line.trim();
                break;
            }
        }
        reader.close();
        return hardwareId;
    }

    private static String getLinuxHardwareID() throws Exception {
        String command = "sudo dmidecode -s system-uuid";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String hardwareId = null;

        if ((line = reader.readLine()) != null) {
            hardwareId = line.trim();
        }
        reader.close();
        return hardwareId;
    }

    private static String getMacHardwareID() throws Exception {
        String command = "system_profiler SPHardwareDataType | grep 'Serial Number (system)'";
        Process process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", command });
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        String hardwareId = null;

        while ((line = reader.readLine()) != null) {
            if (line.contains("Serial Number (system)")) {
                hardwareId = line.split(":")[1].trim();
                break;
            }
        }
        reader.close();
        return hardwareId;
    }

    private static String executeCommand(String[] command) throws IOException, InterruptedException {
        Process process = Runtime.getRuntime().exec(command);
        String output = readProcessOutput(process);
        process.waitFor();
        return output;
    }

    private static String readProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty() && !line.equalsIgnoreCase("ProcessorId") && !line.equalsIgnoreCase("SerialNumber")) {
                    output.append(line);
                    break;
                }
            }
        }
        return output.toString();
    }

    private static String extractMacMainboardSerial(String data) {
        String serialNumber = null;
        String[] lines = data.split("\n");
        for (String line : lines) {
            if (line.trim().startsWith("Serial Number")) {
                serialNumber = line.split(":")[1].trim();
                break;
            }
        }
        return serialNumber;
    }
}
