/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecasistema8v;

/**
 *
 * @author alexa
 */
public class Usuario {
        private int IdUser;
	private String user;
	private String contrasenia;

    public Usuario(int IdUser, String user, String contrasenia) 
    {
        this.IdUser = IdUser;
        this.user = user;
        this.contrasenia = contrasenia;
    }
    public Usuario() 
    {
    }

    public int getIdUser() {
        return IdUser;
    }

    public String getNombreUser() {
        return user;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public void setIdUser(int IdUser) {
        this.IdUser = IdUser;
    }

    public void setNombreUser(String user) {
        this.user = user;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    @Override
    public String toString() {
        return "Idetificador de usuario: " + IdUser + "\nUsuario" + user;
    }
        
        
}


