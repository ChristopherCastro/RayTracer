package objects;

import shader.Material;
import javax.vecmath.Matrix3f;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

public class Triangle extends Object3D
{
    
    protected Point3D t1, t2, t3;
    protected Vector3D v1, v2, normal;

    /**
     * Se deben proporcionar los puntos en sentido anti-horario. El vector
     * normal se calcula siguiendo la regla de la mano derecha.
     *
     * @param m Meterial
     * @param t1 Primer punto
     * @param t2 Segundo punto
     * @param t3 Tercer punto
     */
    public Triangle(final Material m, final Point3D t1, final Point3D t2, final Point3D t3)
    {
        this.material = m;
        this.t1 = t1; // A
        this.t2 = t2; // B
        this.t3 = t3; // C
        this.v1 = t2.sub(t1); // B - A
        this.v2 = t3.sub(t1); // C - A
        this.normal = this.v1.cross(this.v2).normalize();
    }

    @Override
    public Hit intersect(final Ray r, float tmin)
    {
        // https://www.cs.princeton.edu/courses/archive/fall00/cs426/lectures/raycast/sld018.htm
        Hit h = Hit.voidHit;
        float c = r.getDirection().dot(this.normal);
        float t = Float.NEGATIVE_INFINITY;

        if (c < 0) { // Interseccion por cara exterior
            Vector3D ar = r.getOrigin().sub(this.t1);
            float b = ar.dot(this.normal);

            if (b >= 0) { // Interseccion en semiespacio posterior
                float solutions[] = solve(this.v1, this.v2, r.getDirection(), ar);
                float beta = solutions[0];
                float gamma = solutions[1];
                float alpha = -b / c;

                if (beta >= 0 &&
                    beta <= 1 &&
                    gamma >= 0 &&
                    gamma <= 1 &&
                    (beta + gamma) <= 1
                ) {
                    t = alpha;
                }
            }
        }

        if (t > EPSILON && t > tmin) {
            Point3D impact = r.pointAtParameter(t);
            h = new Hit(this, r, impact, this.normal, t); 
        }

        return h;
    }

    protected float[] solve(final Vector3D ba, final Vector3D ca, final Vector3D v, final Vector3D ra)
    {
        float down = v.dot(ca.cross(ba));
        float X = ca.dot(v.cross(ra));
        float Y = ba.dot(v.cross(ra));

        float beta = -X / down;
        float gamma = Y / down;

        return new float[]{beta, gamma};
    }

    @Override
    public String toString()
    {
        return "Triangle [" + this.id + "] -> A = " + this.t1 + "; B = " + this.t2 + "; C = " + this.t3 + "; normal = " + this.normal + "; BA = " + this.v1 + "; CA = " + this.v2;
    }
}
