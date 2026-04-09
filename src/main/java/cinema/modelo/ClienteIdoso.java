package cinema.modelo;

public class ClienteIdoso extends Cliente {
    private String dataNascimento;

    public ClienteIdoso(String nome, String cpf, String email, String telefone, String dataNascimento) {
        super(nome, cpf, email, telefone);
        if (dataNascimento == null)
            throw new IllegalArgumentException("Data de nascimento não pode ser nula");
        this.tipoCliente = "Idoso";
        this.dataNascimento = dataNascimento;
    }

    @Override
    public double calcularDesconto(double valorBase) {
        return valorBase * 0.5;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }
}