package lights;

import objects.Group;
import shader.Color;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;
import tracer.Ray;

public abstract class Light
{

    /**
     * Posición de la fuente en la escena.
     */
    protected Point3D S;

    /**
     * Color de la fuente.
     */
    protected Color color;
    
    /**
     * Vector de orientación de la fuente.
     */
    protected Vector3D direction;

    /**
     * Intensidad de emisión en cada canal RGB.
     * 
     * r = Red
     * g = Green
     * b = Blue
     */
    protected Color intensity = new Color(0.1f, 0.1f, 0.1f);
    
    /**
     * Comprueba si el punto de impacto proporcionado queda iluminado por
     * esta fuente de luminosa.
     *
     * @param hit
     * @return Verdadero si queda iluminado
     */
    public abstract boolean isLighted(final Hit hit);

    /**
     * Comprueba si entre la fuente de luz y el punto de impacto hay algún
     * otro objeto de la escena.
     *
     * @param scene
     * @param hit
     * @return 
     */
    public boolean isShadowed(final Group scene, final Hit hit)
    {
        Vector3D shadowDirection = this.getPosition().sub(hit.getPoint());
        float distance = shadowDirection.length();
        shadowDirection.normalize();

        // TODO: desplazo un poco el origen para evitar impactos contra el mismo objeto
        Ray shadowRay = new Ray(hit.getPoint(), this.getPosition());
        Hit shadowHit = scene.intersectAny(shadowRay, distance);

        // TODO: apaño cutre para intersecar objetos con una única cara visible
        if (!shadowHit.hits()) {
            shadowRay = new Ray(this.getPosition(), hit.getPoint());
            shadowHit = scene.intersectAny(shadowRay, distance);
        }

        return shadowHit.hits() && !hit.getObject().equals(shadowHit.getObject());
    }

    /**
     * Nivel de irradiancia que aporta esta fuente al punto de impacto
     * proporcionado.
     *
     * @param scene La escena
     * @param hit Punto de impacto en forma de objeto Hit
     * @return Cero si el punto de impacto esta fuera del campo iluminado por
     *  esta fuente
     */
    public abstract Color intensityAt(final Group scene, final Hit hit);

    /**
     * Establece una nueva posición.
     *
     * @param p
     */
    public void setPosition(final Point3D p)
    {
        this.S = p;
    }

    /**
     * Getter de posición.
     *
     * @return 
     */
    public Point3D getPosition()
    {
        return this.S;
    }

    /**
     * Establece un vector de intencidades.
     *
     * @param c Intensidades para cada canal RGB
     * @return 
     */
    public Light setIntensity(Color c)
    {
        this.intensity = c;
        return this;
    }

    /**
     * Getter de intensidad.
     *
     * @return 
     */
    public Color getIntensity()
    {
        return this.intensity;
    }
}
