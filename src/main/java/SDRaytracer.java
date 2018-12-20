

import java.util.List;
import java.util.ArrayList;


/* Implementation of a very simple Raytracer
   Stephan Diehl, Universitaet Trier, 2010-2016
*/


public class SDRaytracer //extends JFrame
{

    private static final long serialVersionUID = 1L;
    public Window window;

    public int availableProcessors = Runtime.getRuntime().availableProcessors(); //doppelt aber geht nicht anders

    public int maxRec = 3;
    public int rayPerPixel = 1;

    public int startX;
    public int startY;
    public int startZ;


    List<Triangle> triangles;

    public Light mainLight = new Light(new Vec3D(0, 100, 0), new RGB(0.1f, 0.1f, 0.1f));

    public Light lights[] = new Light[]{mainLight
            , new Light(new Vec3D(100, 200, 300), new RGB(0.5f, 0, 0.0f))
            , new Light(new Vec3D(-100, 200, 300), new RGB(0.0f, 0, 0.5f))
            //,new Light(new Vec3D(-100,0,0), new RGB(0.0f,0.8f,0.0f))
    };




    private RGB ambient_color = new RGB(0.01f, 0.01f, 0.01f);
    private RGB black = new RGB(0.0f, 0.0f, 0.0f);



    public int yAngleFactor = 4;
    public int xAngleFactor = -4;


    SDRaytracer()
    {

        createScene();
        window = new Window(this);
    }



    public RGB rayTrace(Ray ray, int rec) //evt in Klasse RAy tun
    {
        if (rec > maxRec) return black;
        IPoint ip = hitObject(ray);  // (ray, p, n, triangle);
        if (ip.distance > IPoint.epsilon)
            return lighting(ray, ip, rec);
        else
            return black;
    }


    public IPoint hitObject(Ray ray) {
        IPoint isect = new IPoint(null, null, -1);
        float idist = -1;
        for (Triangle t : triangles)
        {
            IPoint ip = ray.intersect(t);
            if (ip.distance != -1)
                if ((idist == -1) || (ip.distance < idist)) { // save that intersection
                    idist = ip.distance;
                    isect.vec3D = ip.vec3D;
                    isect.distance = ip.distance;
                    isect.triangle = t;
                }
        }
        return isect;  // return intersection point and normal
    }


    public RGB lighting(Ray ray, IPoint iPoint, int rec) //nicht anfassen
    {
        Vec3D point = iPoint.vec3D;
        Triangle triangle = iPoint.triangle;
        RGB color = RGB.addColors(triangle.color, ambient_color, 1);
        Ray reflection;

        calcRayShadow(point, triangle, color);

        Vec3D L = ray.direction.multiplication(-1);
        reflection =  calcRayReflection(triangle, point, L);


        RGB rcolor = rayTrace(reflection, rec + 1);
        float ratio = (float) Math.pow(Math.max(0, reflection.direction.dotProduct(L)), triangle.shininess);
        color = RGB.addColors(color, rcolor, ratio);
        return (color);
    }

    private Ray calcRayReflection(Triangle triangle, Vec3D point, Vec3D vectorL)
    {
        Ray reflection = new Ray();
        //R = 2N(N*L)-L)    L ausgehender Vektor
        Vec3D L = vectorL;
        reflection.start = point;
        reflection.direction = triangle.normal.multiplication(2 * triangle.normal.dotProduct(L)).subtraction(L);
        reflection.direction.normalize();

        return reflection;
    }

    private void calcRayShadow(Vec3D point, Triangle triangle, RGB color)
    {
        Ray shadow_ray = new Ray();
        for (Light light : lights)
        {
            shadow_ray.start = point;
            shadow_ray.direction = light.position.subtraction(point).multiplication(-1);
            shadow_ray.direction.normalize();
            IPoint ip2 = hitObject(shadow_ray);
            if (ip2.distance < IPoint.epsilon) {
                float ratio = Math.max(0, shadow_ray.direction.dotProduct(triangle.normal));
                color = RGB.addColors(color, light.color, ratio);
            }
        }

    }

    public void createScene()
    {
        triangles = new ArrayList<Triangle>();


        createCube(0, 35, 0, 10, 10, 10, new RGB(0.3f, 0, 0), 0.4f);       //rot, klein
        createCube(-70, -20, -20, 20, 100, 100, new RGB(0f, 0, 0.3f), .4f);
        createCube(-30, 30, 40, 20, 20, 20, new RGB(0, 0.4f, 0), 0.2f);        // gruen, klein
        createCube(50, -20, -40, 10, 80, 100, new RGB(.5f, .5f, .5f), 0.2f);
        createCube(-70, -26, -40, 130, 3, 40, new RGB(.5f, .5f, .5f), 0.2f);


        Matrix mRx = Matrix.createXRotation((float) (xAngleFactor * Math.PI / 16));
        Matrix mRy = Matrix.createYRotation((float) (yAngleFactor * Math.PI / 16));
        Matrix mT = Matrix.createTranslation(0, 0, 200);
        Matrix m = mT.multiplication(mRx).multiplication(mRy);
        m.print();
        m.apply(triangles);
    }

    public void createCube(int x, int y, int z, int w, int h, int d, RGB c, float sh)
    {  //front
        triangles.add(new Triangle(new Vec3D(x, y, z), new Vec3D(x + w, y, z), new Vec3D(x, y + h, z), c, sh));
        triangles.add(new Triangle(new Vec3D(x + w, y, z), new Vec3D(x + w, y + h, z), new Vec3D(x, y + h, z), c, sh));
        //left
        triangles.add(new Triangle(new Vec3D(x, y, z + d), new Vec3D(x, y, z), new Vec3D(x, y + h, z), c, sh));
        triangles.add(new Triangle(new Vec3D(x, y + h, z), new Vec3D(x, y + h, z + d), new Vec3D(x, y, z + d), c, sh));
        //right
        triangles.add(new Triangle(new Vec3D(x + w, y, z), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y + h, z), c, sh));
        triangles.add(new Triangle(new Vec3D(x + w, y + h, z), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y + h, z + d), c, sh));
        //top
        triangles.add(new Triangle(new Vec3D(x + w, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z), c, sh));
        triangles.add(new Triangle(new Vec3D(x, y + h, z), new Vec3D(x + w, y + h, z + d), new Vec3D(x, y + h, z + d), c, sh));
        //bottom
        triangles.add(new Triangle(new Vec3D(x + w, y, z), new Vec3D(x, y, z), new Vec3D(x, y, z + d), c, sh));
        triangles.add(new Triangle(new Vec3D(x, y, z + d), new Vec3D(x + w, y, z + d), new Vec3D(x + w, y, z), c, sh));
        //back
        triangles.add(new Triangle(new Vec3D(x, y, z + d), new Vec3D(x, y + h, z + d), new Vec3D(x + w, y, z + d), c, sh));
        triangles.add(new Triangle(new Vec3D(x + w, y, z + d), new Vec3D(x, y + h, z + d), new Vec3D(x + w, y + h, z + d), c, sh));

    }



}



















