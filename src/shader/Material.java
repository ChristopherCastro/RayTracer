package shader;

/**
 * Representa el material de un objeto.
 *
 * @author Chris
 */
public class Material
{

    /**
     * Identificar unico de esta instancia
     */
    private String id;

    /**
     * Color especular.
     */
    protected Color specularColor = new Color(0, 0, 0);

    /**
     * Color difuso.
     */
    protected Color diffuseColor = new Color(0, 0, 0);
    
    /**
     * Color difuso.
     */
    private Color ambientColor = new Color(0, 0, 0);

    /**
     * Qué proporción del rayo reflejado.
     */
    protected float reflectionIndex = 0;

    /**
     * Proporción rayo refractado.
     */
    protected float refractionIndex = 0;

    /**
     * Brillo del objeto.
     */
    protected float shininess = 1;

    /**
     * Shader por defecto.
     */
    public static final Shader defaultShader = new PhongBlinnShader();

    /**
     * Instancia única de shader Phong-Blinn.
     */
    public static final PhongBlinnShader phongBlinnShader = new PhongBlinnShader();
    
    /**
     * Instancia única de shader Phong-Horn.
     */
    public static final PhongHornShader phongHornShader = new PhongHornShader();

    /**
     * Instancia única de shader LafortuneShader.
     */
    public static final LafortuneShader lafortuneShader = new LafortuneShader();
    
    /**
     * Instancia única de shader LabertShader.
     */
    public static final LambertShader lambertShader = new LambertShader();

    /**
     * Shader del material, por defecto a Phong.
     */
    protected Shader shader;

    /**
     * Verdadero si este material refleja de forma especular los rayos que
     * inciden sobre él.
     *
     * @return
     */
    public boolean isSpecular()
    {
        return this.reflectionIndex > 0.0f;
    }

    /**
     * Verdadero si este material refracta los rayos que inciden sobre él.
     *
     * @return
     */
    public boolean isTransparent()
    {
        return this.refractionIndex > 0.0f;
    }

    /**
     * Obtiene el indice de reflexión de este material.
     *
     * @return índice, un valor entre 0 y 1
     */
    public float getReflectionIndex()
    {
        return this.reflectionIndex;
    }

    /**
     * Sets reflection index.
     *
     * @param i
     * @return 
     */
    public Material setReflectionIndex(final float i)
    {
        this.reflectionIndex = i;
        return this;
    }

    /**
     * Obtiene el indice de refracción de este material.
     *
     * @return índice, un valor entre 0 y 1
     */
    public float getRefractionIndex()
    {
        return this.refractionIndex;
    }

    /**
     * Sets refraction index.
     *
     * @param i
     * @return 
     */
    public Material setRefractionIndex(final float i)
    {
        this.refractionIndex = i;
        return this;
    }

    /**
     * @return the diffuseColor
     */
    public Color getDiffuseColor()
    {
        return this.diffuseColor;
    }

    /**
     * @param dc Color difuso
     * @return this
     */
    public Material setDiffuseColor(final Color dc)
    {
        this.diffuseColor = dc;
        return this;
    }
    
    /**
     * @return the ambientColor
     */
    public Color getAmbientColor()
    {
        return ambientColor;
    }

    /**
     * @param ambientColor the ambientColor to set
     */
    public void setAmbientColor(Color ambientColor)
    {
        this.ambientColor = ambientColor;
    }

    /**
     * @return the specular index
     */
    public Color getSpecularColor()
    {
        return this.specularColor;
    }

    /**
     * @param sc
     * @return this
     */
    public Material setSpecularColor(final Color sc)
    {
        this.specularColor = sc;
        return this;
    }

    /**
     * @return the shininess
     */
    public float getShininess()
    {
        return shininess;
    }

    /**
     * @param shininess the shininess to set
     * @return this
     */
    public Material setShininess(final float shininess)
    {
        this.shininess = shininess;
        return this;
    }

    /**
     * @return the shader
     */
    public Shader getShader()
    {
        return shader;
    }

    /**
     * @param shader the shader to set.
     *
     * @return this
     */
    public Material setShader(Shader shader)
    {
        this.shader = shader;
        return this;
    }
    
    @Override
    public String toString()
    {
        return "material: {diffuseColor = " + this.getDiffuseColor() + "}";
    }

    /**
     * @return the id
     */
    public String getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id)
    {
        this.id = id;
    }
}
