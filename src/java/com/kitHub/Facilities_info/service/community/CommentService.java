package com.kitHub.Facilities_info.service.community;

import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.domain.community.Comment;
import com.kitHub.Facilities_info.domain.community.Post;
import com.kitHub.Facilities_info.dto.community.CreateCommentRequest;
import com.kitHub.Facilities_info.dto.community.UpdateCommentRequest;
import com.kitHub.Facilities_info.repository.community.CommentRepository;
import com.kitHub.Facilities_info.repository.community.PostRepository;
import com.kitHub.Facilities_info.service.UserService;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;

    public Comment createComment(CreateCommentRequest request) {
        try {
            User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
            Post post = postRepository.findById(request.getPostId())
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            Comment comment = Comment.builder()
                    .content(request.getContent())
                    .user(user)
                    .post(post)
                    .modifiedAt(LocalDateTime.now())
                    .build();

            post.getComments().add(comment);  // Post 엔티티에 Comment 추가
            postRepository.save(post);  // Post를 저장하면 Comment도 자동으로 저장됨

            return comment;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create comment", e);
        }
    }

    public Comment updateComment(Long id, UpdateCommentRequest request) {
        try {
            Comment foundComment = commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            Long authorUserId = foundComment.getUser().getId();
            User user = userService.getUserInfoIfMatchesAuthor(authorUserId);

            if (user != null) {
                foundComment.updateContent(request.getContent());
                return commentRepository.save(foundComment);
            } else {
                throw new RuntimeException("작성자만 수정할 수 있습니다");
            }
        } catch (RuntimeException e) {
            // 자세한 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to update comment", e);
        } catch (Exception e) {
            // 기타 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to update comment due to an unexpected error", e);
        }
    }

    public void deleteComment(Long id) {
        try {
            Comment foundComment = commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));

            Long authorUserId = foundComment.getUser().getId();
            User user = userService.getUserInfoIfMatchesAuthor(authorUserId);

            if (user != null) {
                commentRepository.delete(foundComment);
            } else {
                throw new RuntimeException("작성자만 삭제할 수 있습니다");
            }
        } catch (RuntimeException e) {
            // 자세한 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to delete comment", e);
        } catch (Exception e) {
            // 기타 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to delete comment due to an unexpected error", e);
        }
    }

    public List<Comment> getAllComments() {
        try {
            return commentRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all comments", e);
        }
    }

    public Comment getCommentById(Long id) {
        try {
            return commentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comment not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get comment by id", e);
        }
    }
}
