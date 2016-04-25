package projections;

import camera.Camera;
import tracer.Ray;
import tracer.RayGenerator;
import primitives.Point3D;
import primitives.Vector3D;

public class Ortographic extends Projection
{

    public Ortographic(final float w, final float h)
    {
        this.width = w;
        this.height = h;
        this.distance = 0;
    }

    @Override
    public RayGenerator getRayGenerator(Camera c, int w, int h)
    {
        return new OrtographicRayGenerator(c, w, h);
    }

    @Override
    public String toString()
    {
        return "{w: " + this.width + ", h: " + this.height + "}";
    }

    protected static class OrtographicRayGenerator extends RayGenerator
    {
        protected Camera camera;
        protected int W;
        protected int H;

        OrtographicRayGenerator(final Camera c, final int W, final int H)
        {
            this.camera = c;
            this.W = W;
            this.H = H;
        }

        @Override
        public Ray getRay(final int m, final int n)
        {
            float w = this.camera.getProjection().getWidth();
            float W = this.W;
            float h = this.camera.getProjection().getHeight();
            float H = this.H;

            float x = (float)((m + 0.5f) * (w / W)) - (w * 0.5f);
            float y = (float)((n + 0.5f) * (h / H)) - (h * 0.5f);
            float z = 0;

            Point3D aux = new Point3D(x, y, z);
            Point3D pixelPosition = this.camera.getTransformation().times(aux); // de coordenadas camara -> escena
            Vector3D dir = (new Vector3D(this.camera.getLook()));

            return new Ray(pixelPosition, dir);
        }
    };
}
