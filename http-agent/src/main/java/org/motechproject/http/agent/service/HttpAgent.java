package org.motechproject.http.agent.service;

import org.springframework.http.ResponseEntity;

public interface HttpAgent {

    void execute(String url, Object data, Method method);

    void executeSync(String url, Object data, Method method);
    
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data, Method method);
    
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data, Method method, Integer retry_count);
    
    ResponseEntity<?> executeWithReturnTypeSync(String url, Object data, Method method, Integer retry_count, Long retry_interval);
}
