package cinema.modelo;

public class ClienteEstudante extends Cliente {
    private String instituicao;
    private String matricula;

    public ClienteEstudante(String nome, String cpf, String email, String telefone, String instituicao, String matricula) {
        super(nome, cpf, email, telefone);
        if (matricula == null)
            throw new IllegalArgumentException("Matrícula não pode ser nula");
        if (instituicao == null)
            throw new IllegalArgumentException("Instituição não pode ser nula");
        this.tipoCliente = "Estudante";
        this.instituicao = instituicao;
        this.matricula = matricula;
    }

    @Override
    public double calcularDesconto(double valorBase) {
        return valorBase * 0.5;
    }

    @Override
    public String getTipoCliente() {
        return this.tipoCliente;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public String getMatricula() {
        return matricula;
    }
}