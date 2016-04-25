package shader;

import java.util.HashMap;

/**
 * Clase utilizada por el parser para agrupar varias instancias de materiales
 * y de esa forma evitar crear multiples instancias similares a la hora de
 * definir objetos.
 *
 * @author chris
 */
public class MaterialCollection
{

    protected HashMap<String, Material> collection = new HashMap();

    /**
     * @param k
     * @return 
     */
    public Material getMaterial(final String k)
    {
        return this.collection.get(k);
    }
    
    /**
     * @param m
     */
    public void addMaterial(final Material m)
    {
        this.collection.put(m.getId(), m);
    }
}
