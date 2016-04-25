package camera;

import shader.Color;
import tracer.Hit;
import tracer.RayGenerator;
import tracer.Ray;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import lights.LightGroup;
import objects.Group;
import shader.Material;
import primitives.Point3D;
import primitives.Vector3D;

public class Image
{

    protected Color backgroundColor;
    protected Color ambientIntensity;
    protected int maxDepth = 100;
    protected int w;
    protected int h;
    protected BufferedImage viewport;
    protected JFrame frame = null;
    protected Camera camera;
    protected Group scene;
    protected LightGroup lights = new LightGroup();

    public Image(final int W, final int H, final Color bg, final Color ambient)
    {
        this.w = W;
        this.h = H;
        this.backgroundColor = bg;
        this.ambientIntensity = ambient;
    }

    public void synthesis(final Group scene, final Camera camera)
    {
        this.camera = camera;
        this.scene = scene;
        this.viewport = new BufferedImage(this.w, this.h, BufferedImage.TYPE_INT_ARGB);

        System.out.println(camera);
        System.out.println(scene);

        final RayGenerator rg = camera.getRayGenerator(this.w, this.h);
        for (int m = 0; m < this.w; ++m) {
            for (int n = 0; n < this.h; ++n) {
                final Ray ray = rg.getRay(m, n);
                final Color color = this.traceRay(ray, scene, lights, 0, this.camera.getProjection().getDistance());
                this.putPixel(m, n, color);
            }
        }
        
        this.render();
    }

    /**
     * Trace el rayo indicado sobre la escena y el conjunto de luces indicado.
     *
     * @param ray El rayo a trazar
     * @param G La escena
     * @param L Conjunto de luces
     * @param depth Nivel de recursión
     * @param tmin
     * @return Un color
     */
    public Color traceRay(final Ray ray, final Group G, final LightGroup L, int depth, final float tmin)
    {
        if (depth > this.maxDepth || ray.equals(Ray.voidRay)) {
            return new Color(this.backgroundColor);
        }

        Color color = new Color();
        final Hit hit = G.intersect(ray, tmin);
        if (hit.hits()) {
            final Material material = hit.getObject().getMaterial();

            // ambiente
            material.getShader().ambientShade(color, this.ambientIntensity, material);

            // Luces
            material.getShader().shade(color, G, L, hit);

            // Reflexiones
            if (material.isSpecular()) {
                final Ray reflectedRay = hit.getReflectedRay(ray);
                final Color reflectedColor = traceRay(reflectedRay, G, L, depth + 1, 0);

                color.scale(1 - material.getReflectionIndex());
                color.add(reflectedColor.scale(material.getReflectionIndex()));
            }

            if (material.isTransparent()) {
                //final Ray refractedRay = hit.getRefractedRay(ray);
                //final Color refractedColor = traceRay(refractedRay, G, L, depth + 1, 0);
            }
        }

        color.clamp(0, 1);
        return color;
    }

    public Camera getCamera()
    {
        return this.camera;
    }

    public Group getScene()
    {
        return this.scene;
    }

    public void setLights(LightGroup lg)
    {
        this.lights = lg;
    }

    /**
     * Renderiza el lienzo.
     */
    protected void render()
    {
        if (this.frame == null) {
            this.frame = new JFrame();
            this.frame.addKeyListener(new CameraKeys(this));
            this.frame.getContentPane().setLayout(new FlowLayout());
        }

        this.frame.getContentPane().removeAll();
        this.frame.getContentPane().invalidate();
        this.frame.getContentPane().add(new JLabel(new ImageIcon(this.viewport)));
        this.frame.getContentPane().repaint();
        this.frame.pack();
        this.frame.repaint();
        this.frame.setVisible(true);
    }

    protected void putPixel(int x, int y, Color c)
    {
        this.viewport.setRGB(x, this.h - 1 - y, c.toInt());
    }
    
    /**
     * Camera controller.
     */
    private class CameraKeys implements KeyListener
    {
        Image renderer;

        public CameraKeys(Image r)
        {
            this.renderer = r;
        }

        @Override
        public void keyPressed(KeyEvent e)
        {
            float x = 0;
            float y = 0;
            float z = 0;
            float moveStep = 30f;
            float angleStep = (float)Math.toRadians(10);

            Point3D currentPosition = new Point3D(this.renderer.getCamera().getPosition());
            Vector3D look = new Vector3D(this.renderer.getCamera().getLook());
            Vector3D newLook;
            Vector3D up = new Vector3D(this.renderer.getCamera().getUp());
            Point3D lookAt = new Point3D(this.renderer.getCamera().getLookAt());

            switch (e.getKeyCode()) {
                case KeyEvent.VK_W:
                    z -= moveStep;
                break;

                case KeyEvent.VK_S:
                    z += moveStep;
                break;

                case KeyEvent.VK_LEFT:
                    x -= moveStep;
                break;

                case KeyEvent.VK_RIGHT:
                    x += moveStep;
                break;

                case KeyEvent.VK_A:
                    newLook = new Vector3D(
                        look.x * (float)Math.cos(angleStep) + look.z * (float)Math.sin(angleStep),
                        look.y,
                        -look.x * (float)Math.sin(angleStep) + look.z * (float)Math.cos(angleStep)
                    );
                    lookAt = new Point3D(currentPosition).add(newLook);
                break;

                case KeyEvent.VK_D:
                    newLook = new Vector3D(
                        look.x * (float)Math.cos(-angleStep) + look.z * (float)Math.sin(-angleStep),
                        look.y,
                        -look.x * (float)Math.sin(-angleStep) + look.z * (float)Math.cos(-angleStep)
                    );
                    lookAt = new Point3D(currentPosition).add(newLook);
                break;

                default:
                    return;
            }

            Point3D newPosition = new Point3D(currentPosition.x + x, currentPosition.y + y, currentPosition.z + z);
            Camera c = new Camera(newPosition, new Point3D(lookAt.x + x, lookAt.y + y, lookAt.z + z), new Vector3D(up));
            c.setProjection(this.renderer.getCamera().getProjection());
            this.renderer.synthesis(this.renderer.getScene(), c);
        }

        @Override
        public void keyTyped(KeyEvent e)
        {
        }

        @Override
        public void keyReleased(KeyEvent e)
        {
        }
    }
}
