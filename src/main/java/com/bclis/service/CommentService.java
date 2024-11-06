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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final DocumentRepository documentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final JwtUtils jwtUtils;

    public CommentResponseDTO createComment(CommentCreateDTO commentCreateDTO) {
        DocumentEntity documentEntity = documentRepository.findById(commentCreateDTO.getDocumentId())
                .orElseThrow(() -> new NotFoundException("Document not found"));

        UserEntity userEntity = userRepository.findByUsername(jwtUtils.getUsernameFromSecurityContext())
                .orElseThrow(() -> new NotFoundException("User not found"));

        CommentEntity commentEntity = CommentEntity.builder()
                .content(commentCreateDTO.getContent())
                .document(documentEntity)
                .user(userEntity)
                .state(CommentEntity.CommentState.VISIBLE)
                .build();

        commentRepository.save(commentEntity);

        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }

    public List<CommentResponseDTO> getAllCommentsByDocumentId(Long documentId) {
        List<CommentEntity> commentEntities = commentRepository.findAllByDocumentId(documentId);

        return commentEntities.stream()
                .map(commentEntity -> modelMapper.map(commentEntity, CommentResponseDTO.class))
                .toList();
    }

    public CommentResponseDTO updateCommentState(CommentStateUpdateDTO commentStateUpdateDTO){
        CommentEntity commentEntity = commentRepository.findById(commentStateUpdateDTO.getId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));;

        commentEntity.setState(commentStateUpdateDTO.getCommentState());
        commentRepository.save(commentEntity);

        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }

    public CommentResponseDTO updateCommentContent(CommentContentUpdateDTO commentContentUpdateDTO) {
        CommentEntity commentEntity = commentRepository.findById(commentContentUpdateDTO.getId())
                .orElseThrow(() -> new NotFoundException("Comment not found"));

        String author = commentEntity.getUser().getUsername();
        String authenticatedUser = jwtUtils.getUsernameFromSecurityContext();

        if (!author.equalsIgnoreCase(authenticatedUser)) {
            throw new UnauthorizedModificationException("User not authorized to edit comment");
        }

        commentEntity.setContent(commentContentUpdateDTO.getContent());
        commentRepository.save(commentEntity);

        return modelMapper.map(commentEntity, CommentResponseDTO.class);
    }
}
