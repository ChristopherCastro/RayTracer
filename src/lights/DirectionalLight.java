package lights;

import objects.Group;
import primitives.Point3D;
import primitives.Vector3D;
import shader.Color;
import tracer.Hit;
import tracer.Ray;

/**
 * Representa una fuente de luz direccional de sección circular.
 *
 * @author chris
 */
public class DirectionalLight extends Light
{
    /**
     * Expotente de atenuación.
     */
    protected float attenuationExponent = 0;

    /**
     * Radio de la sección circular.
     */
    protected float radius;

    /**
     * Constructor.
     *
     * @param position Ubicación del centro de la fuente
     * @param lookAt Punto de referencia hacia donde apunta la fuente
     * @param radius Radio de la sección circular
     * @param intensity Intensidad
     */
    public DirectionalLight(final Point3D position, final Point3D lookAt, final float radius, final Color intensity)
    {
        this.S = position;
        this.direction = (new Vector3D(this.S, lookAt)).normalize();
        this.radius = radius;
        this.intensity = intensity;
    }

    @Override
    public boolean isLighted(final Hit hit)
    {
        // calculo de distancia punto-recta: https://ca.wikipedia.org/wiki/Dist%C3%A0ncia_d%27un_punt_a_una_recta
        Vector3D u = this.direction;
        Vector3D PQ = this.S.sub(hit.getPoint());
        Vector3D distance = u.cross(PQ);
        float d = distance.length() / u.length();
        return d <= this.radius;       
    }

    @Override
    public boolean isShadowed(final Group scene, final Hit hit)
    {
        Vector3D PL = hit.getPoint().sub(this.S);
        Vector3D vInverted = this.direction.copy().invert();
        float distance = this.direction.dot(PL); // distancia paralela al eje
        
        // lanzo un rayo desde el punto de impacto y paralelo
        // al eje de la fuente
        Ray shadowRay = new Ray(hit.getPoint(), vInverted);
        Hit shadowHit = scene.intersectAny(shadowRay, distance);

        return shadowHit.hits() && !hit.getObject().equals(shadowHit.getObject());
    }

    @Override
    public Color intensityAt(final Group scene, final Hit hit)
    {
        Color c = Color.BLACK;

        if (this.isLighted(hit) && !this.isShadowed(scene, hit)) {
            Vector3D I = this.S.sub(hit.getPoint()).normalize();
            float up = hit.getNormal().dot(I);
            float down = (float)Math.PI * this.radius * this.radius;
            c = new Color(this.intensity);
            c.scale(up / down);

            // factor de atenuación
            float cosA = this.direction.copy().invert().dot(I);
            float attenuationFactor = (float)Math.pow(cosA, this.attenuationExponent);
            c.scale(attenuationFactor);
        }

        // zero irradiance
        return c;
    }
    
    /**
     * @return the attenuationIndex
     */
    public float getAttenuationExponent()
    {
        return this.attenuationExponent;
    }

    /**
     * @param attenuationExponent the attenuationExponent to set
     */
    public void setAttenuationExponent(final float attenuationExponent)
    {
        this.attenuationExponent = attenuationExponent;
    }
}
