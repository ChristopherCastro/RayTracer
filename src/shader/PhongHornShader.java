package shader;

import lights.Light;
import lights.LightGroup;
import objects.Group;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

/**
 * 
 */
public class PhongHornShader extends Shader
{

    @Override
    public void shade(Color outColor, final Group scene, final LightGroup lights, final Hit hit)
    {
        Material material = hit.getObject().getMaterial();
        Vector3D v = hit.getRay().getDirection().copy().invert();
        Vector3D n = hit.getNormal();
        float q = material.getShininess();

        for (Light light : lights.getCollection()) {
            Point3D op = light.getPosition(); // original position

            for (int xx = 0; xx < Shader.softLighting; xx++) {
                for (int yy = 0; yy < Shader.softLighting; yy++) {
                    light.setPosition(new Point3D(op.x + xx, op.y, op.z + yy));
                    Color lightIntensity = light.intensityAt(scene, hit);
                    
                    // solo si aporta alguna intensidad
                    if (!lightIntensity.equals(Color.BLACK)) {
                        Vector3D l = light.getPosition().sub(hit.getPoint()).normalize();

                        // I = punto a la luz
                        // v = rayo invertido
                        // 2 (I · N) * (v · n) - (v · I)

                        float x = (float)Math.max(0, hit.getNormal().dot(l));
                        float Fs = 2f * l.dot(n) * v.dot(n) - v.dot(l);
                        float y = (float)Math.pow(Math.max(0, Fs), q); // función de reflectancia glossy: Fs(Ij, v, n, q)

                        // Lrj(v) = Lij * (kd * (Ij · n) + ks * Fs(Ij, v, n, q))
                        outColor.r += ((lightIntensity.r * (material.getDiffuseColor().r * x + material.getSpecularColor().r * y)) / (Shader.softLighting * Shader.softLighting));
                        outColor.g += ((lightIntensity.g * (material.getDiffuseColor().g * x + material.getSpecularColor().g * y)) / (Shader.softLighting * Shader.softLighting));
                        outColor.b += ((lightIntensity.b * (material.getDiffuseColor().b * x + material.getSpecularColor().b * y)) / (Shader.softLighting * Shader.softLighting));
                    }
                }
            }

            light.setPosition(op);
        }

        outColor.clamp(0, 1);
    }
}
