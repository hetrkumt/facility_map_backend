package com.kitHub.Facilities_info.service.community;

import com.kitHub.Facilities_info.domain.User;
import com.kitHub.Facilities_info.domain.community.Post;
import com.kitHub.Facilities_info.dto.community.CreatePostRequest;
import com.kitHub.Facilities_info.dto.community.UpdatePostRequest;
import com.kitHub.Facilities_info.repository.UserRepository;
import com.kitHub.Facilities_info.repository.community.PostRepository;
import com.kitHub.Facilities_info.service.UserService;
import com.kitHub.Facilities_info.util.Authentication.AuthenticationProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private AuthenticationProvider authenticationProvider;

    @Autowired
    private UserService userService;

    public Post createPost(CreatePostRequest request) {
        try {
            User user = authenticationProvider.getUserInfoFromSecurityContextHolder();
            Post post = Post.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .user(user)
                    .modifiedAt(LocalDateTime.now())
                    .build();

            user.addPost(post);  // User 엔티티에 Post 추가
            return postRepository.save(post);  // Post를 저장하면 User도 자동으로 저장됨
        } catch (Exception e) {
            throw new RuntimeException("Failed to create post", e);
        }
    }

    public Post updatePost(Long id, UpdatePostRequest request) {
        try {
            Post foundPost = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            Long authorUserId = foundPost.getUser().getId();
            User user = userService.getUserInfoIfMatchesAuthor(authorUserId);

            if (user != null) {
                foundPost.updatePost(request.getTitle(), request.getContent());
                return postRepository.save(foundPost);
            } else {
                throw new RuntimeException("작성자만 수정할 수 있습니다");
            }
        } catch (RuntimeException e) {
            // 자세한 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to update post", e);
        } catch (Exception e) {
            // 기타 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to update post due to an unexpected error", e);
        }
    }

    public void deletePost(Long id) {
        try {
            Post foundPost = postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));

            Long authorUserId = foundPost.getUser().getId();
            User user = userService.getUserInfoIfMatchesAuthor(authorUserId);

            if (user != null) {
                postRepository.delete(foundPost);
            } else {
                throw new RuntimeException("작성자만 삭제할 수 있습니다");
            }
        } catch (RuntimeException e) {
            // 자세한 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to delete post", e);
        } catch (Exception e) {
            // 기타 예외 메시지를 출력
            e.printStackTrace();
            throw new RuntimeException("Failed to delete post due to an unexpected error", e);
        }
    }

    public List<Post> getAllPosts() {
        try {
            return postRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all posts", e);
        }
    }

    public Post getPostById(Long id) {
        try {
            return postRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Post not found"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to get post by id", e);
        }
    }
}
