package primitives;

import javax.vecmath.Tuple4f;

public class Point3D extends Tuple4f
{
    /**
     * Constante para valor "cero".
     */
    public final static float EPSILON = 0.00001f;

    /**
     * Crea un nuevo punto que es copia de otro.
     *
     * @param p Punto a copiar
     */
    public Point3D(Point3D p)
    {
        super(p.x, p.y, p.z, p.w);
    }

    /**
     * Crea un nuevo punto en el espacio dadas sus coordenadas x,y,z.
     *
     * @param x
     * @param y
     * @param z 
     */
    public Point3D(final float x, final float y, final float z)
    {
        super(x, y, z, 1);
    }

    /**
     * Crea un nuevo punto en el espacio dadas sus coordenadas x,y,z,w.
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public Point3D(float x, float y, float z, float w)
    {
        super(x, y, z, w);
    }
    
    /**
     * Retorna un nuevo vector que es copia de este.
     *
     * @return 
     */
    public Point3D copy()
    {
        return new Point3D(this);
    }

    /**
     * Calcula la distancia entre este punto y el punto dado.
     *
     * @param p
     * @return Distancia euclidea
     */
    public float distanceTo(final Point3D p)
    {
        Vector3D v = new Vector3D(p, this);
        return (float)Math.abs(v.length());
    }

    /**
     * Suma este punto con el vector dado, lo que es equivalente a desplazar
     * este punto según las magnitudes indicadas por el vector.
     *
     * @param v Vector de desplazamiento
     * @return this Este punto desplazado
     */
    public Point3D add(final Vector3D v)
    {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    /**
     * Similar a add() pero multiplicando.
     *
     * @param v Vector de desplazamiento
     * @return Este punto desplazado
     */
    public Point3D mul(final Vector3D v)
    {
        this.x *= v.x;
        this.y *= v.y;
        this.z *= v.z;
        return this;
    }

    /**
     * Substracción de puntos afín. Retorna el vector formado por este punto
     * como destino y el punto dado como origen.
     *
     * @param origin Punto de partida
     * @return Un vector formado por (this - origin)
     */
    public Vector3D sub(final Point3D origin)
    {
        return new Vector3D(origin, this);
    }

    /**
     * Crea un nuevo vector cuyas componentes son las de este punto.
     *
     * @return Un vector
     */
    public Vector3D toVector3D()
    {
        return new Vector3D(this);
    }

    /**
     * Compara dos puntos.
     *
     * @param p
     * @return 
     */
    public boolean equals(Point3D p)
    {
        return
            (this.x - p.x) <= EPSILON &&
            (this.y - p.y) <= EPSILON &&
            (this.z - p.z) <= EPSILON;
    }

    /**
     * {@inheritDoc}
     *
     * @return 
     */
    @Override
    public String toString()
    {
        return "point: {" + this.x +", " + this.y + ", " + this.z + "}";
    }    
}
