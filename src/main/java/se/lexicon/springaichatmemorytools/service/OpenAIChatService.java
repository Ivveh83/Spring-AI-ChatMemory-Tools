package se.lexicon.springaichatmemorytools.service;

public interface OpenAIChatService {

    String processSimpleChatQuery(final String question);

    String chatWithMemory(String question, String conversationId);
}
