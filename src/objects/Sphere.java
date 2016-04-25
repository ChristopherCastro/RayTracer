package objects;

import shader.Material;
import primitives.Point3D;
import tracer.Hit;
import tracer.Ray;
import primitives.Vector3D;

public class Sphere extends Object3D
{

    protected Point3D center;
    protected float radius;

    public Sphere(final Material m, final Point3D center, final float radius)
    {
        this.material = m;
        this.center = center;
        this.radius = radius;
    }

    @Override
    public Hit intersect(Ray r, float tmin)
    {
        // Realistic Ray Tracing, Second Edition -> pag 76
        Hit h = Hit.voidHit;
        float t = Float.NaN;
        
        Vector3D tmp = this.center.sub(r.getOrigin()); // C - R
        float c = tmp.dot(tmp) - this.radius * this.radius;

        if (c > 0) {
            float b =  tmp.dot(r.getDirection());
            if (b >= 0) {
                if (c == b * b) {
                    // tangente
                    t = b;
                } else if (c < b * b) {
                    // corte
                    float d = (float) Math.sqrt(b * b - c);
                    float tp = b + d;
                    float tm = c / tp;
                    t = Math.min(tp, tm);
                }
            }
        }

        if (t > EPSILON && t > tmin) {
            Point3D impact = r.pointAtParameter(t);
            Vector3D normal = impact.sub(this.center);
            h = new Hit(this, r, impact, normal.normalize(), t);
        }

        return h;
    }

    @Override
    public String toString()
    {
        return "Sphere [" + this.id + "] -> radius = " + this.radius + "; center = " + this.center + "; material = " + this.material;
    }
}
