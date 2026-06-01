/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author User
 */
public class Hash<K, V> {

    private static class Entrada<K, V> {
        K clave;
        V valor;
        Entrada<K, V> siguiente; 

        Entrada(K clave, V valor) {
            this.clave = clave;
            this.valor = valor;
            this.siguiente = null;
        }
    }

    private Entrada<K, V>[] tabla;
    private int size;
    private final int CAPACIDAD_INICIAL = 16;

    @SuppressWarnings("unchecked")
    public Hash() {
        tabla = new Entrada[CAPACIDAD_INICIAL];
        size = 0;
    }

    private int calcularHash(K clave) {
        return Math.abs(clave.hashCode()) % CAPACIDAD_INICIAL;
    }

    public void put(K clave, V valor) {
        int indice = calcularHash(clave);
        Entrada<K, V> actual = tabla[indice];
        while (actual != null) {
            if (actual.clave.equals(clave)) {
                actual.valor = valor;
                return;
            }
            actual = actual.siguiente;
        }
        Entrada<K, V> nuevaEntrada = new Entrada<>(clave, valor);
        nuevaEntrada.siguiente = tabla[indice];
        tabla[indice] = nuevaEntrada;
        size++;
    }

    public V get(K clave) {
        int indice = calcularHash(clave);
        Entrada<K, V> actual = tabla[indice];

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                return actual.valor;
            }
            actual = actual.siguiente;
        }
        return null;
    }

    public boolean containsKey(K clave) {
        return get(clave) != null;
    }

    public void remove(K clave) {
        int indice = calcularHash(clave);
        Entrada<K, V> actual = tabla[indice];
        Entrada<K, V> anterior = null;

        while (actual != null) {
            if (actual.clave.equals(clave)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                size--;
                return;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
    }
    public List<K> keySet() {
        List<K> claves = new List<>();
        for (int i = 0; i < CAPACIDAD_INICIAL; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                claves.add(actual.clave);
                actual = actual.siguiente;
            }
        }
        return claves;
    }
    public List<V> values() {
        List<V> valores = new List<>();
        for (int i = 0; i < CAPACIDAD_INICIAL; i++) {
            Entrada<K, V> actual = tabla[i];
            while (actual != null) {
                valores.add(actual.valor);
                actual = actual.siguiente;
            }
        }
        return valores;
    }
    public int size() {
        return size;
    }
    public boolean isEmpty() {
        return size == 0;
    }
}
