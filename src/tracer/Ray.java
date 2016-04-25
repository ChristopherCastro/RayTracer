package tracer;

import primitives.Point3D;
import primitives.Vector3D;

public class Ray
{
    protected Point3D origin;
    protected Vector3D direction;
    public static Ray voidRay = new Ray();

    private Ray()
    {
    }

    public Ray(final Point3D R, final Vector3D v)
    {
        this.origin = R;
        this.direction = v.normalize();
    }

    public Ray(final Point3D R, final Point3D Q)
    {
        this.origin = R;
        this.direction = Q.sub(R).normalize();
    }

    public Point3D pointAtParameter(final float t)
    {
        return new Point3D(
            this.origin.x + (t * this.direction.x),
            this.origin.y + (t * this.direction.y),
            this.origin.z + (t * this.direction.z)
        );
    }

    public Point3D getOrigin()
    {
        return this.origin;
    }
    
    public Vector3D getDirection()
    {
        return this.direction;
    }

    @Override
    public String toString()
    {
        return "Ray -> " + this.origin + "; " + this.direction;
    }
}
