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
import estructuras.Pila;
import estructuras.List; 
import Modelo.Nodo;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class VentanaSimulador extends JFrame {

    private List<Grafo> rutas;

    private JSpinner spHora;
    private JTextArea txtRegistro;
    private PanelMapa panelMapaGrafico;
    private JLabel lblTiempoTotal;
    private JLabel lblDistanciaTotal;
    private JLabel lblGanador;
    private JLabel lblTrafico;
    private DefaultTableModel modeloTabla;

    private final int ID_ORIGEN  = 1;
    private final int ID_DESTINO = 131;

    private static final Color AZUL_PRINCIPAL = new Color(0, 51, 153);
    private static final Color VERDE_OK       = new Color(0, 153, 51);
    private static final Color ROJO_DESCARTE  = new Color(180, 0, 0);
    private static final Color AMARILLO_PICO  = new Color(204, 102, 0);

    public VentanaSimulador(List<Grafo> rutas) {
        this.rutas = rutas;
        configurarVentana();
        inicializarComponentes();
    }

    private void configurarVentana() {
        setTitle("GPS Dijkstra");
        setSize(1200, 680); 
        setExtendedState(JFrame.MAXIMIZED_BOTH); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);
    }

    private void inicializarComponentes() {

        JPanel panelIzquierdo = new JPanel();
        panelIzquierdo.setLayout(new BoxLayout(panelIzquierdo, BoxLayout.Y_AXIS));
        panelIzquierdo.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));
        panelIzquierdo.setBackground(Color.WHITE);
        panelIzquierdo.setPreferredSize(new Dimension(210, 0));

        JLabel lblTitulo = new JLabel("GPS Dijkstra");
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblTitulo.setForeground(AZUL_PRINCIPAL);
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblOrigen = new JLabel("<html><b>Origen:</b><br>Plaza Tigo</html>");
        lblOrigen.setBorder(BorderFactory.createEmptyBorder(20, 0, 8, 0));

        JLabel lblDestino = new JLabel("<html><b>Destino:</b><br>UMG Jocotenango</html>");
        lblDestino.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JLabel lblHora = new JLabel("<html><b>Hora de salida (0-23hrs):</b></html>");
        lblHora.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));

        SpinnerNumberModel modeloHora = new SpinnerNumberModel(18, 0, 23, 1);
        spHora = new JSpinner(modeloHora);
        spHora.setMaximumSize(new Dimension(90, 30));
        spHora.setAlignmentX(Component.LEFT_ALIGNMENT);

        lblTrafico = new JLabel("─");
        lblTrafico.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTrafico.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));
        lblTrafico.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton btnCalcular = new JButton("Trazar Ruta");
        btnCalcular.setBackground(AZUL_PRINCIPAL);
        btnCalcular.setForeground(Color.WHITE);
        btnCalcular.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnCalcular.setFocusPainted(false);
        btnCalcular.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnCalcular.setMaximumSize(new Dimension(180, 38));

        panelIzquierdo.add(lblTitulo);
        panelIzquierdo.add(lblOrigen);
        panelIzquierdo.add(lblDestino);
        panelIzquierdo.add(lblHora);
        panelIzquierdo.add(spHora);
        panelIzquierdo.add(lblTrafico);
        panelIzquierdo.add(Box.createVerticalStrut(25));
        panelIzquierdo.add(btnCalcular);
        panelIzquierdo.add(Box.createVerticalStrut(15)); 
        
        JButton btnAccidente = new JButton("Simular Accidente");
        btnAccidente.setBackground(new Color(204, 51, 51)); // Rojo alerta
        btnAccidente.setForeground(Color.WHITE);
        btnAccidente.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnAccidente.setFocusPainted(false);
        btnAccidente.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnAccidente.setMaximumSize(new Dimension(180, 30));
        
        btnAccidente.addActionListener(e -> {
            if (rutas.isEmpty()) return;
            
            String[] opciones = new String[rutas.size()];
            for (int i = 0; i < rutas.size(); i++) {
                opciones[i] = rutas.get(i).getNombre();
            }
            
            String seleccion = (String) JOptionPane.showInputDialog(this, 
                "Seleccione la ruta donde ocurrió un accidente múltiple:\n(Esto reducirá la velocidad al 2% por tráfico pesado)", 
                "Emergencia de Tráfico", JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
                
            if (seleccion != null) {
                for (int i = 0; i < rutas.size(); i++) {
                    if (rutas.get(i).getNombre().equals(seleccion)) {
                        rutas.get(i).provocarAccidenteMasivo(); // Dispara el caos en esa ruta
                        break;
                    }
                }
                JOptionPane.showMessageDialog(this, 
                    "¡Accidente reportado en '" + seleccion + "'!\nPresione 'Trazar Ruta' para recalcular el impacto.", 
                    "Alerta de Tráfico", JOptionPane.ERROR_MESSAGE);
                
                modeloTabla.setRowCount(0);
                lblGanador.setText(" ");
                panelMapaGrafico.setRutaActiva(new estructuras.List<>());
                panelMapaGrafico.repaint();
            }
        });
        
        panelIzquierdo.add(btnAccidente);

        panelIzquierdo.add(Box.createVerticalStrut(10)); 
        
        JButton btnEliminarRuta = new JButton("Eliminar Ruta Completa");
        btnEliminarRuta.setBackground(new Color(102, 102, 102));
        btnEliminarRuta.setForeground(Color.WHITE);
        btnEliminarRuta.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnEliminarRuta.setFocusPainted(false);
        btnEliminarRuta.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnEliminarRuta.setMaximumSize(new Dimension(180, 30));
        
        btnEliminarRuta.addActionListener(e -> {
            if (rutas.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Ya no hay rutas en memoria para eliminar.");
                return;
            }
            String[] opciones = new String[rutas.size()];
            for (int i = 0; i < rutas.size(); i++) {
                opciones[i] = rutas.get(i).getNombre();
            }
            String seleccion = (String) JOptionPane.showInputDialog(this, 
                "Seleccione la ruta completa que desea sacar de la simulación:\n(No borrará el archivo .txt)", 
                "Eliminar Ruta Completa", JOptionPane.WARNING_MESSAGE, null, opciones, opciones[0]);
                
            if (seleccion != null) {
                for (int i = 0; i < rutas.size(); i++) {
                    if (rutas.get(i).getNombre().equals(seleccion)) {
                        rutas.remove(rutas.get(i));
                        break;
                    }
                }
                
                JOptionPane.showMessageDialog(this, 
                    "La ruta '" + seleccion + "' ha sido eliminada.\nPresione 'Trazar Ruta' para simular con las opciones restantes.", 
                    "Ruta Desactivada", JOptionPane.INFORMATION_MESSAGE);
                    
                modeloTabla.setRowCount(0);
                lblGanador.setText(" ");
                panelMapaGrafico.setRutaActiva(new List<>());
                panelMapaGrafico.repaint();
            }
        });
        
        panelIzquierdo.add(btnEliminarRuta);
        
        JButton btnReiniciar = new JButton("Limpiar Accidentes");
        btnReiniciar.setBackground(new Color(40, 167, 69)); // Verde agradable
        btnReiniciar.setForeground(Color.WHITE);
        btnReiniciar.setFont(new Font("SansSerif", Font.BOLD, 12));
        btnReiniciar.setFocusPainted(false);
        btnReiniciar.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnReiniciar.setMaximumSize(new Dimension(180, 30));
        
        btnReiniciar.addActionListener(e -> {
            // 1. Curar todos los grafos
            for (int i = 0; i < rutas.size(); i++) {
                rutas.get(i).limpiarAccidentes();
            }
            
            // 2. Limpiar la interfaz visual
            modeloTabla.setRowCount(0);
            lblGanador.setText(" ");
            txtRegistro.setText("");
            lblTiempoTotal.setText("Tiempo estimado: -- min");
            lblDistanciaTotal.setText("Distancia total: -- km");
            panelMapaGrafico.setRutaActiva(new estructuras.List<>());
            panelMapaGrafico.repaint();
            
            JOptionPane.showMessageDialog(this, 
                "El tráfico ha vuelto a la normalidad en todas las rutas.", 
                "Vías Despejadas", JOptionPane.INFORMATION_MESSAGE);
        });
        
        panelIzquierdo.add(Box.createVerticalStrut(10)); // Espaciador
        panelIzquierdo.add(btnReiniciar);
        
        add(panelIzquierdo, BorderLayout.WEST);

        panelMapaGrafico = new PanelMapa(rutas.get(0));
        panelMapaGrafico.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        add(panelMapaGrafico, BorderLayout.CENTER);

        JPanel panelDerecho = new JPanel(new BorderLayout(0, 8));
        panelDerecho.setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 12));
        panelDerecho.setBackground(Color.WHITE);
        panelDerecho.setPreferredSize(new Dimension(310, 0));

        lblGanador = new JLabel(" ");
        lblGanador.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblGanador.setForeground(VERDE_OK);
        lblGanador.setHorizontalAlignment(SwingConstants.CENTER);
        lblGanador.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(VERDE_OK, 1, true),
            BorderFactory.createEmptyBorder(6, 8, 6, 8)
        ));
        lblGanador.setOpaque(false);
        panelDerecho.add(lblGanador, BorderLayout.NORTH);

        String[] columnas = {"Ruta", "Tiempo", "Estado"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        JTable tabla = new JTable(modeloTabla);
        tabla.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    String nombreRuta = (String) modeloTabla.getValueAt(fila, 0);
                    for (int i = 0; i < rutas.size(); i++) {
                        Grafo g = rutas.get(i);
                        if (g.getNombre().equals(nombreRuta)) {
                            dibujarRutaEnMapa(g);
                            break;
                        }
                    }
                }
            }
        });
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setRowHeight(24);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        tabla.getTableHeader().setBackground(new Color(230, 236, 255));
        tabla.setGridColor(new Color(220, 220, 220));
        tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object val,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, val, sel, foc, row, col);
                String estado = (String) t.getModel().getValueAt(row, 2);
                if ("✅ MAS RAPIDA".equals(estado)) {
                    setBackground(new Color(220, 255, 220));
                    setForeground(new Color(0, 100, 0));
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setBackground(new Color(255, 245, 245));
                    setForeground(new Color(100, 0, 0));
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
                return this;
            }
        });

        JScrollPane scrollTabla = new JScrollPane(tabla);
        scrollTabla.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Comparativa de rutas"
        ));
        scrollTabla.setPreferredSize(new Dimension(300, 195));
        txtRegistro = new JTextArea();
        txtRegistro.setEditable(false);
        txtRegistro.setFont(new Font("SansSerif", Font.PLAIN, 12));
        txtRegistro.setMargin(new Insets(8, 8, 8, 8));
        JScrollPane scrollRegistro = new JScrollPane(txtRegistro);
        scrollRegistro.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180)),
            "Historial de paradas"
        ));
        JPanel panelTablaHistorial = new JPanel(new BorderLayout(0, 6));
        panelTablaHistorial.setBackground(Color.WHITE);
        panelTablaHistorial.add(scrollTabla, BorderLayout.NORTH);
        panelTablaHistorial.add(scrollRegistro, BorderLayout.CENTER);
        panelDerecho.add(panelTablaHistorial, BorderLayout.CENTER);
        JPanel panelEstadisticas = new JPanel(new GridLayout(2, 1, 2, 2));
        panelEstadisticas.setBackground(Color.WHITE);
        panelEstadisticas.setBorder(BorderFactory.createEmptyBorder(6, 0, 0, 0));

        lblTiempoTotal = new JLabel("Tiempo estimado: -- min");
        lblTiempoTotal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblTiempoTotal.setForeground(VERDE_OK);

        lblDistanciaTotal = new JLabel("Distancia total: -- km");
        lblDistanciaTotal.setFont(new Font("SansSerif", Font.BOLD, 13));
        lblDistanciaTotal.setForeground(new Color(51, 51, 51));

        panelEstadisticas.add(lblTiempoTotal);
        panelEstadisticas.add(lblDistanciaTotal);
        panelDerecho.add(panelEstadisticas, BorderLayout.SOUTH);

        add(panelDerecho, BorderLayout.EAST);
        btnCalcular.addActionListener(e -> calcularYMostrarRuta());
    }

    private void calcularYMostrarRuta() {
        int hora = (Integer) spHora.getValue();
        String estadoTrafico;
        Color colorTrafico;
        if (hora >= 6 && hora <= 9) {
            estadoTrafico = "🔴 TRÁFICO PESADO (pico mañana)";
            colorTrafico  = ROJO_DESCARTE;
        } else if (hora >= 16 && hora <= 19) {
            estadoTrafico = "🟠 TRÁFICO PESADO (pico tarde)";
            colorTrafico  = AMARILLO_PICO;
        } else {
            estadoTrafico = "🟢 TRÁFICO FLUIDO";
            colorTrafico  = VERDE_OK;
        }
        lblTrafico.setText("<html>" + estadoTrafico + "</html>");
        lblTrafico.setForeground(colorTrafico);
        Grafo rutaGanadora  = null;
        double tiempoMinimo = Double.MAX_VALUE;
        double[] tiemposPorRuta = new double[rutas.size()];

        for (int i = 0; i < rutas.size(); i++) {
            double t = rutas.get(i).calcularTiempoTotal(ID_ORIGEN, ID_DESTINO, hora);
            
            tiemposPorRuta[i] = t;
            
            if (t < tiempoMinimo) {
                tiempoMinimo = t;
                rutaGanadora = rutas.get(i);
            }
        }

        modeloTabla.setRowCount(0); 
        for (int i = 0; i < rutas.size(); i++) {
            String nombre  = rutas.get(i).getNombre();
            double tiempo  = tiemposPorRuta[i];
            boolean esGanadora = (tiempo == tiempoMinimo);
            String tiempoStr = (tiempo == Double.MAX_VALUE) ? "Sin ruta" : formatearTiempo(tiempo);
            String estadoFila = esGanadora ? "✅ MAS RAPIDA" : "✗ Descartada";
            modeloTabla.addRow(new Object[]{nombre, tiempoStr, estadoFila});
        }
        if (rutaGanadora != null) {
            lblGanador.setText("✅ Ruta mas rapida: " + rutaGanadora.getNombre() 
                             + " (" + formatearTiempo(tiempoMinimo) + ")");
            lblGanador.setForeground(VERDE_OK);
            lblGanador.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(VERDE_OK, 1, true),
                BorderFactory.createEmptyBorder(6, 8, 6, 8)
            ));
        }
        txtRegistro.setText("");
        txtRegistro.append("Hora: " + hora + ":00 hrs\n");
        txtRegistro.append("Ruta: " + (rutaGanadora != null ? rutaGanadora.getNombre() : "--") + "\n");
        txtRegistro.append("─────────────────────────\n");

        if (rutaGanadora == null) {
            lblTiempoTotal.setText("Tiempo estimado: " + formatearTiempo(tiempoMinimo));
            lblDistanciaTotal.setText("Distancia total: -- km");
            panelMapaGrafico.setRutaActiva(new List<>());
            return;
        }

        Pila ruta = rutaGanadora.obtenerRutaMasRapida(ID_ORIGEN, ID_DESTINO, hora, 600);

        List<Integer> listaIDs = new List<>();
        int nodoAnterior = -1;
        double distanciaAcumulada = 0.0;
        int paso = 1;

        while (!ruta.isEmpty()) {
            int idActual = ruta.pop();
            listaIDs.add(idActual);

            Nodo infoNodo = rutaGanadora.getNodo(idActual);
            if (nodoAnterior != -1) {
                distanciaAcumulada += rutaGanadora.calcularDistanciaHaversine(nodoAnterior, idActual);
            }

            txtRegistro.append(paso + ". " + infoNodo.getNombre() + "\n");
            nodoAnterior = idActual;
            paso++;
        }

        txtRegistro.append("─────────────────────────\n");
        txtRegistro.append("¡Llegaste a la UMG!\n");

        lblTiempoTotal.setText("Tiempo estimado: " + formatearTiempo(tiempoMinimo));
        lblDistanciaTotal.setText(String.format("Distancia total: %.2f km", distanciaAcumulada));

        panelMapaGrafico.actualizarGrafo(rutaGanadora);
        panelMapaGrafico.setRutaActiva(listaIDs);
    }

    private void dibujarRutaEnMapa(Grafo grafoParaDibujar) {
        int hora = (Integer) spHora.getValue();
        double tiempo = grafoParaDibujar.calcularTiempoTotal(ID_ORIGEN, ID_DESTINO, hora);
        Pila ruta = grafoParaDibujar.obtenerRutaMasRapida(ID_ORIGEN, ID_DESTINO, hora, 600);

        List<Integer> listaIDs = new List<>();
        double distanciaAcumulada = 0.0;
        int nodoAnterior = -1;

        List<Integer> auxPila = new List<>();
        while (!ruta.isEmpty()) {
            auxPila.add(ruta.pop());
        }

        txtRegistro.setText("");
        txtRegistro.append("Hora: " + hora + ":00 hrs\n");
        txtRegistro.append("Ruta: " + grafoParaDibujar.getNombre() + "\n");
        txtRegistro.append("─────────────────────────\n");
        int paso = 1;

        for (int i = 0; i < auxPila.size(); i++) {
            int idActual = auxPila.get(i);
            listaIDs.add(idActual);
            Nodo infoNodo = grafoParaDibujar.getNodo(idActual);
            if (infoNodo != null) {
                txtRegistro.append(paso + ". " + infoNodo.getNombre() + "\n");
                paso++;
            }

            if (nodoAnterior != -1) {
                distanciaAcumulada += grafoParaDibujar.calcularDistanciaHaversine(nodoAnterior, idActual);
            }
            nodoAnterior = idActual;
        }

        txtRegistro.append("─────────────────────────\n");
        txtRegistro.append("¡Llegaste a la UMG!\n");

        panelMapaGrafico.actualizarGrafo(grafoParaDibujar);
        panelMapaGrafico.setRutaActiva(listaIDs);

        lblTiempoTotal.setText("Tiempo estimado: " + formatearTiempo(tiempo));
        lblDistanciaTotal.setText(String.format("Distancia total: %.2f km", distanciaAcumulada));
    }

    private String formatearTiempo(double minutos) {
        if (minutos >= 60) {
            int h = (int) (minutos / 60);
            int m = (int) (minutos % 60);
            return String.format("%d hr %d min", h, m);
        }
        return String.format("%.0f min", minutos);
    }
}
