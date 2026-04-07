package fr.cpe.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.cpe.model.Abilite;

public class AbiliteDAO implements IDAO<Abilite> {

    @Override
    public Optional<Abilite> get(int id) {
        String sql = "SELECT * FROM Abilite WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Abilite abilite = new Abilite(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description")
                );
                return Optional.of(abilite);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Abilite> getAll() {
        String sql = "SELECT * FROM Abilite";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
             List<Abilite> abiliteList = new ArrayList<>();
             while(rs.next()) {
                Abilite abilite = new Abilite(
                    rs.getInt("id"),
                    rs.getString("nom"),
                    rs.getString("description")
                );
                abiliteList.add(abilite);
            }
            return abiliteList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public void save(Abilite t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Abilite t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Abilite t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
