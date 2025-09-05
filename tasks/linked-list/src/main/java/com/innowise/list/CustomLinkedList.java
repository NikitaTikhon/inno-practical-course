package com.innowise.list;

import java.util.NoSuchElementException;

/**
 * A custom implementation of a linked list.
 *
 * @param <E> the type of elements in this list
 */
public class CustomLinkedList<E> {

    /**
     * Pointer to first node.
     */
    private Node<E> first;

    /**
     * Pointer to last node.
     */
    private Node<E> last;

    /**
     * Size of the list
     */
    private int size;

    /**
     * Represents a node in the doubly linked list.
     * Each node contains an element and references to the next and previous nodes.
     *
     * @param <E> the type of the element held by this node
     */
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

    /**
     * Returns the number of elements in this list.
     *
     * @return the number of elements in this list
     */
    public int size() {
        return this.size;
    }

    /**
     * Inserts the specified element at the beginning of this list.
     *
     * @param el the element to add
     */
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

    /**
     * Appends the specified element to the end of this list.
     *
     * @param el the element to add
     */
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

    /**
     * Inserts the specified element at the specified position in this list.
     * Shifts the element currently at that position (if any) and any subsequent
     * elements to the right (adds one to their indices).
     *
     * @param index the index at which the specified element is to be inserted
     * @param el    the element to be inserted
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index > size()})
     */
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

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
     */
    public E get(int index) {
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("Index %d, Size: %d".formatted(index, size));

        Node<E> node = node(index);
        return node.item;
    }

    /**
     * Retrieves the node at the specified index. This method optimizes
     * access by starting the search from either the beginning or the end
     * of the list, depending on which is closer to the index.
     *
     * @param index the index of the node to retrieve
     * @return the node at the specified index
     */
    private Node<E> node(int index) {
        Node<E> node;
        if (size / 2 >= index) {
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

    /**
     * Returns the first element in this list.
     *
     * @return the first element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getFirst() {
        Node<E> first = this.first;
        if (first == null)
            throw new NoSuchElementException();

        return first.item;
    }

    /**
     * Returns the last element in this list.
     *
     * @return the last element in this list
     * @throws NoSuchElementException if this list is empty
     */
    public E getLast() {
        Node<E> last = this.last;
        if (last == null)
            throw new NoSuchElementException();

        return last.item;
    }

    /**
     * Removes and returns the first element from this list.
     *
     * @return the first element from this list
     * @throws NoSuchElementException if this list is empty
     */
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

    /**
     * Removes and returns the last element from this list.
     *
     * @return the last element from this list
     * @throws NoSuchElementException if this list is empty
     */
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

    /**
     * Removes the element at the specified position in this list.
     * Shifts any subsequent elements to the left (subtracts one from their indices).
     *
     * @param index the index of the element to be removed
     * @return the element that was removed from the list
     * @throws IndexOutOfBoundsException if the index is out of range ({@code index < 0 || index >= size()})
     */
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
