package projections;

import camera.Camera;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Ray;
import tracer.RayGenerator;

public class SemisphericalFisheye extends Projection
{

    /**
     * Constructor de proyección fisheye.
     *
     * @param k 
     */
    public SemisphericalFisheye(final float k)
    {
        this.height = 2 * k;
        this.width = 2 * k; // w
        this.distance = k; // h
    }

    /**
     * Retorna un generador de rayos para esta proyección.
     *
     * @param c Cámara
     * @param W Anchura del viewport
     * @param H Altura del viewport
     * @return Un generador de rayos
     */
    @Override
    public RayGenerator getRayGenerator(Camera c, int W, int H)
    {
        return new FisheyeRayGenerator(c, W, H);
    }

    /**
     * Internal class
     */
    private static class FisheyeRayGenerator extends RayGenerator
    {
        protected Camera camera;
        protected int W;
        protected int H;

        /**
         * Constructor.
         *
         * @param c Cámara
         * @param W Anchura del viewport
         * @param H Altura del viewport
         */
        FisheyeRayGenerator(final Camera c, final int W, final int H)
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
            float k = this.camera.getProjection().getDistance();

            float xa = (float)((m + 0.5f) * (w / W)) - (w * 0.5f);
            float ya = (float)((n + 0.5f) * (h / H)) - (h * 0.5f);
            float za = -k;

            Point3D Pa = new Point3D(xa, ya, za);
            float hh = (new Vector3D(new Point3D(0, 0, -k), Pa)).length();

            if (hh > k) {
                return Ray.voidRay;
            }

            float s = (2f * k * k) / (k * k - hh * hh);
            Point3D P = new Point3D(s * Pa.x, s * Pa.y, s * Pa.z);
            Point3D pixelPosition = this.camera.getTransformation().times(P); // de coordenadas camara -> escena
            Vector3D dir = (new Vector3D(this.camera.getLook()));

            return new Ray(pixelPosition, dir);
        }
    };
}
