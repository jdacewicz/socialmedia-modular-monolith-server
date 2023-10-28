package pl.jdacewicz.socialmediaserver.discussiondatareceiver;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.DiscussionImage;
import pl.jdacewicz.socialmediaserver.discussiondatareceiver.dto.PostDto;
import pl.jdacewicz.socialmediaserver.elapsedtimeformatter.ElapsedDateTimeFormatterFacade;
import pl.jdacewicz.socialmediaserver.reactioncounter.ReactionCounterFacade;

@Component
@RequiredArgsConstructor
class PostMapper {

    private final ReactionCounterFacade reactionCounterFacade;
    private final ElapsedDateTimeFormatterFacade elapsedDateTimeFormatterFacade;
    private final CommentMapper commentMapper;

    PostDto mapToDto(Post post) {
        var discussionImage = new DiscussionImage(post.getImageName(), post.getImageMainDirectory());
        var elapsedDateTime = elapsedDateTimeFormatterFacade.formatDateTime(post.getCreationDateTime());
        var reactionCounts = reactionCounterFacade.countReactions(post.getReactionUsers());
        var comments = commentMapper.mapToDto(post.getComments());
        return PostDto.builder()
                .postId(post.getPostId())
                .content(post.getContent())
                .creator(post.getCreator())
                .image(discussionImage)
                .elapsedDateTime(elapsedDateTime)
                .reactionCounts(reactionCounts)
                .comments(comments)
                .build();
    }
}
