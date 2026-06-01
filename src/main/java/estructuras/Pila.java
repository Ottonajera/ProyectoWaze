/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author User
 */
public class Pila {
    private int[] datos;
    private int tope;
    public Pila(int capacidad) {
        this.datos = new int[capacidad];
        this.tope = -1;
    }

    public void push(int v) {
        if (tope < datos.length - 1) {
            datos[++tope] = v;
        } else {
            System.out.println("Error: Desbordamiento de Pila (Overflow)");
        }
    }

    public int pop() {
        if (!isEmpty()) {
            return datos[tope--];
        }
        return -1; 
    }

    public int peek() {
        return (tope == -1) ? -1 : datos[tope];
    }

    public boolean isEmpty() {
        return tope == -1;
    }

    public int tamanoActual() {
        return tope + 1;
    }
}
