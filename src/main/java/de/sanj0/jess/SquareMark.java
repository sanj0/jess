package de.sanj0.jess;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.transform.Vector2f;
import de.edgelord.saltyengine.utils.ColorUtil;

import java.awt.*;
import java.util.Objects;

/**
 * Square mark of a chess board square.
 */
public class SquareMark extends Mark {

    public static final Color COLOR = ColorUtil.withAlpha(ColorUtil.FIREBRICK_RED, .7f);

    /**
     * Position of the upper right corner
     */
    private Vector2f position;

    public SquareMark(final Vector2f position) {
        this.position = position;
    }

    @Override
    public void draw(final SaltyGraphics saltyGraphics) {
        saltyGraphics.setColor(COLOR);
        saltyGraphics.drawRect(position, BoardRenderer.SQUARE_SIZE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SquareMark that = (SquareMark) o;
        return Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(position);
    }

    /**
     * Gets {@link #position}.
     *
     * @return the value of {@link #position}
     */
    public Vector2f getPosition() {
        return position;
    }
}
