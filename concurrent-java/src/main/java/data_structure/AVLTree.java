package data_structure;

/**
 * 自平衡二叉树
 *
 * @author xiaoyu
 * @since 1.0
 */
public class AVLTree<T extends Comparable<T>> {

    private Node<T> root;

    private class Node<T extends Comparable> {// 没有 parent 成员
        private T       value;
        private Node<T> left;
        private Node<T> right;
        private int     height;

        public Node(T value, Node<T> left, Node<T> right) {
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = 0;
        }
    }

    /**
     * LL：
     * 根节点的左子树的左子树还有非空子节点 ( x 有子节点, 造成左边偏重)
     * <pre>
     *         rn                    l
     *       /  \                  /  \
     *      l    r     -->        x    rn
     *    /  \                   /\   / \
     *   x    y                      y   r
     *  /\
     * </pre>
     *
     * @param rotationNode 围绕旋转的节点
     * @return 旋转后的根节点
     */
    public Node<T> leftLeftRotation(Node<T> rotationNode) {
        Node<T> leftToTop = rotationNode.left;
        rotationNode.left = leftToTop.right;
        leftToTop.right = rotationNode;

        rotationNode.height = max(height(rotationNode.left), height(rotationNode.right)) + 1;
        leftToTop.height = max(height(leftToTop.left), height(leftToTop.right)) + 1;

        return leftToTop;
    }

    /**
     * RR:
     * 根节点的右子树的右子树还有非空子节点(y 有子节点, 造成右边偏重)
     *
     * <pre>
     *         rn                     r
     *        /  \                  /  \
     *      l    r     -->        rn    y
     *          / \              / \   /\
     *         x   y            l   x
     *            /\
     * </pre>
     *
     * @param rotationNode
     * @return
     */
    public Node<T> rightRightRotation(Node<T> rotationNode) {
        Node<T> rightToTop = rotationNode.right;
        rotationNode.right = rightToTop.left;
        rightToTop.left = rotationNode;

        rotationNode.height = max(height(rotationNode.left), height(rotationNode.right)) + 1;
        rightToTop.height = max(height(rightToTop.left), height(rightToTop.right)) + 1;

        return rightToTop;
    }

    /**
     * LR:
     * 根节点的左子树的右子树还有非空子节点
     * 第一次旋转 - RR - 通过局部 RR旋转, 转化为完全的 左左类型
     * 第二次旋转 - LL - LL 旋转
     *
     * <pre>
     *         rn                        rn                          y
     *       /  \                      /  \                        /  \
     *      l    r     -- RR ->       y    r   -- LL -->          l    rn
     *    /  \                      /  \                        / \   / \
     *   x    y                    l    w                      x  z  w   r
     *       / \                 /  \
     *      z   w               x   z
     * </pre>
     *
     * @param rotationNode
     * @return
     */
    public Node<T> leftRightRotation(Node<T> rotationNode) {
        rotationNode.left = this.rightRightRotation(rotationNode.left);
        return leftLeftRotation(rotationNode);
    }

    /**
     * RL:
     * 和 LR 相反
     * @param rotationNode
     * @return
     */
    public Node<T> rightLeftRotation(Node<T> rotationNode) {
        rotationNode.right = this.leftLeftRotation(rotationNode.right);
        return rightRightRotation(rotationNode);
    }

    // ----------------------------------------------------

    /**
     * 插入
     * @param enterNode 根节点
     * @param value 插入值
     * @return
     */
    private Node<T> insert(Node<T> enterNode, T value) {
        if (enterNode == null) {
            enterNode = new Node<>(value, null, null);
        } else {
            int compareValue = value.compareTo(enterNode.value);
            if (compareValue < 0) { // 将 value 插入到 左子树
                enterNode.left = insert(enterNode.left, value);
                // 插入后， 如果失去平衡， 需要旋转
                if (height(enterNode.left) - height(enterNode.right) >= 2) {
                    if (value.compareTo(enterNode.left.value) < 0) {
                        enterNode = leftLeftRotation(enterNode);
                    } else {
                        enterNode = leftRightRotation(enterNode);
                    }
                }
            } else if (compareValue > 0) {// 将value 插入到右子树
                enterNode.right = insert(enterNode.right, value);
                if (height(enterNode.right) - height(enterNode.left) >= 2) {
                    if (value.compareTo(enterNode.right.value) > 0) {
                        enterNode = rightRightRotation(enterNode);
                    } else {
                        enterNode = rightLeftRotation(enterNode);
                    }
                }
            } else {
                throw new RuntimeException("insert failed: cannot insert repeating value");
            }

            //while (true) {
            //    int compareValue = value.compareTo(enterNode.value);
            //    if (compareValue < 0) {
            //        enterNode = enterNode.left;
            //    } else if (compareValue > 0) {
            //        enterNode = enterNode.right;
            //    } else {
            //        throw new RuntimeException("insert failed: cannot insert repeating value");
            //    }
            //    if (enterNode == null) {
            //        break;
            //    }
            //}
        }

        enterNode.height = max(height(enterNode.left), height(enterNode.right)) + 1;
        return enterNode;

    }

