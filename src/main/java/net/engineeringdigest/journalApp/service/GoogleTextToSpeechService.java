package net.engineeringdigest.journalApp.service;


import com.google.cloud.texttospeech.v1.*;
import org.springframework.stereotype.Service;

@Service
public class GoogleTextToSpeechService {

    public byte[] synthesize(String text) throws Exception {
        try (TextToSpeechClient client = TextToSpeechClient.create()) {
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setName("en-US-Standard-C")
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            SynthesizeSpeechResponse response =
                    client.synthesizeSpeech(input, voice, audioConfig);

            return response.getAudioContent().toByteArray();
        }
    }
}
