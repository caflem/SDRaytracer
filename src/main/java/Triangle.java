class Triangle
{
    Vec3D x, y, z;
    RGB color;
    Vec3D normal;
    float shininess;

    Triangle(Vec3D x, Vec3D y, Vec3D z, RGB color, float shininess)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = color;
        this.shininess = shininess;

        normalizeTriangle();

    }

    private void normalizeTriangle() //neu
    {
        Vec3D e1 = this.y.subtraction(this.x),
                e2 = this.z.subtraction(this.x);

        normal = e1.crossProduct(e2);
        normal.normalize();
    }
}
