package tn.esprit.spring.kaddem.services;

import tn.esprit.spring.kaddem.dto.ContratDTO;
import tn.esprit.spring.kaddem.entities.Contrat;
import java.util.List;

public interface IContratService {
     List<Contrat> retrieveAllContrats();

    Contrat updateContrat(ContratDTO c);

    Contrat addContrat(ContratDTO c);

     Contrat retrieveContrat (Integer  idContrat);

      void removeContrat(Integer idContrat);


}

