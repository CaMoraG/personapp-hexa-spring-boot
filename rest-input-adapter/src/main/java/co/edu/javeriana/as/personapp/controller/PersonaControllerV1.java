package co.edu.javeriana.as.personapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.as.personapp.adapter.PersonaInputAdapterRest;
import co.edu.javeriana.as.personapp.common.exceptions.InvalidOptionException;
import co.edu.javeriana.as.personapp.model.request.PersonaRequest;
import co.edu.javeriana.as.personapp.model.response.PersonaResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/persona")
public class PersonaControllerV1 {
	
	@Autowired
	private PersonaInputAdapterRest personaInputAdapterRest;
	
	@ResponseBody
	@GetMapping(path = "/{database}", produces = MediaType.APPLICATION_JSON_VALUE)
	public List<PersonaResponse> personas(@PathVariable String database) {
		log.info("Into personas REST API");
			return personaInputAdapterRest.historial(database.toUpperCase());
	}
	
	@ResponseBody
	@PostMapping(path = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	public PersonaResponse crearPersona(@RequestBody PersonaRequest request) {
		log.info("esta en el metodo crearTarea en el controller del api");
		return personaInputAdapterRest.crearPersona(request);
	}

	 // — UPDATE
    @PutMapping(
      path = "/{database}/{id}",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PersonaResponse actualizarPersona(
        @PathVariable String database,
        @PathVariable Integer id,
        @RequestBody PersonaRequest req
    ) {
        log.info("PUT /persona/{}/{} — actualizarPersona", database, id);
        return personaInputAdapterRest.edit(database.toUpperCase(), id, req);
    }

	 // — DELETE
    @DeleteMapping("/{database}/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrarPersona(
        @PathVariable String database,
        @PathVariable Integer id
    ) {
        log.info("DELETE /persona/{}/{} — borrarPersona", database, id);
        personaInputAdapterRest.drop(database.toUpperCase(), id);
    }

	// — READ BY ID
	@ResponseBody
	@GetMapping(
	path = "/{database}/{id}",
	produces = MediaType.APPLICATION_JSON_VALUE
	)
	public PersonaResponse getById(
		@PathVariable String database,
		@PathVariable Integer id
	) {
		log.info("GET /persona/{}/{} — getById", database, id);
		return personaInputAdapterRest.findOne(database.toUpperCase(), id);
	}

	
}
