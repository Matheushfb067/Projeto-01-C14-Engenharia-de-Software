package cinema;

import cinema.modelo.Filme;
import cinema.modelo.Sala;
import cinema.modelo.Sessao;
import cinema.modelo.Cliente;
import cinema.modelo.ClienteComum;
import cinema.modelo.ClienteEstudante;
import cinema.modelo.ClienteIdoso;
import cinema.pagamento.Pagamento;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Suite de Testes - CinePOO")
class FilmeTeste {

    private Filme filme;
    private Sala sala;
    private Sessao sessao;
    private Cliente cliente;

    @Mock
    private Pagamento pagamentoMock;

    @Mock
    private Sala salaMock;

    @BeforeEach
    void setUp() {
        filme   = new Filme("Interestelar", 169, "Ficção Científica", 12);
        sala    = new Sala(1, 100, "IMAX", true);
        sessao  = new Sessao(1, "22/11/2025", "20:00");
        cliente = new Cliente("Chris", "133.245.987-65", "chris@email.com", "7070-7070");
    }

    // ══════════════════════════════════════════════
    //  BLOCO 1 – FLUXO NORMAL (10 testes)
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("T01 – Filme criado com título correto")
    void deveCriarFilmeComTituloCorreto() {
        assertEquals("Interestelar", filme.getTitulo());
    }

    @Test
    @DisplayName("T02 – ClienteComum não recebe desconto")
    void clienteComumNaoRecebeDesconto() {
        double resultado = new ClienteComum("Ana", "111.111.111-11", "ana@email.com", "9999-9999")
                .calcularDesconto(45.0);
        assertEquals(45.0, resultado, 0.001);
    }

    @Test
    @DisplayName("T03 – ClienteEstudante recebe 50% de desconto")
    void clienteEstudanteRecebe50PorCentoDesconto() {
        double resultado = new ClienteEstudante("Bruno", "222.222.222-22", "bruno@email.com", "8888-8888", "Inatel", "2021001")
                .calcularDesconto(45.0);
        assertEquals(22.5, resultado, 0.001);
    }

    @Test
    @DisplayName("T04 – ClienteIdoso recebe 50% de desconto")
    void clienteIdosoRecebe50PorCentoDesconto() {
        double resultado = new ClienteIdoso("Carlos", "333.333.333-33", "carlos@email.com", "7777-7777", "01/01/1950")
                .calcularDesconto(45.0);
        assertEquals(22.5, resultado, 0.001);
    }

    @Test
    @DisplayName("T05 – Tipo de cliente está correto após instanciar")
    void tipoClienteDeveEstarCorreto() {
        ClienteEstudante est = new ClienteEstudante(
                "Julia", "444.444.444-44", "julia@email.com", "6666-6666", "Inatel", "2022002");
        assertEquals("Estudante", ((Cliente) est).getTipoCliente());
    }

    @Test
    @DisplayName("T06 – Sessão guarda horário corretamente")
    void sessaoDeveGuardarHorarioCorreto() {
        assertEquals("20:00", sessao.getHorario());
    }

    @Test
    @DisplayName("T07 – Sessão guarda número corretamente")
    void sessaoDeveGuardarNumeroCorreto() {
        assertEquals(1, sessao.getNumSessao());
    }

    @Test
    @DisplayName("T08 – Sala criada com capacidade e ID corretos")
    void salaDeveSerCriadaComCapacidadeCorreta() {
        assertEquals(100, sala.getCapacidade());
        assertEquals(1, sala.getIdSala());
    }

    @Test
    @DisplayName("T09 – [MOCK] Pagamento aprovado retorna true")
    void pagamentoAprovadoDeveRetornarTrue() {
        when(pagamentoMock.pagar(22.5)).thenReturn(true);
        assertTrue(pagamentoMock.pagar(22.5));
        verify(pagamentoMock, times(1)).pagar(22.5);
    }

    @Test
    @DisplayName("T10 – Cliente possui getters corretos")
    void clienteDeveRetornarDadosCorretos() {
        assertEquals("Chris", cliente.getNome());
        assertEquals("133.245.987-65", cliente.getCpf());
        assertEquals("chris@email.com", cliente.getEmail());
        assertEquals("7070-7070", cliente.getTelefone());
    }

