package com.example.demo.web.v1.member;

import com.example.demo.core.member.result.FindMemberResult;
import com.example.demo.core.member.service.CreateMemberService;
import com.example.demo.web.ApiResponse;
import com.example.demo.web.v1.member.request.CreateMemberRequest;
import com.example.demo.web.v1.member.response.CreateMemberResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateMemberController {

    private final CreateMemberService createMemberService;

    @PostMapping("/v1/member")
    public ApiResponse<CreateMemberResponse> create(@RequestBody @Valid CreateMemberRequest request) {
        FindMemberResult result = createMemberService.create(request.toParam());

        return ApiResponse.success(CreateMemberResponse.from(result));
    }
}
