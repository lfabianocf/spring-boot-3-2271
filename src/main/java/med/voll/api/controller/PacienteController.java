package med.voll.api.controller;


import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import med.voll.api.paciente.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pacientes")
public class PacienteController {

    @Autowired
    private PacienteRepository repository;

    @PostMapping
    @Transactional
    public void cadastrar(@RequestBody @Valid DadosCadastroPaciente dados)  {

        repository.save(new Paciente(dados));
    }

    @GetMapping
   public ResponseEntity <Page<DadosListagemPaciente>> listar(@PageableDefault(page=0, size=10, sort = {"nome"}) Pageable paginacao) {
        //return repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);

        var page = repository.findAllByAtivoTrue(paginacao).map(DadosListagemPaciente::new);
        // Retorna código 200
        return  ResponseEntity.ok(page);
        
   }

    @PutMapping
    @Transactional
    public ResponseEntity atualizar(@RequestBody @Valid DadosAtualizacaoPaciente dados) {

        var paciente = repository.getReferenceById(dados.id());

        paciente.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoPaciente(paciente));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity excluir(@PathVariable Long id) {

        var paciente = repository.getReferenceById(id);
        paciente.excluir();

        // Retorna código 204 Requisição processada e sem retorno
        return ResponseEntity.noContent().build();
    }
;}
