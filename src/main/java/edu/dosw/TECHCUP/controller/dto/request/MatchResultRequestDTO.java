package edu.dosw.TECHCUP.controller.dto.request;

import lombok.*;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MatchResultRequestDTO {

    private int localScore;

    private int awayScore;

    private List<String> scorerIds;

    private List<String> yellowCardPlayerIds;

    private List<String> redCardPlayerIds;

}