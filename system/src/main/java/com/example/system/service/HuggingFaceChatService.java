package com.example.system.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class HuggingFaceChatService {

    private static final String HF_INFERENCE_URL = "https://api-inference.huggingface.co/pipeline/text-generation/";

    @Value("${huggingface.api.key:}")
    private String apiKey;

    @Value("${huggingface.api.model:microsoft/DialoGPT-small}")
    private String modelId;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final String FAQ_SYSTEM = "You are a helpful assistant for a Student Complaint Management System. "
            + "You help students with: How to submit a complaint (go to Submit Complaint / New Complaint), "
            + "how to check complaint status (see dashboard or History), "
            + "categories (Academics, Hostel, Library, Transport, Infrastructure, IT Support), "
            + "and general guidance. Keep answers short and friendly. "
            + "If the user asks something outside this system, say you can only help with complaint system questions.";

    public String chat(List<ChatMessage> messages) {
        if (apiKey == null || apiKey.isBlank()) {
            return "Chat is not configured. Please set HUGGINGFACE_API_KEY.";
        }
        String userMessage = messages.isEmpty()
                ? "Hello"
                : messages.get(messages.size() - 1).getContent();
        String prompt = buildPrompt(messages, userMessage);
        return callHuggingFace(prompt);
    }

    private String buildPrompt(List<ChatMessage> messages, String lastMessage) {
        StringBuilder sb = new StringBuilder();
        sb.append(FAQ_SYSTEM).append("\n\n");
        for (ChatMessage m : messages) {
            if ("user".equalsIgnoreCase(m.getRole())) {
                sb.append("User: ").append(m.getContent()).append("\n");
            } else if ("assistant".equalsIgnoreCase(m.getRole())) {
                sb.append("Assistant: ").append(m.getContent()).append("\n");
            }
        }
        sb.append("User: ").append(lastMessage).append("\nAssistant:");
        return sb.toString();
    }

    private String callHuggingFace(String prompt) {
        String url = HF_INFERENCE_URL + modelId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        ObjectNode body = objectMapper.createObjectNode();
        body.put("inputs", prompt);
        ObjectNode params = objectMapper.createObjectNode();
        params.put("max_new_tokens", 180);
        params.put("do_sample", false);
        params.put("return_full_text", false);
        body.set("parameters", params);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(body.toString(), headers),
                    String.class
            );
            
            if (response.getStatusCode() == HttpStatus.OK) {
                return parseGeneratedText(response.getBody());
            } else {
                return "Sorry, I couldn't get a response right now. Server returned: " + response.getStatusCode();
            }
        } catch (Exception e) {
            return "Sorry, I couldn't get a response right now. Try again or check: " + e.getMessage();
        }
    }

    private String parseGeneratedText(String jsonBody) {
        try {
            JsonNode root = objectMapper.readTree(jsonBody);
            if (root.isArray() && root.size() > 0) {
                JsonNode first = root.get(0);
                if (first.has("generated_text")) {
                    return first.get("generated_text").asText().trim();
                }
            }
            return jsonBody != null ? jsonBody : "No response.";
        } catch (Exception e) {
            return jsonBody != null ? jsonBody : "No response.";
        }
    }

    public static class ChatMessage {
        private String role;
        private String content;

        public ChatMessage() {}
        public ChatMessage(String role, String content) {
            this.role = role;
            this.content = content;
        }
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}





// package com.example.system.service;

// import com.fasterxml.jackson.databind.JsonNode;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.*;
// import org.springframework.stereotype.Service;
// import org.springframework.web.client.RestTemplate;

// import java.util.List;

// @Service
// public class HuggingFaceChatService {

//     // ✅ FINAL CORRECT URL
//     private static final String HF_INFERENCE_URL =
//             "https://router.huggingface.co/hf-inference/models/";

//     @Value("${huggingface.api.key:}")
//     private String apiKey;

//     // ✅ SAFE MODEL (works without gating issues)
//     @Value("${huggingface.api.model:google/flan-t5-base}")
//     private String modelId;

//     private final RestTemplate restTemplate = new RestTemplate();
//     private final ObjectMapper objectMapper = new ObjectMapper();

//     private static final String SYSTEM_PROMPT =
//             "You are a helpful assistant for a Student Complaint Management System. "
//             + "Guide students on submitting complaints, checking status, categories "
//             + "(Academics, Hostel, Library, Transport, Infrastructure, IT Support). "
//             + "Keep answers short and clear. Only answer complaint-system-related questions.";

//     public String chat(List<ChatMessage> messages) {

//         if (apiKey == null || apiKey.isBlank()) {
//             return "API Key missing. Please set HUGGINGFACE_API_KEY.";
//         }

//         String userMessage = messages.isEmpty()
//                 ? "Hello"
//                 : messages.get(messages.size() - 1).getContent();

//         String prompt = buildPrompt(messages, userMessage);

//         return callHuggingFace(prompt);
//     }

//     private String buildPrompt(List<ChatMessage> messages, String lastMessage) {
//         StringBuilder sb = new StringBuilder();

//         sb.append(SYSTEM_PROMPT).append("\n\n");

//         for (ChatMessage m : messages) {
//             if ("user".equalsIgnoreCase(m.getRole())) {
//                 sb.append("User: ").append(m.getContent()).append("\n");
//             } else if ("assistant".equalsIgnoreCase(m.getRole())) {
//                 sb.append("Assistant: ").append(m.getContent()).append("\n");
//             }
//         }

//         sb.append("User: ").append(lastMessage).append("\nAssistant:");

//         return sb.toString();
//     }

//     private String callHuggingFace(String prompt) {

//         String url = HF_INFERENCE_URL + modelId;

//         HttpHeaders headers = new HttpHeaders();
//         headers.setContentType(MediaType.APPLICATION_JSON);
//         headers.setBearerAuth(apiKey);

//         ObjectNode body = objectMapper.createObjectNode();
//         body.put("inputs", prompt);

//         ObjectNode params = objectMapper.createObjectNode();
//         params.put("max_new_tokens", 120);
//         params.put("temperature", 0.7);
//         params.put("return_full_text", false);

//         body.set("parameters", params);

//         try {
//             ResponseEntity<String> response = restTemplate.exchange(
//                     url,
//                     HttpMethod.POST,
//                     new HttpEntity<>(body.toString(), headers),
//                     String.class
//             );

//             if (response.getStatusCode() == HttpStatus.OK) {
//                 return extractText(response.getBody());
//             } else {
//                 return "Error: " + response.getStatusCode() + " -> " + response.getBody();
//             }

//         } catch (Exception e) {
//             return "Error calling Hugging Face API: " + e.getMessage();
//         }
//     }

//     private String extractText(String json) {
//         try {
//             JsonNode root = objectMapper.readTree(json);

//             if (root.isArray() && root.size() > 0) {
//                 JsonNode first = root.get(0);

//                 if (first.has("generated_text")) {
//                     return first.get("generated_text").asText().trim();
//                 }
//             }

//             return json;

//         } catch (Exception e) {
//             return json;
//         }
//     }

//     public static class ChatMessage {
//         private String role;
//         private String content;

//         public ChatMessage() {}

//         public ChatMessage(String role, String content) {
//             this.role = role;
//             this.content = content;
//         }

//         public String getRole() { return role; }
//         public void setRole(String role) { this.role = role; }

//         public String getContent() { return content; }
//         public void setContent(String content) { this.content = content; }
//     }
// }