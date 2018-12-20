import java.util.List;

class Matrix
{
    float value[][] = new float[4][4]; //faellt nichts besseres als namen ein

    Matrix()
    { }

    Matrix(float[][] value)
    {
        this.value = value;
    }

    void print()
    {
        for (int i = 0; i < 4; i++)
        {
            for (int j = 0; j < 4; j++)
            {
                System.out.print(" " + (value[i][j] + "       ").substring(0, 8));
            }
            System.out.println();
        }
    }


    Matrix multiplication(Matrix matrix)
    {
        Matrix resultMatrix = new Matrix();
        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
            {
                float sum = 0f;
                for (int k = 0; k < 4; k++) sum = sum + value[i][k] * matrix.value[k][j];
                resultMatrix.value[i][j] = sum;
            }
        return resultMatrix;
    }

    Vec3D multiplication(Vec3D vector) //VEC3d? funktion auf der Matrix --> ueberladene funktion
    {
        Vec3D resultVector = new Vec3D(value[0][0] * vector.x + value[0][1] * vector.y + value[0][2] * vector.z + value[0][3] * vector.w,
                value[1][0] * vector.x + value[1][1] * vector.y + value[1][2] * vector.z + value[1][3] * vector.w,
                value[2][0] * vector.x + value[2][1] * vector.y + value[2][2] * vector.z + value[2][3] * vector.w,
                value[3][0] * vector.x + value[3][1] * vector.y + value[3][2] * vector.z + value[3][3] * vector.w);
        //return new Vec3D(resultVector.x/resultVector.w,resultVector.y/resultVector.w,resultVector.z/resultVector.w,1);
        resultVector.x = resultVector.x / resultVector.w;
        resultVector.y = resultVector.y / resultVector.w;
        resultVector.z = resultVector.z / resultVector.w;
        resultVector.w = 1;
        return resultVector;
    }

    static Matrix createId() //delete?
    {
        return new Matrix(new float[][]
                {
                {1, 0, 0, 0},
                {0, 1, 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }


    static Matrix createXRotation(float angle)
    {
        return new Matrix(new float[][]{
                {1, 0, 0, 0},
                {0, (float) Math.cos(angle), (float) -Math.sin(angle), 0},
                {0, (float) Math.sin(angle), (float) Math.cos(angle), 0},
                {0, 0, 0, 1}});
    }

    static Matrix createYRotation(float angle)
    {
        return new Matrix(new float[][]{
                {(float) Math.cos(angle), 0, (float) Math.sin(angle), 0},
                {0, 1, 0, 0},
                {(float) -Math.sin(angle), 0, (float) Math.cos(angle), 0},
                {0, 0, 0, 1}});
    }

    static Matrix createZRotation(float angle) //unbenutzt
    {
        return new Matrix(new float[][]{
                {(float) Math.cos(angle), (float) -Math.sin(angle), 0, 0},
                {(float) Math.sin(angle), (float) Math.cos(angle), 0, 0},
                {0, 0, 1, 0},
                {0, 0, 0, 1}});
    }

    static Matrix createTranslation(float dx, float dy, float dz) {
        return new Matrix(new float[][]{
                {1, 0, 0, dx},
                {0, 1, 0, dy},
                {0, 0, 1, dz},
                {0, 0, 0, 1}});
    }

    void apply(List<Triangle> triangleList)
    {
        for (Triangle t : triangleList)
        {
            t.x = this.multiplication(t.x);
            t.y = this.multiplication(t.y);
            t.z = this.multiplication(t.z);
            Vec3D e1 = t.y.subtraction(t.x),
                    e2 = t.z.subtraction(t.x);
            t.normal = e1.crossProduct(e2);
            t.normal.normalize();
        }
    }
}