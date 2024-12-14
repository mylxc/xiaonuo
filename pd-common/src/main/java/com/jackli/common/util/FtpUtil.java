package com.jackli.common.util;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.*;

/**
 * @description: ftp
 * @author: luoxingcheng
 * @created: 2024/02/01 10:28
 */
public class FtpUtil {

        private FTPClient ftpClient;

        public FtpUtil() {
            this.ftpClient = new FTPClient();
        }

        public boolean connect(String server, int port, String user, String password) {
            boolean flag = false;
            try {
                ftpClient.connect(server, port);
                ftpClient.login(user, password);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setConnectTimeout(5000);

                // 是否成功登录FTP服务器
                int replyCode = ftpClient.getReplyCode();
                if (!FTPReply.isPositiveCompletion(replyCode)) {
                    ftpClient.disconnect();
                    return flag;
                }

                ftpClient.enterLocalPassiveMode();
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 上传文件到 FTP 服务器指定目录
        public boolean uploadFile(String localFilePath, String remoteDirectory, String remoteFileName) {
            try (InputStream input = new FileInputStream(new File(localFilePath))) {
                String gg  = remoteDirectory+remoteFileName;
                ftpClient.changeWorkingDirectory(remoteDirectory+remoteFileName);
                return ftpClient.storeFile(remoteFileName, input);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 检查 FTP 指定目录下是否存在文件
        public boolean checkFileExists(String remoteDirectory, String fileName) {
            try {
                ftpClient.changeWorkingDirectory(remoteDirectory);
                FTPFile[] files = ftpClient.listFiles();
                if (files != null) {
                    for (FTPFile file : files) {
                        if (file.getName().equals(fileName)) {
                            return true;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        // 删除 FTP 指定目录下的文件
        public boolean deleteFile(String remoteDirectory, String fileName) {
            try {
                ftpClient.changeWorkingDirectory(remoteDirectory);
                return ftpClient.deleteFile(fileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }

        // 关闭 FTP 连接
        public void disconnect() {
            try {
                if (ftpClient.isConnected()) {
                    ftpClient.logout();
                    ftpClient.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private boolean uploadFileToFTP(InputStream input, String remoteDirectory, String remoteFileName) throws IOException {
        ftpClient.changeWorkingDirectory(remoteDirectory);
        return ftpClient.storeFile(remoteFileName, input);
    }

        public static void main(String[] args) {
            FtpUtil ftpUtil = new FtpUtil();
            boolean connected = ftpUtil.connect("192.168.0.63", 21, "fmsftp", "fmsftp");
            if (connected) {
                // 上传文件到 FTP 服务器指定目录
                boolean uploaded = ftpUtil.uploadFile("F:\\ceshi1.txt", "/basefile/", "ceshi1.txt");

                if (uploaded) {
                    System.out.println("File uploaded successfully.");
                } else {
                    System.err.println("Failed to upload file.");
                }

                // 检查 FTP 指定目录下是否存在文件
                boolean fileExists = ftpUtil.checkFileExists("/basefile", "1.txt");
                if (fileExists) {
                    System.out.println("File exists on the FTP server.");
                } else {
                    System.out.println("File does not exist on the FTP server.");
                }

                // 删除 FTP 指定目录下的文件
                boolean fileDeleted = ftpUtil.deleteFile("/remoteDirectory", "remoteFileName");
                if (fileDeleted) {
                    System.out.println("File deleted successfully.");
                } else {
                    System.err.println("Failed to delete file.");
                }

                // 关闭 FTP 连接
                ftpUtil.disconnect();
            } else {
                System.err.println("Failed to connect to FTP server.");
            }
        }
}
