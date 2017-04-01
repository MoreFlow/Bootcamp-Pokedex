package pl.temomuko.pokedexbootcampfinal.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pokemons")
public class Pokemon {

    @DatabaseField(id = true)
    private String id;

    @DatabaseField
    private String weight;

    @DatabaseField
    private String height;

    @DatabaseField
    private String name;

    @DatabaseField
    private String type;

    private Types[] types;

    public Pokemon() {
    }

    public String getWeight() {
        return weight;
    }

    public String getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public Types[] getTypes() {
        return types;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String formatTypes() {
        String output = "";
        Types[] types = getTypes();
        for (Types type : types) {
            output += type.getType().getName() + ", ";
        }
        return type = output.substring(0, output.length() - 2);
    }
}
