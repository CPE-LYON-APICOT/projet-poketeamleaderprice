package fr.cpe.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Stade;

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
                    new TypeDAO().get(rs.getInt("type_id")).orElse(null)
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
                    new TypeDAO().get(rs.getInt("type_id")).orElse(null)
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
}
