class IPoint
{
    final static float epsilon = 0.0001f;
    Triangle triangle;
    Vec3D vec3D;
    float distance;

    IPoint(Triangle triangle, Vec3D vec3D, float distance)
    {
        this.triangle = triangle;
        this.vec3D = vec3D;
        this.distance = distance;
    }
}
