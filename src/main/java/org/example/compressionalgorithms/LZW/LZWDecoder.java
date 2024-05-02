package org.example.compressionalgorithms.LZW;

import org.example.compressionalgorithms.filebitio.FileBitReader;

import java.io.*;
import java.util.*;

public class LZWDecoder implements LZWInterface {

    private String inputFilename, outputFilename; // Імена вхідного та вихідного файлів
    private long inputFilelen, outputFilelen; // Довжини вхідного та вихідного файлів
    private FileOutputStream fout; // Потік для запису вихідного файлу
    private BufferedOutputStream out; // Буферизований потік для запису

    // Конструктор класу, ініціалізує імена файлів та довжини
    public LZWDecoder(String txt, String txt2) {
        inputFilename = txt;
        outputFilename = txt2;
        inputFilelen = 0;
        outputFilelen = 0;
    }

    public void decodeFile() throws Exception {
        // Якщо ім'я вхідного файлу порожнє, повертаємося
        if (inputFilename.isEmpty()) return;

        FileBitReader in; // Об'єкт для читання бітів з вхідного файлу
        try {
            // Ініціалізація потоку для читання та запису вихідного файлу
            in = new FileBitReader(inputFilename);
            fout = new FileOutputStream(outputFilename);
            out = new BufferedOutputStream(fout);
        } catch (Exception e) {
            throw e;
        }
        try {
            // Отримання довжини вхідного файлу та перевірка на порожній файл
            inputFilelen = in.available();
            if (inputFilelen == 0) throw new Exception("\nFile is Empty!");

            // Ініціалізація та заповнення початкової таблиці кодів
            Hashtable<Integer, String> table = new Hashtable<>();
            for (int k = 0; k < MAXCHARS; k++) {
                String buf = "" + (char) k;
                table.put(k, buf);
            }

            // Зчитування сигнатури з вхідного файлу та перевірка її на відповідність
            String sig = in.getBytes(lzwSignature.length());
            if (!sig.equals(lzwSignature)) return;

            // Отримання довжини оригінального файлу та вивід інформації
            outputFilelen = Long.parseLong(in.getBits(32), 2);
            System.out.println("Original Size : " + outputFilelen + "\n");

            long i = 0;
            int codesUsed = MAXCHARS;
            int encodedCodeword = 0;
            String prevStr, codeWord = "";

            // Зчитування та вивід першого кодованого слова
            encodedCodeword = Integer.parseInt(in.getBits(MAXBITS), 2); // Отримуємо перше кодове слово.
            String encodedString = table.get(encodedCodeword); // Отримуємо рядок за закодованим кодовим словом.
            out.write(encodedString.getBytes());
            i += encodedString.length();
            codeWord = encodedString; // : Встановлюємо поточне кодове слово.

            // Процес декодування
            while (i < outputFilelen) {
                encodedCodeword = Integer.parseInt(in.getBits(MAXBITS), 2);
                //Отримуємо наступне кодове слово з бітового потоку та перетворюємо його у ціле число.
                encodedString = table.get(encodedCodeword);
                // Отримуємо рядок, що відповідає отриманому кодовому слову з таблиці.

                if (encodedString != null) { // Якщо кодований рядок присутній в таблиці, встановлюємо prevStr як цей рядок.
                    prevStr = encodedString;
                } else {
                    prevStr = codeWord;
                    // Якщо кодованого рядка немає в таблиці, prevStr отримує значення поточного кодового слова (codeWord).
                    prevStr = prevStr + codeWord.charAt(0);
                    // Додаємо до prevStr перший символ поточного кодового слова
                }

                for (int j = 0; j < prevStr.length(); j++) // Записуємо розкодований рядок у вихідний файл.
                    out.write(prevStr.charAt(j));

                i += prevStr.length();

                // Додавання нового кодованого слова до таблиці
                if (codesUsed < MAXCODES) table.put(codesUsed++, codeWord + prevStr.charAt(0));

                codeWord = prevStr;
            }

            // Завершення запису вихідного файлу
            out.close();
        } catch (Exception e) {
            throw e;
        }
        // Отримання довжини вихідного файлу після завершення
        outputFilelen = new File(outputFilename).length();
    }
}

