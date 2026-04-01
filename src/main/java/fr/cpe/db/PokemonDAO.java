package fr.cpe.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.cpe.model.Abilite;
import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.model.StatType;
import fr.cpe.model.Type;

public class PokemonDAO implements IDAO<Pokemon> {

    @Override
    public Optional<Pokemon> get(int id) {
        String sql = "SELECT * FROM pokemon WHERE id = ?";
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
                Pokemon pokemon = new Pokemon(
                    rs.getInt("id"),
                    rs.getString("name"),
                    this.getTypes(rs.getInt("id")),
                    this.getLesAttaquesDisponibles(),
                    this.getLesAttaquesPrises(),
                    rs.getInt("hp"),
                    stats,
                    rs.getString("imageFacePath"),
                    rs.getString("imageDosPath"),
                    rs.getString("imageSpritePath"),
                    rs.getString("description").charAt(0),
                    this.getAbility(rs.getInt("abilityId"))
                );
                return Optional.of(pokemon);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Pokemon> getAll() {
        String sql = "SELECT * FROM pokemon";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            var rs = stmt.executeQuery();
            List<Pokemon> pokemonList = new ArrayList<>();
            while(rs.next()) {
                Map<StatType, Integer> stats = new HashMap<StatType, Integer>() {{
                    put(StatType.Atk, rs.getInt("attack"));
                    put(StatType.AtkSpe, rs.getInt("special_attack"));
                    put(StatType.Def, rs.getInt("defense"));
                    put(StatType.DefSpe, rs.getInt("special_defense"));
                    put(StatType.Spd, rs.getInt("speed"));
                }};
                pokemonList.add(new Pokemon(
                    rs.getInt("id"),
                    rs.getString("name"),
                    this.getTypes(rs.getInt("id")),
                    this.getLesAttaquesDisponibles(),
                    this.getLesAttaquesPrises(),
                    rs.getInt("hp"),
                    stats,
                    rs.getString("imageFacePath"),
                    rs.getString("imageDosPath"),
                    rs.getString("imageSpritePath"),
                    rs.getString("description").charAt(0),
                    this.getAbility(rs.getInt("abilityId"))
                ));
            }
            return pokemonList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public void save(Pokemon t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

    @Override
    public void update(Pokemon t, String[] params) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Pokemon t) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    private List<Type> getTypes(int pokemonId) {
        String sql = "SELECT * FROM type, pokemon, pokemon_type pt join on type.id = pt.type_id WHERE pt.pokemon_id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, pokemonId);
            var rs = stmt.executeQuery();
            List<Type> pokemonList = new ArrayList<>();
            while(rs.next()) {
                pokemonList.add(new Type(
                    rs.getString("index"),
                    rs.getString("name"),
                    new TypeDAO().getFaiblesses(rs.getInt("id")),
                    new TypeDAO().getAvantages(rs.getInt("id"))
                ));
            }
            return pokemonList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Attaque[] getLesAttaquesDisponibles() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLesAttaquesDisponibles'");
    }

    private Attaque[] getLesAttaquesPrises() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getLesAttaquesPrises'");
    }

    private Abilite getAbility(int abilityId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAbility'");
    }


}
