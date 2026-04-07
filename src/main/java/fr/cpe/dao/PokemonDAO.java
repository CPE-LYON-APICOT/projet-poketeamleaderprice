package fr.cpe.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import fr.cpe.model.Attaque;
import fr.cpe.model.Pokemon;
import fr.cpe.model.StatType;
import fr.cpe.model.Type;

public class PokemonDAO implements IDAO<Pokemon> {

    @Override
    public Optional<Pokemon> get(int id) {
        String sql = "SELECT * FROM Pokemon WHERE id = ?";
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
                    this.getLesAttaquesDisponibles(rs.getInt("id")),
                    this.getLesAttaquesPrises(rs.getInt("id")),
                    rs.getInt("hp"),
                    stats,
                    rs.getString("imageFacePath"),
                    rs.getString("imageDosPath"),
                    rs.getString("imageSpritePath"),
                    rs.getString("description"),
                    new AbiliteDAO().get(rs.getInt("abilityId")).orElse(null)
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
        String sql = "SELECT * FROM Pokemon";
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
                    this.getLesAttaquesDisponibles(rs.getInt("id")),
                    this.getLesAttaquesPrises(rs.getInt("id")),
                    rs.getInt("hp"),
                    stats,
                    rs.getString("imageFacePath"),
                    rs.getString("imageDosPath"),
                    rs.getString("imageSpritePath"),
                    rs.getString("description"),
                    new AbiliteDAO().get(rs.getInt("abilityId")).orElse(null)
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
                    rs.getInt("index"),
                    rs.getString("name")
                ));
            }
            return pokemonList;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private Attaque[] getLesAttaquesDisponibles(int pokemonId) {
        String sql = "SELECT * FROM type, pokemon, pokemon_type pt join on type.id = pt.type_id WHERE pt.pokemon_id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, pokemonId);
            var rs = stmt.executeQuery();
            List<Attaque> attaqueList = new ArrayList<>();
            while(rs.next()) {
                attaqueList.add(new Attaque(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("power"),
                    rs.getInt("accuracy"),
                    rs.getInt("pp"),
                    new TypeDAO().get(rs.getInt("type_id")).orElseThrow()
                ));
            }
            return attaqueList.toArray(new Attaque[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Attaque[0];
        }
    }

    private Attaque[] getLesAttaquesPrises(int pokemonId) {
        String sql = "SELECT * FROM attaque, pokemon, pokemon_attaque pa join on attaque.id = pa.attaque_id WHERE pa.pokemon_id = ?";
        try (
            var cnx = DBSingleton.getInstance().getConnection();
            var stmt = cnx.prepareStatement(sql)
        ) {
            stmt.setInt(1, pokemonId);
            var rs = stmt.executeQuery();
            List<Attaque> attaqueList = new ArrayList<>();
            while(rs.next()) {
                attaqueList.add(new Attaque(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getInt("power"),
                    rs.getInt("accuracy"),
                    rs.getInt("pp"),
                    new TypeDAO().get(rs.getInt("type_id")).orElseThrow()
                ));
            }
            return attaqueList.toArray(new Attaque[0]);
        } catch (SQLException e) {
            e.printStackTrace();
            return new Attaque[0];
        }
    }
}
