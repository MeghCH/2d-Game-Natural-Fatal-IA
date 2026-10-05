package com.shootemup.entity;

import javafx.scene.canvas.GraphicsContext;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {

    private static class TestEntity extends Entity {

        TestEntity(double x, double y, double width, double height) {
            super(x, y, width, height);
        }

        @Override
        public void update(double dt) {
        }

        @Override
        public void render(GraphicsContext gc) {
        }
    }

    @Test
    void collidesWhenEntitiesOverlap() {
        Entity a = new TestEntity(0, 0, 50, 50);
        Entity b = new TestEntity(25, 25, 50, 50);

        assertTrue(a.collidesWith(b));
    }

    @Test
    void DistantEntitiesDontTouch() {
        Entity a = new TestEntity(0, 0, 50, 50);
        Entity b = new TestEntity(200, 200, 50, 50);

        assertFalse(a.collidesWith(b));
    }

    @Test
    void doesNotCollideWhenOnlyEdgesTouch() {
        Entity a = new TestEntity(0, 0, 50, 50);
        Entity b = new TestEntity(50, 0, 50, 50);

        assertFalse(a.collidesWith(b));
    }

    @Test
    void doesNotCollideWhenSeparatedVertically() {
        Entity a = new TestEntity(0, 0, 50, 50);
        Entity b = new TestEntity(0, 100, 50, 50);

        assertFalse(a.collidesWith(b));
    }

    @Test
    void collidesWhenOneInsideAnother() {
        Entity big = new TestEntity(0, 0, 100, 100);
        Entity small = new TestEntity(25, 25, 10, 10);

        assertTrue(big.collidesWith(small));
    }

    @Test
    void collisionIsSymmetric() {
        Entity a = new TestEntity(0, 0, 50, 50);
        Entity b = new TestEntity(25, 25, 50, 50);

        assertTrue(a.collidesWith(b));
        assertTrue(b.collidesWith(a));
    }

    @Test
    void settersUpdatePosition() {
        Entity entity = new TestEntity(0, 0, 50, 50);

        entity.setX(42);
        entity.setY(17);

        assertEquals(42.0, entity.getX());
        assertEquals(17.0, entity.getY());
    }

    @Test
    void gettersReturnConstructorValues() {
        Entity entity = new TestEntity(10, 20, 30, 40);

        assertEquals(10.0, entity.getX());
        assertEquals(20.0, entity.getY());
        assertEquals(30.0, entity.getWidth());
        assertEquals(40.0, entity.getHeight());
    }
}