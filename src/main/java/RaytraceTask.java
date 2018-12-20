import java.util.concurrent.Callable;

class RaytraceTask implements Callable // fuer multithreading
{
    private SDRaytracer tracer;
    private Window window;
    private int i;




    RaytraceTask(SDRaytracer sdRaytracer, int i, Window window) //kriegt raytracer
    {
        tracer = sdRaytracer;
        this.i = i;
        this.window = window;
    }

    public RGB[] call()
    {
        RGB[] color = new RGB[window.windowHeight];
        for (int j = 0; j < window.windowHeight; j++)
        {
            window.image[i][j] = new RGB(0, 0, 0);
            for (int k = 0; k < tracer.rayPerPixel; k++)
            {
                double di = i + calculateRandom();
                double dj = j + calculateRandom();
                if (tracer.rayPerPixel == 1) {
                    di = i;
                    dj = j;
                }

                Ray eye_ray = calculateEyeRay(di, dj);

                color[j]= calculateNewColor(j, color, eye_ray);
            }
        }
        return color;
    }

    private double calculateRandom()//neu wiederverwendet
    {
        double tmp = Math.random() / 2 - 0.25;
        return tmp;
    }

    private Ray calculateEyeRay(double di, double dj) //neu uebersichtlich
    {
        Ray eye_ray = new Ray();
        eye_ray.setStart(tracer.startX, tracer.startY, tracer.startZ);   // ro
        eye_ray.setDirection(calculateDirectionX(di), calculateDirectionY(dj),
                (float) 1f);    // rd
        eye_ray.normalize();

        return eye_ray;
    }

    private float calculateDirectionX(double di) //neu uebersicht
    {
        float tmp = (float)(((0.5 + di) * window.tanFovX* 2.0) / window.windowWidth - window.tanFovX);
        return tmp;
    }

    private float calculateDirectionY(double dj)//neu uebersicht
    {
        float tmp = (float) (((0.5 + dj) * window.tanFovY * 2.0) / window.windowHeight - window.tanFovY);
        return tmp;
    }

    private RGB calculateNewColor(int j, RGB[] color, Ray eye_ray) //neu uebersicht
    {
        color[j] = RGB.addColors(window.image[i][j], tracer.rayTrace(eye_ray, 0), 1.0f / tracer.rayPerPixel);
        return color[j];
    }
}