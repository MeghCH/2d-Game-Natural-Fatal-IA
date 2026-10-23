package com.shootemup;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import com.shootemup.audio.MusicPlayer;
import com.shootemup.core.Game;
import com.shootemup.core.GameLoop;

/**
 * Point d'entrée du jeu.
 * <p>
 * Crée la fenêtre JavaFX et relie ses événements (temps qui passe, touches)
 * au {@link Game}, qui se charge de tout le reste.
 */
public class Main extends Application {

    /** Musique de fond jouée en boucle pendant toute la partie. */
    private final MusicPlayer music = new MusicPlayer();

    /**
     * Crée l'application.
     */
    public Main() {
        super();
    }

    /**
     * Crée la fenêtre en plein écran, adapte l'échelle au ratio 16:9,
     * démarre le jeu et lance la boucle de jeu.
     *
     * @param stage la fenêtre principale fournie par JavaFX
     */
    @Override
    public void start(Stage stage) {
        Canvas canvas = new Canvas(Game.WIDTH, Game.HEIGHT);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setImageSmoothing(false);

        StackPane root = new StackPane(canvas);
        StackPane.setAlignment(canvas, Pos.CENTER);
        Scene scene = new Scene(root);
        scene.setFill(Color.BLACK);

        DoubleBinding scale = Bindings.createDoubleBinding(
                () -> Math.min(scene.getWidth() / Game.WIDTH,
                        scene.getHeight() / Game.HEIGHT),
                scene.widthProperty(), scene.heightProperty());
        canvas.scaleXProperty().bind(scale);
        canvas.scaleYProperty().bind(scale);

        Game game = new Game();
        GameLoop loop = new GameLoop(game, gc);
        game.start(loop);
        music.start();

        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
                return;
            }
            game.keyPressed(e.getCode());
        });
        scene.setOnKeyReleased(e -> game.keyReleased(e.getCode()));

        stage.setTitle("NATURAL FATAL IA");
        stage.setScene(scene);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint("F11 : basculer plein écran / fenêtré");
        stage.setFullScreen(true);
        stage.show();
    }

    /**
     * Arrête la musique et libère les ressources audio à la fermeture du jeu.
     */
    @Override
    public void stop() {
        music.stop();
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