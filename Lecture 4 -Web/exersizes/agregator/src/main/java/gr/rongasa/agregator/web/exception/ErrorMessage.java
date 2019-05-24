package gr.rongasa.agregator.web.exception;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
class ErrorMessage {
    private String message;
    private List<String> messages;
}
