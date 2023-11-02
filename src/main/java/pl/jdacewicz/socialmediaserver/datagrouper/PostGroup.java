package pl.jdacewicz.socialmediaserver.datagrouper;

import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "post_groups")
@Getter
@SuperBuilder
class PostGroup extends Group {

    private final static String MAIN_DIRECTORY = "data/groups/post-groups";

    @Override
    String getFolderDirectory() {
        return String.format("%s/%s", MAIN_DIRECTORY, getGroupId());
    }
}
