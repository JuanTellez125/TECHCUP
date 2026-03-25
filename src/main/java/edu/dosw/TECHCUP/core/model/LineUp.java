package edu.dosw.TECHCUP.core.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="lineUps")
@Entity
public class LineUp{

    @Id
    private String id;

    private List<String> titular;

    private List<String> substitutes;

    private List<String> suspended;

    private Team team;



}
