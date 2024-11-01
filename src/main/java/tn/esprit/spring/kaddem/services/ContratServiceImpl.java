package tn.esprit.spring.kaddem.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import tn.esprit.spring.kaddem.entities.Contrat;

import tn.esprit.spring.kaddem.repositories.ContratRepository;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class ContratServiceImpl implements IContratService{

private final ContratRepository contratRepository;


	public List<Contrat> retrieveAllContrats(){
		return  contratRepository.findAll();
	}

	public Contrat updateContrat (Contrat  ce){
		return contratRepository.save(ce);
	}

	public  Contrat addContrat (Contrat ce){
		return contratRepository.save(ce);
	}

	public Contrat retrieveContrat (Integer  idContrat){
		return contratRepository.findById(idContrat).orElse(null);
	}

	public  void removeContrat(Integer idContrat){
		Contrat c=retrieveContrat(idContrat);
		contratRepository.delete(c);
	}



}
