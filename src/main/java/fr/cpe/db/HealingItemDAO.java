package fr.cpe.db;

import java.util.List;
import java.util.Optional;

import fr.cpe.model.HealingItem;

public class HealingItemDAO implements IDAO<HealingItem> {

    @Override
    public Optional<HealingItem> get(int id) {
        String sql = "SELECT * FROM HealingItem WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                HealingItem healingItem = new HealingItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("hp_heal")
                );
                return Optional.of(healingItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<HealingItem> getAll() {
        String sql = "SELECT * FROM HealingItem";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
             List<HealingItem> itemList = new java.util.ArrayList<>();
             while(rs.next()) {
                HealingItem healingItem = new HealingItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("hp_heal")
                );
                itemList.add(healingItem);
             }
             return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public void save(HealingItem t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(HealingItem t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(HealingItem t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }


}
