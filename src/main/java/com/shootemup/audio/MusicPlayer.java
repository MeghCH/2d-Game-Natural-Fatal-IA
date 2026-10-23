package com.shootemup.audio;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Musique de fond du jeu, jouée en boucle.
 * <p>
 * Le morceau est un fichier WAV rangé dans les ressources du projet. Il est
 * chargé une seule fois puis joué avec un {@link Clip} de l'API Java Sound :
 * {@link Clip#loop(int)} avec {@link Clip#LOOP_CONTINUOUSLY} relance
 * automatiquement le son depuis le début à chaque fin de piste, sans timer
 * ni surveillance manuelle. Le volume est légèrement baissé pour rester en
 * fond sonore.
 * <p>
 * Si aucun périphérique audio n'est disponible ou si la ressource est absente,
 * le jeu continue normalement, sans musique : l'audio ne doit jamais empêcher
 * de jouer.
 */
public final class MusicPlayer {

    /** Chemin de la musique dans les ressources du projet. */
    public static final String MUSIC_RESOURCE =
            "/audio/freesound_community-idea-34284.wav";

    /** Volume de la musique, en décibels. */
    private static final float VOLUME_DB = -8.0f;

    /** Clip audio en cours de lecture, ou {@code null} quand la musique est arrêtée. */
    private Clip clip;

    /** {@code true} tant que la musique est lancée. */
    private boolean running = false;

    /**
     * Crée un lecteur de musique, sans démarrer la lecture.
     */
    public MusicPlayer() {
    }

    /**
     * Charge la musique puis la joue en boucle.
     * <p>
     * Ne fait rien si la musique est déjà en cours. En cas d'absence de fichier
     * ou de périphérique audio, affiche un avertissement et laisse le jeu
     * continuer silencieusement.
     */
    public void start() {
        if (running) {
            return;
        }
        InputStream input = MusicPlayer.class.getResourceAsStream(MUSIC_RESOURCE);
        if (input == null) {
            System.err.println("Musique de fond introuvable : " + MUSIC_RESOURCE);
            return;
        }

        try (AudioInputStream stream = AudioSystem.getAudioInputStream(input)) {
            clip = AudioSystem.getClip();
            clip.open(stream);
            applyVolume();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            running = true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException
                 | IllegalArgumentException e) {
            System.err.println("Musique de fond indisponible : " + e.getMessage());
            stop();
        }
    }

    /**
     * Applique le volume de fond au clip, si le périphérique le permet.
     */
    private void applyVolume() {
        if (clip == null || !clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
            return;
        }
        FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        float volume = Math.max(gain.getMinimum(), Math.min(gain.getMaximum(), VOLUME_DB));
        gain.setValue(volume);
    }

    /**
     * Arrête la musique et libère le clip audio.
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
        }
        running = false;
    }

    /**
     * Indique si la musique est actuellement lancée.
     *
     * @return {@code true} si la musique joue
     */
    public boolean isRunning() {
        return running;
    }
}
