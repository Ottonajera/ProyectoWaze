/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author User
 */
public class Arista {
    private int idDestino;
    private double distancia; 
    private double velocidadMax; 
    private double[] trafico24h; 
    private boolean tieneAccidente; 

    public Arista(int idDestino, double distancia, double velocidadMax, double[] trafico24h) {
        this.idDestino = idDestino;
        this.distancia = distancia;
        this.velocidadMax = velocidadMax;
        this.trafico24h = trafico24h;
        this.tieneAccidente = false;
    }

    public int getIdDestino() {
        return idDestino;
    }
    public double getDistancia() {
        return distancia;
    }

    public double calcularTiempo(int hora) {
        double porcentajeTrafico = tieneAccidente ? 0.70 : trafico24h[hora];
        if (porcentajeTrafico >= 1.0) porcentajeTrafico = 0.99;
        double velocidadReal = velocidadMax * (1.0 - porcentajeTrafico);
        if (velocidadReal < 1.0) velocidadReal = 1.0; 
        double tiempoEnHoras = distancia / velocidadReal;
        return tiempoEnHoras * 60.0;
    }

    public void provocarAccidente() {
        this.tieneAccidente = true;
    }
    public void limpiarAccidente() {
        this.tieneAccidente = false;
    }
}