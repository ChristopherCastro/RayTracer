package shader;

import lights.Light;
import lights.LightGroup;
import objects.Group;
import primitives.Point3D;
import primitives.Vector3D;
import tracer.Hit;

public class LambertShader extends Shader
{
    @Override
    public void shade(Color outColor, final Group scene, final LightGroup lights, final Hit hit)
    {
        Material material = hit.getObject().getMaterial();
        Vector3D l = new Vector3D();
        float softFactor = (Shader.softLighting * Shader.softLighting);

        for (Light light : lights.getCollection()) {
            Point3D op = light.getPosition(); // original position

            for (int xx = 0; xx < Shader.softLighting; xx++) {
                for (int yy = 0; yy < Shader.softLighting; yy++) {
                    light.setPosition(new Point3D(op.x + xx, op.y, op.z + yy));
                    Color lightIntensity = light.intensityAt(scene, hit);

                    if (!lightIntensity.equals(Color.BLACK)) {
                        l.sub(light.getPosition().toVector3D(), hit.getPoint().toVector3D()).normalize();

                        // TODO: http://www.vrarchitect.net/anu/cg/GlobalIllumination/RayTrace.java
                        float x = (float)Math.max(0, hit.getNormal().dot(l));
                        outColor.r += (light.getIntensity().r * material.getDiffuseColor().r * x) / softFactor;
                        outColor.g += (light.getIntensity().g * material.getDiffuseColor().g * x) / softFactor;
                        outColor.b += (light.getIntensity().b * material.getDiffuseColor().b * x) / softFactor;
                    }
                }
            }

            light.setPosition(op);
        }

        outColor.clamp(0, 1);
    }
}
