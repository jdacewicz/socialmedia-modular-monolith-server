package pl.jdacewicz.socialmediaserver.infrastructure.controller.reactiondatareceiver;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.jdacewicz.socialmediaserver.reactiondatareceiver.ReactionDataReceiverFacade;
import pl.jdacewicz.socialmediaserver.reactiondatareceiver.dto.ReactionDto;
import pl.jdacewicz.socialmediaserver.reactiondatareceiver.dto.ReactionRequest;
import pl.jdacewicz.socialmediaserver.reactiondatareceiver.dto.ReactionUpdateRequest;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api/reactions")
@RequiredArgsConstructor
public class ReactionDataReceiverRestController {

    private final ReactionDataReceiverFacade reactionDataReceiverFacade;

    @GetMapping("/{id}")
    public ReactionDto getReactionById(@PathVariable @NotBlank String id) {
        return reactionDataReceiverFacade.getReactionById(id);
    }

    @GetMapping
    public Page<ReactionDto> getReactionsByActive (@RequestParam boolean active,
                                                   @RequestParam int pageNumber,
                                                   @RequestParam int pageSize) {
        return reactionDataReceiverFacade.getReactionsByActive(active, pageNumber, pageSize);
    }

    @GetMapping("/archived")
    public Page<ReactionDto> getArchivedReactions(@RequestParam int pageNumber,
                                                  @RequestParam int pageSize) {
        return reactionDataReceiverFacade.getArchivedReactions(pageNumber, pageSize);
    }

    @PostMapping
    public ReactionDto createReaction(@RequestPart @NotNull MultipartFile reactionImage,
                                      @RequestPart @Valid ReactionRequest reactionRequest) throws IOException {
        return reactionDataReceiverFacade.createReaction(reactionImage, reactionRequest);
    }

    @PutMapping("/{id}")
    public void updateReaction(@PathVariable @NotBlank String id,
                               @RequestPart MultipartFile reactionImage,
                               @RequestPart @Valid ReactionUpdateRequest reactionUpdateRequest) throws IOException {
        reactionDataReceiverFacade.updateReaction(id, reactionImage, reactionUpdateRequest);
    }

    @PutMapping("/{id}/activate")
    public void activateReaction(@PathVariable @NotBlank String id) {
        reactionDataReceiverFacade.activateReaction(id);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivateReaction(@PathVariable @NotBlank String id) {
        reactionDataReceiverFacade.deactivateReaction(id);
    }

    @PutMapping("/{id}/archive")
    public void archiveReaction(@PathVariable @NotBlank String id) {
        reactionDataReceiverFacade.archiveReaction(id);
    }

    @PutMapping("/{id}/unarchive")
    public void unarchiveReaction(@PathVariable @NotBlank String id) {
        reactionDataReceiverFacade.unarchiveReaction(id);
    }
}
