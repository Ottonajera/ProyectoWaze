/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui;

/**
 *
 * @author User
 */
import estructuras.Grafo;
import estructuras.List; 
import Modelo.Arista;
import Modelo.Nodo;
import javax.swing.*;
import java.awt.*;

public class PanelMapa extends JPanel {

    private Grafo mapa;
    private List<Integer> rutaActiva;

    public PanelMapa(Grafo mapa) {
        this.mapa = mapa;
        this.rutaActiva = new List<>(); // Usamos tu List
        setBackground(new Color(245, 247, 250)); 
    }

    public void actualizarGrafo(Grafo nuevoMapa) {
        this.mapa = nuevoMapa;
    }

    public void setRutaActiva(List<Integer> ruta) {
        this.rutaActiva = ruta;
        repaint(); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (mapa == null || mapa.getNodos().size() == 0) return;

        double minLat = Double.MAX_VALUE, maxLat = -Double.MAX_VALUE;
        double minLon = Double.MAX_VALUE, maxLon = -Double.MAX_VALUE;
        List<Nodo> listaNodos = mapa.getNodos().values();
        for (int i = 0; i < listaNodos.size(); i++) {
            Nodo n = listaNodos.get(i);
            if (n.getLatitud() < minLat) minLat = n.getLatitud();
            if (n.getLatitud() > maxLat) maxLat = n.getLatitud();
            if (n.getLongitud() < minLon) minLon = n.getLongitud();
            if (n.getLongitud() > maxLon) maxLon = n.getLongitud();
        }

        int padding = 45; 
        int margenDerechoExtra = 70; 
        int anchoUtil = getWidth() - (padding * 2) - margenDerechoExtra;
        int altoUtil = getHeight() - padding * 2;

        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(new Color(210, 214, 219)); 

        List<Integer> clavesAdyacencia = mapa.getAdyacencia().keySet();
        for (int i = 0; i < clavesAdyacencia.size(); i++) {
            Integer idOrigen = clavesAdyacencia.get(i);
            Nodo origen = mapa.getNodo(idOrigen);
            int x1 = escalarLongitud(origen.getLongitud(), minLon, maxLon, anchoUtil) + padding;
            int y1 = escalarLatitud(origen.getLatitud(), minLat, maxLat, altoUtil) + padding;

            List<Arista> conexiones = mapa.getAdyacencia().get(idOrigen);
            if (conexiones != null) {
                for (int j = 0; j < conexiones.size(); j++) {
                    Arista arista = conexiones.get(j);
                    Nodo destino = mapa.getNodo(arista.getIdDestino());
                    if (destino != null) {
                        int x2 = escalarLongitud(destino.getLongitud(), minLon, maxLon, anchoUtil) + padding;
                        int y2 = escalarLatitud(destino.getLatitud(), minLat, maxLat, altoUtil) + padding;
                        g2.drawLine(x1, y1, x2, y2);
                    }
                }
            }
        }

        if (rutaActiva != null && rutaActiva.size() > 1) {
            g2.setStroke(new BasicStroke(4.0f));
            g2.setColor(new Color(0, 102, 204)); 

            for (int i = 0; i < rutaActiva.size() - 1; i++) {
                Nodo n1 = mapa.getNodo(rutaActiva.get(i));
                Nodo n2 = mapa.getNodo(rutaActiva.get(i + 1));
                
                if (n1 != null && n2 != null) {
                    int x1 = escalarLongitud(n1.getLongitud(), minLon, maxLon, anchoUtil) + padding;
                    int y1 = escalarLatitud(n1.getLatitud(), minLat, maxLat, altoUtil) + padding;
                    int x2 = escalarLongitud(n2.getLongitud(), minLon, maxLon, anchoUtil) + padding;
                    int y2 = escalarLatitud(n2.getLatitud(), minLat, maxLat, altoUtil) + padding;
                    
                    g2.drawLine(x1, y1, x2, y2);
                }
            }
        }
        int[] nodosImportantes = {
            1, 4, 7, 10, 14, 22, 49, 73, 95, 100, 110, 125, 131, 
            201, 206, 209, 222, 232, 238, 273, 309, 320, 417, 430, 444, 
            456, 485, 501, 612, 613, 645, 708
        };
        for (int i = 0; i < listaNodos.size(); i++) {
            Nodo n = listaNodos.get(i);
            int x = escalarLongitud(n.getLongitud(), minLon, maxLon, anchoUtil) + padding;
            int y = escalarLatitud(n.getLatitud(), minLat, maxLat, altoUtil) + padding;
            if (contieneRuta(rutaActiva, n.getId())) {
                g2.setColor(new Color(255, 69, 0)); 
                g2.fillOval(x - 5, y - 5, 10, 10);  
            } else {
                g2.setColor(new Color(100, 110, 120)); 
                g2.fillOval(x - 3, y - 3, 6, 6);       
            }
            if (contieneImportante(nodosImportantes, n.getId())) {
                g2.setColor(new Color(33, 37, 41));
                g2.setFont(new Font("SansSerif", Font.BOLD, 11)); 
                g2.setColor(Color.WHITE);
                g2.drawString(n.getNombre(), x + 9, y - 3);
                g2.drawString(n.getNombre(), x + 7, y - 5);
                g2.setColor(new Color(33, 37, 41));
                g2.drawString(n.getNombre(), x + 8, y - 4);
            }
        }
    }

    private int escalarLongitud(double lon, double minLon, double maxLon, int anchoPantalla) {
        if (maxLon == minLon) return 0;
        return (int) (((lon - minLon) / (maxLon - minLon)) * anchoPantalla);
    }

    private int escalarLatitud(double lat, double minLat, double maxLat, int altoPantalla) {
        if (maxLat == minLat) return 0;
        return (int) (((maxLat - lat) / (maxLat - minLat)) * altoPantalla);
    }

    private boolean contieneRuta(List<Integer> lista, int valor) {
        if (lista == null) return false;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i) != null && lista.get(i) == valor) {
                return true;
            }
        }
        return false;
    }

    private boolean contieneImportante(int[] arreglo, int valor) {
        for (int i = 0; i < arreglo.length; i++) {
            if (arreglo[i] == valor) return true;
        }
        return false;
    }
}
