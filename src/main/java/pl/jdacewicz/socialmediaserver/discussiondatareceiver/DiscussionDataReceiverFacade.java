package pl.jdacewicz.socialmediaserver.discussiondatareceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostDto;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostReactionRequest;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostRequest;
import pl.jdacewicz.socialmediaserver.filestorage.FileStorageFacade;
import pl.jdacewicz.socialmediaserver.filestorage.dto.DirectoryDeleteRequest;
import pl.jdacewicz.socialmediaserver.filestorage.dto.FileUploadRequest;
import pl.jdacewicz.socialmediaserver.reactionuser.ReactionUserFacade;
import pl.jdacewicz.socialmediaserver.reactionuser.dto.ReactionUserRequest;

import java.io.IOException;

@RequiredArgsConstructor
public class DiscussionDataReceiverFacade {

    private final DiscussionDataReceiverService discussionDataReceiverService;
    private final ReactionUserFacade reactionUserFacade;
    private final FileStorageFacade fileStorageFacade;
    private final PostMapper postMapper;

    public PostDto getPostById(String postId) {
        var foundPost = discussionDataReceiverService.getPostById(postId);
        return postMapper.mapToDto(foundPost);
    }

    @Transactional
    public PostDto createPost(MultipartFile postImage, PostRequest postRequest) throws IOException {
        var newFileName = fileStorageFacade.generateFilename(postImage)
                .fileName();
        var createdPost = discussionDataReceiverService.createPost(postRequest.content(), newFileName);
        var imageUploadRequest = FileUploadRequest.builder()
                .fileName(newFileName)
                .fileUploadDirectory(createdPost.getFolderDirectory())
                .build();
        fileStorageFacade.uploadImage(postImage, imageUploadRequest);
        return postMapper.mapToDto(createdPost);
    }

    public PostDto reactToPost(PostReactionRequest postReactionRequest) {
        var reactionUserRequest = mapToReactionUserRequest(postReactionRequest);
        var reactionUser = reactionUserFacade.createReactionUser(reactionUserRequest);
        var reactedPost = discussionDataReceiverService.reactToPostById(postReactionRequest.postId(), reactionUser);
        return postMapper.mapToDto(reactedPost);
    }

    @Transactional
    public void deletePost(String postId) throws IOException {
        var foundPost = discussionDataReceiverService.getPostById(postId);
        var directoryDeleteRequest = new DirectoryDeleteRequest(foundPost.getFolderDirectory());
        discussionDataReceiverService.deletePost(foundPost);
        fileStorageFacade.deleteDirectory(directoryDeleteRequest);
    }

    private ReactionUserRequest mapToReactionUserRequest(PostReactionRequest postReactionRequest) {
        return ReactionUserRequest.builder()
                .reactionId(postReactionRequest.reactionId())
                .build();
    }
}
