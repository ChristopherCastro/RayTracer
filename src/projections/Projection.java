package projections;

import camera.Camera;
import tracer.RayGenerator;

public abstract class Projection {
    
    public float distance;
    public float width;
    public float height;

    public float getDistance()
    {
        return this.distance;
    }

    public float getWidth()
    {
        return this.width;
    }

    public float getHeight()
    {
        return this.height;
    }

    public abstract RayGenerator getRayGenerator(final Camera c, final int w, final int h);
}
