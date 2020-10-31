package lesson6;

import kotlin.NotImplementedError;

import java.util.*;

@SuppressWarnings("unused")
public class JavaGraphTasks {
    /**
     * Эйлеров цикл.
     * Средняя
     * <p>
     * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
     * Если в графе нет Эйлеровых циклов, вернуть пустой список.
     * Соседние дуги в списке-результате должны быть инцидентны друг другу,
     * а первая дуга в списке инцидентна последней.
     * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
     * Веса дуг никак не учитываются.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
     * <p>
     * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
     * связного графа ровно по одному разу
     */
    public static List<Graph.Edge> findEulerLoop(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Минимальное остовное дерево.
     * Средняя
     * <p>
     * Дан связный граф (получатель). Найти по нему минимальное остовное дерево.
     * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
     * вернуть любое из них. Веса дуг не учитывать.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ:
     * <p>
     * G    H
     * |    |
     * A -- B -- C -- D
     * |    |    |
     * E    F    I
     * |
     * J ------------ K
     */
    public static Graph minimumSpanningTree(Graph graph) {
        throw new NotImplementedError();
    }

    /**
     * Максимальное независимое множество вершин в графе без циклов.
     * Сложная
     * <p>
     * Дан граф без циклов (получатель), например
     * <p>
     * G -- H -- J
     * |
     * A -- B -- D
     * |         |
     * C -- F    I
     * |
     * E
     * <p>
     * Найти в нём самое большое независимое множество вершин и вернуть его.
     * Никакая пара вершин в независимом множестве не должна быть связана ребром.
     * <p>
     * Если самых больших множеств несколько, приоритет имеет то из них,
     * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
     * <p>
     * В данном случае ответ (A, E, F, D, G, J)
     * <p>
     * Если на входе граф с циклами, бросить IllegalArgumentException
     * <p>
     * Эта задача может быть зачтена за пятый и шестой урок одновременно
     */

    //Трудоемкость O(V + E),где V - вершины графа, E - ребра
    //Ресурсоемкость O(V)
    public static Set<Graph.Vertex> largestIndependentVertexSet(Graph graph) {
        Set<Graph.Vertex> setOfIndependentVertices = graph.getVertices();
        Set<Graph.Vertex> setOfNeighbors;

        if (setOfIndependentVertices.isEmpty()) {
            return new HashSet<>();
        }
        requireGraphNotCycled(graph); //проверка графа на зацикленность

        for (Graph.Vertex item : graph.getVertices()) {    //для каждой вершины графа:
            if (setOfIndependentVertices.contains(item)) {  //если вершина еще не была удалена из множества независимых
                setOfNeighbors = graph.getNeighbors(item);  //множество ее соседей
                setOfNeighbors.forEach(setOfIndependentVertices::remove);//удаляем каждого соседа из множества независимых вершин графа
            }
            Set<Graph.Vertex> biggestIndependent = graph.getVertices();  //из найденных множеств независимых вершин ищем наибольшее
            if (setOfIndependentVertices.size() < graph.getVertices().size() - setOfIndependentVertices.size()) {  // существует ли еще набор независимых вершин, или найденный единственный -> наибольший
                setOfIndependentVertices.forEach(biggestIndependent::remove); //вычитаем независимые вершины из общего множества
                return biggestIndependent;
            }
        }
        return setOfIndependentVertices;
    }

    public static void requireGraphNotCycled(Graph graph) {
        Set<Graph.Vertex> setOfStarts = new HashSet<>();
        Set<Graph.Vertex> setOfEnds = new HashSet<>();
        Set<Graph.Edge> setOfEdges = graph.getEdges();
        Graph.Edge edge;

        for (Graph.Edge e : setOfEdges) {  // для каждого ребра заполняем set, делаем проверку на зацикленность графа
            Graph.Vertex startVertex = e.getBegin();
            Graph.Vertex endVertex = e.getEnd();
            if (setOfStarts.contains(startVertex) && setOfEnds.contains(endVertex) || setOfStarts.contains(endVertex) && setOfEnds.contains(startVertex)) {
                throw new IllegalArgumentException(); //общие вершины в множествах -> зациклен
            }
            if (!setOfStarts.contains(startVertex) && !setOfEnds.contains(endVertex)) {//ребро еще не было рассмотрено -> добавляем начало и конец в соответствующие множества
                setOfStarts.add(startVertex);
                setOfEnds.add(endVertex);
            } else if (setOfEnds.contains(startVertex)) {//начало или конец ребра уже есть в 1 множестве -> помещаем противоположную вершину в оба множества
                setOfStarts.add(endVertex);
                setOfEnds.add(endVertex);
            } else if (setOfStarts.contains(endVertex)) {
                setOfEnds.add(startVertex);
                setOfStarts.add(startVertex);
            }
        }
    }


    /**
     * Наидлиннейший простой путь.
     * Сложная
     * <p>
     * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
     * Простым считается путь, вершины в котором не повторяются.
     * Если таких путей несколько, вернуть любой из них.
     * <p>
     * Пример:
     * <p>
     * G -- H
     * |    |
     * A -- B -- C -- D
     * |    |    |    |
     * E    F -- I    |
     * |              |
     * J ------------ K
     * <p>
     * Ответ: A, E, J, K, D, C, H, G, B, F, I
     */
    //Трудоемкость O(V!),где V - количество вершин графа. Не имеет быстрого решения, NP-полная задача
    //Ресурсоемкость O(V!)
    public static Path longestSimplePath(Graph graph) {
        Set<Graph.Vertex> setOfVertices = graph.getVertices(); // множество всех вершин графа
        if (setOfVertices.isEmpty()) {  //если нулевой граф
            return new Path();
        }
        Path longestRoute = new Path(setOfVertices.iterator().next()); //по умолчанию считаем наидлиннейший маршрут = через все вершины
        PriorityQueue<Path> priorityQueue = new PriorityQueue<>();
        int maxLength = Integer.MIN_VALUE;
        setOfVertices.forEach(element -> priorityQueue.add(new Path(element)));
        while (!priorityQueue.isEmpty()) {
            Path currentPath = priorityQueue.poll();
            List<Graph.Vertex> currentVertices = currentPath.getVertices(); // составляем список вершин пути, извлеченного из очереди
            Set<Graph.Vertex> neighbors = graph.getNeighbors(currentVertices.get(currentVertices.size() - 1)); //множество соседей последнего элемента текущего пути
            if (currentPath.getLength() > maxLength) {  // если находим более длинный путь -> становится максимальным
                maxLength = currentPath.getLength();
                longestRoute = currentPath;

            }

            neighbors.forEach(element -> {
                if (!currentPath.contains(element)) { //для каждого нового соседа добавляем новый путь в очередь
                    priorityQueue.add(new Path(currentPath, graph, element));
                }
            });

        }
        return longestRoute;
    }


    /**
     * Балда
     * Сложная
     * <p>
     * Задача хоть и не использует граф напрямую, но решение базируется на тех же алгоритмах -
     * поэтому задача присутствует в этом разделе
     * <p>
     * В файле с именем inputName задана матрица из букв в следующем формате
     * (отдельные буквы в ряду разделены пробелами):
     * <p>
     * И Т Ы Н
     * К Р А Н
     * А К В А
     * <p>
     * В аргументе words содержится множество слов для поиска, например,
     * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
     * <p>
     * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
     * и вернуть множество найденных слов. В данном случае:
     * ТРАВА, КРАН, АКВА, НАРТЫ
     * <p>
     * И т Ы Н     И т ы Н
     * К р а Н     К р а н
     * А К в а     А К В А
     * <p>
     * Все слова и буквы -- русские или английские, прописные.
     * В файле буквы разделены пробелами, строки -- переносами строк.
     * Остальные символы ни в файле, ни в словах не допускаются.
     */
    static public Set<String> baldaSearcher(String inputName, Set<String> words) {
        throw new NotImplementedError();
    }
}
