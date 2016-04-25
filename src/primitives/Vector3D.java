package primitives;

import javax.vecmath.Tuple4f;

public class Vector3D extends Tuple4f
{
    public final static Vector3D UNIT = new Vector3D(1, 1, 1);

    /**
     * Crea el vector [0,0,0].
     */
    public Vector3D()
    {
        super(0, 0, 0, 0);
    }

    /**
     * Crea un nuevo vector que es copia de otro.
     *
     * @param v Vector a copiar
     */
    public Vector3D(Vector3D v)
    {
        super(v.x, v.y, v.z, v.w);
    }

    /**
     * Crea un nuevo vector utilizando como valores la posicion de un punto.
     *
     * @param p El punto
     */
    public Vector3D(Point3D p)
    {
        super(p.x, p.y, p.z, 0);
    }

    /**
     * Crea un nuevo vector dado en coordenadas x,y,z.
     *
     * @param x
     * @param y
     * @param z 
     */
    public Vector3D(float x, float y, float z)
    {
        super(x, y, z, 0);
    }
    
    /**
     * Crea un nuevo vector dado en coordenadas x,y,z,w.
     *
     * @param x
     * @param y
     * @param z
     * @param w
     */
    public Vector3D(final float x, final float y, final float z, final float w)
    {
        super(x, y, z, w);
    }

    /**
     * Define un nuevo vector en base a dos puntos, uno de origen y otro de
     * destino.
     *
     * @param start
     * @param end 
     */
    public Vector3D(final Point3D start, final Point3D end)
    {
        super(end.x - start.x, end.y - start.y, end.z - start.z, 0);
    }

    /**
     * Retorna un nuevo vector que es copia de este.
     *
     * @return 
     */
    public Vector3D copy()
    {
        return new Vector3D(this);
    }

    /**
     * Producto cruz entre este vector y el vector proporcionado.
     *
     * @param v Segundo vector
     * @return Un nuevo vector que es perpendicular a este vector y
     * al vector proporcionado
     */
    public Vector3D cross(Vector3D v)
    {
        float a = this.y * v.z - v.y * this.z;
        float b = this.z * v.x - v.z * this.x;
        float c = this.x * v.y - v.x * this.y;

        return new Vector3D(a, b, c);
    }

    /**
     * Producto punto, también llamado producto escalar, entre este vector y el
     * vector proporcionado.
     *
     * @param v Segundo vector
     * @return Resultado del producto escalar
     */
    public float dot(Vector3D v)
    {
        return
            (this.x * v.x) +
            (this.y * v.y) +
            (this.z * v.z);
    }

    /**
     * Normaliza este vector, es decir, divide sus componentes por el módulo.
     *
     * @return Este mismo vector pero normalizado
     */
    public Vector3D normalize()
    {
        float m = this.length();
        this.x = this.x / m;
        this.y = this.y / m;
        this.z = this.z / m;
        return this;
    }

    /**
     * Invierte este vector.
     *
     * @return Este vector, pero invertido
     */
    public Vector3D invert()
    {
        return this.mul(-1f);
    }

    /**
     * Calcula el módulo de este vector.
     *
     * @return Módulo de este vector
     */
    public float length()
    {
        return (float) Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    /**
     * Retorna un vector escalado segun el parametro `s`.
     *
     * @param s Factor de escala
     * @return Un nuevo vector escalado
     */
    public Vector3D mul(float s)
    {
        this.x *= s;
        this.y *= s;
        this.z *= s;
        return this;
    }

    /**
     * Suma a este vector el vector "a".
     * 
     * @param a
     * @return 
     */
    public Vector3D add(Vector3D a)
    {
        this.x += a.x;
        this.y += a.y;
        this.z += a.z;
        this.w += a.w;
        return this;
    }
    
    /**
     * Este vector se convierte en la suma de "a" y "b".
     *
     * @param a
     * @param b
     * @return this
     */
    public Vector3D add(Vector3D a, Vector3D b)
    {
        this.x = a.x + b.x;
        this.y = a.y + b.y;
        this.z = a.z + b.z;
        this.w = a.w + b.w;
        return this;
    }

    /**
     * Retorna un nuevo vector tras realizar una diferencia de componentes
     * entre este vector y el proporcionado.
     *
     * @param a
     * @return this
     */
    public Vector3D sub(Vector3D a)
    {
        this.x -= a.x;
        this.y -= a.y;
        this.z -= a.z;
        this.w -= a.w;
        return this;
    }
    
    /**
     * Este vector se convierte en la diferencia de "a" y "b".
     *
     * @param a
     * @param b
     * @return this
     */
    public Vector3D sub(Vector3D a, Vector3D b)
    {
        this.x = a.x - b.x;
        this.y = a.y - b.y;
        this.z = a.z - b.z;
        this.w = a.w - b.w;
        return this;
    }

    /**
     * {@inheritDoc}
     *
     * @return 
     */
    @Override
    public String toString()
    {
        return "vector: {" + this.x +", " + this.y + ", " + this.z + ", " + this.w +  "}";
    }
}
