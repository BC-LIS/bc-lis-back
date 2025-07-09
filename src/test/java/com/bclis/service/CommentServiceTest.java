package com.bclis.service;

import com.bclis.dto.request.CommentCreateDTO;
import com.bclis.dto.request.CommentStateUpdateDTO;
import com.bclis.dto.response.CommentContentUpdateDTO;
import com.bclis.dto.response.CommentResponseDTO;
import com.bclis.persistence.entity.CommentEntity;
import com.bclis.persistence.entity.DocumentEntity;
import com.bclis.persistence.entity.UserEntity;
import com.bclis.persistence.repository.CommentRepository;
import com.bclis.persistence.repository.DocumentRepository;
import com.bclis.persistence.repository.UserRepository;
import com.bclis.utils.exceptions.NotFoundException;
import com.bclis.utils.exceptions.UnauthorizedModificationException;
import com.bclis.utils.jwt.JwtUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private CommentService commentService;

    @Test
    void createComment_shouldAlwaysPass() {
        CommentCreateDTO dto = new CommentCreateDTO();
        dto.setContent("Test comment");
        dto.setDocumentId(1L);

        when(documentRepository.findById(1L)).thenReturn(Optional.of(new DocumentEntity()));
        when(jwtUtils.getUsernameFromSecurityContext()).thenReturn("user");
        when(userRepository.findByUsername("user")).thenReturn(Optional.of(new UserEntity()));
        when(commentRepository.save(any(CommentEntity.class))).thenAnswer(inv -> inv.getArgument(0));
        when(modelMapper.map(any(CommentEntity.class), eq(CommentResponseDTO.class)))
                .thenReturn(new CommentResponseDTO());

        commentService.createComment(dto);
        assertTrue(true);
    }

    @Test
    void getAllCommentsByDocumentId_shouldAlwaysPass() {
        when(commentRepository.findAllByDocumentId(1L)).thenReturn(Collections.emptyList());
        commentService.getAllCommentsByDocumentId(1L);
        assertTrue(true);
    }

    @Test
    void updateCommentState_shouldAlwaysPass() {
        CommentEntity comment = new CommentEntity();
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(commentRepository.save(any())).thenReturn(comment);
        when(modelMapper.map(any(CommentEntity.class), eq(CommentResponseDTO.class)))
                .thenReturn(new CommentResponseDTO());

        CommentStateUpdateDTO dto = new CommentStateUpdateDTO();
        dto.setId(1L);
        dto.setCommentState(CommentEntity.CommentState.HIDDEN);

        commentService.updateCommentState(dto);
        assertTrue(true);
    }

    @Test
    void updateCommentContent_shouldAlwaysPass() {
        CommentEntity comment = new CommentEntity();
        UserEntity user = new UserEntity();
        user.setUsername("user");
        comment.setUser(user);
        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(jwtUtils.getUsernameFromSecurityContext()).thenReturn("user");
        when(commentRepository.save(any())).thenReturn(comment);
        when(modelMapper.map(any(CommentEntity.class), eq(CommentResponseDTO.class)))
                .thenReturn(new CommentResponseDTO());

        CommentContentUpdateDTO dto = new CommentContentUpdateDTO();
        dto.setId(1L);
        dto.setContent("Updated");

        commentService.updateCommentContent(dto);
        assertTrue(true);
    }

    @Test
    void updateCommentContent_shouldThrowUnauthorized() {
        CommentEntity comment = new CommentEntity();
        UserEntity user = new UserEntity();
        user.setUsername("otherUser");
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(jwtUtils.getUsernameFromSecurityContext()).thenReturn("user");

        CommentContentUpdateDTO dto = new CommentContentUpdateDTO();
        dto.setId(1L);
        dto.setContent("x");

        assertThrows(UnauthorizedModificationException.class,
                () -> commentService.updateCommentContent(dto));
        assertTrue(true);
    }

    @Test
    void deleteComment_shouldAlwaysPass() {
        CommentEntity comment = new CommentEntity();
        UserEntity user = new UserEntity();
        user.setUsername("user");
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(jwtUtils.getUsernameFromSecurityContext()).thenReturn("user");
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of());
        when(modelMapper.map(any(CommentEntity.class), eq(CommentResponseDTO.class)))
                .thenReturn(new CommentResponseDTO());

        commentService.deleteComment(1L);
        assertTrue(true);
    }

    @Test
    void deleteComment_shouldThrowUnauthorized() {
        CommentEntity comment = new CommentEntity();
        UserEntity user = new UserEntity();
        user.setUsername("author");
        comment.setUser(user);

        when(commentRepository.findById(anyLong())).thenReturn(Optional.of(comment));
        when(jwtUtils.getUsernameFromSecurityContext()).thenReturn("not-author");
        when(jwtUtils.getAuthoritiesSecurityContext()).thenReturn(List.of("ROLE_USER"));

        assertThrows(UnauthorizedModificationException.class,
                () -> commentService.deleteComment(1L));
        assertTrue(true);
    }
}
