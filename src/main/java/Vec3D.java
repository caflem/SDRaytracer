class Vec3D
{
    public float x;
    public float y;
    public float z;
    public float w = 1;

    public Vec3D(float x, float y, float z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vec3D(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    public Vec3D addition(Vec3D vec3D)
    {
        return new Vec3D(x + vec3D.x, y + vec3D.y, z + vec3D.z);
    }

    public Vec3D subtraction(Vec3D vec3D)
    {
        return new Vec3D(x - vec3D.x, y - vec3D.y, z - vec3D.z);
    }

    public Vec3D multiplication(float factor)
    {
        return new Vec3D(factor * x, factor * y, factor * z);
    }

    public void normalize()
    {
        float distance = calculateDistance();
        x = x / distance;
        y = y / distance;
        z = z / distance;
    }

    private float calculateDistance()//neu
    {
        float distance = (float) Math.sqrt((x * x) + (y * y) + (z * z));
        return distance;
    }

    public float dotProduct(Vec3D vec3D)//Skalarprodukt
    {
        return x * vec3D.x + y * vec3D.y + z * vec3D.z;
    }

    public Vec3D crossProduct(Vec3D vec3D) //kreuzprodukt
    {
        return new Vec3D(y * vec3D.z - z * vec3D.y, z * vec3D.x - x * vec3D.z, x * vec3D.y - y * vec3D.x);
    }
}