package data_structure;

import java.text.MessageFormat;

/**
 * BST tree
 *
 * @author xiaoyu
 * @since 1.0
 */
public class BinarySearchTree<T extends Comparable<T>> {

    private Node<T> root;// root node

    public class Node<T extends Comparable<T>> {
        private T       value;
        private Node<T> parent;
        private Node<T> left;
        private Node<T> right;

        public Node(T value, Node<T> parent, Node<T> left, Node<T> right) {
            this.value = value;
            this.parent = parent;
            this.left = left;
            this.right = right;
        }
    }

    public BinarySearchTree(T value) {
        root = new Node<>(value, null, null, null);
    }

    // ------------------------------------------------

    /**
     * 前序遍历 - parent节点排最先，然后同级先左后右
     *
     * @param node
     */
    public void preOrderTraverse(Node<T> node) {
        if (node == null) {
            throw new RuntimeException("can not traverse null node");
        }
        System.out.print(node.value + " ");// print current node's value
        preOrderTraverse(node.left);   //
        preOrderTraverse(node.right);
    }

    /**
     * 中序遍历 - 先左后parent最后右
     *
     * @param node
     */
    public void middleOrderTraverse(Node<T> node) {
        if (node == null) {
            throw new RuntimeException("cannot traverse null node");
        }
        middleOrderTraverse(node.left);
        System.out.print(node.value + " ");
        middleOrderTraverse(node.right);
    }

    /**
     * 后序遍历 - 先左后右最后parent
     *
     * @param node
     */
    public void postOrderTraverse(Node<T> node) {
        if (node == null) {
            throw new RuntimeException("cannot traverse null node");
        }
        postOrderTraverse(node.left);
        postOrderTraverse(node.right);
        System.out.println(node.value + " ");
    }

    // ------------------------------------------------------

    /**
     * 使用递归查找
     *
     * @param enterNode
     * @param value
     * @return
     */
    public Node<T> find(Node<T> enterNode, T value) {
        if (enterNode == null) {
            throw new RuntimeException("cannot find with null enterNode");
        }

        int tmp = value.compareTo(enterNode.value);
        if (tmp == 0) {
            return enterNode;
        } else if (tmp < 0) {  // value < enterNode.value
            return find(enterNode.left, value);
        } else {
            return find(enterNode.right, value);
        }
    }

    /**
     * 没有递归的查找
     *
     * @param enterNode
     * @param value
     * @return
     */
    public Node<T> find_2(Node<T> enterNode, T value) {
        if (enterNode == null) {
            throw new RuntimeException("cannot find with null enterNode");
        }
        while (enterNode != null) {// 如果 enterNode == null 了， 证明循环
            // 到叶子节点了， 跳出, 返回null
            int tmp = value.compareTo(enterNode.value);
            if (tmp == 0) {
                return enterNode;
            } else if (tmp < 0) {
                enterNode = enterNode.left;
            } else {
                enterNode = enterNode.right;
            }
        }
        return null;
    }

    public Node<T> find(T value) {
        return this.find(root, value);
    }

    // -------------------------------------------------------------

    /**
     * 最大值
     *
     * @param enterNode
     * @return
     */
    public Node<T> maxNode(Node<T> enterNode) {
        while (true) {
            Node<T> right = enterNode.right;
            if (right == null) { // 证明此时enterNode 是叶子节点了
                return enterNode;
            }
            enterNode = right;
        }
    }

    /**
     * 最小值
     *
     * @param enterNode
     * @return
     */
    public Node<T> minNode(Node<T> enterNode) {
        while (true) {
            Node<T> left = enterNode.left;
            if (left == null) {
                return enterNode;
            }
            enterNode = left;
        }
    }

    // ------------------------------------

    /**
     * 节点的前驱：是该节点的左子树中的最大节点。即比当前节点小的最大节点
     *
     * @param enterNode
     * @return
     */
    public Node<T> predecessor(Node<T> enterNode) {
        Node<T> left = enterNode.left;
        // enterNode 存在 left node
        //        p
        //      /  \
        //     *    *
        //      \
        //      ...
        //       \
        //        o
        //    - 那么只需要 以这个left node 为 根节点， 找到最大值即可, 即这里的 o
        if (left != null) {
            return this.maxNode(left);
        }

        // 如果 enterNode 没有 left node，分两种情况
        // 1. enterNode 自身是一个 right node
        //        o
        //      /  \
        //     *    p
        //          \
        //     - 找到 parent, 即这里的 o， 返回parent (这个 parent 就是比 enterNode 小的最大节点)
        // 2. enterNode 自身是一个left node
        //      o
        //       \
        //        *
        //      /  \
        //     p    *
        //     \
        //  - 则查找"enterNode的最低的父结点，并且该父结点要具有右孩子", 即这里的 o，找
        //        到的这个"最低的父结点"就是"x的前驱结点"
        Node<T> parent = enterNode.parent;
        if (parent == null) {// enterNode is root
            return null;
        }
        if (enterNode == parent.right) {
            return parent;
        }
        if (enterNode == parent.left) {
            // 循环找 parent
            while (true) {
                enterNode = parent; // enterNode 指针移动到 parent
                parent = enterNode.parent; // parent 指针移动到 新的 parent
                if (enterNode == parent.right) {
                    return parent;
                }
            }
        }
        throw new RuntimeException(MessageFormat.format(
                "子节点{0} 和 父节点 {1} 断开了", enterNode, parent));
    }

