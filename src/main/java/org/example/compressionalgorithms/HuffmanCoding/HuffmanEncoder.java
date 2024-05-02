package org.example.compressionalgorithms.HuffmanCoding;

import org.example.compressionalgorithms.filebitio.FileBitWriter;

import java.io.*;

public class HuffmanEncoder implements HuffmanSignature {
    private String fileName, outputFilename;
    private HuffmanNode root = null;
    private long[] freq = new long[MAXCHARS];
    private String[] hCodes = new String[MAXCHARS];
    private int distinctChars = 0;
    private long fileLen = 0;
    private FileInputStream fin;
    private BufferedInputStream in;

    // Обнулення частот та кількості різних символів
    void resetFrequency() {
        for (int i = 0; i < MAXCHARS; i++)
            freq[i] = 0;

        distinctChars = 0;
        fileLen = 0;
    }

    // Конструктор, який завантажує файл та викликає resetFrequency
    public HuffmanEncoder(String txt, String txt2) {
        loadFile(txt, txt2);
    }

    // Ініціалізація полів класу та виклик resetFrequency
    public void loadFile(String txt, String txt2) {
        fileName = txt;
        outputFilename = txt2;
        resetFrequency();
    }

    // Кодування файлу алгоритмом Хаффмана
    public void encodeFile() throws Exception {
        // Перевірка, чи не порожнє ім'я файлу
        if (fileName.isEmpty()) return;
        try {
            // Відкриття вхідного потоку для читання файлу
            fin = new FileInputStream(fileName);
            in = new BufferedInputStream(fin);
        } catch (Exception e) {
            throw e;
        }

        try {
            // Отримання розміру файлу та перевірка на порожній файл
            fileLen = in.available();
            if (fileLen == 0) throw new Exception("File is Empty!");

            long i = 0;
            in.mark((int) fileLen);
            distinctChars = 0;
            // Читання вмісту файлу та обчислення кількості різних символів (distinctChars) та їхніх частот (freq).
            while (i < fileLen) {
                int ch = in.read();
                i++;
                if (freq[ch] == 0) distinctChars++;
                freq[ch]++;
            }
            // Повернення потоку читання на початок
            in.reset();
        } catch (IOException e) {
            throw e;
        }

        // Ініціалізація пріоритетної черги
        PriorityQueue cpq = new PriorityQueue(distinctChars + 1);

        for (int i = 0; i < MAXCHARS; i++) {
            if (freq[i] > 0) {
                HuffmanNode np = new HuffmanNode(freq[i], (char) i, null, null);
                //Додавання вузлів Хаффмана до пріоритетної черги для кожного символу, який зустрічається у файлі.
                cpq.Enqueue(np);
            }
        }

        HuffmanNode low1, low2; // 2 найменших вузла

        // Побудова дерева Хаффмана з допомогою пріоритетної черги
        while (cpq.totalNodes() > 1) { // Поки в черзі не залишиться лише один вузол
            low1 = cpq.Dequeue(); // Обираємо найменший вузол із черги
            low2 = cpq.Dequeue(); // Обираємо другий найменший вузол
            if (low1 == null || low2 == null) { // Якщо найменших вузлів немає, видаємо помилку
                throw new Exception("PQueue Error!");
            }
            HuffmanNode intermediate = new HuffmanNode((low1.freq + low2.freq), (char) 0, low1, low2);
            // Формуємо новий вузол із суми 2 найменших, 2 найменші робимо нащадками
            if (intermediate == null) { // Якщо не можливо створити батьківський вузол, видаємо помилку
                throw new Exception("Not Enough Memory!");
            }
            cpq.Enqueue(intermediate);
        }
        // Залишився один вузол в черзі
        root = cpq.Dequeue(); // Вилучаємо його

        buildHuffmanCodes(root, ""); // Використовуємо його як корінь дерева

        for (int i = 0; i < MAXCHARS; i++) hCodes[i] = ""; // Скидаємо коди
        getHuffmanCodes(root); // Конвертуємо дерево в одиниці та нулі

        // Запис в файл
        FileBitWriter hFile = new FileBitWriter(outputFilename);

        // Запис сигнатури Хаффмана у вихідний файл
        // записуємо 1 і 0 у файл разом із деревом
        hFile.putString(hSignature);
        String buf;
        // Запис розміру файлу та кількості різних символів
        buf = leftPadder(Long.toString(fileLen, 2), 32);
        hFile.putBits(buf); // Запис довжини вихідного файлу у бінарному форматі.
        buf = leftPadder(Integer.toString(distinctChars - 1, 2), 8);
        hFile.putBits(buf); // Запис кількості різних символів у бінарному форматі.

        // Запис кодів символів у вихідний файл
        for (int i = 0; i < MAXCHARS; i++) {
            if (hCodes[i].length() != 0) {
                buf = leftPadder(Integer.toString(i, 2), 8);
                //  Перетворюємо індекс символу i у бінарний формат та додаємо ведучі нулі так, щоб довжина результуючого рядка була 8 символів.
                hFile.putBits(buf);
                buf = leftPadder(Integer.toString(hCodes[i].length(), 2), 5);
                // Перетворюємо довжину коду Хаффмана для поточного символу у бінарний формат та додаємо ведучі нулі так, щоб довжина результуючого рядка була 5 символів
                // (це відповідає діапазону можливих довжин коду).
                hFile.putBits(buf);
                hFile.putBits(hCodes[i]);
                // Записуємо сам код Хаффмана для поточного символу у вихідний файл.
            }
        }

        // Кодування вихідного файлу та запис закодованих бітів
        long lcount = 0;
        while (lcount < fileLen) {
            int ch = in.read();
            hFile.putBits(hCodes[ch]);
            lcount++;
        }
        // Закриття вихідного файлу
        hFile.closeFile();
    }

    // Рекурсивне побудова кодів Хаффмана для символів
    void buildHuffmanCodes(HuffmanNode parentNode, String parentCode) {
        parentNode.huffCode = parentCode;
        if (parentNode.left != null)
            buildHuffmanCodes(parentNode.left, parentCode + "0"); // Якщо ми йдемо ліворуч, 0

        if (parentNode.right != null)
            buildHuffmanCodes(parentNode.right, parentCode + "1"); // Якщо ми йдемо праворуч, 1
    }

    // Заповнення масиву кодів Хаффмана для символів
    void getHuffmanCodes(HuffmanNode parentNode) {
        if (parentNode == null) return;
        int asciiCode = parentNode.ch; // Отримання ASCII-коду символу, який представлений поточним вузлом
        if (parentNode.left == null || parentNode.right == null)
            hCodes[asciiCode] = parentNode.huffCode;

        if (parentNode.left != null) getHuffmanCodes(parentNode.left);
        if (parentNode.right != null) getHuffmanCodes(parentNode.right);
    }

    // Доповнення тексту до певної довжини зліва нулями
    String leftPadder(String txt, int n) {
        while (txt.length() < n)
            txt = "0" + txt;
        return txt;
    }
}
