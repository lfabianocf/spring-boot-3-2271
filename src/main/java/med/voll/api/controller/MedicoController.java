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

@RestController
@RequestMapping("medicos")
public class MedicoController {

    @Autowired
    private MedicoRepository repository;

    @PostMapping
    @Transactional
    public  void cadastrar(@RequestBody @Valid DadosCadastroMedico dados) {

        repository.save(new Medico(dados));
       // System.out.println(dados);
    }

//    @GetMapping
//    public Page<DadosListagemMedico> lista(Pageable paginacao) {
//        //return  repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
//
//        return  repository.findAll(paginacao).map(DadosListagemMedico::new);
//    }

    @GetMapping
    public Page<DadosListagemMedico> lista(@PageableDefault(size=10, sort = {"nome"}) Pageable paginacao) {
        //return  repository.findAll(paginacao).stream().map(DadosListagemMedico::new).toList();
        // return  repository.findAll(paginacao).map(DadosListagemMedico::new);

        return  repository.findAllByAtivoTrue(paginacao).map(DadosListagemMedico::new);
    }

    @PutMapping
    @Transactional
    public void atualizar(@RequestBody @Valid DadosAtualizacaoMedico dados) {

        var medico = repository.getReferenceById(dados.id());

        medico.atualizarInformcoes(dados);

        //System.out.println("teste");
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        var medico = repository.getReferenceById(id);
        // exclusão fisica.
        //repository.deleteById(id);
        medico.excluir();

        return  ResponseEntity.noContent().build();
    }
}
