package se.lexicon.springaichatmemorytools.controller;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import se.lexicon.springaichatmemorytools.service.ChatService;
import se.lexicon.springaichatmemorytools.service.OpenAIChatService;

@RestController
@RequestMapping("api/chat")
@Validated
public class ChatController {

    private final OpenAIChatService openAiChatService;
    private final ChatService chatService;


    public ChatController(OpenAIChatService openAiChatService,  ChatService chatService) {

        this.openAiChatService = openAiChatService;
        this.chatService = chatService;
    }

    @GetMapping("/message")
    public String ask(
            @RequestParam(value = "question")
            @NotNull(message = "Can not be null")
            @NotBlank(message = "Can not be blank")
            @Size(max = 200, message = "Question can not exceed 200 chars")
            String question
    ) {
        return openAiChatService.processSimpleChatQuery(question);
    }

    @GetMapping("/message/memory")
    public String askWithMemory(
            @RequestParam(value = "question")
            @NotNull(message = "question Can not be null")
            @NotBlank(message = "question Can not be blank")
            @Size(max = 200, message = "Question can not exceed 200 chars")
            String question,
            @RequestParam(value = "conversationId")
            @NotNull(message = "conversationId Can not be null")
            @NotBlank(message = "conversationId Can not be blank")
            String conversationId

    ) {
        return openAiChatService.chatWithMemory(question,  conversationId);
    }

    @GetMapping("/message/new-memory")
    public String askWithNewMemory(
            @RequestParam(value = "question")
            @NotNull(message = "question Can not be null")
            @NotBlank(message = "question Can not be blank")
            @Size(max = 200, message = "Question can not exceed 200 chars")
            String question,
            @RequestParam(value = "conversationId")
            @NotNull(message = "conversationId Can not be null")
            @NotBlank(message = "conversationId Can not be blank")
            String conversationId

    ) {
        return chatService.chatWithMemory(question, conversationId);
    }

}
