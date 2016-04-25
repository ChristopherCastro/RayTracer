package projections;

import camera.Camera;
import primitives.Point3D;
import tracer.Ray;
import tracer.RayGenerator;

public class Perspective extends Projection
{

    protected float fov;
    protected float ar;

    /**
     * Proyección en perspectiva.
     *
     * @param distance Distancia entre el plano de proyección y el ojo
     * @param fov Angulo de visión EN GRADOS
     * @param ar Relación de aspecto del plano de proyección
     */
    public Perspective(float distance, float fov, float ar)
    {
        fov = (float)Math.toRadians(fov); // grados -> radianes
        this.distance = distance;
        this.fov = fov;
        this.ar = ar;
        this.height = (float)Math.tan(fov / 2f) * 2f * distance; // h
        this.width = ar * this.height; // w
    }

    @Override
    public RayGenerator getRayGenerator(Camera camera, int W, int H)
    {
        return new PerspectiveRayGenerator(camera, W, H);
    }

    @Override
    public String toString()
    {
        return "{fov: " + this.fov + "º, distance: " + this.distance + ", ar: " + this.ar + "}";
    }

    /**
     * Internal class
     */
    private static class PerspectiveRayGenerator extends RayGenerator
    {
        protected Camera camera;
        protected int W;
        protected int H;

        PerspectiveRayGenerator(final Camera c, final int W, final int H)
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
            float z = -this.camera.getProjection().getDistance();

            Point3D aux = new Point3D(x, y, z);
            Point3D pixelPosition = this.camera.getTransformation().times(aux); // de coordenadas camara -> escena

            return new Ray(this.camera.getPosition(), pixelPosition); // desde posición del ojo y pasando por posición en el grid
        }
    };
}
