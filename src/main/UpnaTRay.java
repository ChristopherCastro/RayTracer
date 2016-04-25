/**
 * 27/10/2015: 18:30 - 19:20 (00:50)
 * 31/10/2015: 15:40 - 17:00 (01:30)
 * 03/11/2015: 18:30 - 19:20 (00:50)
 * 10/11/2015  17:50 - 19:20 (01:30)
 * 12/11/2015: 17:45 - 19:20 (01:35)
 * 13/11/2015: 10:00 - 12:20 (02:20)
 * 13/11/2015: 22:00 - 02:10 (04:10)
 * 14/11/2015: 10:35 - 13:40 (03:05)
 * 14/11/2015: 19:30 - 00:00 (02:30)
 * 15/11/2015: 14:50 - 17:50 (03:00)
 * 24/11/2015: 17:45 - 19:20 (01:35)
 * 
 * TOTAL: 24 horas 55 minutos
 */
package main;

import camera.Image;
import objects.Group;
import camera.*;
import java.io.File;
import java.io.FileNotFoundException;
import lights.LightGroup;
import parser.Parser;
import projections.Projection;

/**
 * Modo de uso:
 *
 *     java upnatray {w}x{h} {scn}
 *
 * En donde {w} y {h} representan respectivamente la anchura y altura de la
 * imagen a generar, y {scn} el nombre de un fichero de escena.
 * 
 * Por ejemplo, 800x600 y la escena "scene9a":
 * 
 *     ~$ java upnatray 800x600 scene9a
 *
 * @author Chris
 */
public class UpnaTRay
{

    /**
     * Main.
     *
     * @param args
     * @throws FileNotFoundException
     * @throws Exception 
     */
    public static void main(String[] args) throws FileNotFoundException, Exception
    {  
        File inputFile = new File("./test/scenes/" + args[0]);
        Parser parser = new Parser(inputFile);

        Camera camera = parser.camera;
        Projection projection = parser.projection;
        Group scene = parser.scene;
        LightGroup lights = parser.lights;

        Image renderer = new Image(parser.viewport.width, parser.viewport.height, parser.viewport.background, parser.viewport.ambient);
        camera.setProjection(projection);

        if (lights != null) { 
            renderer.setLights(lights);
        }

        renderer.synthesis(scene, camera);
    }
}
