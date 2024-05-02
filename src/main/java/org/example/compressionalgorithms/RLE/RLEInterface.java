package org.example.compressionalgorithms.RLE;

public interface RLEInterface {
    // Спеціальний символ, який використовується для позначення повторюваного блоку в RLE
    int ESCAPECHAR = 255;

    // Сигнатура, яка використовується для визначення файлів, закодованих RLE
    String rleSignature = "RLE";

    // Тривалість повторень (кількість символів), яку можна застосовувати в RLE
    int toleranceFrequency = 4;
}

