package bo.com.ucb.psymanager.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SessionNoteDto {

    private Long sessionNoteId;
    private Long treatmentSessionId;
    private String topicAddressed;
    private String sessionSummary;
    private String relevantObservations;
    private String nextTopic;
    private LocalDateTime createdAt;
}