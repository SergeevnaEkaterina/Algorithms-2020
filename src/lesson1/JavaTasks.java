package lesson1;

import kotlin.NotImplementedError;

import java.io.*;
import java.util.*;

@SuppressWarnings("unused")
public class JavaTasks {
    /**
     * Сортировка времён
     *
     * Простая
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС AM/PM,
     * каждый на отдельной строке. См. статью википедии "12-часовой формат времени".
     *
     * Пример:
     *
     * 01:15:19 PM
     * 07:26:57 AM
     * 10:00:03 AM
     * 07:56:14 PM
     * 01:15:19 PM
     * 12:40:31 AM
     *
     * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
     * сохраняя формат ЧЧ:ММ:СС AM/PM. Одинаковые моменты времени выводить друг за другом. Пример:
     *
     * 12:40:31 AM
     * 07:26:57 AM
     * 10:00:03 AM
     * 01:15:19 PM
     * 01:15:19 PM
     * 07:56:14 PM
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    //Трудоемкость= O(N*logN) - по стандарту в Collections.sort() используется mergeSort
    //Ресурсоемкость = O(N), где N- количество строк во входном файле
    static public void sortTimes(String inputName, String outputName) throws IOException {
        ArrayList<Integer> day = new ArrayList<>();
        ArrayList<Integer> night = new ArrayList<>();
        String line;
        int hour, minute, second, dayNight, amountOfTime;
        int count = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            while ((line = reader.readLine()) != null) {
                if (!line.trim().matches("[01][0-9]:[0-5][0-9]:[0-5][0-9] [AP]M")) { // проверяем соответствие формату
                    throw new IOException();
                }
                hour = Integer.parseInt(line.substring(0, 2));
                if (hour ==12) { // присваиваем часам 0, т.к. отсчет начинается с 12 и это время должно быть наименьшим
                    hour =0;
                }
                minute = Integer.parseInt(line.substring(3, 5));
                second = Integer.parseInt(line.substring(6, 8));
                amountOfTime = 3600*hour + 60*minute + second;

                if (line.substring(9, 11).equals("AM")) {     // делим на два списка: "дневной" и "ночной"
                    day.add(amountOfTime);
                } else if (line.substring(9, 11).equals("PM")) {
                    night.add(amountOfTime);
                }
            }
        }

        Collections.sort(day);  //сортировка слиянием: O(N*logN)
        Collections.sort(night);  //O(N*logN)

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) { // записываем в файл отформатированные элементы дневного и вечернего списков
            StringBuilder build = new StringBuilder();
            day.forEach(s->build.append(formatter(s)).append("AM").append("\n"));
            night.forEach(s->build.append(formatter(s)).append("PM").append("\n"));

            writer.write(build.toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e);
        }

    }

    private static String formatter(int time) {  // получаем строку в 12- часовом формате из времени в секундах
        int hour, minute, second;
        Formatter form = new Formatter();

        hour = time/3600;
        if (hour ==0) { // меняем обратно, т.к. приводим к 12-часовому формату
            hour =12;
        }
        minute = time/60%60;
        second = time%60;
        return String.valueOf(form.format("%02d:%02d:%02d ",hour,minute,second));

    }


    /**
     * Сортировка адресов
     *
     * Средняя
     *
     * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
     * где они прописаны. Пример:
     *
     * Петров Иван - Железнодорожная 3
     * Сидоров Петр - Садовая 5
     * Иванов Алексей - Железнодорожная 7
     * Сидорова Мария - Садовая 5
     * Иванов Михаил - Железнодорожная 7
     *
     * Людей в городе может быть до миллиона.
     *
     * Вывести записи в выходной файл outputName,
     * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
     * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
     *
     * Железнодорожная 3 - Петров Иван
     * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
     * Садовая 5 - Сидоров Петр, Сидорова Мария
     *
     * В случае обнаружения неверного формата файла бросить любое исключение.
     */
    static public void sortAddresses(String inputName, String outputName) {
        throw new NotImplementedError();
    }

    /**
     * Сортировка температур
     *
     * Средняя
     * (Модифицированная задача с сайта acmp.ru)
     *
     * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
     * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
     * Например:
     *
     * 24.7
     * -12.6
     * 121.3
     * -98.4
     * 99.5
     * -12.6
     * 11.0
     *
     * Количество строк в файле может достигать ста миллионов.
     * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
     * Повторяющиеся строки сохранить. Например:
     *
     * -98.4
     * -12.6
     * -12.6
     * 11.0
     * 24.7
     * 99.5
     * 121.3
     */
    //Трудоёмкость = O(N^2), проходимся по элементам map дважды
    //Ресурсоёмкость = О(N) , где N- количество строк во входном файле

