package com.elderlycare.controller;

import com.volcengine.ark.runtime.model.completion.chat.ChatCompletionRequest;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessage;
import com.volcengine.ark.runtime.model.completion.chat.ChatMessageRole;
import com.volcengine.ark.runtime.service.ArkService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ArkService arkService;

    public ChatController() {
        // 建议用配置文件管理API Key，这里为了演示写死
        String apiKey = "f93f70a9-0fe8-40c4-a7d7-b6fda8883c9e";
        this.arkService = ArkService.builder().apiKey(apiKey).build();
    }

    @PostMapping("/doubao")
    public ChatResponse chat(@RequestBody ChatRequest request) {
        try {
            // 将前端传来的消息转换为SDK的ChatMessage列表，role手动映射
            List<ChatMessage> sdkMessages = request.getMessages().stream()
                    .map(m -> {
                        ChatMessageRole role;
                        if ("user".equalsIgnoreCase(m.getRole())) {
                            role = ChatMessageRole.USER;
                        } else if ("assistant".equalsIgnoreCase(m.getRole())) {
                            role = ChatMessageRole.ASSISTANT;
                        } else {
                            role = ChatMessageRole.SYSTEM;
                        }
                        return ChatMessage.builder()
                                .role(role)
                                .content(m.getContent())
                                .build();
                    })
                    .collect(Collectors.toList());

            ChatCompletionRequest completionRequest = ChatCompletionRequest.builder()
                    .model("ep-20250528123908-2f75h") // 替换成你的模型ID
                    .messages(sdkMessages)
                    .build();

            var response = arkService.createChatCompletion(completionRequest);

            // 安全获取回答内容
            String answer = "";
            if (response != null && response.getChoices() != null && !response.getChoices().isEmpty()) {
                var firstChoice = response.getChoices().get(0);
                if (firstChoice.getMessage() != null) {
                    answer = String.valueOf(firstChoice.getMessage().getContent());

                }
            }

            return new ChatResponse(answer);

        } catch (Exception e) {
            e.printStackTrace();
            return new ChatResponse("请求失败：" + e.getMessage());
        }
    }

    // 请求类
    public static class ChatRequest {
        private List<Message> messages;

        public List<Message> getMessages() { return messages; }
        public void setMessages(List<Message> messages) { this.messages = messages; }
    }

    // 消息类
    public static class Message {
        private String role;
        private String content;

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }

    // 响应类
    public static class ChatResponse {
        private String answer;

        public ChatResponse() {}
        public ChatResponse(String answer) { this.answer = answer; }

        public String getAnswer() { return answer; }
        public void setAnswer(String answer) { this.answer = answer; }
    }
}
