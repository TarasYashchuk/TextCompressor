package org.example.compressionalgorithms.filebitio;

import java.io.*;

public class FileBitWriter {
    private String fileName;
    private File outputFile;
    private FileOutputStream fout;
    private BufferedOutputStream outf;
    private String currentByte;

    public FileBitWriter(String txt) throws Exception {
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
            outputFile = new File(fileName);
            fout = new FileOutputStream(outputFile);
            outf = new BufferedOutputStream(fout);

            currentByte = "";

        } catch (Exception e) {
            throw e;
        }
    }

    public void putBit(int bit) throws Exception { // Записує один біт у вихідний файл.
        try {
            bit = bit % 2;
            currentByte = currentByte + bit;

            if (currentByte.length() >= 8) {
                int byteval = Integer.parseInt(currentByte.substring(0, 8), 2);
                outf.write(byteval);
                currentByte = "";
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void putBits(String bits) throws Exception { // Записує рядок бітів у вихідний файл.
        try {
            while (!bits.isEmpty()) {
                int bit = Integer.parseInt(bits.substring(0, 1));
                putBit(bit);
                bits = bits.substring(1);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public void putString(String txt) throws Exception { // Записує рядок символів у вихідний файл, конвертуючи кожен символ у відповідний бінарний формат.
        try {
            while (!txt.isEmpty()) {
                String binstring = Integer.toString(txt.charAt(0), 2);
                binstring = leftPad8(binstring);

                putBits(binstring);
                txt = txt.substring(1);
            }
        } catch (Exception e) {
            throw e;
        }
    }

    String leftPad8(String txt) { //Внутрішній метод для доповнення рядка нулями зліва до завдовжки 8 біт.
        while (txt.length() < 8)
            txt = "0" + txt;
        return txt;
    }

    public void closeFile() throws Exception {
        try {
            while (currentByte.length() > 0) {
                putBit(1);
            }
            outf.close();

        } catch (Exception e) {
            throw e;
        }
    }
}
