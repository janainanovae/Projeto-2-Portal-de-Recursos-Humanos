import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Classe base de todos os tipos de funcionário
public abstract class Funcionario {

    private static final LocalTime ENTRADA_MINIMA = LocalTime.of(6, 0);
    private static final LocalTime SAIDA_MAXIMA = LocalTime.of(22, 0);

    private final int id;
    private String nome;
    private final List<RegistroPonto> registros = new ArrayList<>();

    protected Funcionario(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<RegistroPonto> getRegistros() {
        return Collections.unmodifiableList(registros);
    }

    // Cada tipo de funcionário define o seu cargo, se bate ponto e o limite de horas extras (polimorfismo)
    public abstract String getTipo();

    public abstract boolean batePonto();

    public abstract int getLimiteHorasExtras();

    // Barra o registro de ponto para quem não bate ponto (gerente e estagiário)
    public void validarBatePonto() throws RegistroInvalidoException {
        if (!batePonto()) {
            throw new RegistroInvalidoException(getTipo() + " não bate ponto, por isso não é possível registrar horários.");
        }
    }

    // Valida todas as regras de negócio e, se estiver tudo certo, guarda o registro
    public void registrarPonto(LocalDate data, LocalTime entrada, LocalTime saida)
            throws RegistroInvalidoException {

        validarBatePonto();

        if (!saida.isAfter(entrada)) {
            throw new RegistroInvalidoException("O horário de saída deve ser posterior ao horário de entrada.");
        }
        if (entrada.isBefore(ENTRADA_MINIMA)) {
            throw new RegistroInvalidoException("O horário de entrada não pode ser antes das 06:00.");
        }
        if (saida.isAfter(SAIDA_MAXIMA)) {
            throw new RegistroInvalidoException("O horário de saída não pode ser após as 22:00.");
        }

        for (RegistroPonto existente : registros) {
            if (existente.getData().equals(data)) {
                throw new RegistroInvalidoException("Já existe um registro de horas para essa data.");
            }
        }

        RegistroPonto novo = new RegistroPonto(data, entrada, saida);

        Duration extras = novo.calcularHorasExtras();
        Duration limite = Duration.ofHours(getLimiteHorasExtras());
        if (extras.compareTo(limite) > 0) {
            throw new RegistroInvalidoException("Limite de horas extras excedido: " + getTipo()
                    + " pode fazer no máximo " + getLimiteHorasExtras()
                    + " horas extras por dia (este registro teria " + RegistroPonto.formatar(extras) + ").");
        }

        registros.add(novo);
    }

    @Override
    public String toString() {
        return "ID: " + id + " | Nome: " + nome + " | Cargo: " + getTipo();
    }
}
