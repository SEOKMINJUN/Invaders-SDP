package engine;

import javax.sound.sampled.*;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SoundManager {
    private static SoundManager instance;
    static Map<String, String[]> EffectSounds;
    static Map<String, Clip> BGMs;
    static ArrayList<String[]> ESFiles;
    static ArrayList<String[]> BGMFiles;
    private static Logger logger;
/**
* Code Description
* Base: BGM files are stored in res/sound/BGM
*       ES files are stored in res/sound/ES, and should be specified in res/ES file in the format: [Type];[Alias];[File Name];[Volume]
        *         -> Type: bgm, es
*         -> Volume: A value between -80.0 and 6.0
        * Usage
* Manager Call: Use getInstance() to call the manager
* BGM Call: Use playBGM(String fileName) to play BGM. It will loop indefinitely, and you can stop it by calling stopBGM()
* ES Call: Use playES(String effectName) to play sound effects. The same ES can be called simultaneously.
* Change BGM Volume: Use modifyBGMVolume(String name, float volume) to modify the volume
* Change ES Volume: Use modifyESVolume(String name, float volume) to modify the volume
*
* All the above functionalities have been implemented and tested successfully.
*/


    private SoundManager() {
        try {
            logger = Core.getLogger();

            InputStream stream = SoundManager.class.getClassLoader().getResourceAsStream("sound");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            EffectSounds = new HashMap<>();
            BGMs = new HashMap<>();
            ESFiles = new ArrayList<>();
            BGMFiles = new ArrayList<>();

            int idx = 0;
            int idy = 0;
            String line;

            while ((line = br.readLine()) != null) {
                // 세미콜론(;)로 구분된 데이터를 파싱
                String[] data = line.split(";");
                if(data[0].equals("es")){
                    ESFiles.add(idx, new String[] {data[1], data[2], data[3]});
                    this.presetEffectSound(ESFiles.get(idx)[0], "Sound.assets/ES/"+ ESFiles.get(idx)[1], Float.parseFloat(ESFiles.get(idx)[2]));
                    idx += 1;
                }else if(data[0].equals("bgm")){
                    BGMFiles.add(idy, new String[] {data[1], data[2], data[3]});
                    this.preloadBGM(BGMFiles.get(idy)[0], "Sound.assets/BGM/"+ BGMFiles.get(idy)[1], Float.parseFloat(BGMFiles.get(idy)[2]));
                    idy += 1;
                }
            }
        } catch (IOException e) {
            logger.info(String.valueOf(e));
        }
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    public void stopAllBGM() {
        for (Clip c : BGMs.values()) {
            if (c != null)
                c.stop();
        }
    }

    public void preloadBGM(String name, String filePath, float volume){
        try {
            if (!BGMs.containsKey(name)) {
                logger.info(filePath +" is loading");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundManager.class.getClassLoader().getResource(filePath));
                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        44100,
                        false
                );
                AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);

                Clip clip = AudioSystem.getClip();
                clip.open(convertedStream);

//                볼륨 조절
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(volume);

//                해쉬멥에 추가
                BGMs.put(name, clip); // 미리 로드하여 맵에 저장
                logger.fine(name+" load complete");
            }
        } catch (UnsupportedAudioFileException | IOException | NullPointerException | LineUnavailableException e) {
            logger.info(String.valueOf(e));
        }
    }

    public int playPreloadedBGM(String name){
        Clip clip = BGMs.get(name);
        if(clip != null){
            clip.setFramePosition(0);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            return 1;
        }else{
            return 0;
        }
    }

    public void presetEffectSound(String name, String filePath, float volume) {
        try {
            if (!EffectSounds.containsKey(name)) {
                String[] tmp = {filePath, String.valueOf(volume)};
                EffectSounds.put(name, tmp);
                logger.fine(name+ "is set");
            }
        } catch (Exception e) {
            logger.info(String.valueOf(e));
        }
    }

    public int playEffectSound(String name) {
        try {
            if (EffectSounds.containsKey(name)) {
                String[] tmp = EffectSounds.get(name);
                logger.finest(tmp[0] + " is loading");
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(SoundManager.class.getClassLoader().getResource(tmp[0]));
                AudioFormat baseFormat = audioStream.getFormat();
                AudioFormat targetFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100,
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        44100,
                        false
                );
                AudioInputStream convertedStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);

                Clip clip = AudioSystem.getClip();
                clip.open(convertedStream);

//                볼륨 조절
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                volumeControl.setValue(Float.parseFloat(tmp[1]));

                clip.start();
                clip.addLineListener(event -> {
                            if(event.getType() == LineEvent.Type.STOP){
                                clip.close();
                            }
                        }
                );
                logger.finest(name + " load complete");
                return 1;
            }
            logger.warning("Failed to find Sound : " + name);
            return 0;
        } catch (UnsupportedAudioFileException | IOException | NullPointerException | LineUnavailableException e) {
            logger.info(String.valueOf(e));
            return 0;
        }
    }

    public static void playBGM(String name){
        SoundManager soundManager = Globals.getSoundManager();
        try {
            soundManager.stopAllBGM();
            new Thread(() -> soundManager.playPreloadedBGM(name)).start();
        }catch (Exception e){
            logger.info(String.valueOf(e));
        }
    }

    public static void playES(String name){
        SoundManager soundManager = Globals.getSoundManager();
        try {
            new Thread(() -> soundManager.playEffectSound(name)).start();
        }catch (Exception e){
            logger.info(String.valueOf(e));
        }
    }

    public int modifyBGMVolume(String name, float volume){
        if(volume > 2 || volume < -60){
            logger.info("Error : volume is out of index!!!!!");
            logger.info("input volume : "+ volume);
            return 0;
        }
        if(BGMs.containsKey(name)){
            Clip clip = BGMs.get(name);
            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            volumeControl.setValue(volume);
            return 1;
        }
        return 0;
    }

    public int modifyESVolume(String name, float volume){
        if(volume > 2 || volume < -60){
            logger.info("Error : volume is out of index!!!!!");
            logger.info("input volume : "+ volume);
            return 0;
        }
        if(EffectSounds.containsKey(name)){
            EffectSounds.get(name)[1] = String.valueOf(volume);
            return 1;
        }else{
            return 0;
        }
    }

    // ksm
    public static void playShipDieSounds() {
        SoundManager.playES("ally_airship_destroy_explosion");
        new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.info(String.valueOf(e));
            }
            SoundManager.playES("ally_airship_destroy_die");
        }).start();
    }
}