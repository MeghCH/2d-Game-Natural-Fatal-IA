package com.shootemup.audio;

import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MusicPlayerTest {

    @Test
    void musicResourceIsPackagedAsWaveFile() throws Exception {
        try (InputStream music = MusicPlayer.class.getResourceAsStream(MusicPlayer.MUSIC_RESOURCE)) {
            assertNotNull(music, "La musique de fond doit être présente dans les ressources");
            byte[] header = music.readNBytes(12);
            assertEquals(12, header.length);
            assertEquals("RIFF", new String(header, 0, 4, StandardCharsets.US_ASCII));
            assertEquals("WAVE", new String(header, 8, 4, StandardCharsets.US_ASCII));
        }
    }
}
