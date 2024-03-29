package pl.jdacewicz.socialmediaserver.discussiondatareceiver;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;
import pl.jdacewicz.socialmediaserver.reactionuser.dto.ReactionUser;
import pl.jdacewicz.socialmediaserver.userdatareceiver.dto.UserDto;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
abstract class Discussion<T extends Discussion<T>> {

    @Id
    private String discussionId;

    @NotBlank
    @Size(max = 255)
    private String content;

    @NotNull
    private UserDto creator;

    private String imageName;

    @NotBlank
    private String imageMainDirectory;

    @Builder.Default
    private LocalDateTime creationDateTime = LocalDateTime.now();

    @Builder.Default
    private List<ReactionUser> reactionUsers = new LinkedList<>();

    abstract T withReactionUser(ReactionUser reactionUser);

    abstract T withoutReactionUser(ReactionUser reactionUser);

    String getFullImageDirectory() {
        return String.format("%s/%s", getFolderDirectory(), imageName);
    }

    String getFolderDirectory() {
        return String.format("%s/%s", getImageMainDirectory(), discussionId);
    }

    boolean isReactionUserStored(ReactionUser reactionUser) {
        return reactionUsers.contains(reactionUser);
    }
}
