class Light //bleibt da sonst ind er hauptklasse zwei weitere arrays benoetigt werden wuerden
{
    RGB color;
    Vec3D position;

    Light(Vec3D position, RGB rgb)
    {
        this.position = position;
        color = rgb;
    }
}
