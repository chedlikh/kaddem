package tn.esprit.spring.kaddem.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.spring.kaddem.entities.Contrat;

import java.util.List;


@Repository
public interface ContratRepository extends CrudRepository<Contrat, Integer> {


 List<Contrat> findAll();
}
