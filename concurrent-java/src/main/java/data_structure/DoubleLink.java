package data_structure;

/**
 * DoubleLink - 模拟 单列的 LinkedHashMap
 */
public class DoubleLink<T> {

    private int size;
    private Node<T> head;// important
                // 作为基本坐标

    private class Node<T> {
    
        private Node<T> prev;
        private Node<T> next;
        private T value;

        public Node(T value, Node<T> prev, Node<T> next) {
            this.next = next;
            this.prev = prev;
            this.value = value;
        }
    }

    public DoubleLink() {
        head = new Node<>(null, null, null);
        head = head.next = head.prev;
        this.size = 0;
    }

    /**
     * insert element to the end
     */
    public void insertAtEnd(T value) {
        Node<T> prev = head.prev;// 前一个节点
        Node<T> next = head; // 后一个节点
        Node<T> node = new Node<T>(value, prev, next);
        prev.next = node;
        next.prev = node;

        size++;
    }

    public void insertAtHead(T value) {
        Node<T> prev = head;
        Node<T> next = head.next;
        Node<T> node = new Node<T>(value, prev, next);
        prev.next = node;
        next.prev = node;

        size++;
    }

    public void insertAt(int index, T value) {
        Node<T> findNode = this.findNode(index);
        Node<T> prev = findNode.prev;
        Node<T> next = findNode.next;
        Node<T> node = new Node<T>(value, prev, next);
        prev.next = node;
        next.prev = node;

        size++;
    }

    public void deleteAt(int index) {
        Node<T> findNode = this.findNode(index);
        Node<T> prev = findNode.prev;
        Node<T> next = findNode.next;
        prev.next = next;
        next.prev = prev;

        findNode = null;
        size--;
    }

    /**
     * find specific node
     * @param index index, start with 0
     * @return
     */
    public Node<T> findNode(int index) {
        Node<T> ret = null;

        if (index < 0) {
            throw new RuntimeException("index can not less than 0");
        }

        // 参考 LinkedHashmap 实现
        if (index <= size / 2) {// 正向
            Node<T> first =  head.next;
            for (int i = 0; i < index; i++) {
                ret = first.next;
            }
            return ret;
        }

        // 反向查找
        Node<T> last = head.prev;
        for (int i = size - 1; i >= index; i--) {
            ret = last.prev;
        }
        return ret;
    }

    public boolean empty() {
        if (size == 0) {
            return true;
        }
        if (size < 0) {
            throw new RuntimeException("size < 0");
        }
        return false;
    }
}