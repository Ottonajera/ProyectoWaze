/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author User
 */
public class List<T> {
    private Object[] elementos;
    private int size;
    private int capacidad = 10;

    public List() {
        elementos = new Object[capacidad];
        size = 0;
    }

    public void add(T elemento) {
        if (size == capacidad) {
            ampliarCapacidad();
        }
        elementos[size++] = elemento;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Índice fuera de rango");
        }
        return (T) elementos[index];
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    private void ampliarCapacidad() {
        capacidad *= 2;
        Object[] nuevosElementos = new Object[capacidad];
        System.arraycopy(elementos, 0, nuevosElementos, 0, size);
        elementos = nuevosElementos;
    }

    public void remove(T elemento) {
        for (int i = 0; i < size; i++) {
            if (elementos[i].equals(elemento)) {
                for (int j = i; j < size - 1; j++) {
                    elementos[j] = elementos[j + 1];
                }
                elementos[--size] = null;
                return;
            }
        }
    }
}
