package com.zuu.chatroom;

import org.junit.jupiter.api.Test;

/**
 * @Author zuu
 * @Description
 * @Date 2024/7/9 23:13
 */
public class SimpleTest {
    class MyLinkedList {
        class Node{
            int val;
            Node next;
            Node(){}
            Node(int val){this.val = val;}
            Node(int val,Node next){
                this.val = val;
                this.next = next;
            }
        }
        Node head,tail;
        public MyLinkedList() {
            head = new Node();
            head.next = null;
            tail = head;
        }

        public int get(int index) {
            Node p = head.next;
            int i = 0;
            while(p != null){
                if(i == index)
                    return p.val;
                p = p.next;
                i++;
            }
            return -1;
        }

        public void addAtHead(int val) {
            Node insert = new Node(val);
            if(tail == head)
                tail = insert;
            insert.next = head.next;
            head.next = insert;
        }

        public void addAtTail(int val) {
            Node insert = new Node(val);
            insert.next = null;
            tail.next = insert;
            tail = insert;
        }

        public void addAtIndex(int index, int val) {
            //找到index前面的元素
            Node pre = head.next;
            int i = 0;
            while(pre != null){
                if(i == index-1)
                    break;
                i++;
                pre = pre.next;
            }
            if(i != index-1)
                return;
            if(pre == tail)
                addAtTail(val);
            Node insert = new Node(val);
            insert.next = pre.next;
            pre.next = insert;
        }

        public void deleteAtIndex(int index) {
            //找到index前面的元素
            Node pre = head.next;
            int i = 0;
            while(pre != null){
                if(i == index-1)
                    break;
                i++;
                pre = pre.next;
            }
            if(i != index-1 || pre == null || pre.next == null)
                return;
            pre.next = pre.next.next;
        }
    }
    @Test
    public void aaa() throws InterruptedException {
        MyLinkedList myLinkedList = new MyLinkedList();
//        ["MyLinkedList","addAtHead","addAtHead","addAtHead","addAtIndex","deleteAtIndex","addAtHead","addAtTail","get","addAtHead","addAtIndex","addAtHead"]
//[[],[7],[2],[1],[3,0],[2],[6],[4],[4],[4],[5,0],[6]]

        myLinkedList.addAtHead(7);
        myLinkedList.addAtHead(2);
        myLinkedList.addAtHead(1);
        myLinkedList.addAtIndex(3,0);
        myLinkedList.deleteAtIndex(2);
        myLinkedList.addAtHead(6);
        myLinkedList.addAtTail(4);
        System.out.println(myLinkedList.get(4));//6 1 2 0 4
    }

}
