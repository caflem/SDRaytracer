import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Window extends JFrame
{
    boolean profiling = false;

    int windowWidth = 1000;
    int windowHeight = 1000;
    //int maxRec = 3;
    float fovx = (float) 0.628;
    float fovy = (float) 0.628;

    double tanFovX;
    double tanFovY;


    Future[] futureList = new Future[windowWidth];

    RGB[][] image = new RGB[windowWidth][windowHeight];

    SDRaytracer sdRaytracer;
    int availableProcessors = Runtime.getRuntime().availableProcessors();

    ExecutorService executorService = Executors.newFixedThreadPool(availableProcessors);


    Window(SDRaytracer sdraytracer)
    {

        sdRaytracer = sdraytracer;


        if (!profiling) renderImage();
        else profileRenderImage();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());


        JPanel area = createNewPanel();

        area.setPreferredSize(new Dimension(windowWidth, windowHeight));
        contentPane.add(area);
        this.pack();
        this.setVisible(true);

    }



    private JPanel createNewPanel()
    {
        JPanel area = new JPanel()
        {
            public void paint(Graphics g)
            {
                System.out.println("fovx=" + fovx + ", fovy=" + fovy + ", xangle=" + sdRaytracer.xAngleFactor + ", yangle=" + sdRaytracer.xAngleFactor);
                if (image == null) return;
                for (int i = 0; i < windowWidth; i++)
                    for (int j = 0; j < windowHeight; j++)
                    {
                        g.setColor(image[i][j].createColor());
                        // zeichne einzelnen Pixel
                        g.drawLine(i, windowHeight - j, i, windowHeight - j);
                    }
            }

        };

        addNewListener();

        return area;


    }
    private void addNewListener()
    {
        addKeyListener(new KeyAdapter()
        {


            public void keyPressed(KeyEvent e)
            {
                boolean redraw = false;
                if (e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    sdRaytracer.xAngleFactor--;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_UP)
                {
                    sdRaytracer.xAngleFactor++;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_LEFT)
                {
                    sdRaytracer.yAngleFactor--;
                    redraw = true;
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT)
                {
                    sdRaytracer.yAngleFactor++;
                    redraw = true;
                }
                if (redraw)
                {
                    sdRaytracer.createScene();
                    renderImage();
                    repaint();
                }
            }
        });
    }

    void renderImage()
    {
        tanFovX = Math.tan(fovx);
        tanFovY = Math.tan(fovy);



        for (int i = 0; i < windowWidth; i++)
        {
            futureList[i] = (Future) executorService.submit(new RaytraceTask(sdRaytracer, i,this));
        }

        for (int i = 0; i < windowWidth; i++) {
            try {
                RGB[] col = (RGB[]) futureList[i].get();
                for (int j = 0; j < windowHeight; j++)
                    image[i][j] = col[j];
            } catch (InterruptedException e) {
            } catch (ExecutionException e) {
            }
        }
    }

    void profileRenderImage() {
        long end, start, time;

        this.renderImage(); // initialisiere Datenstrukturen, erster Lauf verfaelscht sonst Messungen

        for (int procs = 1; procs < 6; procs++) {

            sdRaytracer.maxRec= procs -1;
            System.out.print(procs);
            for (int i = 0; i < 10; i++) {
                start = System.currentTimeMillis();

                this.renderImage();

                end = System.currentTimeMillis();
                time = end - start;
                System.out.print(";" + time);
            }
            System.out.println("");
        }
    }

}
