package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Editora;
import model.Livro;
import util.ConnectionJDBC;

public class LivroDAO {

    Connection connection;

    public LivroDAO() throws Exception {
        //Obtém uma conexão 
        connection = ConnectionJDBC.getConnection();
    }

    public void save(Livro livro) throws Exception {
        String SQL = "INSERT INTO LIVRO(EDITORA_ID, TITULO, ANO, DESCRICAO) "
                + " VALUES(?, ?, ?, ?)";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            //p.setInt(1, livro.getLivro_id());
            p.setInt(1, livro.getEditora().getEditora_id() );
            p.setString(2, livro.getTitulo());
            p.setInt(3, livro.getAno());
            p.setString(4, livro.getDescricao());
            p.execute();
            p.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

    public void update(Livro livro) throws Exception {
        String SQL = "UPDATE LIVRO SET EDITORA_ID=?,TITULO=?,ANO=?,DESCRICAO=? WHERE LIVRO_ID=?";
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getEditora().getEditora_id());
            p.setString(2, livro.getTitulo());
            p.setInt(3, livro.getAno());
            p.setString(4, livro.getDescricao());
            p.setInt(5, livro.getLivro_id());
            p.execute();
            p.close();
        } catch (SQLException ex) {
            throw new Exception(ex);
        }
    }

    public void delete(Livro livro) {
        String SQL = "DELETE FROM LIVRO WHERE LIVRO_ID=?";

        PreparedStatement p;
        try {
            p = connection.prepareStatement(SQL);
            p.setInt(1, livro.getLivro_id());
            p.execute();
            p.close();
        } catch (SQLException ex) {
            Logger.getLogger(LivroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    public Livro findById(int id) throws Exception {
        Livro objeto = new Livro();
        String SQL = "SELECT L.*, E.NOME, E.MUNICIPIO FROM LIVRO L"
                + " INNER JOIN EDITORA E ON E.EDITORA_ID = L.EDITORA_ID "
                + "WHERE LIVRO_ID = ?";
        
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            p.setInt(1, id);
            // Executa a SQL e mantém os valores no ResultSet rs
            ResultSet rs = p.executeQuery();
            // Navega pelos registros no rs
            while (rs.next()) {
                // Instancia a classe Livro e informa os valores do banco
                objeto = new Livro();
                objeto.setLivro_id(rs.getInt("livro_id"));
                objeto.setTitulo(rs.getString("titulo"));
                objeto.setAno( rs.getInt("ano") );
                objeto.setDescricao( rs.getString("descricao") );
                
                Editora editora = new Editora();
                editora.setEditora_id( rs.getInt("editora_id") );
                editora.setNome_editora(rs.getString("nome"));
                editora.setMun_editora(rs.getString("municipio"));
                
                objeto.setEditora(editora);
            }
            rs.close();
            p.close();

        } catch (SQLException ex) {
            throw new Exception(ex);
        }
        return objeto;
    }

    public List<Livro> findAll() throws Exception {
        //Lista para manter os valores do resultset
        List<Livro> list = new ArrayList<>();
        Livro objeto;
        String SQL = "SELECT L.*, E.NOME FROM LIVRO L"
                + " INNER JOIN EDITORA E ON E.EDITORA_ID = L.EDITORA_ID "
                + "ORDER BY LIVRO_ID";
        
        try {
            PreparedStatement p = connection.prepareStatement(SQL);
            // Executa a SQL e mantém os valores no ResultSet rs
            ResultSet rs = p.executeQuery();
            // Navega pelos registros no rs
            while (rs.next()) {
                // Instancia a classe Livro e informa os valores do banco
                objeto = new Livro();
                objeto.setLivro_id(rs.getInt("livro_id"));
                objeto.setTitulo(rs.getString("titulo"));
                objeto.setAno( rs.getInt("ano") );
                objeto.setDescricao( rs.getString("descricao") );
                
                Editora editora = new Editora();
                editora.setEditora_id( rs.getInt("editora_id") );
                editora.setNome_editora(rs.getString("nome"));
                
                objeto.setEditora(editora);
                //Inclui na lista
                list.add(objeto);
            }
            rs.close();
            p.close();

        } catch (SQLException ex) {
            throw new Exception(ex);
        }
        // Retorna a lista
        return list;
    }
    public static void main(String[] args) {
        try {
            new LivroDAO().findAll();
        } catch (Exception ex) {
            Logger.getLogger(LivroDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
