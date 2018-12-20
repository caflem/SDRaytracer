class Ray
{
    public Vec3D start=new Vec3D(0,0,0);
    public Vec3D direction =new Vec3D(0,0,0);

    public void setStart(float x, float y, float z) { start=new Vec3D(x,y,z); }

    public void setDirection(float dx, float dy, float dz) { direction =new Vec3D(dx, dy, dz); }

    public void normalize() {  direction.normalize(); }

    // see Mueller&Haines, page 305
    public IPoint intersect(Triangle triangle) //keine extra Methoden
    {
        float epsilon=IPoint.epsilon;
        Vec3D e1 = triangle.y.subtraction(triangle.x); //subtrahiert  2 Vektoren x von y
        Vec3D e2 = triangle.z.subtraction(triangle.x);
        Vec3D p =  direction.crossProduct(e2);

        float a = e1.dotProduct(p);

        if ((a>-epsilon) && (a<epsilon))
            return new IPoint(null,null,-1);

        float f = 1/a;

        Vec3D s = start.subtraction(triangle.x);

        float u = f*s.dotProduct(p);

        if ((u<0.0) || (u>1.0)) return new IPoint(null,null,-1);

        Vec3D q = s.crossProduct(e1);

        float v = f* direction.dotProduct(q);

        if ((v<0.0) || (u+v>1.0)) return new IPoint(null,null,-1);

        // intersection point is u,v
        float dist=f*e2.dotProduct(q);

        if (dist<epsilon) return new IPoint(null,null,-1);

        Vec3D ip=triangle.x.multiplication(1-u-v).addition(triangle.y.multiplication(u)).addition(triangle.z.multiplication(v));

        //DEBUG.debug("Intersection point: "+ip.x+","+ip.y+","+ip.z);
        return new IPoint(triangle,ip,dist);
    }



}
