/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fxproject.Vendors;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 *
 * @author vixx_
 */
public class Reversed<T> implements Iterable<T> {

    // https://stackoverflow.com/questions/1098117/can-one-do-a-for-each-loop-in-java-in-reverse-order
    private final List<T> original;

    public Reversed(List<T> original) {
        this.original = original;
    }

    public Iterator<T> iterator() {
        final ListIterator<T> i = original.listIterator(original.size());

        return new Iterator<T>() {
            public boolean hasNext() {
                return i.hasPrevious();
            }

            public T next() {
                return i.previous();
            }

            public void remove() {
                i.remove();
            }
        };
    }

    public static <T> Reversed<T> reversed(List<T> original) {
        return new Reversed<>(original);
    }
}
