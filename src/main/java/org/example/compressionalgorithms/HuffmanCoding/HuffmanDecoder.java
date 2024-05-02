package org.example.compressionalgorithms.HuffmanCoding;

import java.io.*;
import org.example.compressionalgorithms.filebitio.FileBitReader;

public class HuffmanDecoder implements HuffmanSignature {
    private String fileName, outputFilename;
    private String[] hCodes = new String[MAXCHARS];
    private int distinctChars = 0;
    private long fileLen = 0, outputFilelen;
    private FileOutputStream outf;

    // Конструктор класу HuffmanDecoder
    public HuffmanDecoder(String txt, String txt2) {
        loadFile(txt, txt2); // Виклик методу для завантаження файлів
    }

    // Метод для завантаження файлів
    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
    }

    // Метод для розкодування файлу
    public void decodeFile() throws Exception {

        if (fileName.isEmpty()) return; // Якщо ім'я файлу порожнє, вийти

        for (int i = 0; i < MAXCHARS; i++) hCodes[i] = ""; // Зробити всі коди порожніми
        long i;
        FileBitReader fin = new FileBitReader(fileName); // Створення об'єкта для зчитування файлу по бітах
        fileLen = fin.available(); // Отримання довжини файлу

        String buf;
        buf = fin.getBytes(hSignature.length()); // Отримання бітової послідовності довжиною, рівною довжині підпису

        if (!buf.equals(hSignature)) return; // Якщо бітова послідовність не збігається з підписом, вийти

        // Читання додаткової інформації з файлу
        outputFilelen = Long.parseLong(fin.getBits(32), 2);
        distinctChars = Integer.parseInt(fin.getBits(8), 2) + 1;

        // Зчитування кодів Хаффмана для кожного символу
        for (i = 0; i < distinctChars; i++) {
            int ch = Integer.parseInt(fin.getBits(8), 2); // Зчитуємо 8 біт з файлу та перетворює його в ціле число
            int len = Integer.parseInt(leftPadder(fin.getBits(5), 8), 2);
            // Зчитуємо 5 біт з файлу, які представляють довжину кодового слова для даного символу. Потім використовує leftPadder для доповнення нулями зліва до 8 біт і перетворює його в ціле число.
            hCodes[ch] = fin.getBits(len);
            // Зчитуємо бітову послідовність довжини len та призначаємо її коду Хаффмана для символу ch. Ця бітова послідовність представляє кодове слово для даного символу.
        }

        try {
            outf = new FileOutputStream(outputFilename); // Створення потоку для запису в файл
            i = 0;
            int k;
            int ch;

            // Розкодування файлу
            while (i < outputFilelen) {
                buf = "";
                for (k = 0; k < 32; k++) {
                    buf += fin.getBit(); // Зчитуємо біт з файлу і додає його до буфера.
                    ch = findCodeword(buf); // Пошук кодового слова у таблиці Хаффмана
                    if (ch > -1) { // Якщо кодове слово знайдено, записуємо вихідний символ у вихідний файл.
                        outf.write((char) ch); // Запис символу в файл
                        i++;
                        break;
                    }
                }
                if (k >= 32)
                    throw new Exception("Corrupted File!"); // Якщо символ має більше 32 бітів, вийти
            }
            outf.close(); // Закриття потоку запису в файл

        } catch (Exception e) {
            throw e;
        }
    }

    // Метод для пошуку кодового слова в таблиці Хаффмана
    int findCodeword(String cw) {
        int ret = -1; // Відсутність символу в таблиці
        for (int i = 0; i < MAXCHARS; i++) {
            if (!hCodes[i].isEmpty() && cw.equals(hCodes[i])) { // Перевірка, чи кодове слово не порожнє і збігається із збереженим у таблиці
                ret = i; // Повернення індексу символу
                break;
            }
        }
        return ret;
    }

    // Метод для додавання нулів зліва до заданої довжини
    String leftPadder(String txt, int n) {
        while (txt.length() < n)
            txt = "0" + txt;
        return txt;
    }
}
