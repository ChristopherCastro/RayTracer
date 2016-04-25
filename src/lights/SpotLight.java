package lights;

import objects.Group;
import shader.Color;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

/**
 * Luz puntual.
 *
 * @author Chris
 */
public class SpotLight extends Light
{

    /**
     * Expotente de atenuación.
     */
    protected float attenuationExponent = 0;

    /**
     * Ángulo de apertura EN RADIANES.
     */
    protected float aperture;

    /**
     * Constructor.
     *
     * @param position Emplazamiento de la fuente
     * @param lookAt Pundo de referencia, hacia donde "enfoca" la fuente
     * @param aperture Ángulo de apertura EN GRADOS [0-90]
     * @param intensity Intensidad de cada canal RGB
     * @throws java.lang.IllegalArgumentException Si
     */
    public SpotLight(final Point3D position, final Point3D lookAt, final float aperture, final Color intensity)
    throws IllegalArgumentException
    {
        if (aperture > 90.0f) {
            throw new IllegalArgumentException("El ángulo de apertura proporcionado (" + aperture + "º) es inválido, debe estar comprendido entre 0º y 90º.");
        }

        this.S = position;
        this.direction = (new Vector3D(this.S, lookAt)).normalize();
        this.aperture = (float)Math.toRadians(aperture);
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
            float down = (2f * (float)Math.PI) * (1f - (float)Math.cos(this.aperture)) * distanceSqt;
            c = new Color(this.intensity).scale(up / down).clamp(0, 1);

            // factor de atenuación
            float cosA = this.direction.copy().invert().dot(I);
            float attenuationFactor = (float)Math.pow(cosA, this.attenuationExponent);
            c.scale(attenuationFactor);
        }
        
        // zero irradiance
        return c;
    }

    @Override
    public boolean isLighted(final Hit hit)
    {
        Vector3D I = this.S.sub(hit.getPoint()).normalize();
        float v = -1f * (this.direction.dot(I));
        float cosA = (float)Math.cos(this.aperture);
        return cosA <= v && v <= 1f;
    }

    @Override
    public Point3D getPosition()
    {
        return this.S;
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
    public void setAttenuationExponent(float attenuationExponent)
    {
        this.attenuationExponent = attenuationExponent;
    }
}
