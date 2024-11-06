package com.bclis.controller;

import com.bclis.dto.request.CommentCreateDTO;
import com.bclis.dto.request.CommentStateUpdateDTO;
import com.bclis.dto.response.CommentContentUpdateDTO;
import com.bclis.dto.response.CommentResponseDTO;
import com.bclis.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByDocumentId(@RequestParam Long documentId) {
        List<CommentResponseDTO> commentResponseList = commentService.getAllCommentsByDocumentId(documentId);
        return ResponseEntity.ok(commentResponseList);
    }

    @PutMapping("/state")
    public ResponseEntity<CommentResponseDTO> updateCommentState(@RequestBody CommentStateUpdateDTO commentStateUpdateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateCommentState(commentStateUpdateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @PutMapping("/content")
    public ResponseEntity<CommentResponseDTO> updateCommentState(@RequestBody CommentContentUpdateDTO commentContentUpdateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateCommentContent(commentContentUpdateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @DeleteMapping
    public ResponseEntity<CommentResponseDTO> deleteComment(@RequestParam Long commentId) {
        CommentResponseDTO commentResponseDTO = commentService.deleteComment(commentId);
        return ResponseEntity.ok(commentResponseDTO);
    }
}
