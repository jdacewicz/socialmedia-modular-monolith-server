package pl.jdacewicz.socialmediaserver.discussiondatareceiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import pl.jdacewicz.socialmediaserver.reactionuser.dto.ReactionUser;
import pl.jdacewicz.socialmediaserver.userdatareceiver.UserDataReceiverFacade;
import pl.jdacewicz.socialmediaserver.userdatareceiver.dto.UserDto;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class DiscussionDataReceiverServiceTest {

    DiscussionDataReceiverService discussionDataReceiverService;

    DiscussionDataReceiverRepositoryTest discussionDataReceiverRepository;
    UserDataReceiverFacade userDataReceiverFacade;

    @BeforeEach
    void setUp() {
        userDataReceiverFacade = Mockito.mock(UserDataReceiverFacade.class);
        discussionDataReceiverRepository = new DiscussionDataReceiverRepositoryTest();
        discussionDataReceiverService = new DiscussionDataReceiverService(discussionDataReceiverRepository,
                userDataReceiverFacade);
    }

    @Test
    public void should_return_post_when_getting_existing_post() {
        //Given
        var postId = "id";
        var post = Post.builder()
                .postId(postId)
                .build();
        discussionDataReceiverRepository.save(post);
        //When
        var result = discussionDataReceiverService.getPostById(postId);
        //Then
        assertEquals(postId, result.postId());
    }

    @Test
    public void should_throw_unsupported_operation_exception_when_getting_not_existing_post() {
        //Given
        var postId = "id";
        //When
        //Then
        assertThrows(UnsupportedOperationException.class,
                () -> discussionDataReceiverService.getPostById(postId));
    }

    @Test
    void should_return_created_post_when_creating_post() {
        //Given
        var content = "content";
        var imageName = "name";
        var loggedUserDto = UserDto.builder()
                .build();
        when(userDataReceiverFacade.getLoggedInUser()).thenReturn(loggedUserDto);
        //When
        var result = discussionDataReceiverService.createPost(content, imageName);
        //Then
        assertEquals(content, result.content());
        assertEquals(imageName, result.imageName());
        assertEquals(loggedUserDto, result.creator());
    }

    @Test
    void should_return_post_with_multiple_reactions_users_when_adding_unique_reaction_user_to_reacted_post() {
        //Given
        var postId = "id";
        var reactionUser = ReactionUser.builder()
                .userId("id")
                .reactionId("id")
                .build();
        var reactionUserTwo = ReactionUser.builder()
                .userId("idTwo")
                .reactionId("idTwo")
                .build();
        var post = Post.builder()
                .postId(postId)
                .reactionUsers(List.of(reactionUser))
                .build();
        discussionDataReceiverRepository.save(post);
        //When
        var result = discussionDataReceiverService.addReactionUserToPostById(postId, reactionUserTwo);
        //Then
        assertFalse(result.reactionUsers()
                .isEmpty());
        assertEquals(2, result.reactionUsers()
                .size());
    }

    @Test
    void should_return_post_with_single_reaction_user_when_adding_not_unique_reaction_user_to_reacted_post() {
        //Given
        var postId = "id";
        var reactionUser = ReactionUser.builder()
                .userId("id")
                .reactionId("id")
                .build();
        var reactionUserTwo = ReactionUser.builder()
                .userId("id")
                .reactionId("id")
                .build();
        var post = Post.builder()
                .postId(postId)
                .reactionUsers(List.of(reactionUser))
                .build();
        discussionDataReceiverRepository.save(post);
        //When
        var result = discussionDataReceiverService.addReactionUserToPostById(postId, reactionUserTwo);
        //Then
        assertFalse(result.reactionUsers()
                .isEmpty());
        assertEquals(1, result.reactionUsers()
                .size());
    }

}