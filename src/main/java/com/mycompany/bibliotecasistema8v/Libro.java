/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecasistema8v;

/**
 *
 * @author alexa
 */
public class Libro {
    //Atributos
        private String ISBN;
	private String titulo;
	private Autor autor;
	private String anio;
	private int cantidad;
	private int prestados;
	
	//Constructores
	public Libro(String ISBN, String titulo, Autor autor, String anio, int cantidad) {
		
                this.ISBN=ISBN;
                this.titulo = titulo;
		this.autor = autor;
                this.anio=anio;
		this.prestados = 0;
	}

	//Getters y Setters
	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Autor getAutor() {
		return autor;
	}

	public void setAutor(Autor autor) {
		this.autor = autor;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public int getPrestados() {
		return prestados;
	}

	public void setPrestados(int prestados) {
		this.prestados = prestados;
	}

        public String getISBN() {
            return ISBN;
        }

        public void setISBN(String ISBN) {
            this.ISBN = ISBN;
        }

        
        public String getAnio() {
            return anio;
        }

        public void setAnio(String anio) {
            this.anio = anio;
        }
        
	//toString
	public String toString() {
		return "\n°Libro: " + titulo + " - " + autor + "\n°ISBN: " + ISBN + "\n°Año: "+ anio + "\n°Ejemplares: "+ cantidad;
	}
}
