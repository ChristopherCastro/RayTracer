package parser;

import camera.Camera;
import camera.Viewport;
import java.io.File;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import lights.DirectionalLight;
import lights.Light;
import lights.LightGroup;
import lights.PointLight;
import lights.SpotLight;
import objects.Group;
import objects.Object3D;
import shader.Material;
import objects.Plane;
import objects.Sphere;
import objects.Triangle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import primitives.Point3D;
import primitives.Vector3D;
import projections.Ortographic;
import projections.Perspective;
import projections.Projection;
import projections.SemisphericalFisheye;
import shader.Color;
import shader.MaterialCollection;
import shader.Shader;

public final class Parser
{
    protected File inputFile;
    protected MaterialCollection materialCollection;
    public Viewport viewport;
    public Camera camera;
    public Projection projection;
    public Group scene;
    public LightGroup lights;

    public Parser(final File in) throws Exception
    {
        this.inputFile = in;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(this.inputFile);

        this.materialCollection = this.parseMaterials(doc);
        this.viewport = this.parseViewport(doc);
        this.camera = this.parseCamera(doc);
        this.projection = this.parseProjection(doc);
        this.scene = this.parseScene(doc);
        this.lights = this.parseLights(doc);
    }
    
    public MaterialCollection parseMaterials(Document doc)
    {
        MaterialCollection c = new MaterialCollection();
        NodeList items = doc.getElementsByTagName("material");

        for (int tmp = 0; tmp < items.getLength(); tmp++) {
            Node node = items.item(tmp);
            Element el = (Element) node;
            c.addMaterial(this.parseMaterial(el));
        }

        return c;
    }

    public Viewport parseViewport(Document doc)
    {
        NodeList items = doc.getElementsByTagName("viewport");
        Node node = items.item(0);
        Element el = (Element) node;
        Viewport vp = new Viewport();

        String width = el.getElementsByTagName("width").item(0).getTextContent();
        String height = el.getElementsByTagName("height").item(0).getTextContent();
        String recursion = el.getElementsByTagName("recursion").item(0).getTextContent();
        String backgroundColor = el.getElementsByTagName("backgroundColor").item(0).getTextContent();
        String ambientIntensity = el.getElementsByTagName("ambientIntensity").item(0).getTextContent();
        String softLighting = el.getElementsByTagName("softLighting").item(0).getTextContent();

        vp.width = Integer.parseInt(width);
        vp.height = Integer.parseInt(height);
        vp.background = this.parseColor(backgroundColor);
        vp.ambient = this.parseIntensity(ambientIntensity);
        vp.recursion = Integer.parseInt(recursion);
        vp.softLighting = Integer.parseInt(softLighting);

        if (vp.softLighting > 0) {
            Shader.softLighting = vp.softLighting;
        }

        return vp;
    }
    
    public Camera parseCamera(Document doc)
    {
        NodeList items = doc.getElementsByTagName("camera");
        Node node = items.item(0);
        Element el = (Element) node;

        Point3D pos = this.parsePoint3D(el.getElementsByTagName("position").item(0).getTextContent());
        Point3D lookAt = this.parsePoint3D(el.getElementsByTagName("lookAt").item(0).getTextContent());
        Vector3D up = this.parseVector3D(el.getElementsByTagName("up").item(0).getTextContent());

        return new Camera(pos, lookAt, up);
    }

    public Projection parseProjection(Document doc)
    {
        NodeList items = doc.getElementsByTagName("projection");
        Node node = items.item(0);
        Element el = (Element) node;
        String type = el.getAttribute("type");

        switch (type) {
            case "Perspective":
            {
                String distance = el.getElementsByTagName("distance").item(0).getTextContent();
                String fov = el.getElementsByTagName("fov").item(0).getTextContent();
                String aspect = el.getElementsByTagName("aspect").item(0).getTextContent();

                return new Perspective(
                    Float.parseFloat(distance),
                    Float.parseFloat(fov),
                    Float.parseFloat(aspect)
                );
            }

            case "Ortographic":
            {
                String width = el.getElementsByTagName("width").item(0).getTextContent();
                String height = el.getElementsByTagName("height").item(0).getTextContent();

                return new Ortographic(
                    Float.parseFloat(width),
                    Float.parseFloat(height)
                );
            }

            case "SemisphericalFisheye":
            {
                String distance = el.getElementsByTagName("distance").item(0).getTextContent();
                return new SemisphericalFisheye(Float.parseFloat(distance));
            }
        }

        return null;
    }
    
