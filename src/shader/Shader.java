package shader;

import lights.LightGroup;
import objects.Group;
import tracer.Hit;

/**
 *
 * @author Chris
 */
public abstract class Shader
{

    /**
     * Incrementar para suavizar sombras y luces.
     */
    public static int softLighting = 1;

    /**
     * Calcula el valor BRDF para este material en la intersección proporcionada,
     * el valor de BRDF es almacenado en outColor.
     *
     * @param outColor
     * @param scene
     * @param lights
     * @param hit 
     */
    public abstract void shade(Color outColor, final Group scene, final LightGroup lights, final Hit hit);

    /**
     * Calcula el valor BRDF producto de la irradiancia ambiente.
     *
     * @param outColor
     * @param ambientIntensity
     * @param material 
     */
    public void ambientShade(Color outColor, final Color ambientIntensity, final Material material)
    {
        outColor.r += material.getAmbientColor().r * ambientIntensity.r;
        outColor.g += material.getAmbientColor().g * ambientIntensity.g;
        outColor.b += material.getAmbientColor().b * ambientIntensity.b;
        outColor.clamp(0, 1);
    }
}
