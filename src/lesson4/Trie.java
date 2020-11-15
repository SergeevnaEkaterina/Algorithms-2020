package lesson4;

import java.util.*;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Префиксное дерево для строк
 */
public class Trie extends AbstractSet<String> implements Set<String> {

    private static class Node {
        Map<Character, Node> children = new LinkedHashMap<>();
    }

    private Node root = new Node();

    private int size = 0;

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root.children.clear();
        size = 0;
    }

    private String withZero(String initial) {
        return initial + (char) 0;
    }

    @Nullable
    private Node findNode(String element) {
        Node current = root;
        for (char character : element.toCharArray()) {
            if (current == null) return null;
            current = current.children.get(character);
        }
        return current;
    }

    @Override
    public boolean contains(Object o) {
        String element = (String) o;
        return findNode(withZero(element)) != null;
    }

    @Override
    public boolean add(String element) {
        Node current = root;
        boolean modified = false;
        for (char character : withZero(element).toCharArray()) {
            Node child = current.children.get(character);
            if (child != null) {
                current = child;
            } else {
                modified = true;
                Node newChild = new Node();
                current.children.put(character, newChild);
                current = newChild;
            }
        }
        if (modified) {
            size++;
        }
        return modified;
    }

    @Override
    public boolean remove(Object o) {
        String element = (String) o;
        Node current = findNode(element);
        if (current == null) return false;
        if (current.children.remove((char) 0) != null) {
            size--;
            return true;
        }
        return false;
    }

    /**
     * Итератор для префиксного дерева
     * <p>
     * Спецификация: {@link Iterator} (Ctrl+Click по Iterator)
     * <p>
     * Сложная
     */
    @NotNull
    @Override
    public Iterator<String> iterator() {
        return new IteratorOfTree();
    }
    public class IteratorOfTree implements Iterator<String> {
        private final Stack<String> stack = new Stack<>();
        private String nextWord = "";
        private IteratorOfTree() {
            addElementsToStack(root, "");
        }

        private void addElementsToStack(Node currentNode, String word) {
            if (root != null && !currentNode.children.isEmpty()) {
                Iterator<Map.Entry<Character, Node>> entryIterator = currentNode.children.entrySet().iterator();
                if (entryIterator.hasNext()) {
                    do {  // перебираем элементы map
                        Map.Entry<Character, Node> entry = entryIterator.next();
                        Character elem = entry.getKey();
                        if (elem == 0) {
                            stack.push(word);  // добавляем элемент в стек
                        } else {
                            addElementsToStack(entry.getValue(), word + elem);
                        }
                    } while (entryIterator.hasNext());
                }

            }
        }


        //Трудоемкость = O(1)
        //Ресурсоемкость = O(1)
        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }
        //Трудоемкость = O(1)
        //Ресурсоемкость = O(1)
        @Override
        public String next() {
            if (!hasNext()) throw new IllegalStateException();
            nextWord = stack.pop();
            return nextWord;
        }
        //Трудоемкость = O(N*logN)
        //Ресурсоемкость = O(N*logN)
        @Override
        public void remove() {
            if (nextWord.equals("")) throw new IllegalStateException();
            Trie.this.remove(nextWord);
            nextWord = "";
        }
    }
}