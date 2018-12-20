class IPoint
{
    public final static float epsilon = 0.0001f;
    public Triangle triangle;
    public Vec3D vec3D;
    public float distance;

    IPoint(Triangle triangle, Vec3D vec3D, float distance)
    {
        this.triangle = triangle;
        this.vec3D = vec3D;
        this.distance = distance;
    }
}
