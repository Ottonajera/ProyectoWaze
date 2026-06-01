/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package estructuras;

/**
 *
 * @author User
 */
import Modelo.Arista;
import Modelo.Nodo;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Grafo {
    private Hash<Integer, Nodo> nodos;
    private Hash<Integer, List<Arista>> adyacencia;
    private String nombre; 

    public Grafo() {
        this.nodos = new Hash<>();
        this.adyacencia = new Hash<>();
        this.nombre = "Sin nombre";
    }

    public Grafo(String nombre) {
        this.nodos = new Hash<>();
        this.adyacencia = new Hash<>();
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void agregarNodo(int id, String nombre, double lat, double lon) {
        Nodo nuevo = new Nodo(id, nombre, lat, lon);
        nodos.put(id, nuevo);
        if (!adyacencia.containsKey(id)) {
            adyacencia.put(id, new List<>());
        }
    }

    public void agregarRuta(int idOrigen, int idDestino, double dist, double velocidad, double[] trafico) {
        if (nodos.containsKey(idOrigen) && nodos.containsKey(idDestino)) {
            adyacencia.get(idOrigen).add(new Arista(idDestino, dist, velocidad, trafico));
        }
    }

    public Nodo getNodo(int id) {
        return nodos.get(id);
    }

    public Pila obtenerRutaMasRapida(int idInicio, int idFin, int hora, int capacidadPila) {
        if (!nodos.containsKey(idInicio) || !nodos.containsKey(idFin)) {
            return new Pila(1);
        }

        Hash<Integer, Double> tiempos = new Hash<>();
        Hash<Integer, Integer> predecesores = new Hash<>();
        
        PriorityQueue<NodoTiempo> pq = new PriorityQueue<>(Comparator.comparingDouble(nt -> nt.tiempo));
        List<Integer> clavesNodos = nodos.keySet();
        for (int i = 0; i < clavesNodos.size(); i++) {
            Integer id = clavesNodos.get(i);
            tiempos.put(id, Double.MAX_VALUE);
        }
        
        tiempos.put(idInicio, 0.0);
        pq.add(new NodoTiempo(idInicio, 0.0));

        while (!pq.isEmpty()) {
            int actual = pq.poll().idNodo;

            if (actual == idFin) break;

            List<Arista> conexiones = adyacencia.get(actual);
            if (conexiones != null) {
                for (int i = 0; i < conexiones.size(); i++) {
                    Arista arista = conexiones.get(i);
                    int vecino = arista.getIdDestino();
                    
                    double tiempoHaciaVecino = tiempos.get(actual) + arista.calcularTiempo(hora);

                    if (tiempoHaciaVecino < tiempos.get(vecino)) {
                        tiempos.put(vecino, tiempoHaciaVecino);
                        predecesores.put(vecino, actual);
                        pq.add(new NodoTiempo(vecino, tiempoHaciaVecino));
                    }
                }
            }
        }
        Pila ruta = new Pila(capacidadPila);
        Integer paso = idFin;

        if (predecesores.get(paso) == null && paso != idInicio) {
            return ruta; 
        }

        while (paso != null) {
            ruta.push(paso);
            paso = predecesores.get(paso); 
        }

        return ruta;
    }

    private static class NodoTiempo {
        int idNodo;
        double tiempo;
        
        NodoTiempo(int idNodo, double tiempo) {
            this.idNodo = idNodo;
            this.tiempo = tiempo;
        }
    }
    public Hash<Integer, Nodo> getNodos() {
        return nodos;
    }
    public Hash<Integer, List<Arista>> getAdyacencia() {
        return adyacencia;
    }

    public double obtenerTiempoEntreNodos(int idOrigen, int idDestino, int hora) {
        if (adyacencia.containsKey(idOrigen)) {
            List<Arista> conexiones = adyacencia.get(idOrigen);
            for (int i = 0; i < conexiones.size(); i++) {
                Arista a = conexiones.get(i);
                if (a.getIdDestino() == idDestino) {
                    return a.calcularTiempo(hora);
                }
            }
        }
        return 0.0;
    }

    public double calcularDistanciaHaversine(int idOrigen, int idDestino) {
        Nodo origen = nodos.get(idOrigen);
        Nodo destino = nodos.get(idDestino);
        
        if (origen == null || destino == null) return 0.0;

        final int R = 6371; 

        double lat1 = Math.toRadians(origen.getLatitud());
        double lon1 = Math.toRadians(origen.getLongitud());
        double lat2 = Math.toRadians(destino.getLatitud());
        double lon2 = Math.toRadians(destino.getLongitud());

        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
                   
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c; 
    }

    public double calcularTiempoTotal(int idInicio, int idFin, int hora) {
        if (!nodos.containsKey(idInicio) || !nodos.containsKey(idFin)) {
            return Double.MAX_VALUE;
        }

        Hash<Integer, Double> tiempos = new Hash<>();
        PriorityQueue<NodoTiempo> pq = new PriorityQueue<>(Comparator.comparingDouble(nt -> nt.tiempo));

        List<Integer> clavesNodos = nodos.keySet();
        for (int i = 0; i < clavesNodos.size(); i++) {
            Integer id = clavesNodos.get(i);
            tiempos.put(id, Double.MAX_VALUE);
        }

        tiempos.put(idInicio, 0.0);
        pq.add(new NodoTiempo(idInicio, 0.0));

        while (!pq.isEmpty()) {
            int actual = pq.poll().idNodo;
            if (actual == idFin) break;

            List<Arista> conexiones = adyacencia.get(actual);
            if (conexiones != null) {
                for (int i = 0; i < conexiones.size(); i++) {
                    Arista arista = conexiones.get(i);
                    int vecino = arista.getIdDestino();
                    double nuevo = tiempos.get(actual) + arista.calcularTiempo(hora);
                    
                    if (nuevo < tiempos.get(vecino)) {
                        tiempos.put(vecino, nuevo);
                        pq.add(new NodoTiempo(vecino, nuevo));
                    }
                }
            }
        }
        Double tiempoFinal = tiempos.get(idFin);
        return tiempoFinal != null ? tiempoFinal : Double.MAX_VALUE;
    }

    public void agregarCiudad(int id, String nombre, double latitud, double longitud) {
        if (!nodos.containsKey(id)) {
            nodos.put(id, new Nodo(id, nombre, latitud, longitud));
            if (!adyacencia.containsKey(id)) {
                adyacencia.put(id, new List<>());
            }
        }
    }

    public void provocarAccidenteMasivo() {
        estructuras.List<estructuras.List<Arista>> todasLasConexiones = adyacencia.values();
        for (int i = 0; i < todasLasConexiones.size(); i++) {
            estructuras.List<Arista> conexiones = todasLasConexiones.get(i);
            for (int j = 0; j < conexiones.size(); j++) {
                conexiones.get(j).provocarAccidente();
            }
        }
    }

    public void eliminarRuta(int idOrigen, int idDestino) {
        if (adyacencia.containsKey(idOrigen)) {
            List<Arista> conexiones = adyacencia.get(idOrigen);
            for (int i = 0; i < conexiones.size(); i++) {
                if (conexiones.get(i).getIdDestino() == idDestino) {
                    conexiones.remove(conexiones.get(i));
                    i--; 
                }
            }
        }
    }
    public void limpiarAccidentes() {
        estructuras.List<estructuras.List<Arista>> todasLasConexiones = adyacencia.values();
        for (int i = 0; i < todasLasConexiones.size(); i++) {
            estructuras.List<Arista> conexiones = todasLasConexiones.get(i);
            for (int j = 0; j < conexiones.size(); j++) {
                conexiones.get(j).limpiarAccidente();
            }
        }
    }
}