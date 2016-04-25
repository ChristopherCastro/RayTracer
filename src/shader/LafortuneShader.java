package shader;

import lights.Light;
import lights.LightGroup;
import objects.Group;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

public class LafortuneShader extends Shader
{

    @Override
    public void shade(Color outColor, final Group scene, final LightGroup lights, final Hit hit)
    {
        Material material = hit.getObject().getMaterial();
        Vector3D v = hit.getRay().getDirection().copy().invert();
        float q = material.getShininess();
        float softFactor = (Shader.softLighting * Shader.softLighting);

        for (Light light : lights.getCollection()) {
            Point3D op = light.getPosition(); // original position

            for (int xx = 0; xx < Shader.softLighting; xx++) {
                for (int yy = 0; yy < Shader.softLighting; yy++) {
                    light.setPosition(new Point3D(op.x + xx, op.y, op.z + yy));
                    Color lightIntensity = light.intensityAt(scene, hit);
                    
                    // solo si aporta alguna intensidad
                    if (!lightIntensity.equals(Color.BLACK)) {
                        Vector3D l = light.getPosition().sub(hit.getPoint()).normalize();
                        Vector3D h = new Vector3D(l).add(v).normalize();

                        float x = 1f / (float)Math.PI;
                        float y = ((q + 2) / (2f * (float)Math.PI)) * (float)Math.pow(Math.max(0, hit.getNormal().dot(h)), q);

                        outColor.r += lightIntensity.r * ((material.getDiffuseColor().r * x + material.getSpecularColor().r * y) / softFactor);
                        outColor.g += lightIntensity.g * ((material.getDiffuseColor().g * x + material.getSpecularColor().g * y) / softFactor);
                        outColor.b += lightIntensity.b * ((material.getDiffuseColor().b * x + material.getSpecularColor().b * y) / softFactor);
                    }
                }
            }

            light.setPosition(op);
        }

        outColor.clamp(0, 1);
    }
}