    public void insert(T value) {
        root = insert(root, value);
    }

    // ---------------------------------------------------

    /**
     * 删除
     * @param enterNode 根节点
     * @param delete 待删除节点
     * @return 删除操作完成后的根节点
     */
    public Node<T> remove(Node<T> enterNode, Node<T> delete) {
        int compareValue = delete.value.compareTo(enterNode.value);
        if (compareValue < 0) {// 待删除的节点在"tree的左子树"中， 删除后， 右边偏重
            // 进入点往左边移动
            // 删除完后， 原始 enterNode的左指针指向删除完毕后的根节点
            enterNode.left = remove(enterNode.left, delete);

            if (height(enterNode.right) - height(enterNode.left) >= 2) {
                Node<T> r = enterNode.right;
                if (height(r.left) < height(r.right)) {
                    enterNode = rightRightRotation(enterNode);
                } else  {
                    enterNode = rightLeftRotation(enterNode);
                }
            }
        } else if (compareValue > 0) {// 待删除的节点在"tree的右子树"中, 删除后， 左边偏重

            enterNode.right = remove(enterNode.right, delete);

            if (height(enterNode.left) - height(enterNode.right) >= 2) {
                Node<T> l = enterNode.left;
                if (height(l.left) > height(l.right)) {
                    enterNode = leftLeftRotation(enterNode);
                }
                else {
                    enterNode = leftRightRotation(enterNode);
                }
            }

        } else {// enterNode 正是要删除的节点

            if (enterNode.left != null && enterNode.right != null) {// 左右子树都不为空
                if (height(enterNode.left) > height(enterNode.right)) {// 若左子树更高
                    // 找到左子树的最大节点 (enterNode 的 前驱), 代替 enterNode
                    // 此时, AVL树仍然是平衡的
                    Node<T> leftMax = maxNode(enterNode.left);
                    enterNode.value = leftMax.value;
                    enterNode.left = remove(enterNode.left, leftMax);
                } else {// 若右子树更高
                    // 找得到右子树的最小节点(enterNode 的后继), 代替 enterNode
                    Node<T> rightMin = minNode(enterNode.right);
                    enterNode.value = rightMin.value;
                    enterNode.right = remove(enterNode.right, rightMin);
                }
            } else if (enterNode.left == null && enterNode.right == null) {// 没有子树
                // 直接删除
                enterNode = null;
            } else {// 只有一个子树(有一个子树为空)
                // 子节点代替enterNode
                if (enterNode.right != null) {
                    enterNode = enterNode.right;
                } else {
                    enterNode = enterNode.left;
                }
            }
        }

        return enterNode;
    }

    public void remove(T value) {
        Node<T> find = find(root, value);
        if (find == null) {
            throw new RuntimeException("delete error: value ["+value+"] is not exist.");
        }
        root = remove(root, find);
    }

    // ----------------------------------------------------

    /**
     * find
     * @param enterNode 根节点
     * @param value 查找值
     * @return 查找到的节点
     */
    public Node<T> find(Node<T> enterNode, T value) {
        int compare = value.compareTo(enterNode.value);
        if (compare < 0) {
            enterNode =  find(enterNode.left, value);
        } else if (compare > 0) {
            enterNode = find(enterNode.right, value);
        }
        return enterNode;
    }


    private Node<T> maxNode(Node<T> enterNode) {
        Node<T> right = enterNode.right;
        if (right != null) {
            enterNode = maxNode(right);
        }
        return enterNode;
    }

    private Node<T> minNode(Node<T> enterNode) {
        Node<T> left = enterNode.left;
        if (left != null) {
            enterNode = minNode(left);
        }
        return enterNode;
    }

    private int max(int h1, int h2) {
        if (h1 > h2) {
            return h1;
        }
        return h2;
    }

    private int height(Node<T> node) {
        if (node != null) {
            return node.height;
        }
        return 0;
    }

    private int height() {
        return height(root);
    }
}
