package lights;

import objects.Group;
import shader.Color;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

/**
 * Representa una luz puntual (omnidireccional).
 */
public class PointLight extends Light
{

    /**
     * Crea una nueva luz puntual.
     *
     * @param position Ubicación de la fuente de luz
     * @param intensity Intensidades en cada canal RGB
     */
    public PointLight(final Point3D position, final Color intensity)
    {
        this.S = position;
        this.intensity = intensity;
    }

    @Override
    public Color intensityAt(final Group scene, final Hit hit)
    {
        Color c = Color.BLACK;

        if (this.isLighted(hit) && !this.isShadowed(scene, hit)) {
            Vector3D I = this.S.sub(hit.getPoint());
            Vector3D n = hit.getNormal();

            float distanceSqt = I.dot(I);
            I.normalize();

            float up = n.dot(I);
            float down = 4f * (float)Math.PI * distanceSqt;
            c = new Color(this.intensity).scale(up / down).clamp(0, 1);
        }

        return c;
    }

    @Override
    public boolean isLighted(final Hit hit)
    {
        return true;
    }

    @Override
    public String toString()
    {
        return "PointLight -> center = " + this.S;
    }
}