    static public void sortTemperatures(String inputName, String outputName) throws IOException {
        String line;
        double stringToDouble;
        Map<Double,Integer> temperature = new TreeMap<>(); // key-значение температуры value-счетчик количества повторов, TreeMap хранит пары, отсортированные по ключам

        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            while ((line = reader.readLine()) != null) {
                stringToDouble = Double.parseDouble(line);
                if(!temperature.containsKey(stringToDouble)) {
                    temperature.put(stringToDouble, 1); // встретилось 1 раз
                }
                else temperature.put(stringToDouble,temperature.get(stringToDouble)+1); //увеличиваем количество повторов
            }

        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {

            Iterator<Map.Entry<Double, Integer>> entryIterator = temperature.entrySet().iterator();
            if (entryIterator.hasNext()) {
                do {  // перебираем элементы map
                    Map.Entry<Double, Integer> entry = entryIterator.next();
                    for (int i = 1; i <= entry.getValue(); i++) {   //для каждого повторения записываем значение температуры
                        writer.write(entry.getKey() + "\n");
                    }
                } while (entryIterator.hasNext());
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Сортировка последовательности
     *
     * Средняя
     * (Задача взята с сайта acmp.ru)
     *
     * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
     *
     * 1
     * 2
     * 3
     * 2
     * 3
     * 1
     * 2
     *
     * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
     * а если таких чисел несколько, то найти минимальное из них,
     * и после этого переместить все такие числа в конец заданной последовательности.
     * Порядок расположения остальных чисел должен остаться без изменения.
     *
     * 1
     * 3
     * 3
     * 1
     * 2
     * 2
     * 2
     */
    //Трудоёмкость = O(N)
    //Ресурсоёмкость = O(N), где N - количество строк во входном файле
    static public void sortSequence(String inputName, String outputName) throws FileNotFoundException {
        ArrayList<Integer> numbers = new ArrayList<>();
        ArrayList<String> properNumbers = new ArrayList<>(); // числа с максимальным повтором
        ArrayList<String> otherNumbers = new ArrayList<>(); // остальные числа
        Map<Integer,Integer> sorting = new TreeMap<>(); // key - само число, value - счетчик количества повторов

        int maxNumber=0; // числа, встречающиеся максимальное количество раз
        int maxRepeat=Integer.MIN_VALUE; // сколько раз повторяются
        int stringToInteger;
        String line;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputName))) {
            while ((line = reader.readLine()) != null) {
                stringToInteger = Integer.parseInt(line);
                numbers.add(stringToInteger);
                if (sorting.containsKey(stringToInteger)) { // если встречается - увеличиваем количество повторов
                    sorting.put(stringToInteger, sorting.get(stringToInteger) + 1);
                } else {
                    sorting.put(stringToInteger, 0);
                }

                if (sorting.get(stringToInteger) > maxRepeat || (sorting.get(stringToInteger) == maxRepeat && stringToInteger < maxNumber)) { // ищем число с максимальное количеством повторов, если таких несколько - выбираем меньшее

                    maxNumber = stringToInteger;
                    maxRepeat = sorting.get(stringToInteger);
                }

            }
            int finalMaxNumber = maxNumber;
            numbers.forEach(number->{
                if(number== finalMaxNumber) {
                    properNumbers.add(number.toString()); // записываем maxNumber
                }
                else{
                    otherNumbers.add(number.toString()); // записываем остальные
                }
            });


        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputName))) {

            StringBuilder build = new StringBuilder();
            otherNumbers.forEach(s->build.append(s).append("\n")); // сначала обычные числа
            properNumbers.forEach(s->build.append(s).append("\n")); // потом отобранные

            writer.write(String.valueOf(build));
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }

    }

    /**
     * Соединить два отсортированных массива в один
     *
     * Простая
     *
     * Задан отсортированный массив first и второй массив second,
     * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
     * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
     *
     * first = [4 9 15 20 28]
     * second = [null null null null null 1 3 9 13 18 23]
     *
     * Результат: second = [1 3 4 9 9 13 15 20 23 28]
     */
    static <T extends Comparable<T>> void mergeArrays(T[] first, T[] second) {
        throw new NotImplementedError();
    }
}
