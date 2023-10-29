package pl.jdacewicz.socialmediaserver.reactiondatareceiver.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ReactionRequest(@NotBlank
                              @Size(min = 2, max = 32)
                              String name) {
}
