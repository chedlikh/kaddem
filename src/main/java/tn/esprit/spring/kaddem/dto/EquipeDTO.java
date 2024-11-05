package tn.esprit.spring.kaddem.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.spring.kaddem.entities.Niveau;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EquipeDTO {

        private Integer idEquipe;
        private String nomEquipe;
        private Niveau niveau;
}
