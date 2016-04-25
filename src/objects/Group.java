package objects;

import java.util.ArrayList;
import java.util.Iterator;
import tracer.Hit;
import tracer.Ray;

public class Group extends Object3D
{

    /**
     * Colección de objetos.
     */
    protected ArrayList<Object3D> collection;

    /**
     * Constructor.
     */
    public Group()
    {
        this.collection = new ArrayList<>();
    }

    /**
     * Calculo de intersección.
     * 
     * @param r
     * @param tmin
     * @return 
     */
    @Override
    public Hit intersect(final Ray r, float tmin)
    {
        Hit h = Hit.voidHit;
        Hit tmpH; 
        float prevD = Float.POSITIVE_INFINITY;
        float tmpD;

        for (Object3D obj : this.collection) {
            tmpH = obj.intersect(r, tmin);
            if (tmpH.hits()) {
                tmpD = tmpH.getPoint().distanceTo(r.getOrigin());
                if (tmpD <= prevD) {
                    prevD = tmpD;
                    h = tmpH;
                }
            }
        }

        return h;
    }

    /**
     * Retorna la primera intersección que se produzca, y no la más cercana como
     * ocurre con el método "intersect()"
     *
     * @param r Rayo a trazar
     * @param distance Considerar solo objetos contenidos en esta distancia
     * @return 
     */
    public Hit intersectAny(final Ray r, final float distance)
    {
        Hit h = Hit.voidHit;
        Hit tmpH; 
        boolean found = false;
        Iterator<Object3D> it = this.collection.iterator();

        while (it.hasNext() && !found) {
            tmpH = it.next().intersect(r, 0);
            if (tmpH.hits() && tmpH.getT() <= distance) {
                h = tmpH;
                found = true;
            }
        }

        return h;
    }

    /**
     * Agrega un objeto a la lista.
     *
     * @param obj El objeto
     * @return Esta misma instancia para encadenar
     */
    public Group addObject(final Object3D obj)
    {
        if (obj != null) {
            this.collection.add(obj);
        }
        return this;
    }

    /**
     * Quita de la lista el objeto en la posición indicada.
     *
     * @param index Índice del objeto a quitar
     */
    public void removeObject(final int index)
    {
        this.collection.remove(index);
    }

    /**
     * Retorna el objeto en el índice indicado.
     *
     * @param index índice del objeto
     * @return El objeto
     */
    public Object3D getObject(final int index)
    {
        return this.collection.get(index);
    }
    
    @Override
    public String toString()
    {
        String s = "Group ->\n";
        Iterator<Object3D> it = this.collection.iterator();

        while (it.hasNext()) {
            Object3D o = it.next();
            s += "    " + o + ";\n";
        }
        return s;
    }
}
