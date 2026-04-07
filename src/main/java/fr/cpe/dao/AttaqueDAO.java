package fr.cpe.dao;

import java.util.List;
import java.util.Optional;

import fr.cpe.model.Attaque;

public class AttaqueDAO implements IDAO<Attaque> {

    @Override
    public Optional<Attaque> get(int id) {
        String sql = "SELECT * FROM Attaque WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Attaque attaque = new Attaque(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("power"),
                    rs.getInt("accuracy"),
                    rs.getInt("pp"),
                    new TypeDAO().get(rs.getInt("type_id")).orElse(null)
                );
                return Optional.of(attaque);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Attaque> getAll() {
        String sql = "SELECT * FROM Attaque";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
             List<Attaque> attaqueList = new java.util.ArrayList<>();
             while(rs.next()) {
                 attaqueList.add(new Attaque(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("power"),
                    rs.getInt("accuracy"),
                    rs.getInt("pp"),
                    new TypeDAO().get(rs.getInt("type_id")).orElse(null)
                ));
             }
             return attaqueList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public void save(Attaque t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Attaque t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Attaque t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
