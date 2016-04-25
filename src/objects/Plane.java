package objects;

import shader.Material;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

public class Plane extends Object3D
{

    protected Point3D point;
    protected Vector3D normal;

    public Plane(final Material m, final Point3D p, final Vector3D n)
    {
        this.material = m;
        this.point = p;
        this.normal = n.normalize();
    }

    @Override
    public Hit intersect(final Ray r, final float tmin)
    {
        float t = Float.NaN;
        Hit h = Hit.voidHit;

        // http://www.physicsforums.com/showthread.php?t=229813, check if plane on right side
        // https://www.cs.princeton.edu/courses/archive/fall00/cs426/lectures/raycast/sld017.htm
        // http://people.cs.clemson.edu/~dhouse/courses/881/notes/raycast.pdf
        float b = r.getDirection().dot(this.normal);
        if (-b > EPSILON) {
            float a = this.normal.dot(r.getOrigin().sub(this.point));
            t = -a / b;
        }

        if (t > EPSILON && t > tmin) {
            Point3D impact = r.pointAtParameter(t);
            h = new Hit(this, r, impact, this.normal, t);
        }

        return h;
    }
    
    @Override
    public String toString()
    {
        return "Plane [" + this.id + "] -> p = " + this.point + "; n = " + this.normal + "; matarial = " + this.material;
    }
}
