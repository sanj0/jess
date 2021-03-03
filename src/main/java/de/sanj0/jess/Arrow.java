package de.sanj0.jess;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.ColorUtil;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * An arrow
 */
public class Arrow extends Mark {

    public static final double HALF_PI = Math.PI / 2d;

    public static final Color COLOR = ColorUtil.withAlpha(ColorUtil.DARK_ORANGE, .9f);
    public static final int THICKNESS = 15;
    public static final int HEAD_SIZE = 30;

    private final Vector2f start;
    private final Vector2f end;
    private final Vector2f lineEnd;

    private final AffineTransform transform;

    public Arrow(final Vector2f start, final Vector2f end) {
        this.start = start;
        this.end = end;
        final Vector2f lineVector = end.subtracted(start);
        lineEnd = lineVector.withMagnitude((float) lineVector.magnitude() - HEAD_SIZE).add(start);

        transform = new AffineTransform();
        transform.setToIdentity();
        double angle = Math.atan2(end.getY() - start.getY(), end.getX() - start.getX());
        transform.translate(end.getX(), end.getY());
        transform.rotate((angle - HALF_PI));
    }


    @Override
    public void draw(final SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(COLOR);
        saltyGraphics.setStroke(new BasicStroke(THICKNESS));
        drawArrowLine(saltyGraphics.getGraphics2D(), start.getX(), start.getY(),
                end.getX(), end.getY(), HEAD_SIZE, HEAD_SIZE * .75f);
    }

    /**
     * By RubenLaguna on stackoverflow - https://stackoverflow.com/questions/2027613/how-to-draw-a-directed-arrow-line-in-java
     * because my approach didn't work, even though it was so similar ;(
     * <p>
     * Draw an arrow line between two points.
     *
     * @param g  the graphics component.
     * @param x1 x-position of first point.
     * @param y1 y-position of first point.
     * @param x2 x-position of second point.
     * @param y2 y-position of second point.
     * @param d  the width of the arrow.
     * @param h  the height of the arrow.
     */
    private void drawArrowLine(Graphics g, float x1, float y1, float x2, float y2, float d, float h) {
        float dx = x2 - x1;
        float dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d;
        double xn = xm;
        double ym = h;
        double yn = -h;
        double x;
        double sin = dy / D;
        double cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {(int) x2, (int) xm, (int) xn};
        int[] ypoints = {(int) y2, (int) ym, (int) yn};

        g.drawLine((int) x1, (int) y1, (int) lineEnd.getX(), (int) lineEnd.getY());
        g.fillPolygon(xpoints, ypoints, 3);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Arrow arrow = (Arrow) o;
        return Objects.equals(start, arrow.start) && Objects.equals(end, arrow.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    /**
     * Gets {@link #start}.
     *
     * @return the value of {@link #start}
     */
    public Vector2f getStart() {
        return start;
    }

    /**
     * Gets {@link #end}.
     *
     * @return the value of {@link #end}
     */
    public Vector2f getEnd() {
        return end;
    }
}
