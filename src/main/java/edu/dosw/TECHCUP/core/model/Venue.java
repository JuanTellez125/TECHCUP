package edu.dosw.TECHCUP.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Venue {
    private long vanue_id;
    private Tournament tournament;
    private String name;
    private String Location;
    private String description;
}
