package pl.jdacewicz.socialmediaserver.discussiondatareceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostReactionRequest;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostRequest;
import pl.jdacewicz.socialmediaserver.reactionuser.ReactionUserFacade;
import pl.jdacewicz.socialmediaserver.reactionuser.dto.ReactionUserRequest;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
class DiscussionDataReceiverService {

    private final DiscussionDataReceiverRepository discussionDataReceiverRepository;
    private final ReactionUserFacade reactionUserFacade;

    Post getPostById(String id) {
        return discussionDataReceiverRepository.findById(id)
                .orElseThrow(UnsupportedOperationException::new);
    }

    Post createPost(PostRequest postRequest) {
        var post = Post.builder()
                .content(postRequest.content())
                .creationDateTime(LocalDateTime.now())
                .build();
        return discussionDataReceiverRepository.save(post);
    }

    @Transactional
    public Post reactToPost(PostReactionRequest postReactionRequest) {
        var reactionUserRequest = mapToReactionUserRequest(postReactionRequest);
        var reactionUser = reactionUserFacade.createReactionUser(reactionUserRequest);
        var post = getPostById(postReactionRequest.postId());
        post.reactionUsers()
                .add(reactionUser);
        return discussionDataReceiverRepository.save(post);
    }

    private ReactionUserRequest mapToReactionUserRequest(PostReactionRequest postReactionRequest) {
        return ReactionUserRequest.builder()
                .reactionId(postReactionRequest.reactionId())
                .build();
    }
}
