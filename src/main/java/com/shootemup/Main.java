package com.shootemup;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import com.shootemup.core.Game;
import com.shootemup.state.PlayingState;

/**
 * Point d'entrée du jeu.
 * <p>
 * Crée la fenêtre JavaFX et relie ses événements (temps qui passe, touches)
 * au {@link Game}, qui se charge de tout le reste.
 */
public class Main extends Application {

    /**
     * Crée l'application.
     */
    public Main() {
        super();
    }

    /**
     * Crée la fenêtre, démarre le jeu sur l'écran de partie et lance la boucle de jeu.
     *
     * @param stage la fenêtre principale fournie par JavaFX
     */
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        Scene scene = new Scene(new StackPane(canvas));

        Game game = new Game();
        game.setState(new PlayingState(game));

        scene.setOnKeyPressed(e -> game.keyPressed(e.getCode()));
        scene.setOnKeyReleased(e -> game.keyReleased(e.getCode()));

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
                game.update(dt);
                game.render(gc);
            }
        }.start();

        stage.setTitle("Shoot'em up");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Lance l'application JavaFX.
     *
     * @param args arguments de la ligne de commande, non utilisés
     */
    public static void main(String[] args) {
        launch(args);
    }
}