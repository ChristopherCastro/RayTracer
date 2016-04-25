package lights;

import java.util.ArrayList;

public class LightGroup
{

    /**
     * Colección de luces.
     */
    protected ArrayList<Light> collection;

    /**
     * Constructor.
     */
    public LightGroup()
    {
        this.collection = new ArrayList<>();
    }

    /**
     * Añade una nueva fuente a la colección.
     *
     * @param light
     * @return 
     */
    public LightGroup addLight(final Light light)
    {
        if (light != null) {
            this.collection.add(light);
        }
        return this;
    }

    /**
     * Retorna la colección de luces.
     *
     * @return 
     */
    public final ArrayList<Light> getCollection()
    {
        return this.collection;
    }

    @Override
    public String toString()
    {
        String s = "LightsGroup ->\n";
        for (Light light : this.collection) {
            s += "    " + light + ";\n";
        }
        return s;
    }
}
