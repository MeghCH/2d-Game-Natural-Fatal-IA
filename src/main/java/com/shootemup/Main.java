package com.shootemup;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashSet;
import java.util.Set;

/**
 * Point d'entrée du jeu. Version de test : un carré déplacé avec les flèches.
 */
public class Main extends Application {

    private static final int WIDTH = 960;
    private static final int HEIGHT = 540;
    private static final double SPEED = 300;

    private final Set<KeyCode> pressed = new HashSet<>();
    private double x = 100;
    private double y = HEIGHT / 2.0;

    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new StackPane(canvas));

        scene.setOnKeyPressed(e -> pressed.add(e.getCode()));
        scene.setOnKeyReleased(e -> pressed.remove(e.getCode()));

        new AnimationTimer() {
            private long last = 0;

            @Override
            public void handle(long now) {
                if (last == 0) {
                    last = now;
                    return;
                }
                double dt = (now - last) / 1e9;
                last = now;
                update(dt);
                render(gc);
            }
        }.start();

        stage.setTitle("Shoot'em up");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    private void update(double dt) {
        if (pressed.contains(KeyCode.UP))    y -= SPEED * dt;
        if (pressed.contains(KeyCode.DOWN))  y += SPEED * dt;
        if (pressed.contains(KeyCode.LEFT))  x -= SPEED * dt;
        if (pressed.contains(KeyCode.RIGHT)) x += SPEED * dt;
    }

    private void render(GraphicsContext gc) {
        gc.setFill(Color.web("#101820"));
        gc.fillRect(0, 0, WIDTH, HEIGHT);
        gc.setFill(Color.web("#5DCAA5"));
        gc.fillRect(x, y, 40, 40);
    }

    public static void main(String[] args) {
        launch(args);
    }
}