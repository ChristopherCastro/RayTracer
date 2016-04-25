package tracer;

import primitives.Point3D;
import primitives.Vector3D;

/**
 * Representa un rayo de sombra.
 */
public class ShadowRay extends Ray
{
    
    /**
     * Valor máximo del parámetro t.
     */
    private float end;

    /**
     * Constrcutor.
     *
     * @param R
     * @param v 
     */
    public ShadowRay(Point3D R, Vector3D v)
    {
        super(R, v);
    }

    /**
     * @return the end
     */
    public float getEnd()
    {
        return end;
    }

    /**
     * @param end the end to set
     */
    public void setEnd(float end)
    {
        this.end = end;
    }
}
