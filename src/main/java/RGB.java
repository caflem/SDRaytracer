import java.awt.*;

class RGB  //siehe oben
{
    private float red;
    private float green;
    private float blue;
    private Color color;

    RGB(float red, float green, float blue)
    {
       checkSingleColors(red, green, blue);
    }

    private void checkSingleColors(float red, float green, float blue) //neu
    {
        if (red > 1) red = 1;
        else if (red < 0) red = 0;
        if (green > 1) green = 1;
        else if (green < 0) green = 0;
        if (blue > 1) blue = 1;
        else if (blue < 0) blue = 0;

        this.red = red;
        this.green = green;
        this.blue = blue;
    }

    Color createColor()
    {
        if (color != null)
            return color;
        color = new Color((int) (red * 255), (int) (green * 255), (int) (blue * 255));
        return color;
    }

    static RGB addColors(RGB color1, RGB color2, float ratio) //in Klasse RGB Verschieben !
    {
        return new RGB((color1.red + color2.red * ratio),
                (color1.green + color2.green * ratio),
                (color1.blue + color2.blue * ratio));
    }
}