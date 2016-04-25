package primitives;

import javax.vecmath.Matrix4d;

public class Matrix4D extends Matrix4d
{

    /**
     * Crea una matrix cuadrada de 4 dimensiones.
     * 
     * Resulta útil y cómodo paratrabajar con transformaciones de vista.
     *
     * @param m00
     * @param m01
     * @param m02
     * @param m03
     * @param m10
     * @param m11
     * @param m12
     * @param m13
     * @param m20
     * @param m21
     * @param m22
     * @param m23
     * @param m30
     * @param m31
     * @param m32
     * @param m33 
     */
    public Matrix4D(
        float m00, float m01, float m02, float m03,
        float m10, float m11, float m12, float m13,
        float m20, float m21, float m22, float m23,
        float m30, float m31, float m32, float m33
    )
    {
        super(
            m00, m01, m02, m03,
            m10, m11, m12, m13,
            m20, m21, m22, m23,
            m30, m31, m32, m33
        );
    }

    /**
     * Multiplica esta matriz por el vector dado.
     *
     * @param v
     * @return Vector transformado segun la matriz
     */
    public Vector3D times(Vector3D v)
    {
        float[] newValues = new float[4];
        float[] oldValues = new float[4];
        oldValues[0] = v.x;
        oldValues[1] = v.y;
        oldValues[2] = v.z;
        oldValues[3] = v.w;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newValues[i] += this.getElement(i, j) * oldValues[j];
            }
        }

        Vector3D result = new Vector3D(newValues[0], newValues[1], newValues[2]);
        return result;
    }
    
    /**
     * Multiplica esta matriz por el punto dado.
     *
     * @param p
     * @return Vector transformado segun la matriz
     */
    public Point3D times(Point3D p)
    {
        float[] newValues = new float[4];
        float[] oldValues = new float[4];
        oldValues[0] = p.x;
        oldValues[1] = p.y;
        oldValues[2] = p.z;
        oldValues[3] = p.w;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newValues[i] += this.getElement(i, j) * oldValues[j];
            }
        }

        Point3D result = new Point3D(newValues[0], newValues[1], newValues[2], newValues[3]);
        return result;
    }
}
