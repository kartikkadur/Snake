package de.unikl.seda.snake.gui.tools.cache.audio;

import de.unikl.seda.snake.gui.tools.audio.AudioType;
import de.unikl.seda.snake.gui.tools.exception.SnakeException;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class AudioCacheCreation implements AudioCache{
    private static final String AUDIO_FILE_PATTERN = "%s%s.wav";
    private static final String SOUNDS_FOLDER = "/resource/sound/";
    private final Map<AudioType, Clip> audioClip;

    public AudioCacheCreation() throws UnsupportedAudioFileException, LineUnavailableException {
        this.audioClip = createAudioStreams();
    }

    private Map<AudioType, Clip> createAudioStreams() throws UnsupportedAudioFileException, LineUnavailableException {
        Map<AudioType, Clip> map = new HashMap<>();
        for (AudioType audioType : AudioType.values()) {
            if (map.put(audioType, this.createAudioStream(audioType)) != null) {
                throw new IllegalStateException("Duplicate key");
            }
        }
        return map;
    }

    private Clip createAudioStream(final AudioType audioFileName) throws UnsupportedAudioFileException, LineUnavailableException {
        return this.createStream(SOUNDS_FOLDER, audioFileName.name().toLowerCase());
    }

    private Clip createStream(final String audioFolder, final String fileName){
        final String resourceName = String.format(AUDIO_FILE_PATTERN, audioFolder, fileName);
        try {
            InputStream inputStream = AudioCacheCreation.class.getResourceAsStream(resourceName);
            InputStream bufferedInputStream = new BufferedInputStream(inputStream);
            Clip clip = AudioSystem.getClip();
            clip.open(AudioSystem.getAudioInputStream(bufferedInputStream));
            clip.setFramePosition(0);
            return clip;
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException exp) {
            exp.printStackTrace();
            throw new SnakeException(exp);
        }
    }

    @Override
    public Clip getAudioClip(AudioType sound) {
        return this.audioClip.get(sound);
    }

    @Override
    public LinkedList<Clip> getAllAudioClips(){
        LinkedList<Clip> clips = new LinkedList<>(){};
        for (AudioType audioType : AudioType.values()){
            clips.add(this.audioClip.get(audioType));
        }
        return clips;
    }
}
