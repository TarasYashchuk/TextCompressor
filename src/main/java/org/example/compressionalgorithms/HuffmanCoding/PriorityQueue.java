package org.example.compressionalgorithms.HuffmanCoding;

class PriorityQueue {

    HuffmanNode[] nodes; // Масив вузлів
    int capacity, total; // Максимальна ємність та поточна кількість елементів у черзі

    // Конструктор класу, ініціалізує масив вузлів та задає максимальну ємність
    public PriorityQueue(int max) {
        capacity = max;
        total = 0;
        nodes = new HuffmanNode[capacity];
    }

    // Метод для додавання елементу в чергу
    public boolean Enqueue(HuffmanNode np) {
        // Якщо черга повна, повертається false
        if (isFull()) return false;

        // Якщо черга порожня, додається елемент і повертається true
        if (isEmpty()) {
            nodes[total++] = np;
            return true;
        }

        // Знаходження правильної позиції для вставки нового елемента
        int i = total - 1, pos;
        while (i >= 0) {
            if (nodes[i].freq < np.freq) {
                break;
            }
            i--;
        }

        // Зсув елементів для вставки нового елемента
        pos = total - 1;
        while (pos >= i + 1) {
            nodes[pos + 1] = nodes[pos];
            pos--;
        }
        nodes[i + 1] = np;
        total++;
        return true;
    }

    // Метод для вилучення елемента з черги
    public HuffmanNode Dequeue() {
        // Якщо черга порожня, повертається null
        if (isEmpty()) return null;

        // Вилучення першого елемента з черги
        HuffmanNode ret = nodes[0];
        total--;

        // Зсув елементів після вилучення першого елемента
        for (int i = 0; i < total; i++)
            nodes[i] = nodes[i + 1];

        return ret;
    }

    // Перевірка, чи черга порожня
    public boolean isEmpty() {
        return (total == 0);
    }

    // Перевірка, чи черга повна
    public boolean isFull() {
        return (total == capacity);
    }

    // Отримання кількості елементів у черзі
    public int totalNodes() {
        return total;
    }
}

