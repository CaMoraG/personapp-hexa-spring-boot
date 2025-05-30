package co.edu.javeriana.as.personapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.MediaType;
import java.util.List;

import co.edu.javeriana.as.personapp.adapter.PhoneInputAdapterRest;
import co.edu.javeriana.as.personapp.model.request.PhoneRequest;
import co.edu.javeriana.as.personapp.model.response.PhoneResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/phone")
public class PhoneControllerV1 {

    @Autowired
    private PhoneInputAdapterRest adapter;

    @PostMapping(
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PhoneResponse crear(@RequestBody PhoneRequest req) {
        log.info("POST /phone — crear");
        return adapter.crear(req);
    }

    @GetMapping(
        path = "/{db}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public List<PhoneResponse> listar(@PathVariable String db) {
        log.info("GET /phone/{} — listar", db);
        return adapter.listar(db.toUpperCase());
    }

    @GetMapping(
        path = "/{db}/{number}",
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PhoneResponse obtener(
        @PathVariable String db,
        @PathVariable String number
    ) {
        log.info("GET /phone/{}/{} — obtener", db, number);
        return adapter.obtener(db.toUpperCase(), number);
    }

    @PutMapping(
        path = "/{db}/{number}",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public PhoneResponse actualizar(
        @PathVariable String db,
        @PathVariable String number,
        @RequestBody PhoneRequest req
    ) {
        log.info("PUT /phone/{}/{} — actualizar", db, number);
        return adapter.actualizar(db.toUpperCase(), number, req);
    }

    @DeleteMapping("/{db}/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void borrar(
        @PathVariable String db,
        @PathVariable String number
    ) {
        log.info("DELETE /phone/{}/{} — borrar", db, number);
        adapter.borrar(db.toUpperCase(), number);
    }
    
}
