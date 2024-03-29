package pl.jdacewicz.socialmediaserver.reportdatareceiver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import pl.jdacewicz.socialmediaserver.reportdatareceiver.dto.ReportRequest;
import pl.jdacewicz.socialmediaserver.userdatareceiver.UserDataReceiverFacade;
import pl.jdacewicz.socialmediaserver.userdatareceiver.dto.LoggedUserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;

class ReportDataServiceTest {

    ReportDataService reportDataService;

    ReportRepositoryTest reportRepositoryTest;
    UserDataReceiverFacade userDataReceiverFacade;

    @BeforeEach
    void setUp() {
        userDataReceiverFacade = Mockito.mock(UserDataReceiverFacade.class);
        reportRepositoryTest = new ReportRepositoryTest();
        reportDataService = new ReportDataService(reportRepositoryTest, userDataReceiverFacade);
    }

    @Test
    void should_create_report_when_creating_report() {
        //When
        var reportedDataId = "id";
        var reportRequest = new ReportRequest("type", "content");
        var reportDataType = "dataType";
        var loggedUserDto = LoggedUserDto.builder()
                .userId("id")
                .build();
        var authenticationHeader = "token";
        when(userDataReceiverFacade.getLoggedInUser(authenticationHeader)).thenReturn(loggedUserDto);
        try (MockedStatic<ReportType> reportType = Mockito.mockStatic(ReportType.class);
             MockedStatic<DataType> dataType = Mockito.mockStatic(DataType.class)) {
            reportType.when(() -> ReportType.getType(reportRequest.reportType()))
                    .thenReturn(ReportType.SPAM);
            dataType.when(() -> DataType.getType(reportDataType))
                    .thenReturn(DataType.POST);
            //When
            reportDataService.createReport(reportedDataId, authenticationHeader, reportRequest, reportDataType);
            var result = reportRepositoryTest.findAll();
            //Then
            assertFalse(result.isEmpty());
            assertEquals(reportRequest.content(), result.get(0)
                    .content());
            assertEquals(loggedUserDto.getUserId(), result.get(0)
                    .reportingUserId());
            assertEquals(reportedDataId, result.get(0)
                    .reportedDataId());
        }
    }
}