package lesson7;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaDynamicTasks {
    /**
     * Наибольшая общая подпоследовательность.
     * Средняя
     * <p>
     * Дано две строки, например "nematode knowledge" и "empty bottle".
     * Найти их самую длинную общую подпоследовательность -- в примере это "emt ole".
     * Подпоследовательность отличается от подстроки тем, что её символы не обязаны идти подряд
     * (но по-прежнему должны быть расположены в исходной строке в том же порядке).
     * Если общей подпоследовательности нет, вернуть пустую строку.
     * Если есть несколько самых длинных общих подпоследовательностей, вернуть любую из них.
     * При сравнении подстрок, регистр символов *имеет* значение.
     */
    //Трудоемкость  = O(first.length()*second.length())
    //Ресурсоемкость = O(first.length()*second.length())
    public static String longestCommonSubSequence(String first, String second) {

        int firstString = first.length();
        int secondString = second.length();
        int[][] table = new int[firstString + 1][secondString + 1];

        for (int x = 1; x < firstString; x++) {
            for (int y = 1; y < secondString; y++) {
                if (first.charAt(x) == second.charAt(y)) {
                    table[x + 1][y + 1] = table[x][y] + 1;

                } else table[x + 1][y + 1] = Math.max(table[x + 1][y], table[x][y + 1]);
            }
        }
        int lengthF = firstString;
        int lengthS = secondString;

        StringBuilder output = new StringBuilder();

        while (lengthF != 0 && lengthS != 0) {
            if (first.charAt(lengthF - 1) == second.charAt(lengthS - 1)) {
                output.insert(0,first.charAt(lengthF - 1));
                lengthF--;
                lengthS--;

            } else if (table[lengthF][lengthS] == table[lengthF - 1][lengthS]) {
                lengthF--;
            } else {
                lengthS--;
            }
        }
        return output.toString();
    }

    /**
     * Наибольшая возрастающая подпоследовательность
     * Сложная
     * <p>
     * Дан список целых чисел, например, [2 8 5 9 12 6].
     * Найти в нём самую длинную возрастающую подпоследовательность.
     * Элементы подпоследовательности не обязаны идти подряд,
     * но должны быть расположены в исходном списке в том же порядке.
     * Если самых длинных возрастающих подпоследовательностей несколько (как в примере),
     * то вернуть ту, в которой числа расположены раньше (приоритет имеют первые числа).
     * В примере ответами являются 2, 8, 9, 12 или 2, 5, 9, 12 -- выбираем первую из них.
     */
    //Трудоемкость  = O((list.size())^2)
    //Ресурсоемкость = O(list.size())
    public static List<Integer> longestIncreasingSubSequence(List<Integer> list) {
        int length = list.size();
        int[] maxSeqSize = new int[length]; //длина наибольшей возрастающей подпоследовательности
        int[] res = new int[length];

        if (length <= 1) return list;

        for (int x = 0; x < length; x++) {

            res[x] = Integer.MIN_VALUE;
            maxSeqSize[x] = 1;
            for (int y = 0; y < length; y++) {
                if (list.get(y) < list.get(x) && maxSeqSize[y] + 1 > maxSeqSize[x]) {
                    maxSeqSize[x] = maxSeqSize[y] + 1;
                    res[x] = y;
                }
            }
        }

        int lastInd = 0;
        int lengthMaxIncSeq = 0;

        for (int ind = 0; ind < maxSeqSize.length; ind++) {
            if (maxSeqSize[ind] > lengthMaxIncSeq) { // ищем длину искомой последовательности

                lastInd = ind;
                lengthMaxIncSeq = maxSeqSize[ind];
            }
        }


        List<Integer> answer = new ArrayList<>();
        while (lastInd >= 0) {
            answer.add(0, list.get(lastInd));

            lastInd = res[lastInd];
        }


        return answer;
    }

    /**
     * Самый короткий маршрут на прямоугольном поле.
     * Средняя
     * <p>
     * В файле с именем inputName задано прямоугольное поле:
     * <p>
     * 0 2 3 2 4 1
     * 1 5 3 4 6 2
     * 2 6 2 5 1 3
     * 1 4 3 2 6 2
     * 4 2 3 1 5 0
     * <p>
     * Можно совершать шаги длиной в одну клетку вправо, вниз или по диагонали вправо-вниз.
     * В каждой клетке записано некоторое натуральное число или нуль.
     * Необходимо попасть из верхней левой клетки в правую нижнюю.
     * Вес маршрута вычисляется как сумма чисел со всех посещенных клеток.
     * Необходимо найти маршрут с минимальным весом и вернуть этот минимальный вес.
     * <p>
     * Здесь ответ 2 + 3 + 4 + 1 + 2 = 12
     */
    public static int shortestPathOnField(String inputName) {
        throw new NotImplementedError();
    }

    // Задачу "Максимальное независимое множество вершин в графе без циклов"
    // смотрите в уроке 5
}
