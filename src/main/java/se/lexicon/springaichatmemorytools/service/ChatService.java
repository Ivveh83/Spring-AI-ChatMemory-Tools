package se.lexicon.springaichatmemorytools.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChatService {

    //Genom chatClient som är mer abstrakt än OpenAiChatModel kan man interagera även med andra företags modeller, såsom
    //Anthropics, HuggingFace, Mistral AI etc.
    private final ChatClient chatClient;
    private ChatMemory chatMemory; //Går att ta bort här
    private AppToolCalling appToolCalling;

    @Autowired
    public ChatService(ChatClient.Builder chatClient,  ChatMemory chatMemory, AppToolCalling appToolCalling) {
        this.chatClient = chatClient
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                        // config the vector database here
                ).build();
        this.chatMemory = chatMemory; //Går att ta bort här
        this.appToolCalling = appToolCalling;
    }



    public String chatMemory(final String question, final String conversationId){
        if (question == null || conversationId == null) {
            throw new IllegalArgumentException("question or conversationId can not be null");
        }

       ChatResponse chatResponse = chatClient.prompt()
                .system("""
                        You are a specialized name management assistant with the following capabilities:
                        1. You can fetch and display all stored names using the 'fetcAllNames' tool
                        2. You can search for specific names using the 'findByName' tool
                        3. You can add new names using the 'addNewName' tool
                        
                        Guidelines:
                        - Always use the appropriate tool for name-related operations
                        - Only respond with name-related information
                        - If a request is not about names, politely explain that you can only help with name management
                        - When displaying names, present them in a clear, organized manner
                        - Confirm successful operations with brief, clear messages
                        """)
                .user(question)
                .tools(appToolCalling)
                .options(OpenAiChatOptions.builder().model("gpt-4.1-mini").temperature(0.3).maxTokens(1000).build())
                .advisors(a -> a.param(ChatMemory.CONVERSATION_ID, conversationId))
                .call()
                .chatResponse();

        assert chatResponse != null;
        return chatResponse.getResult().getOutput().getText();
    }
}
