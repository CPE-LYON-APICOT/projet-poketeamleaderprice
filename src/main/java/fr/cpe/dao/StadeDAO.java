package fr.cpe.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Stade;
import fr.cpe.model.Type;

public class StadeDAO implements IDAO<Stade> {

    @Override
    public Optional<Stade> get(int id) {
        String sql = "SELECT * FROM stade WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Stade stade = new Stade(
                    rs.getInt("id"),
                    rs.getString("name"),
                    this.getType(rs.getInt("id"))
                );
                return Optional.of(stade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Stade> getAll() {
        String sql = "SELECT * FROM Stade";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            var rs = stmt.executeQuery();
            List<Stade> StadeList = new ArrayList<>();
            while(rs.next()) {
                StadeList.add(new Stade(
                    rs.getInt("id"),
                    rs.getString("name"),
                    this.getType(rs.getInt("id"))
                ));
            }
            return StadeList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Stade t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Stade t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Stade t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private Type getType(int typeId) {
        String sql = "SELECT * FROM abilite WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, typeId);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                return new Type(
                    rs.getInt("id"),
                    rs.getString("name")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
