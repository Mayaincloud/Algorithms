
    // Узел двусвязного списка
    class Node
    {
        int data;
        Node next, prev;
    }

    class Reverse
    {
        // Вспомогательная функция для помещения узла в начало двусвязного списка
        public static Node push(Node head, int key)
        {
            Node node = new Node();
            node.data = key;
            node.prev = null;
            node.next = head;

            // изменить `prev` существующего головного узла, чтобы он указывал на новый узел
            if (head != null) {
                head.prev = node;
            }

            // обновить указатель заголовка и вернуться
            head = node;
            return head;
        }

        // Вспомогательная функция для вывода узлов двусвязного списка
        public static void printDDL(String msg, Node head)
        {
            System.out.print(msg);
            while (head != null)
            {
                System.out.print(head.data + " —> ");
                head = head.next;
            }

            System.out.println("null");
        }

        // Функция для замены указателей `next` и `prev` данного узла
        public static void swap(Node node)
        {
            Node prev = node.prev;
            node.prev = node.next;
            node.next = prev;
        }

        // Функция для реверсирования двусвязного списка
        public static Node reverseDDL(Node head)
        {
            Node prev = null;
            Node curr = head;

            // обход списка
            while (curr != null)
            {
                // поменять местами указатели `next` и `prev` для текущего узла
                swap(curr);

                // обновить предыдущий узел перед переходом к следующему узлу
                prev = curr;

                // перейти к следующему узлу в двусвязном списке (перейти с помощью
                // указатель `prev`, так как указатели `next` и `prev` поменялись местами)
                curr = curr.prev;
            }

            // обновить указатель на последний узел
            if (prev != null) {
                head = prev;
            }

            return head;
        }

        public static void main(String[] args)
        {
            int[] keys = { 1, 2, 3, 4, 5 };

            Node head = null;
            for (int key: keys) {
                head = push(head, key);
            }

            printDDL("Original list: ", head);
            head = reverseDDL(head);
            printDDL("Reversed list: ", head);
        }
    }

