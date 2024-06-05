/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecasistema8v;

/**
 *
 * @author alexa
 */
public class Autor {
    
    //Atributos
        private int IDAutor;
	private String nombre;
	private int edad;
	private String nacionalidad;
	
	
	//Constructores
	public Autor(int IDAutor, String nombre, int edad, String nacionalidad) {
                this.IDAutor= IDAutor;
		this.nombre = nombre;
		this.edad = edad;
		this.nacionalidad = nacionalidad;
	}
	public Autor(int IDAutor){
                this.IDAutor= IDAutor;	
	}

	//Getters y Setters     
        public int getIDAutor() {
                 return IDAutor;
        }
        public void setIDAutor(int IDAutor) {
            this.IDAutor = IDAutor;
        }
        
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getNacionalidad() {
		return nacionalidad;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	//toString
	public String toString() {
		return "\n°Autor: " + nombre + "\n°Edad: " + edad + "\n°Nacionalidad: " + nacionalidad;
	}
    
}
