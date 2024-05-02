package org.example.compressionalgorithms.runner;

import org.example.compressionalgorithms.RLE.RLEDecoder;
import org.example.compressionalgorithms.RLE.RLEEncoder;
import org.example.compressionalgorithms.HuffmanCoding.HuffmanDecoder;
import org.example.compressionalgorithms.HuffmanCoding.HuffmanEncoder;
import org.example.compressionalgorithms.LZW.LZWDecoder;
import org.example.compressionalgorithms.LZW.LZWEncoder;

import java.io.File;
import java.io.FileInputStream;
import java.text.NumberFormat;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class Runner {
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        menu(input);

        input.close();
    }

    private static void menu(Scanner input) {
        long startTime;

        long endTime;

        long elapsedTime;

        long usedMemoryBefore;

        long usedMemoryAfter;

        long memoryUsed;

        System.out.println("\t\t\tПрограма для стиснння текстів без втрат");
        System.out.println("Оберіть опцію:");
        System.out.println("1 >> Стиснути");
        System.out.println("2 >> Розпакувати");
        System.out.println("3 >> Вихід");
        System.out.print("Ваш вибір >>> ");
        int option = input.nextInt();

        input.nextLine();

        try {
            if (option == 1) {
                System.out.println("Введіть повний шлях до файлу, який хочете стиснути:");
                String path = input.nextLine();
                File toCompress = new File(path);

                System.out.println("Введіть бажане ім'я стиснутого файлу");
                String compressedFileName = input.nextLine();
                File compressedFile = new File(compressedFileName);

                System.out.println("Яким методом бажаєте стиснути дані ?:");

                System.out.println("1 >> LZW");
                System.out.println("2 >> HuffmanCoding");
                System.out.println("3 >> RLE");

                double ratio;

                int choise = input.nextInt();
                input.nextLine();

                switch (choise) {
                    case 1 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        LZWEncoder lzwe = new LZWEncoder(path, compressedFileName);

                        startTime = System.currentTimeMillis();

                        lzwe.encodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        ratio = getCompressionRatio(toCompress, compressedFile);
                        System.out.println("Розмір файлу було стиснено на " + NumberFormat.getPercentInstance().format(ratio));

                        System.out.println("Час стиснення: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " мілісекунд");

                        System.out.println("Використана пам'ять до стиснення (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після стиснення (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);

                    }
                    case 2 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        HuffmanEncoder huff = new HuffmanEncoder(path, compressedFileName);

                        startTime = System.currentTimeMillis();

                        huff.encodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        ratio = getCompressionRatio(toCompress, compressedFile);
                        System.out.println("Розмір файлу було стиснено на " + NumberFormat.getPercentInstance().format(ratio));

                        System.out.println("Час стиснення: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " мілісекунд");

                        System.out.println("Використана пам'ять до стиснення (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після стиснення (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);
                    }
                    case 3 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        RLEEncoder rleEncoder = new RLEEncoder(path, compressedFileName);

                        startTime = System.currentTimeMillis();

                        rleEncoder.encodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        ratio = getCompressionRatio(toCompress, compressedFile);
                        System.out.println("Розмір файлу було стиснено на " + NumberFormat.getPercentInstance().format(ratio));

                        System.out.println("Час стиснення: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " мілісекунд");

                        System.out.println("Використана пам'ять до стиснення (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після стиснення (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);
                    }
                    default -> System.out.println("Такого пункту немає");
                }


            } else if (option == 2) {
                System.out.println("Введіть повний шлях до файлу, який хочете розпакувати:");
                String path = input.nextLine();
                FileInputStream in = new FileInputStream(path);
                System.out.println("Введіть ім'я для стисненого файлу: ");
                String filename = input.nextLine();

                System.out.println("Яким методом бажаєте розпакувати дані ?:");

                System.out.println("1 >> LZW");
                System.out.println("2 >> HuffmanCoding");
                System.out.println("3 >> RLE");

                int choise = input.nextInt();
                switch (choise) {
                    case 1 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        LZWDecoder lzwd = new LZWDecoder(path, filename);

                        startTime = System.currentTimeMillis();

                        lzwd.decodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        System.out.println("Час розпакування: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " milliseconds");

                        System.out.println("Використана пам'ять до розпакування (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після розпакування (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);
                    }
                    case 2 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        HuffmanDecoder huffd = new HuffmanDecoder(path, filename);

                        startTime = System.currentTimeMillis();

                        huffd.decodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        System.out.println("Час розпакування: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " milliseconds");

                        System.out.println("Використана пам'ять до розпакування (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після розпакування (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);
                    }
                    case 3 -> {
                        Runtime runtime = Runtime.getRuntime();
                        usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

                        RLEDecoder crleDecoder = new RLEDecoder(path, filename);

                        startTime = System.currentTimeMillis();

                        crleDecoder.decodeFile();

                        endTime = System.currentTimeMillis();
                        elapsedTime = endTime - startTime;

                        System.out.println("Час розпакування: " + TimeUnit.MILLISECONDS.toMillis(elapsedTime) + " milliseconds");

                        System.out.println("Використана пам'ять до розпакування (у байтах): " + usedMemoryBefore);

                        usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                        System.out.println("Використана пам'ять після розпакування (у байтах): " + usedMemoryAfter);

                        memoryUsed = usedMemoryAfter - usedMemoryBefore;
                        System.out.println("Використана пам'ять (у байтах): " + memoryUsed);
                    }
                    default -> System.out.println("Такого пункту немає");
                }

            } else if (option == 3) {
                System.out.println("Вихід із програми...");

            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static double getCompressionRatio(File initial, File compressed) {
        long initSize = initial.length();
        long compSize = compressed.length();

        long diff = initSize - compSize;

        double ratio = ((double) diff / initSize);
        return ratio;
    }

}
