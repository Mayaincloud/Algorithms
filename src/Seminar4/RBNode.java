package Seminar4;

class RBNode<T extends Comparable<T>> {

    public static boolean RED = true;
    public static boolean BLACK = false;

    //элемент поумолчанию красный
    public boolean color = RED;
    public RBNode<T> left;
    public RBNode<T> right;
    public RBNode<T> parent;
    public T data;

    public RBNode(T data) {
        this.data = data;
    }


    public void removeFromParent() {
        if (parent == null)
            return;


        if (parent.left == this)
            parent.left = null;
        else if (parent.right == this)
            parent.right = null;

        //у самого элемента удалим ссылку на родителя
        this.parent = null;
    }


    public void setLeft(RBNode<T> child) {


        if (left != null)
            left.parent = null;

        if (child != null) {
            child.removeFromParent();
            child.parent = this;
        }

        this.left = child;
    }

    public void setRight(RBNode<T> child) {
        if (right != null) {
            right.parent = null;
        }

        if (child != null) {
            child.removeFromParent();
            child.parent = this;
        }

        this.right = child;
    }

    public static boolean isRed(RBNode<?> node) {
        return getColor(node) == RED;
    }

    public static boolean isBlack(RBNode<?> node) {
        return !isRed(node);
    }

    public static void setColor(RBNode<?> node, boolean color) {
        if (node == null)
            return;
        node.color = color;
    }

    public static boolean getColor(RBNode<?> node) {

        return node == null ? BLACK : node.color;
    }

    public static void swapColor(RBNode<?> node) {
        if (node == null)
            return;

        node.color = !node.color;
    }

    public RBNode<T> getGrandparent() {
        return (parent == null) ? null : parent.parent;
    }

    public static RBNode<?> getGrandparent(RBNode<?> node) {
        return (node == null) ? null : node.getGrandparent();
    }

    //брат
    public RBNode<T> getSibling() {
        if (parent != null) { // No sibling of root node
            if (this == parent.right)
                return (RBNode<T>) parent.left;
            else
                return (RBNode<T>) parent.right;
        }
        return null;
    }

    public static RBNode<?> getSibling(RBNode<?> node) {
        return (node == null) ? null : node.getSibling();
    }

    public RBNode<T> getUncle() {
        if (parent != null) { // No uncle of root
            return parent.getSibling();
        }
        return null;
    }


    public static RBNode<?> getUncle(RBNode<?> node) {
        return (node == null) ? null : node.getUncle();
    }

}

//-----------------------------------------

class RBTree<T extends Comparable<T>> {
    protected RBNode<T> root;
    protected int size = 0;

    public void insert(T item) {
        if (this.root == null) {
            this.root = new RBNode<T>(item);
        } else {
            this.insert(this.root, item);
        }
        //корень дерева всегда черный
        this.root.color = RBNode.BLACK;
        ++this.size;
    }

    private void insert(RBNode<T> node, T item) {
        //если элемент уже есть, то пропускаем
        if (node.data.compareTo(item) == 0) {
            return;
        }
        //если вставляемый элемент меньше текущего - идем налево
        if (node.data.compareTo(item) > 0) {
            //доходим до листов дерева
            if (node.left != null) {
                this.insert(node.left, item);
            } else {
                RBNode<T> inserted = new RBNode<T>(item);
                node.setLeft(inserted);
                this.balanceAfterInsert(inserted);
            }
        } else if (node.right != null) {
            this.insert(node.right, item);
        } else {
            //создаем новый элемент
            RBNode<T> inserted = new RBNode<T>(item);
            node.setRight(inserted);
            //после вставки балансируем дерево, чтобы выполнились условия RBTree
            this.balanceAfterInsert(inserted);
        }
    }

    private void balanceAfterInsert(RBNode<T> node) {
        //если вставляем корень или родитель черный, то балансировка не нужна
        if (node == null || node == this.root || RBNode.isBlack(node.parent)) {
            return;
        }
        //если брат дедушки красный
        if (RBNode.isRed(node.getUncle())) {
            //меняем цвета:
            RBNode.swapColor(node.parent);
            RBNode.swapColor(node.getUncle());
            RBNode.swapColor(node.getGrandparent());

            this.balanceAfterInsert(node.getGrandparent());
        } else if (node.getGrandparent().left == node.parent) {
            //если вставка в левую ветвь
            if (node.parent.right == node) {
                //если вставка в правую вевь
                node = node.parent;
                //поворачиваем налево
                this.rotateLeft(node);
            }
            RBNode.setColor(node.parent, RBNode.BLACK);
            RBNode.setColor(node.getGrandparent(), RBNode.RED);
            this.rotateRight(node.getGrandparent());
        } else if (node.getGrandparent().right == node.parent ) {
            //this.hasRightParent(node)
            //this.isLeftChild(node)
            if (node.parent.left == node) {
                node = node.parent;
                this.rotateRight(node);
            }
            RBNode.setColor(node.parent, RBNode.BLACK);
            RBNode.setColor(node.getGrandparent(), RBNode.RED);
            this.rotateLeft(node.getGrandparent());
        }
    }

    private void rotateRight(RBNode<T> node) {
        if (node.left == null) {
            return;
        }
        RBNode<T> leftTree = node.left;
        node.setLeft(leftTree.right);
        if (node.parent == null) {
            this.root = leftTree;
        } else if (node.parent.left == node) {
            node.parent.setLeft(leftTree);
        } else {
            node.parent.setRight(leftTree);
        }
        leftTree.setRight(node);
    }

    private void rotateLeft(RBNode<T> node) {
        //если нет правой ноды, то выходим
        if (node.right == null) {
            return;
        }
        //копируем правую часть
        RBNode<T> rightTree = node.right;
        node.setRight(rightTree.left);
        if (node.parent == null) {
            this.root = rightTree;
        } else if (node.parent.left == node) {
            node.parent.setLeft(rightTree);
        } else {
            node.parent.setRight(rightTree);
        }
        rightTree.setLeft(node);
    }

    //-----

    public void inOrder() {
        this.inOrder(this.root);
    }

    private void inOrder(RBNode<T> node) {
        if (node != null) {
            this.inOrder(node.left);
            for(int i = 0; i < getDepth(node); i++) {
                System.out.print(".");
            }
            System.out.println(" " + node.data);
            this.inOrder(node.right);
        }
    }

    public RBNode<T> find(T data) {
        return this.find(this.root, data);
    }

    private RBNode<T> find(RBNode<T> root, T data) {
        if (root == null) {
            return null;
        }
        if (root.data.compareTo(data) > 0) {
            return this.find(root.left, data);
        }
        if (root.data.compareTo(data) < 0) {
            return this.find(root.right, data);
        }
        return root;
    }

    public int getDepth() {
        return this.getDepth(this.root);
    }

    private int getDepth(RBNode<T> node) {
        if (node != null) {
            int right_depth;
            int left_depth = this.getDepth(node.left);
            return left_depth > (right_depth = this.getDepth(node.right)) ? left_depth + 1 : right_depth + 1;
        }
        return 0;
    }

    public static void main(String[] args) {
        RBTree<Integer> rb = new RBTree<Integer>();
        rb.insert( 24 );
        rb.insert( 5 );
        rb.insert( 1 );
        rb.insert( 15 );
       // rb.insert( 3 );
       // rb.insert( 8 );


        rb.inOrder();
    }
}