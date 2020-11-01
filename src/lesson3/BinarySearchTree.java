package lesson3;

import java.util.*;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// attention: Comparable is supported but Comparator is not
public class BinarySearchTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {


    private static class Node<T> {
        final T value;
        Node<T> left = null;
        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    /**
     * Добавление элемента в дерево
     * <p>
     * Если элемента нет в множестве, функция добавляет его в дерево и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * <p>
     * Спецификация: {@link Set#add(Object)} (Ctrl+Click по add)
     * <p>
     * Пример
     */
    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    /**
     * Удаление элемента из дерева
     * <p>
     * Если элемент есть в множестве, функция удаляет его из дерева и возвращает true.
     * В ином случае функция оставляет множество нетронутым и возвращает false.
     * Высота дерева не должна увеличиться в результате удаления.
     * <p>
     * Спецификация: {@link Set#remove(Object)} (Ctrl+Click по remove)
     * <p>
     * Средняя
     */
    //Трудоёмкость = O(h), где h - высота дерева; O(h) = O (logN) в среднем случае, где N - количество узлов. В среднем случае h = logN, в худшем h = N (если дерево несбалансировано)
    //Ресурсоёмкость = O(1) = const
    @Override
    public boolean remove(Object o) {
        if (root == null) return false;
        T removeValue = (T) o; // приведение о к типу Т, removeValue - значение удаляемого узла
        Node<T> currentNode = root;
        Node<T> parentNode = root;


        while (currentNode.value != null && currentNode.value != removeValue) {      // ищем местонахождение удаляемого узла
            parentNode = currentNode;
            if (removeValue.compareTo(currentNode.value) < 0) {  // сравниваем значение удаляемого с текущим: если меньше текущего - движемся влево
                currentNode = currentNode.left;
            } else {                                      // если больше - движемся вправо
                currentNode = currentNode.right;          // теперь удаляемый узел - это currentNode
            }
            if (currentNode == null)  // элемента нет в множестве
                return false;
        }

        //Случай 1: у удаляемого нет потомков (лист), в этом случае безболезненно удаляем этот лист
        if (currentNode.right == null && currentNode.left == null) {
            replace(currentNode, parentNode);
        }

        //Случай 2: у удаляемого 1 потомок, в этом случае заменяем удаляемый узел его единственным потомком

        else if (currentNode.right == null) {   // Случай 2.1: есть левый потомок
            replace(currentNode, parentNode, currentNode.left);
        } else if (currentNode.left == null) {   // Случай 2.1: есть правый потомок
            replace(currentNode, parentNode, currentNode.right);
        }


        //Случай 3: у удаляемого есть правый и левый потомок, в этом случае место удаляемого узла занимает левый лист из правого поддерева от удаляемого(value следующее после удаляемого).
        // То есть на месте удаляемого оказывается узел, меньший любого правого узла -> наименьшее значение (левый лист) в правом поддереве

        else {
            Node<T> leafParent = currentNode;
            Node<T> leaf = currentNode.right; //идем в правое поддерево

            while (leaf.left != null) {  //движемся к левому листу - > след.элементу по значению после удаляемого

                leafParent = leaf;
                leaf = leaf.left;
            }

            if (leaf != currentNode.right) {
                leafParent.left = leaf.right;  //вместо листа -> правый лист или null, если его нет
                leaf.right = currentNode.right;   // присваиваем найденному листу потомков удаляемого узла ( currentNode)
            }
            leaf.left = currentNode.left;
            replace(currentNode, parentNode, leaf);

        }

        size--;
        return true;


    }

    public void replace(Node<T> current, Node<T> currentParent, Node<T> replace) {
        if (current == root) {
            root = replace;
        } else if (currentParent.left == current) {
            currentParent.left = replace;
        } else {
            currentParent.right = replace;
        }
    }

    public void replace(Node<T> current, Node<T> currentParent) {
        if (currentParent.left == current) {
            currentParent.left = null;
        } else {
            currentParent.right = null;
        }

    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }


    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinarySearchTreeIterator();
    }

    public class BinarySearchTreeIterator implements Iterator<T> {
        private Stack<Node<T>> stack = new Stack<>(); //LIFO
        private T currentValue;

        private BinarySearchTreeIterator() {
            Node<T> node = root;
            while (node != null) {  // помещаем в стек узлы от корня до левого листа
                stack.push(node);
                node = node.left;
            }
        }


        /**
         * Проверка наличия следующего элемента
         * <p>
         * Функция возвращает true, если итерация по множеству ещё не окончена (то есть, если вызов next() вернёт
         * следующий элемент множества, а не бросит исключение); иначе возвращает false.
         * <p>
         * Спецификация: {@link Iterator#hasNext()} (Ctrl+Click по hasNext)
         * <p>
         * Средняя
         */
        //Трудоёмкость = O(1) = const
        //Ресурсоёмкость = O(1) = const
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        /**
         * Получение следующего элемента
         * <p>
         * Функция возвращает следующий элемент множества.
         * Так как BinarySearchTree реализует интерфейс SortedSet, последовательные
         * вызовы next() должны возвращать элементы в порядке возрастания.
         * <p>
         * Бросает NoSuchElementException, если все элементы уже были возвращены.
         * <p>
         * Спецификация: {@link Iterator#next()} (Ctrl+Click по next)
         * <p>
         * Средняя
         */
        //Трудоёмкость = O(N)
        //Ресурсоёмкость = O(1)
        @Override
        public T next() {

            Node<T> node = stack.pop();
            currentValue = node.value;
            if (node.right != null) {
                node = node.right;     // идем в правое поддерево -> больших элементов
                while (node != null) {  // следующий по значению элемент = левый лист
                    stack.push(node);
                    node = node.left;
                }
            }
            if (currentValue == null) throw new NoSuchElementException(); // если все элементы были возвращены
            return currentValue;

        }

