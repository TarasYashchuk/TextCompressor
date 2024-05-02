package org.example.compressionalgorithms.LZW;

public interface LZWInterface {

    int MAXCHARS = 256; // Максимальна кількість символів у кодовому слові
    String lzwSignature = "LZW"; //  Cигнатура, яка додається до початку закодованого файлу
    int MAXCODES = 4096; // Максимальна кількість кодів у таблиці
    int MAXBITS = 12; //  Максимальна кількість бітів для представлення кодів

}
