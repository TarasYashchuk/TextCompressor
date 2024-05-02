package org.example.compressionalgorithms.filebitio;

import java.io.*;

public class FileBitReader {
    private String fileName;
    private File inputFile;
    private FileInputStream fin;
    private BufferedInputStream in;
    private String currentByte;

    public FileBitReader(String txt) throws Exception {
        try {
            fileName = txt;
            loadFile(fileName);
        } catch (Exception e) {
            throw e;
        }
    }

    public void loadFile(String txt) throws Exception {
        fileName = txt;

        try {
            inputFile = new File(fileName);
            fin = new FileInputStream(inputFile);
            in = new BufferedInputStream(fin);
            currentByte = "";

        } catch (Exception e) {
            throw e;
        }
    }

    String leftPad8(String txt) {
        while (txt.length() < 8)
            txt = "0" + txt;
        return txt;
    }

    public String getBit() throws Exception { // Читає один біт з файлу та повертає його у вигляді рядка.
        try {
            if (currentByte.isEmpty() && in.available() >= 1) {
                int k = in.read();

                currentByte = Integer.toString(k, 2);
                currentByte = leftPad8(currentByte);
            }
            if (!currentByte.isEmpty()) {
                String ret = currentByte.substring(0, 1);
                currentByte = currentByte.substring(1);
                return ret;
            }
            return "";
        } catch (Exception e) {
            throw e;
        }
    }
    public String getBits(int n) throws Exception { // Читає n бітів з файлу та повертає їх у вигляді рядка.
        try {
            String ret = "";
            for (int i = 0; i < n; i++) {
                ret += getBit();
            }
            return ret;
        } catch (Exception e) {
            throw e;
        }
    }

    public String getBytes(int n) throws Exception { // Читає n байтів з файлу та повертає їх у вигляді рядка.
        try {
            String ret = "", temp;
            for (int i = 0; i < n; i++) {
                temp = getBits(8);
                char k = (char) Integer.parseInt(temp, 2);
                ret += k;
            }
            return ret;
        } catch (Exception e) {
            throw e;
        }
    }

    public long available() throws Exception { // Повертає кількість байтів, які можна прочитати з потоку вводу.
        try {
            return in.available();
        } catch (Exception e) {
            throw e;
        }
    }
}
