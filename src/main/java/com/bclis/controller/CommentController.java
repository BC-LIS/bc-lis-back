package com.bclis.controller;

import com.bclis.dto.request.CommentCreateDTO;
import com.bclis.dto.response.CommentResponseDTO;
import com.bclis.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(commentCreateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

}
