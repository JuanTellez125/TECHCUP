package edu.dosw.TECHCUP.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Team {
    private Long id;
    private User captain;
    private String name;
    private String shieldUrl;
    private boolean active;
    private List<TeamMember> members;
}