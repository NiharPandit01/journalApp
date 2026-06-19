package net.engineeringdigest.journalApp.controller;

import net.engineeringdigest.journalApp.service.GoogleTextToSpeechService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tts")
public class TextToSpeechController {

    @Autowired
    private GoogleTextToSpeechService textToSpeechService;

    @PostMapping(value = "/speak", produces = "audio/mpeg")
    public ResponseEntity<byte[]> speak(@RequestBody String text) throws Exception {
        byte[] audio = textToSpeechService.synthesize(text);

        return ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(audio);
    }
}