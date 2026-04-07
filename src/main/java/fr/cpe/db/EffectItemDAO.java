package fr.cpe.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.cpe.model.EffectItem;
import fr.cpe.model.StatType;

public class EffectItemDAO implements IDAO<EffectItem> {

    @Override
    public Optional<EffectItem> get(int id) {
        String sql = "SELECT * FROM EffectItem WHERE id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                Map<StatType, Integer> stats = new HashMap<StatType, Integer>() {{
                    put(StatType.Atk, rs.getInt("attack"));
                    put(StatType.AtkSpe, rs.getInt("special_attack"));
                    put(StatType.Def, rs.getInt("defense"));
                    put(StatType.DefSpe, rs.getInt("special_defense"));
                    put(StatType.Spd, rs.getInt("speed"));
                }};
                EffectItem EffectItem = new EffectItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    stats

                );
                return Optional.of(EffectItem);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<EffectItem> getAll() {
        String sql = "SELECT * FROM EffectItem";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql);
            var rs = stmt.executeQuery()
        ) {
             List<EffectItem> itemList = new ArrayList<>();
             while(rs.next()) {
                Map<StatType, Integer> stats = new HashMap<StatType, Integer>() {{
                    put(StatType.Atk, rs.getInt("attack"));
                    put(StatType.AtkSpe, rs.getInt("special_attack"));
                    put(StatType.Def, rs.getInt("defense"));
                    put(StatType.DefSpe, rs.getInt("special_defense"));
                    put(StatType.Spd, rs.getInt("speed"));
                }};
                 itemList.add(new EffectItem(
                    rs.getInt("id"),
                    rs.getString("name"),
                    stats
                ));
             }
             return itemList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return List.of();
    }

    @Override
    public void save(EffectItem t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(EffectItem t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(EffectItem t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

}