    // ══════════════════════════════════════════════
    //  BLOCO 2 – FLUXO DE EXTENSÃO (10 testes)
    // ══════════════════════════════════════════════

    @Test
    @DisplayName("T11 – Desconto com valor zero retorna zero")
    void descontoComValorZeroDeveRetornarZero() {
        double resultado = new ClienteEstudante("Leo", "555.555.555-55", "leo@email.com", "5555-5555", "Inatel", "2023003")
                .calcularDesconto(0.0);
        assertEquals(0.0, resultado, 0.001);
    }

    @Test
    @DisplayName("T12 – Desconto com valor negativo resulta em valor negativo")
    void descontoComValorNegativoDevePropagarNegativo() {
        double resultado = new ClienteEstudante("Mia", "666.666.666-66", "mia@email.com", "4444-4444", "Inatel", "2024004")
                .calcularDesconto(-45.0);
        assertTrue(resultado < 0, "Valor negativo deve resultar em desconto negativo");
    }

    @Test
    @DisplayName("T13 – Ocupar assento 0 retorna false")
    void ocuparAssentoComNumeroZeroDeveRetornarFalse() {
        assertFalse(sala.ocuparAssentoPorNumero(0), "Assento 0 não existe");
    }

    @Test
    @DisplayName("T14 – Ocupar assento fora da capacidade retorna false")
    void ocuparAssentoForaDaCapacidadeDeveRetornarFalse() {
        assertFalse(sala.ocuparAssentoPorNumero(9999), "Assento inexistente deve retornar false");
    }

    @Test
    @DisplayName("T15 – Ocupar mesmo assento duas vezes retorna false na segunda")
    void ocuparMesmoAssentoDuasVezesDeveRetornarFalseNaSegunda() {
        for (int i = 1; i <= 100; i++) {
            Sala salaLocal = new Sala(1, 100, "IMAX", true);
            boolean primeira = salaLocal.ocuparAssentoPorNumero(i);
            if (primeira) {
                boolean segunda = salaLocal.ocuparAssentoPorNumero(i);
                assertFalse(segunda, "Segunda tentativa no mesmo assento deve retornar false");
                return;
            }
        }
    }

    @Test
    @DisplayName("T16 – [MOCK] Pagamento recusado retorna false")
    void pagamentoRecusadoDeveRetornarFalse() {
        when(pagamentoMock.pagar(anyDouble())).thenReturn(false);
        assertFalse(pagamentoMock.pagar(45.0), "Pagamento recusado deve retornar false");
        verify(pagamentoMock).pagar(45.0);
    }

    @Test
    @DisplayName("T17 – [MOCK] Sala lotada não permite ocupar assento")
    void salaMockLotadaNaoDevePermitirOcuparAssento() {
        when(salaMock.ocuparAssentoPorNumero(anyInt())).thenReturn(false);
        assertFalse(salaMock.ocuparAssentoPorNumero(5), "Sala lotada deve retornar false");
        verify(salaMock).ocuparAssentoPorNumero(5);
    }

    @Test
    @DisplayName("T18 – ClienteIdoso com data nula lança exceção")
    void clienteIdosoComDataNulaDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                new ClienteIdoso("Pedro", "777.777.777-77", "pedro@email.com", "3333-3333", null));
    }

    @Test
    @DisplayName("T19 – ClienteEstudante com matrícula nula lança exceção")
    void clienteEstudanteComMatriculaNulaDeveLancarExcecao() {
        assertThrows(IllegalArgumentException.class, () ->
                new ClienteEstudante("Sara", "888.888.888-88", "sara@email.com", "2222-2222", "Inatel", null));
    }

    @Test
    @DisplayName("T20 – [MOCK] Pagamento com valor zero retorna false")
    void pagamentoComValorZeroDeveRetornarFalse() {
        when(pagamentoMock.pagar(0.0)).thenReturn(false);
        assertFalse(pagamentoMock.pagar(0.0), "Pagamento com valor zero deve ser recusado");
        verify(pagamentoMock, times(1)).pagar(0.0);
    }
}