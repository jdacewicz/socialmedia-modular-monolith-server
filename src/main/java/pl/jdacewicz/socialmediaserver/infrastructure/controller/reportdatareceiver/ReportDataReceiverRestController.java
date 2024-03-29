package pl.jdacewicz.socialmediaserver.infrastructure.controller.reportdatareceiver;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import pl.jdacewicz.socialmediaserver.reportdatareceiver.ReportDataReceiverFacade;
import pl.jdacewicz.socialmediaserver.reportdatareceiver.dto.ReportDto;

@RestController
@RequestMapping(value = "/api/reports")
@RequiredArgsConstructor
public class ReportDataReceiverRestController {

    private final ReportDataReceiverFacade reportDataReceiverFacade;

    @GetMapping
    public Page<ReportDto> getReports(@RequestParam @NotBlank String dataType,
                                      @RequestParam int pageNumber,
                                      @RequestParam int pageSize) {
        return reportDataReceiverFacade.getReports(dataType, pageNumber, pageSize);
    }

    @DeleteMapping("/{id}")
    public void deleteReport(@PathVariable @NotBlank String id) {
        reportDataReceiverFacade.deleteReport(id);
    }
}
