package tracer;

import objects.Object3D;
import primitives.Point3D;
import primitives.Vector3D;

public class Hit
{

    protected final static float EPSILON = 0.00001f;
    
    /**
     * Rayo que originó este hit.
     */
    protected Ray ray;

    /**
     * Parámetro t del rayo.
     */
    protected float t;

    /**
     * Punto de impacto en el espacio.
     */
    protected Point3D point;

    /**
     * Vectopr normal a la superficie de impacto.
     */
    protected Vector3D normal;

    /**
     * El objeto sobre el cual se ha producido el impacto.
     */
    protected Object3D object;

    /**
     * Instancia unica que simboliza un impacto nulo.
     */
    public static Hit voidHit = new Hit();

    private Hit()
    {
    }

    public Hit(final Object3D obj, final Ray ray, final Point3D p, final Vector3D n, final float t)
    {
        this.ray = ray;
        this.point = p;
        this.t = t;
        this.normal = n;
        this.object = obj;
    }

    public boolean hits()
    {
        return !this.equals(Hit.voidHit);
    }

    public Vector3D getNormal()
    {
        return this.normal;
    }

    public float getT()
    {
        return this.t;
    }

    public Object3D getObject()
    {
        return this.object;
    }

    public Point3D getPoint()
    {
        return this.point;
    }

    public Ray getRay()
    {
        return this.ray;
    }

    public Ray getReflectedRay()
    {
        return this.getReflectedRay(this.ray);
    }
        
    public Ray getRefractedRay()
    {
        return this.getRefractedRay(this.ray);
    }

    /**
     * Dado un rayo y este punto de impacto, retorna un nuevo rayo que es el
     * reflejo del proporcionado.
     *
     * @param r El rayo original para el cual computar su reflexión
     * @return El rayo reflejado
     */
    public Ray getReflectedRay(final Ray r)
    {
        if (!this.object.getMaterial().isSpecular()) {
            return null;
        }

        // I = l - 2 * (n · l) * n
        Vector3D direction = this.normal.copy();
        Vector3D l = r.getDirection().copy();
        float aux = direction.dot(l) * -1f;
        direction.scale(2 * aux);
        direction.add(l);

        return new Ray(this.point, direction);
    }

    /**
     * Dado un rayo y el punto de impacto (con su material asociado), retorna
     * un rayo refractado del proporcionado.
     *
     * @param r
     * @return 
     */
    public Ray getRefractedRay(final Ray r)
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "Hit -> " + this.point + "; " + this.normal;
    }
}