        /**
         * Удаление предыдущего элемента
         * <p>
         * Функция удаляет из множества элемент, возвращённый крайним вызовом функции next().
         * <p>
         * Бросает IllegalStateException, если функция была вызвана до первого вызова next() или же была вызвана
         * более одного раза после любого вызова next().
         * <p>
         * Спецификация: {@link Iterator#remove()} (Ctrl+Click по remove)
         * <p>
         * Сложная
         */
        //Трудоёмкость = O(h), где h - высота дерева; O(h) = O (logN) в среднем случае, где N - количество узлов. В среднем случае h = logN, в худшем h = N (если дерево несбалансировано)
        //Ресурсоёмкость = O(1) = const
        @Override
        public void remove() {

            if (currentValue == null) {
                throw new IllegalStateException();
            }
            BinarySearchTree.this.remove(currentValue);
            currentValue = null;
        }

    }

    /**
     * Подмножество всех элементов в диапазоне [fromElement, toElement)
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева, которые
     * больше или равны fromElement и строго меньше toElement.
     * При равенстве fromElement и toElement возвращается пустое множество.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#subSet(Object, Object)} (Ctrl+Click по subSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Очень сложная (в том случае, если спецификация реализуется в полном объёме)
     */
    // Трудоемкость = O(1)
    //Ресурсоемкость = O(1)
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return new subTree(this,
                fromElement, toElement, false, false);
    }

    /**
     * Подмножество всех элементов строго меньше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева строго меньше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#headSet(Object)} (Ctrl+Click по headSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    // Трудоемкость = O(1)
    //Ресурсоемкость = O(1)
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        return new subTree(this,
                null, toElement, true, false);
    }

    /**
     * Подмножество всех элементов нестрого больше заданного
     * <p>
     * Функция возвращает множество, содержащее в себе все элементы дерева нестрого больше toElement.
     * Изменения в дереве должны отображаться в полученном подмножестве, и наоборот.
     * <p>
     * При попытке добавить в подмножество элемент за пределами указанного диапазона
     * должен быть брошен IllegalArgumentException.
     * <p>
     * Спецификация: {@link SortedSet#tailSet(Object)} (Ctrl+Click по tailSet)
     * (настоятельно рекомендуется прочитать и понять спецификацию перед выполнением задачи)
     * <p>
     * Сложная
     */
    // Трудоемкость = O(1)
    //Ресурсоемкость = O(1)
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return new subTree(this, fromElement, null, false, true);
    }

    public class subTree extends BinarySearchTree<T> {

        private BinarySearchTree<T> tree;
        private T fromElement, toElement;
        private boolean isStartAvailable, isEndAvailable;

        subTree(BinarySearchTree<T> tree,
                T fromElement, T toElement, boolean isStartAvailable, boolean isEndAvailable) {
            this.tree = tree;
            this.fromElement = fromElement;
            this.toElement = toElement;
            this.isStartAvailable = isStartAvailable;
            this.isEndAvailable = isEndAvailable;

        }

        private boolean isInRange(T item) {                         //проверка, входит ли элемент в заданный диапазон
            return (fromElement == null && item.compareTo(toElement) < 0) || (toElement == null && item.compareTo(fromElement) >= 0)
                    || (fromElement != null && toElement != null && item.compareTo(fromElement) >= 0 && item.compareTo(toElement) < 0);
        }

        @Override
        public boolean add(T t) {
            if (!isInRange(t)) throw new IllegalArgumentException();
            return isInRange(t) && tree.add(t);
        }

        @Override
        public boolean remove(Object o) {
            T t = (T) o;
            if (!isInRange(t)) throw new IllegalArgumentException();
            return isInRange(t) && tree.remove(o);
        }

        @Override
        public boolean contains(Object o) {
            T t = (T) o;
            return isInRange(t) && tree.contains(o);
        }

        @Override
        public Iterator<T> iterator() {
            return null;
        }

        @Override
        public int size() {
            long result = BinarySearchTree.this.stream().filter(t -> isInRange((T) t)).count();
            return (int) result;
        }

        @Nullable
        @Override
        public Comparator<? super T> comparator() {
            return null;
        }


        @Override
        public T first() {
            if (size() == 0) throw new NoSuchElementException();
            if (fromElement == null) {
                return tree.first();
            } else if (isEndAvailable) {
                return fromElement;
            } else {
                Iterator<T> it = tree.iterator();
                T curr = null;
                while (it.hasNext()) {
                    curr = it.next();
                    if (curr.compareTo(fromElement) >= 0) {
                        break;
                    }
                }
                return curr;
            }
        }

        @Override
        public T last() {
            if (size() == 0) throw new NoSuchElementException();
            if (toElement == null) {
                return tree.last();
            } else {
                Iterator<T> it = tree.iterator();
                T curr;
                T last = null;
                while (it.hasNext()) {
                    curr = it.next();
                    if (curr.compareTo(toElement) >= 0) {
                        break;
                    }
                    last = curr;
                }
                return last;
            }
        }


    }


    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }

    public int height() {
        return height(root);
    }

    private int height(Node<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

}