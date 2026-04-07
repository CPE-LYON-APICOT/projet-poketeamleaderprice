package fr.cpe.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Type;

public class TypeDAO implements IDAO<Type> {

    @Override
    public Optional<Type> get(int id) {
        String sql = "SELECT * FROM Type WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Type type = new Type(
                    rs.getInt("id"),
                    rs.getString("name")
                );
                return Optional.of(type);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Type> getAll() {
        String sql = "SELECT * FROM Type";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
             List<Type> pokemonList = new ArrayList<>();
             while(rs.next()) {
                 pokemonList.add(new Type(
                    rs.getInt("id"),
                    rs.getString("name")
                ));
             }
             return pokemonList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public void save(Type t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Type t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Type t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public List<Type> getFaiblesses(int int1) {
        String sql = "SELECT * FROM Type, Type_Type_Faiblesses tt join on Type.id = tt.type_id WHERE tt.type_id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, int1);
            var rs = stmt.executeQuery();
            List<Type> typeList = new ArrayList<>();
            while(rs.next()) {
                typeList.add(new Type(
                    rs.getInt("index"),
                    rs.getString("name")
                ));
            }
            return typeList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Type> getAvantages(int int1) {
        String sql = "SELECT * FROM Type, Type_Type_Avantages tt join on Type.id = tt.type_id WHERE tt.type_id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, int1);
            var rs = stmt.executeQuery();
            List<Type> typeList = new ArrayList<>();
            while(rs.next()) {
                typeList.add(new Type(
                    rs.getInt("index"),
                    rs.getString("name")
                ));
            }
            return typeList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
