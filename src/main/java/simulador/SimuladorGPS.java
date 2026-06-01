/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package simulador;

/**
 *
 * @author User
 */
import estructuras.Grafo;
import estructuras.List;
import ui.VentanaSimulador;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class SimuladorGPS {

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("INICIANDO MOTOR DE GPS DIJKSTRA");
        System.out.println("====================================================");

        Grafo grafoRoosevelt  = cargarMapaDesdeArchivo("ruta_roosevelt.txt",  "Roosevelt");
        Grafo grafoCaes = cargarMapaDesdeArchivo("ruta_caes.txt", "Carretera al Salvador");
        Grafo grafoProceres   = cargarMapaDesdeArchivo("ruta_proceres.txt",   "Proceres");
        Grafo grafoBarcenas   = cargarMapaDesdeArchivo("ruta_barcenas.txt",   "Barcenas");
        Grafo grafoSanCris    = cargarMapaDesdeArchivo("ruta_sancris.txt",    "San Cristobal");
        Grafo grafoSantaMaria = cargarMapaDesdeArchivo("ruta_santamaria.txt", "Santa Maria");

        List<Grafo> todasLasRutas = new List<>();
        todasLasRutas.add(grafoRoosevelt);
        todasLasRutas.add(grafoCaes);
        todasLasRutas.add(grafoProceres);
        todasLasRutas.add(grafoBarcenas);
        todasLasRutas.add(grafoSanCris);
        todasLasRutas.add(grafoSantaMaria);

        java.awt.EventQueue.invokeLater(() -> {
            new VentanaSimulador(todasLasRutas).setVisible(true);
        });
    }

    public static Grafo cargarMapaDesdeArchivo(String rutaArchivo, String nombreRuta) {
        Grafo mapa = new Grafo(nombreRuta);

        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            int contadorNodos = 0;
            int contadorAristas = 0;

            while ((linea = br.readLine()) != null) {
                linea = linea.trim();
                if (linea.startsWith("#") || linea.isEmpty()) continue;

                String[] datos = linea.split(",");
                String tipo = datos[0].trim();

                if (tipo.equals("NODO")) {
                    int id       = Integer.parseInt(datos[1].trim());
                    String nombre = datos[2].trim();
                    double lat   = Double.parseDouble(datos[3].trim());
                    double lon   = Double.parseDouble(datos[4].trim());
                    mapa.agregarNodo(id, nombre, lat, lon);
                    contadorNodos++;

                } else if (tipo.equals("ARISTA")) {
                    int idOrigen    = Integer.parseInt(datos[1].trim());
                    int idDestino   = Integer.parseInt(datos[2].trim());
                    double distancia = Double.parseDouble(datos[3].trim());
                    double velocidad = Double.parseDouble(datos[4].trim()); 

                    double[] trafico24h = new double[24];
                    for (int i = 0; i < 24; i++) {
                        int indiceLectura = i + 5;
                        if (indiceLectura >= datos.length) {
                            indiceLectura = datos.length - 1; 
                        }
                        
                        trafico24h[i] = Double.parseDouble(datos[indiceLectura].trim());
                    }
                    mapa.agregarRuta(idOrigen, idDestino, distancia, velocidad, trafico24h);
                    contadorAristas++;
                }
            }    
            System.out.println("[" + nombreRuta + "] cargado: " + contadorNodos + " nodos, " + contadorAristas + " aristas");

        } catch (IOException e) {
            System.err.println("ERROR: No se encontro el archivo: " + rutaArchivo);
        } catch (NumberFormatException | IndexOutOfBoundsException e) {
            System.err.println("ERROR DE FORMATO en " + rutaArchivo + ": " + e.getMessage());
        }

        return mapa;
    }
}