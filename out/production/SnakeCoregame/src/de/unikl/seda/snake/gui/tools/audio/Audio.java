package de.unikl.seda.snake.gui.tools.audio;

import de.unikl.seda.snake.gui.tools.cache.audio.AudioCache;
import de.unikl.seda.snake.gui.tools.cache.audio.AudioCacheCreation;

import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.util.Collection;

public class Audio {
    private AudioCache audioCache;
    private Collection<Clip> audioClips;
    private Long currentFrame;

    public Audio(){
        try {
            this.audioCache = new AudioCacheCreation();
        } catch(Exception exp){
            exp.printStackTrace();
        }
        this.audioClips = this.audioCache.getAllAudioClips();
    }

    public void play(AudioType audioType, boolean loop){
        if (loop){
            this.audioCache.getAudioClip(audioType).loop(Clip.LOOP_CONTINUOUSLY);
        }
        else{
            if (this.audioCache.getAudioClip(audioType).isRunning()) {
                this.audioCache.getAudioClip(audioType).stop();
            }
            this.audioCache.getAudioClip(audioType).setFramePosition(0);
            this.audioCache.getAudioClip(audioType).start();
        }
    }

    public void pause(AudioType audioType){
        if (!this.audioCache.getAudioClip(audioType).isRunning()){
            return;
        }
        this.currentFrame = this.audioCache.getAudioClip(audioType).getMicrosecondPosition();
        this.audioCache.getAudioClip(audioType).stop();
    }

    public void resume(AudioType audioType){
        this.audioCache.getAudioClip(audioType).stop();
        this.reset(audioType);
        this.audioCache.getAudioClip(audioType).setFramePosition(Math.toIntExact(this.currentFrame));
        this.audioCache.getAudioClip(audioType).start();
    }

    public void stop(AudioType audioType){
        if(!this.audioCache.getAudioClip(audioType).isRunning()){
            this.audioCache.getAudioClip(audioType).close();
            return;
        }
        this.audioCache.getAudioClip(audioType).stop();
        this.audioCache.getAudioClip(audioType).close();
    }

    public void reset(AudioType audioType){
        this.audioCache.getAudioClip(audioType).setFramePosition(0);
    }

    public void stopAll(){
        for (AudioType audioType : AudioType.values()){
            this.stop(audioType);
        }
    }

    public void setVolume(AudioType audioType, final VolumeLevel level){
        FloatControl gainControl = (FloatControl) this.audioCache.getAudioClip(audioType).getControl(FloatControl.Type.MASTER_GAIN);
        Float value;
        if(level == VolumeLevel.HIGH){
            value = 1.0f;
        } else if (level == VolumeLevel.MEDIUM){
            value = -5.0f;
        } else if (level == VolumeLevel.LOW){
            value = -10.0f;
        } else {
            value = -1000.0f;
        }
        gainControl.setValue(value);
    }
}