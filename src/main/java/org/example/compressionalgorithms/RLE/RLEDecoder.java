package org.example.compressionalgorithms.RLE;

import java.io.*;

public class RLEDecoder implements RLEInterface {

    private String inputFilename, outputFilename;
    private long fileLen, outputFilelen;
    private FileInputStream fin;
    private FileOutputStream fout;
    private BufferedInputStream in;
    private BufferedOutputStream out;

    public RLEDecoder(String txt, String txt2) {
        inputFilename = txt;
        outputFilename = txt2;
        fileLen = 0;
        outputFilelen = 0;
    }
    public void decodeFile() throws Exception {
        // Перевірка, чи не є пустою назва вхідного файлу
        if (inputFilename.isEmpty()) return;

        try {
            // Ініціалізація потоків для читання та запису файлів
            fin = new FileInputStream(inputFilename);
            in = new BufferedInputStream(fin);

            fout = new FileOutputStream(outputFilename);
            out = new BufferedOutputStream(fout);
        } catch (Exception e) {
            // Викидання винятку, якщо виникає проблема з відкриттям файлів
            throw e;
        }

        try {
            // Отримання загальної довжини файлу
            fileLen = in.available();
            // Викидання винятку, якщо файл порожній
            if (fileLen == 0) throw new Exception("\nФайл порожній!");

            // Читання сигнатури RLE з файлу та порівняння з очікуваною
            byte[] sig = new byte[rleSignature.length()];
            String buf = "";
            long i = 0;
            int ch, count;

            in.read(sig, 0, rleSignature.length());
            buf = new String(sig);

            if (!rleSignature.equals(buf)) return;

            i = rleSignature.length();

            // Цикл для декодування файлу
            while (i < fileLen) {
                // Читання символу з файлу
                ch = in.read();
                i++;

                // Перевірка, чи символ - це спеціальний символ
                if (ch == ESCAPECHAR && i < fileLen) {
                    // Читання наступного символу (повторюваного символу) та кількості повторень
                    ch = in.read();
                    count = in.read();
                    i += 2;

                    // Запис повторюваного символу у вихідний файл відповідну кількість разів
                    for (int k = 0; k < count; k++) out.write((char) ch);
                } else {
                    // Якщо символ не є спеціальним, записати його у вихідний файл
                    out.write((char) ch);
                }
            }

            // Закриття потоку для запису
            out.close();
        } catch (Exception e) {
            // Викидання винятку у разі помилки при читанні чи записі файлу
            throw e;
        }
    }
}
