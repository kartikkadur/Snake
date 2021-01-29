package de.unikl.seda.snake.gui.tools.cache.audio;

import de.unikl.seda.snake.gui.tools.audio.AudioType;

import javax.sound.sampled.Clip;
import java.util.LinkedList;

public interface AudioCache {
    Clip getAudioClip(AudioType sound);

    LinkedList<Clip> getAllAudioClips();
}
