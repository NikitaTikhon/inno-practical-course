package com;

import java.util.NoSuchElementException;

public class CustomLinkedList<E> {

    private Node<E> first;
    private Node<E> last;

    private int size;

    private static class Node<E> {
        E item;
        Node<E> next;
        Node<E> prev;

        private Node(E item, Node<E> next, Node<E> prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    public int size() {
        return this.size;
    }

    public void addFirst(E el) {
        Node<E> first = this.first;

        Node<E> newNode = new Node<>(el, first, null);
        this.first = newNode;
        if (first == null) {
            this.last = newNode;
        } else {
            first.prev = newNode;
        }

        size++;
    }

    public void addLast(E el) {
        Node<E> last = this.last;

        Node<E> newNode = new Node<>(el, null, last);
        this.last = newNode;
        if (last == null) {
            this.first = newNode;
        } else {
            last.next = newNode;
        }

        size++;
    }

    public void add(int index, E el) {
        if (index < 0 || index > size)
            throw new IndexOutOfBoundsException("Index %d, Size: %d".formatted(index, size));

        if (size == index) {
            addLast(el);
        } else {
            Node<E> node = node(index);
            Node<E> prev = node.prev;

            Node<E> newNode = new Node<>(el, node, prev);
            node.prev = newNode;
            if (prev == null) {
                first = newNode;
            } else {
                prev.next = newNode;
            }

            size++;
        }
    }

    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index %d, Size: %d".formatted(index, size));

        Node<E> node = node(index);
        return node.item;
    }

    private Node<E> node(int index) {
        Node<E> node;
        if (size / 2 > index) {
            node = this.first;

            for (int i = 0; i < index; i++) {
                node = node.next;
            }
        } else {
            node = this.last;

            for (int i = size - 1; i > index; i--) {
                node = node.prev;
            }
        }

        return node;
    }

    public E getFirst() {
        Node<E> first = this.first;
        if (first == null)
            throw new NoSuchElementException();

        return first.item;
    }

    public E getLast() {
        Node<E> last = this.last;
        if (last == null)
            throw new NoSuchElementException();

        return last.item;
    }

    public E removeFirst() {
        Node<E> first = this.first;

        if (first == null)
            throw new NoSuchElementException();

        Node<E> next = first.next;
        this.first = next;
        if (next != null) {
            next.prev = null;
        } else {
            this.last = null;
        }

        size--;
        return first.item;
    }

    public E removeLast() {
        Node<E> last = this.last;

        if (last == null)
            throw new NoSuchElementException();

        Node<E> prev = last.prev;
        this.last = prev;
        if (prev != null) {
            prev.next = null;
        } else {
            this.first = null;
        }

        size--;
        return last.item;
    }

    public E remove(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index %d, Size: %d".formatted(index, size));

        if (index == 0) {
            return removeFirst();
        } else if (index == size - 1) {
            return removeLast();
        }

        Node<E> node = node(index);
        Node<E> prev = node.prev;
        Node<E> next = node.next;

        prev.next = node.next;
        next.prev = node.prev;

        return node.item;
    }

}