    public Group parseScene(Document doc)
    {
        NodeList items = doc.getElementsByTagName("object");
        Group g = new Group();

        for (int tmp = 0; tmp < items.getLength(); tmp++) {
            Node node = items.item(tmp);
            Element el = (Element) node;
            g.addObject(this.parseObject(el));
        }

        return g;
    }

    protected Object3D parseObject(Element el)
    {
        String type = el.getAttribute("type");
        String id = el.getAttribute("id");
        Object3D obj = null;

        switch (type) {
            case "Sphere":
            {
                String center = el.getElementsByTagName("center").item(0).getTextContent();
                String radius = el.getElementsByTagName("radius").item(0).getTextContent();
                String materialId = el.getElementsByTagName("material").item(0).getTextContent();
                Material material = this.materialCollection.getMaterial(materialId);

                obj = new Sphere(material, this.parsePoint3D(center), Float.parseFloat(radius));
            }
            break;

            case "Plane":
            {
                String point = el.getElementsByTagName("point").item(0).getTextContent();
                String normal = el.getElementsByTagName("normal").item(0).getTextContent();
                String materialId = el.getElementsByTagName("material").item(0).getTextContent();
                Material material = this.materialCollection.getMaterial(materialId);

                obj = new Plane(material, this.parsePoint3D(point), this.parseVector3D(normal));
            }
            break;

            case "Triangle":
            {
                String vertex0 = el.getElementsByTagName("vertex0").item(0).getTextContent();
                String vertex1 = el.getElementsByTagName("vertex1").item(0).getTextContent();
                String vertex2 = el.getElementsByTagName("vertex2").item(0).getTextContent();
                String materialId = el.getElementsByTagName("material").item(0).getTextContent();
                Material material = this.materialCollection.getMaterial(materialId);

                obj = new Triangle(material, this.parsePoint3D(vertex0), this.parsePoint3D(vertex1), this.parsePoint3D(vertex2));
            }
            break;
        }

        if (obj != null) {
            obj.setId(id.length() > 0 ? id : ("unknow-" + Integer.toHexString(System.identityHashCode(el))));
        }

        return obj;
    }

    protected LightGroup parseLights(Document doc)
    {
        NodeList items = doc.getElementsByTagName("light");
        LightGroup g = new LightGroup();

        for (int tmp = 0; tmp < items.getLength(); tmp++) {
            Node node = items.item(tmp);
            Element el = (Element) node;
            g.addLight(this.parseLight(el));
        }

        return g;
    }

    protected Light parseLight(Element el)
    {
        String type = el.getAttribute("type");
        switch (type) {
            case "PointLight":
            {
                String position = el.getElementsByTagName("position").item(0).getTextContent();
                String intensity = el.getElementsByTagName("intensity").item(0).getTextContent();

                return new PointLight(
                    this.parsePoint3D(position),
                    this.parseIntensity(intensity)
                );
            }

            case "DirectionalLight":
            {
                String position = el.getElementsByTagName("position").item(0).getTextContent();
                String lookAt = el.getElementsByTagName("lookAt").item(0).getTextContent();
                String radius = el.getElementsByTagName("radius").item(0).getTextContent();
                String intensity = el.getElementsByTagName("intensity").item(0).getTextContent();
                String attenuationExponent = el.getElementsByTagName("attenuationExponent").item(0).getTextContent();

                DirectionalLight dl = new DirectionalLight(
                    this.parsePoint3D(position),
                    this.parsePoint3D(lookAt),
                    Float.parseFloat(radius),
                    this.parseIntensity(intensity)
                );

                if (attenuationExponent.length() > 0) {
                    dl.setAttenuationExponent(Float.parseFloat(attenuationExponent));
                }

                return dl;
            }

            case "SpotLight":
            {
                String position = el.getElementsByTagName("position").item(0).getTextContent();
                String lookAt = el.getElementsByTagName("lookAt").item(0).getTextContent();
                String aperture = el.getElementsByTagName("aperture").item(0).getTextContent();
                String intensity = el.getElementsByTagName("intensity").item(0).getTextContent();
                String attenuationExponent = el.getElementsByTagName("attenuationExponent").item(0).getTextContent();

                SpotLight sl = new SpotLight(
                    this.parsePoint3D(position),
                    this.parsePoint3D(lookAt),
                    Float.parseFloat(aperture),
                    this.parseIntensity(intensity)
                );

                if (attenuationExponent.length() > 0) {
                    sl.setAttenuationExponent(Float.parseFloat(attenuationExponent));
                }

                return sl;
            }
        }

        return null;
    }
    

    


