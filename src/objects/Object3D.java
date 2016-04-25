package objects;

import shader.Material;
import tracer.Hit;
import tracer.Ray;

public abstract class Object3D
{

    /**
     * Identificador único de objeto, utilizado para depurar.
     */
    protected String id;

    /**
     * Constante para valor "cero".
     */
    public final static float EPSILON = 0.00001f;

    /**
     * Metrial del objeto.
     */
    protected Material material;

    /**
     * Intersección rayo-objeto.
     *
     * @param r El rayo a trazar
     * @param tmin Valor mínimo del parametro T
     * @return 
     */
    abstract public Hit intersect(final Ray r, final float tmin);

    //abstract public boolean intersect(final ShadowRay r);

    /**
     * Establece un identificador a este objeto.
     *
     * @param id 
     */
    public void setId(final String id)
    {
        this.id = id;
    }

    /**
     * Retorna el identificador de este objeto.
     *
     * @return 
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Retorna la información de material de este objeto.
     *
     * @return this.material
     */
    public Material getMaterial()
    {
        return this.material;
    }

    /**
     * Establece nueva información de material para este objeto.
     *
     * @param m El nuevo material
     */
    public void setMaterial(Material m)
    {
        this.material = m;
    }
}
