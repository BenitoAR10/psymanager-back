package bo.com.ucb.psymanager.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionNoteSummaryDto {
    private LocalDate sessionDate;
    private String topicAddressed;
    private String sessionSummary;
    private String relevantObservations;
    private String nextTopic;
}
