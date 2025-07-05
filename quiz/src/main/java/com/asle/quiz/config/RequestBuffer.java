package com.asle.quiz.config;

import com.asle.quiz.domain.UserSubmission;
import org.springframework.stereotype.Component;

@Component
public class RequestBuffer extends LockFreeMPMCRingBuffer<UserSubmission> {

    public RequestBuffer() {
        super(1024);
    }
}
