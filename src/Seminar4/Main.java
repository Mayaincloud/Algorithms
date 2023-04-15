package Seminar4;

public class Main {
    public static void main(String[] args) {
        BinaryTree<Integer> tree = new BinaryTree<>();
        tree.add(4);
        tree.add(5);
        tree.add(2);
        System.out.println(tree.contains(6));
    }

}