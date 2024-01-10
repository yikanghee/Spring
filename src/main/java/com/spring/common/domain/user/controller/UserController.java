package com.spring.common.domain.user.controller;

import com.spring.common.domain.user.dto.*;
import com.spring.common.domain.user.service.UserService;
import com.spring.common.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/index")
    public String index() {
        return "index";
    }

    @PostMapping("reissue")
    public ResponseEntity<StatusResponse> reissue(HttpServletRequest request,
                                                  HttpServletResponse response) {
        userService.reissue(request, response);
        return HttpResponseEntity.RESPONSE_OK;
    }

    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(
            @Validated @RequestBody SignupRequsetDto request) {
        userService.signup(request);
        return HttpResponseEntity.CREATED_OK;
    }

    @PostMapping("/login")
    public ResponseEntity<StatusResponse> login(
            @Validated @RequestBody LoginRequestDto request, HttpServletResponse response) {
        userService.login(response, request);
        return HttpResponseEntity.RESPONSE_OK;
    }

    @PostMapping("/duplicate")
    public ResponseEntity<DuplicateCheckResponseDto> duplicateCheck(
            @RequestBody DuplicateCheckRequestDto request) {
        return ResponseEntity.ok().body(userService.duplicateCheck(request));
    }
}
