package shader;

public class Color
{

    /**
     * Negro.
     */
    public static final Color BLACK = new Color();

    /**
     * Blanco.
     */
    public static final Color WHITE = new Color(1, 1, 1);

    /**
     * Componente rojo.
     */
    public float r;

    /**
     * Componente verde.
     */
    public float g;

    /**
     * Componente azul.
     */
    public float b;

    /**
     * Constructor por defecto, produce negro.
     */
    public Color()
    {
        this(0, 0, 0);
    }

    /**
     * Copia un color.
     *
     * @param newColor Color a duplicar.
     */
    public Color(Color newColor)
    {
        this(newColor.r, newColor.g, newColor.b);
    }

    /**
     * Constructor explítico.
     *
     * @param r
     * @param g
     * @param b
     */
    public Color(float r, float g, float b)
    {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    /**
     * Copia los valores de inColor a este color.
     *
     * @param inColor
     * @return this
     */
    public Color set(Color inColor)
    {
        this.r = inColor.r;
        this.g = inColor.g;
        this.b = inColor.b;
        return this;
    }

    /**
     * Establece los valores de este color a los poroporcionados (inR, inG, inB).
     *
     * @param R
     * @param G
     * @param B 
     * @return this
     */
    public Color set(final float R, final float G, final float B)
    {
        this.r = R;
        this.g = G;
        this.b = B;
        return this;
    }

    /**
     * Multiplica las componentes de este color y el proporcionado.
     *
     * @param rhs
     * @return this
     */
    public Color scale(final Color rhs)
    {
        this.r *= rhs.r;
        this.g *= rhs.g;
        this.b *= rhs.b;
        return this;
    }

    /**
     * Escala cada componente de este color por el factor de escala
     * proporcionado.
     *
     * @param rhs The scale value to use.
     * @return this
     */
    public Color scale(final float rhs)
    {
        this.r *= rhs;
        this.g *= rhs;
        this.b *= rhs;
        return this;
    }

    /**
     * Suma las componentes de este color y el proporcionado.
     *
     * @param rhs
     * @return this
     */
    public Color add(final Color rhs)
    {
        this.r += rhs.r;
        this.g += rhs.g;
        this.b += rhs.b;
        return this;
    }

    /**
     * Asegura que cada componentes tiene unos valores asignados comprendidos
     * entre min y max.
     *
     * @param min
     * @param max
     * @return this
     */
    public Color clamp(final float min, final float max)
    {
        this.r = (float)Math.max(Math.min(this.r, max), min);
        this.g = (float)Math.max(Math.min(this.g, max), min);
        this.b = (float)Math.max(Math.min(this.b, max), min);
        return this;
    }

    /**
     * Retorna un entero que representa este color en espacio RGB.
     *
     * @return
     */
    public int toInt()
    {
        java.awt.Color cc = new java.awt.Color(this.r, this.g, this.b);
        return cc.getRGB();
    }

    /**
     * @return 
     * @see Object#toString()
     */
    @Override
    public String toString()
    {
        return "color: {" + this.r + ", " + this.g + ", " + this.b + "}";
    }
}