    ///////////////////////
    //  Generic Parsers  //
    ///////////////////////



    protected Material parseMaterial(Element el)
    {
        Material material = new Material();

        String id = el.getAttribute("id");
        String diffuseColor = el.getAttribute("diffuseColor");
        String specularColor = el.getAttribute("specularColor");
        String ambientColor = el.getAttribute("ambientColor");
        String reflectionIndex = el.getAttribute("reflectionIndex");
        String refractionIndex = el.getAttribute("refractionIndex");
        String shininess = el.getAttribute("shininess");
        String shader = el.getAttribute("shader");

        if (id.length() > 0) {
            material.setId(id);
        }

        if (diffuseColor.length() > 0) {
            material.setDiffuseColor(this.parseColor(diffuseColor));
        }
 
        if (specularColor.length() > 0) {
            material.setSpecularColor(this.parseColor(specularColor));
        } else {
            material.setDiffuseColor(material.getDiffuseColor());
        }
        
        if (ambientColor.length() > 0) {
            material.setSpecularColor(this.parseColor(ambientColor));
        } else {
            material.setAmbientColor(material.getDiffuseColor());
        }

        if (reflectionIndex.length() > 0) {
            material.setReflectionIndex(Float.parseFloat(reflectionIndex));
        }

        if (refractionIndex.length() > 0) {
            material.setRefractionIndex(Float.parseFloat(refractionIndex));
        }

        if (shininess.length() > 0) {
            material.setShininess(Float.parseFloat(shininess));
        }

        switch (shader) {
            case "PhongBlinn":
            {
                material.setShader(Material.phongBlinnShader);
            }
            break;
            
            case "PhongHorn":
            {
                material.setShader(Material.phongHornShader);
            }
            break;

            case "Lambert":
            {
                material.setShader(Material.lambertShader);
            }
            break;
                
            case "Lafortune":
            {
                material.setShader(Material.lafortuneShader);
            }
            break;
 
            default:
            {
                material.setShader(Material.defaultShader);
            }
            break;
        }
        
        System.out.println(material);

        return material;
    }

    protected Color parseColor(String c)
    {
        StringTokenizer st = new StringTokenizer(c);
        float r = Float.parseFloat(st.nextToken());
        float g = Float.parseFloat(st.nextToken());
        float b = Float.parseFloat(st.nextToken());

        r = r > 1f ? r / 255f : r;
        g = g > 1f ? g / 255f : g;
        b = b > 1f ? b / 255f : b;

        return new Color(r, g, b);
    }
    
    protected Color parseIntensity(String c)
    {
        StringTokenizer st = new StringTokenizer(c);
        float r = Float.parseFloat(st.nextToken());
        float g = Float.parseFloat(st.nextToken());
        float b = Float.parseFloat(st.nextToken());

        return new Color(r, g, b);
    }

    protected Vector3D parseVector3D(String v)
    {
        StringTokenizer st = new StringTokenizer(v);
        float x = Float.parseFloat(st.nextToken());
        float y = Float.parseFloat(st.nextToken());
        float z = Float.parseFloat(st.nextToken());

        return new Vector3D(x, y, z);
    }
    
    protected Point3D parsePoint3D(String p)
    {
        StringTokenizer st = new StringTokenizer(p);
        float x = Float.parseFloat(st.nextToken());
        float y = Float.parseFloat(st.nextToken());
        float z = Float.parseFloat(st.nextToken());

        return new Point3D(x, y, z);
    }
}
