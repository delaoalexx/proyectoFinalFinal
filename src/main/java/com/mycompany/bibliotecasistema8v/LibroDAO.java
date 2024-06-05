/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.bibliotecasistema8v;

/**
 *
 * @author alexa
 */
import static com.mycompany.bibliotecasistema8v.BibliotecaSistema8v.getConnection;
import java.awt.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;

public class LibroDAO {
    Connection connection = BibliotecaSistema8v.getConnection();
    
    public DefaultTableModel getLibrosTableModel() {
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("ISBN");
        model.addColumn("Titulo");
        model.addColumn("Autor");
        model.addColumn("Año");
        model.addColumn("Ejemplares Totales");
        model.addColumn("Ejemplares Disponibles");

        String query = "SELECT " +
                       "l.ISBN, " +
                       "l.titulo, " +
                       "a.nombre AS autor, " +
                       "l.año, " +
                       "ce.cantidad AS ejemplares_totales, " +
                       "ce.cantidad - COALESCE(pl.prestados, 0) AS ejemplares_disponibles " +
                       "FROM libros l " +
                       "JOIN autores a ON l.autor_id = a.id " +
                       "JOIN cantidadEjemplares ce ON l.ISBN = ce.id_libro " +
                       "LEFT JOIN " +
                       "(SELECT id_libro, COUNT(*) AS prestados FROM prestamosLibros GROUP BY id_libro) pl ON l.ISBN = pl.id_libro";

        try (
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                model.addRow(new Object[]{
                    resultSet.getString("ISBN"),
                    resultSet.getString("titulo"),
                    resultSet.getString("autor"),
                    resultSet.getInt("año"),
                    resultSet.getInt("ejemplares_totales"),
                    resultSet.getInt("ejemplares_disponibles")
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }
    
    public boolean pedirLibro(String isbn) {
        String querySelect = "SELECT cantidad FROM cantidadEjemplares WHERE id_libro = ?";
        String queryUpdate = "UPDATE cantidadEjemplares SET cantidad = cantidad - 1 WHERE id_libro = ?";
        String queryInsert = "INSERT INTO prestamosLibros (id_usuario, id_libro) VALUES (?, ?)";
        int idUsuario = 1; // ID de usuario fijo

        try (
             PreparedStatement preparedStatementSelect = connection.prepareStatement(querySelect)) {

            preparedStatementSelect.setString(1, isbn);
            ResultSet resultSet = preparedStatementSelect.executeQuery();

            if (resultSet.next()) {
                int cantidadDisponible = resultSet.getInt("cantidad");
                if (cantidadDisponible > 0) {
                    // Disminuir la cantidad de ejemplares disponibles
                    try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdate)) {
                        preparedStatementUpdate.setString(1, isbn);
                        preparedStatementUpdate.executeUpdate();
                    }

                    // Registrar el préstamo
                    try (PreparedStatement preparedStatementInsert = connection.prepareStatement(queryInsert)) {
                        preparedStatementInsert.setInt(1, idUsuario);
                        preparedStatementInsert.setString(2, isbn);
                        preparedStatementInsert.executeUpdate();
                        return true; // El préstamo fue exitoso
                    }
                } else {
                    return false; // No hay ejemplares disponibles
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean devolverLibro(String isbn) {
    String querySelect = "SELECT * FROM prestamosLibros WHERE id_libro = ?";
    String queryDelete = "DELETE FROM prestamosLibros WHERE id_libro = ? LIMIT 1";
    String queryUpdate = "UPDATE cantidadEjemplares SET cantidad = cantidad + 1 WHERE id_libro = ?";

    try (
        PreparedStatement statementSelect = connection.prepareStatement(querySelect)) {
        
        statementSelect.setString(1, isbn);
        ResultSet resultSet = statementSelect.executeQuery();

        if (resultSet.next()) {
            // Eliminar el registro de préstamo
            try (PreparedStatement statementDelete = connection.prepareStatement(queryDelete)) {
                statementDelete.setString(1, isbn);
                statementDelete.executeUpdate();
            }

            // Incrementar la cantidad de ejemplares disponibles
            try (PreparedStatement statementUpdate = connection.prepareStatement(queryUpdate)) {
                statementUpdate.setString(1, isbn);
                statementUpdate.executeUpdate();
                return true;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
    }
    
    public boolean eliminarLibro(String isbn) {
    String querySelect = "SELECT * FROM libros WHERE ISBN = ?";
    String queryDeletePrestamos = "DELETE FROM prestamosLibros WHERE id_libro = ?";
    String queryDeleteCantidad = "DELETE FROM cantidadEjemplares WHERE id_libro = ?";
    String queryDeleteLibro = "DELETE FROM libros WHERE ISBN = ?";

    try (
        PreparedStatement statementSelect = connection.prepareStatement(querySelect)) {
        
        statementSelect.setString(1, isbn);
        ResultSet resultSet = statementSelect.executeQuery();

        if (resultSet.next()) {
            // Eliminar registros de préstamos
            try (PreparedStatement statementDeletePrestamos = connection.prepareStatement(queryDeletePrestamos)) {
                statementDeletePrestamos.setString(1, isbn);
                statementDeletePrestamos.executeUpdate();
            }

            // Eliminar registro de cantidad de ejemplares
            try (PreparedStatement statementDeleteCantidad = connection.prepareStatement(queryDeleteCantidad)) {
                statementDeleteCantidad.setString(1, isbn);
                statementDeleteCantidad.executeUpdate();
            }

            // Eliminar el libro
            try (PreparedStatement statementDeleteLibro = connection.prepareStatement(queryDeleteLibro)) {
                statementDeleteLibro.setString(1, isbn);
                statementDeleteLibro.executeUpdate();
                return true;
            }
        } else {
            System.out.println("El libro con ISBN " + isbn + " no existe.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    return false;
    }
  
    // agregar un libro a la bbdd
    public boolean agregarLibro(String isbn, String titulo, String autorNombre, int autorEdad, String autorNacionalidad, int anio, int cantidadEjemplares) {
        // Consultas SQL
        String querySelectAutor = "SELECT id FROM autores WHERE nombre = ? AND edad = ? AND nacionalidad = ?";
        String queryInsertAutor = "INSERT INTO autores (nombre, edad, nacionalidad) VALUES (?, ?, ?)";
        String queryInsertLibro = "INSERT INTO libros (ISBN, titulo, autor_id, año) VALUES (?, ?, ?, ?)";
        String queryInsertCantidad = "INSERT INTO cantidadEjemplares (id_libro, cantidad) VALUES (?, ?)";

        try {
            // Verificar si el autor ya existe
            PreparedStatement preparedStatementSelectAutor = connection.prepareStatement(querySelectAutor);
            preparedStatementSelectAutor.setString(1, autorNombre);
            preparedStatementSelectAutor.setInt(2, autorEdad);
            preparedStatementSelectAutor.setString(3, autorNacionalidad);
            ResultSet resultSet = preparedStatementSelectAutor.executeQuery();

            int autorId;
            if (resultSet.next()) {
                autorId = resultSet.getInt("id");  // Autor ya existe
            } else {
                // Insertar nuevo autor
                PreparedStatement preparedStatementInsertAutor = connection.prepareStatement(queryInsertAutor, Statement.RETURN_GENERATED_KEYS);
                preparedStatementInsertAutor.setString(1, autorNombre);
                preparedStatementInsertAutor.setInt(2, autorEdad);
                preparedStatementInsertAutor.setString(3, autorNacionalidad);
                preparedStatementInsertAutor.executeUpdate();

                ResultSet generatedKeys = preparedStatementInsertAutor.getGeneratedKeys();
                if (generatedKeys.next()) {
                    autorId = generatedKeys.getInt(1);
                } else {
                    throw new SQLException("Fallo al obtener el ID del autor insertado.");
                }
            }

            // Insertar el libro
            PreparedStatement preparedStatementInsertLibro = connection.prepareStatement(queryInsertLibro);
            preparedStatementInsertLibro.setString(1, isbn);
            preparedStatementInsertLibro.setString(2, titulo);
            preparedStatementInsertLibro.setInt(3, autorId);
            preparedStatementInsertLibro.setInt(4, anio);
            preparedStatementInsertLibro.executeUpdate();

            // Insertar la cantidad de ejemplares
            PreparedStatement preparedStatementInsertCantidad = connection.prepareStatement(queryInsertCantidad);
            preparedStatementInsertCantidad.setString(1, isbn);
            preparedStatementInsertCantidad.setInt(2, cantidadEjemplares);
            preparedStatementInsertCantidad.executeUpdate();

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean donarEjemplar(String isbn) {
        String querySelect = "SELECT cantidad FROM cantidadEjemplares WHERE id_libro = ?";
        String queryUpdate = "UPDATE cantidadEjemplares SET cantidad = cantidad + 1 WHERE id_libro = ?";

        try (
            PreparedStatement preparedStatementSelect = connection.prepareStatement(querySelect)) {

            preparedStatementSelect.setString(1, isbn);
            ResultSet resultSet = preparedStatementSelect.executeQuery();

            if (resultSet.next()) {
                // Incrementar la cantidad de ejemplares disponibles
                try (PreparedStatement preparedStatementUpdate = connection.prepareStatement(queryUpdate)) {
                    preparedStatementUpdate.setString(1, isbn);
                    preparedStatementUpdate.executeUpdate();
                    return true; // Donación exitosa
                }
            } else {
                System.out.println("El libro con ISBN " + isbn + " no existe en la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
    
      
}