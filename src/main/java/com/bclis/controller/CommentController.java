package com.bclis.controller;

import com.bclis.dto.request.CommentCreateDTO;
import com.bclis.dto.request.CommentStateUpdateDTO;
import com.bclis.dto.response.CommentContentUpdateDTO;
import com.bclis.dto.response.CommentResponseDTO;
import com.bclis.service.CommentService;
import com.bclis.utils.constans.ApiDescription;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
@Tag(name = "Comments", description = ApiDescription.COMMENT_CONTROLLER_DESCRIPTION)
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Create comments", description = ApiDescription.CREATE_COMMENT_DESCRIPTION)
    @PostMapping
    public ResponseEntity<CommentResponseDTO> createComment(@RequestBody CommentCreateDTO commentCreateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.createComment(commentCreateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(summary = "Get comment by documentId", description = ApiDescription.GET_COMMENT_DESCRIPTION)
    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getAllCommentsByDocumentId(@RequestParam Long documentId) {
        List<CommentResponseDTO> commentResponseList = commentService.getAllCommentsByDocumentId(documentId);
        return ResponseEntity.ok(commentResponseList);
    }

    @Operation(summary = "Update the status of the comment", description = ApiDescription.UPDATE_COMMENT_STATE_DESCRIPTION)
    @PutMapping("/state")
    public ResponseEntity<CommentResponseDTO> updateCommentState(@RequestBody CommentStateUpdateDTO commentStateUpdateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateCommentState(commentStateUpdateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(summary = "Update the content of the comment", description = ApiDescription.UPDATE_COMMENT_CONTENT_DESCRIPTION)
    @PutMapping("/content")
    public ResponseEntity<CommentResponseDTO> updateCommentState(@RequestBody CommentContentUpdateDTO commentContentUpdateDTO) {
        CommentResponseDTO commentResponseDTO = commentService.updateCommentContent(commentContentUpdateDTO);
        return ResponseEntity.ok(commentResponseDTO);
    }

    @Operation(summary = "Delete the comment", description = ApiDescription.DELETE_COMMENT_DESCRIPTION)
    @DeleteMapping
    public ResponseEntity<CommentResponseDTO> deleteComment(@RequestParam Long commentId) {
        CommentResponseDTO commentResponseDTO = commentService.deleteComment(commentId);
        return ResponseEntity.ok(commentResponseDTO);
    }
}
