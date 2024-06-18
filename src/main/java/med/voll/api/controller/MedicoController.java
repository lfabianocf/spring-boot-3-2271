package med.voll.api.controller;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.medico.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroMedico dados, UriComponentsBuilder uriBuilder) {

        var medico = new Medico(dados);

        repository.save(medico);

        //Retorno código 201 - Requisição processadado e novo recurso criado

        var uri = uriBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DadosDetalhamentoMedico(medico));
    }

//    @GetMapping
//    public Page<DadosListagemMedico> lista(Pageable paginacao) {
//        //return  repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
//        return  repository.findAll(paginacao).map(DadosListagemMedico::new);
//    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemMedico>> lista(@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao) {
        //return  repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
        // return  repository.findAll(paginacao).map(DadosListagemMedico::new);
        //return  repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);

        // Retorna código 200
        return ResponseEntity.ok(page);
    }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {

        var medico = repository.getReferenceById(dados.id());

        medico.atualizarInformcoes(dados);

        // Retornando dados atualizado. Criando um DTO para isso.
        return ResponseEntity.ok(new DadosDetalhamentoMedico(medico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        var medico = repository.getReferenceById(id);
        // exclusão fisica.
        //repository.deleteById(id);
        medico.excluir();

        // Retorna código 204 Requisição processada e sem retorno
        return  ResponseEntity.noContent().build();
    }

}
