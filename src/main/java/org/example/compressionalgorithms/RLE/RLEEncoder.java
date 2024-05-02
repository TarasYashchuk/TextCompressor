package org.example.compressionalgorithms.RLE;

import java.io.*;

public class RLEEncoder implements RLEInterface {

    private String inputFilename, outputFilename;
    private long fileLen, outputFilelen;
    private FileInputStream fin;
    private FileOutputStream fout;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    public RLEEncoder(String txt, String txt2) {
        inputFilename = txt;
        outputFilename = txt2;
        fileLen = 0;
        outputFilelen = 0;
    }

    public void encodeFile() throws Exception {
        // Перевірка, чи ім'я вхідного файлу не порожнє
        if (inputFilename.isEmpty()) return;

        try {
            // Відкриття потоків для вхідного та вихідного файлів
            fin = new FileInputStream(inputFilename);
            in = new BufferedInputStream(fin);

            fout = new FileOutputStream(outputFilename);
            out = new BufferedOutputStream(fout);

        } catch (Exception e) {
            throw e;
        }
        try {
            // Отримання довжини вхідного файлу
            fileLen = in.available();
            // Перевірка, чи файл не порожній
            if (fileLen == 0) throw new Exception("\nFile is Empty!");

            long i = 0;
            int count = 0;
            int currentCh = 0, prevCh = 0;

            // Запис сигнатури RLE у вихідний файл
            out.write(rleSignature.getBytes());

            // Читання першого символу
            prevCh = in.read();
            i++;
            count = 1;

            while (i < fileLen) {
                // Читання наступного символу, поки він збігається з попереднім
                do {
                    currentCh = in.read();
                    i++;
                    if (prevCh == currentCh) count++;
                    if (count >= 255) break;
                } while (prevCh == currentCh && i < fileLen);

                // Запис повторюваного блоку або символу в вихідний файл
                if (count >= toleranceFrequency || prevCh == ESCAPECHAR) {
                    out.write(ESCAPECHAR);
                    out.write((char) prevCh);
                    out.write((char) count);
                } else {
                    for (int k = 0; k < count; k++) out.write(prevCh);
                }

                // Скидання лічильника, якщо символи різні
                if (prevCh == currentCh) count = 0;
                else count = 1;
                prevCh = currentCh;
            }
            // Закриття потоку вихідного файлу
            out.close();

        } catch (Exception e) {
            throw e;
        }
    }
}