    /**
     * 节点的后继：是该节点的右子树中的最小节点， 即比当前节点大的最小节点
     *
     * @param enterNode
     * @return
     */
    public Node<T> successor(Node<T> enterNode) {
        // 如果存在 right node，则返回 以这个 right node 为子树的最小节点
        Node<T> right = enterNode.right;
        if (right != null) {
            return minNode(right);
        }

        Node<T> parent = enterNode.parent;
        if (parent == null) {
            return null;
        }
        if (enterNode == parent.left) {
            return parent;
        }
        if (enterNode == parent.right) {
            while (true) {
                enterNode = parent;
                parent = enterNode.parent;
                if (enterNode == parent.left) {
                    return parent;
                }
            }
        }
        throw new RuntimeException(MessageFormat.format(
                "子节点{0} 和 父节点 {1} 断开了", enterNode, parent));
    }

    // ----------------------------------------------

    /**
     * insert node into the tree
     *
     * @param node 待插入节点
     */
    public void insert(Node<T> node) {
        Node<T> enterPoint = this.root;
        while (true) {
            Node<T> enterPointTmp = enterPoint;// 暂存指针移动前的 enterPoint

            int tmp = node.value.compareTo(enterPoint.value);
            if (tmp == 0) {
                throw new RuntimeException("value already exist");
            } else if (tmp < 0) {
                enterPoint = enterPoint.left;
            } else {
                enterPoint = enterPoint.right;
            }

            if (enterPoint == null) {
                enterPoint = enterPointTmp;
                break;
            }
        }

        int tmp = node.value.compareTo(enterPoint.value);
        if (tmp == 0) {
            throw new RuntimeException("value already exist");
        } else if (tmp < 0) {
            enterPoint.left = node;
        } else {
            enterPoint.right = node;
        }

    }

    // ------------------------------------------

    /**
     * delete specific node
     *
     * @param node
     */
    public void delete(Node<T> node) {
        // 若 node 为 leaf节点，直接删除
        //       *           *
        //      /             \
        //     p               p
        // 若 node 为 "只有一个子节点", 这个子节点为 singleSon, 那么用 singleSon 代替 node 即可
        //       p       p
        //      /         \
        //     *           *
        // 若 node 为 "有两个子节点",   需要找node的后继节点successor 或者 前驱 predecessor, 替换 node, 分 2 种情况
        //      1. successor 为 node 的直接子节点(即右节点)
        //         p
        //        / \
        //       *   su
        //          - 直接用 successor 替代 node
        //     2. successor 不为 node 的直接子节点(即不是右节点)
        //         p
        //        / \
        //       *   *
        //          /
        //         su
        //          - 用 successor 替换 node
        // # 为什么要找 successor代替node? - 想要用 o 代替 node , o 需要 满足 node.left < o (node) < node.right
        //      而 successor, predecessor的位置满足 node.left < predecessor < o (node) < successor < node.right
        Node<T> left   = node.left;
        Node<T> right  = node.right;
        Node<T> parent = node.parent;

        if (left == null && right == null) {// 没有子节点
            if (parent.left == node) {
                parent.left = null;
            }
            if (parent.right == null) {
                parent.right = null;
            }
        }

        if (left != null && right != null) {// 有两个子节点
            Node<T> successor = this.successor(node);
            if (parent.left == node) {
                parent.left = successor;
            }
            if (parent.right == node) {
                parent.right = successor;
            }
            successor.parent = parent;

            left.parent = successor;
            successor.left = left;

            if (right != successor) {
                right.parent = successor;
                successor.right = right;
            }

        }

        // 只有一个子节点
        Node<T> son = null;
        if (left != null) {
            son = left;
        }
        if (right != null) {
            son = right;
        }
        if (parent.left == node) {
            parent.left = son;
        }
        if (parent.right == node) {
            parent.right = son;
        }

        node.parent = node.left = node.right = null;// 删除node
    }

    // -----------------------------------------

    /**
     * destroy tree
     */
    public void destroy(Node<T> enterNode) {
        if (enterNode == null) {
            return;
        }
        Node<T> left = enterNode.left;
        if (left != null) {
            destroy(left);
        }
        Node<T> right = enterNode.right;
        if (right != null) {
            destroy(right);
        }
        enterNode = null;
    }

    public void destroy() {
        destroy(root);
    }

}
