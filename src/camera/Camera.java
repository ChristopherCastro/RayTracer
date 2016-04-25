package camera;

import projections.Projection;
import primitives.Matrix4D;
import tracer.RayGenerator;
import primitives.Point3D;
import primitives.Vector3D;

public final class Camera
{

    protected Point3D position;
    protected Point3D lookAt;
    protected Vector3D look;
    protected Vector3D up;
    protected Matrix4D transformation;
    protected Projection lente;    

    public Camera(final Point3D position, final Point3D lookAt, final Vector3D up)
    {
        this.position = position;
        this.lookAt = lookAt;
        this.look = lookAt.sub(position).normalize();
        this.up = up.copy().normalize();
        
        Vector3D w = this.look.copy().invert();
        Vector3D u = this.up.copy();

        float s = u.dot(w);
        float t = (float) (1 / Math.sqrt(1 - s * s));
        this.transformation = new Matrix4D(
            t * (u.y * w.z - u.z * w.y), t * (u.x - s * w.x), w.x, this.position.x,
            t * (u.z * w.x - u.x * w.z), t * (u.y - s * w.y), w.y, this.position.y,
            t * (u.x * w.y - u.y * w.x), t * (u.z - s * w.z), w.z, this.position.z,
            0.0f, 0.0f, 0.0f, 1f
        );
    }

    public RayGenerator getRayGenerator(final int W, final int H)
    {
        return this.lente.getRayGenerator(this, W, H);
    }

    public Matrix4D getTransformation()
    {
        return this.transformation;
    }
    
    public void setPosition(Point3D p)
    {
        this.position = p;
    }

    public Point3D getPosition()
    {
        return this.position;
    }
    
    public Vector3D getLook()
    {
        return this.look;
    }
    
    public Point3D getLookAt()
    {
        return this.lookAt;
    }
    
    public Vector3D getUp()
    {
        return this.up;
    }

    public void setProjection(final Projection p)
    {
        this.lente = p;
    }

    public Projection getProjection()
    {
        return this.lente;
    }

    @Override
    public String toString()
    {
        return "Camera: position = " + this.position + "; look = " + this.getLook() + "; up = " + this.getUp() + "; projection = " + this.lente;
    }
}
