package med.voll.api.domain.consulta;

import jakarta.validation.ValidationException;
import med.voll.api.domain.ValidacaoException;
import med.voll.api.domain.medico.Medico;
import med.voll.api.domain.medico.MedicoRepository;
import med.voll.api.domain.paciente.PacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AgendaDeConsulta {

    @Autowired
    private ConsultaRepository consultaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    public void agendar(DadosAgendamentoConsulta dados){

        var paciente = pacienteRepository.getReferenceById(dados.idPaciente());

        //var medico = medicoRepository.findById(dados.idMedico()).get();

        var medico = escolherMedico(dados);

        if (!pacienteRepository.existsById(dados.idPaciente())){
            throw new ValidationException("Id do paciente informado não existe!");
        }

        if (dados.idMedico() != null && !medicoRepository.existsById(dados.idMedico())){
            throw new ValidationException("Id do paciente informado não existe!");
        }

        var consulta = new Consulta(null, medico, paciente, dados.data());

        consultaRepository.save(consulta);

    }

    private Medico escolherMedico(DadosAgendamentoConsulta dados) {

        if (dados.idMedico() != null) {
            return medicoRepository.getReferenceById(dados.idMedico());
        }

        if (dados.especialidade() == null) {

            throw new ValidacaoException("Especilidade é obritatória quando médico não for escolhido!");
        }

        return medicoRepository.escolherMedicoAleatorioLivreNaData(dados.especialidade(), dados.data());

    }

}
