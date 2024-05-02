package org.example.compressionalgorithms.LZW;


import org.example.compressionalgorithms.filebitio.FileBitWriter;

import java.io.*;
import java.util.*;

public class LZWEncoder implements LZWInterface {

    private String inputFilename, outputFilename; // Імена вхідного та вихідного файлів
    private long inputFilelen, outputFilelen; // Довжини вхідного та вихідного файлів
    private FileInputStream fin; // Потік для читання з вхідного файлу
    private BufferedInputStream in; // Буферизований потік для читання

    // Конструктор класу, ініціалізує імена файлів та довжини
    public LZWEncoder(String txt, String txt2) {
        inputFilename = txt;
        outputFilename = txt2;
        inputFilelen = 0;
        outputFilelen = 0;
    }

    // Метод для кодування вхідного файлу методом LZW
    public void encodeFile() throws Exception {
        // Якщо ім'я вхідного файлу порожнє, повертаємося
        if (inputFilename.isEmpty()) return;

        FileBitWriter out; // Об'єкт для запису бітів у вихідний файл

        try {
            // Ініціалізація потоків для читання вхідного та запису вихідного файлів
            fin = new FileInputStream(inputFilename);
            in = new BufferedInputStream(fin);
            out = new FileBitWriter(outputFilename);

        } catch (Exception e) {
            throw e;
        }

        try {
            // Отримання довжини вхідного файлу та перевірка на порожній файл
            inputFilelen = in.available();
            if (inputFilelen == 0) throw new Exception("\nFile is Empty!");

            // Ініціалізація та заповнення початкової таблиці кодів
            Hashtable<String, Integer> table = new Hashtable<String, Integer>();
            for (int k = 0; k < MAXCHARS; k++) {
                String buf = "" + (char) k;
                table.put(buf, k);
            }

            // Запис сигнатури та довжини вхідного файлу в вихідний файл
            out.putString(lzwSignature);
            out.putBits(leftPadder(Long.toString(inputFilelen, 2), 32));

            long i = 0;
            int codesUsed = MAXCHARS;

            int currentCh = 0;
            String prevStr = "";

            while (i < inputFilelen) { // Поки не кінець файлу
                currentCh = in.read(); // Зчитуємо наступний символ
                i++;
                String temp = prevStr + (char) currentCh; // Формуємо комбінований рядок, додаючи поточний символ до попереднього рядка.
                Integer e = table.get(temp); // Перевіряємо, чи така комбінація вже є у таблиці, отримуючи відповідний код з таблиці.

                if (e == null) {
                    // Якщо код відсутній у таблиці, додаємо його
                    if (codesUsed < MAXCODES) table.put(temp, codesUsed++);

                    // Записуємо код попереднього рядка у вихідний файл
                    Integer encoded = table.get(prevStr);
                    if (encoded != null) {
                        String wri = leftPadder(Integer.toString(encoded.intValue(), 2), MAXBITS);
                        out.putBits(wri);
                    }

                    prevStr = "" + (char) currentCh; // Оновлюємо попередній рядок на поточний символ.
                } else {
                    prevStr = temp; // Перевстановлюємо попередній рядок на комбінацію.
                }
            }

            // Записуємо код останнього рядка у вихідний файл
            Integer encoded = table.get(prevStr);
            if (encoded != null) {
                String wri = leftPadder(Integer.toString(encoded.intValue(), 2), MAXBITS);
                out.putBits(wri);
            }

            // Завершення запису вихідного файлу
            out.closeFile();
        } catch (Exception e) {
            throw e;
        }
    }

    // Метод для додавання нулів до строки до заданої довжини
    String leftPadder(String txt, int n) {
        while (txt.length() < n)
            txt = "0" + txt;
        return txt;
    }
}

