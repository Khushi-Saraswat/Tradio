package com.trading.demo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.trading.demo.request.PromptBody;
import com.trading.demo.response.ApiResponse;
import com.trading.demo.service.ChatBotService;

@RestController()
@RequestMapping("/chat")
public class ChatBotController {

    @Autowired
    private ChatBotService chatBotService;

    @PostMapping
    public ResponseEntity<ApiResponse> getCoinDetails(@RequestBody PromptBody promptBody) {
        // This method is commented out as per the context provided.
        // Uncomment and implement if needed in the future.

        ApiResponse apiresponse = chatBotService.getCoinDetails(promptBody.getPrompt());
        // ApiResponse response = new ApiResponse();
        // response.setMessage(promptBody.getPrompt());

        return new ResponseEntity<>(apiresponse, HttpStatus.OK);

    }

    /*
     * @PostMapping("/simple")
     * public ResponseEntity<String> simpleChatHandler(@RequestBody PromptBody
     * promptBody) {
     * // This method is commented out as per the context provided.
     * // Uncomment and implement if needed in the future.
     * 
     * String responses = chatBotService.simpleChat(promptBody.getPrompt());
     * // ApiResponse apiresponse =
     * // chatBotService.getCoinDetails(promptBody.getPrompt());
     * // ApiResponse responses = new ApiResponse();
     * // responses.setMessage(promptBody.getPrompt());
     * 
     * return new ResponseEntity<>(responses, HttpStatus.OK);
     * 
     * }
     */

    /*
     * @GetMapping("/coin/{coinName}")
     * public ResponseEntity<CoinDTO> getCoinDetails(@PathVariable String coinName)
     * {
     * 
     * CoinDTO coinDTO = chatBotService.getCoinByName(coinName);
     * return new ResponseEntity<>(coinDTO, HttpStatus.OK);
     * }
     * 
     * @PostMapping("/bot")
     * public ResponseEntity<String> simpleChat(@RequestBody PromptBody promptBody)
     * {
     * 
     * String res = chatBotService.simpleChat(promptBody.getPrompt());
     * return new ResponseEntity<>(res, HttpStatus.OK);
     * }
     * 
     * @PostMapping("/bot/coin")
     * public ResponseEntity<ApiResponse> getCoinRealtimeTime(@RequestBody
     * PromptBody promptBody) {
     * 
     * ApiResponse res = chatBotService.getCoinDetails(promptBody.getPrompt());
     * return new ResponseEntity<>(res, HttpStatus.OK);
     * }
     */

}
