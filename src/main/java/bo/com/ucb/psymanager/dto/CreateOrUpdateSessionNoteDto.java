package bo.com.ucb.psymanager.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrUpdateSessionNoteDto {

    private Long treatmentSessionId;
    private String topicAddressed;
    private String sessionSummary;
    private String relevantObservations;
    private String nextTopic;
}