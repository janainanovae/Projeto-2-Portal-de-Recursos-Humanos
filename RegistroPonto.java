import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

// Registro de entrada e saída de um funcionário em um dia
public class RegistroPonto {

    // Desconto padrão de almoço e jornada diária considerada "normal" (acima dela é hora extra)
    public static final Duration ALMOCO = Duration.ofHours(1);
    public static final Duration JORNADA_PADRAO = Duration.ofHours(8);

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate data;
    private final LocalTime entrada;
    private final LocalTime saida;

    public RegistroPonto(LocalDate data, LocalTime entrada, LocalTime saida) {
        this.data = data;
        this.entrada = entrada;
        this.saida = saida;
    }

    public LocalDate getData() {
        return data;
    }

    public LocalTime getEntrada() {
        return entrada;
    }

    public LocalTime getSaida() {
        return saida;
    }

    // Horas trabalhadas no dia = (saída - entrada) - 1 hora de almoço
    public Duration calcularHorasTrabalhadas() {
        Duration trabalhadas = Duration.between(entrada, saida).minus(ALMOCO);
        return trabalhadas.isNegative() ? Duration.ZERO : trabalhadas;
    }

    // Horas extras = o que passar da jornada padrão de 8 horas
    public Duration calcularHorasExtras() {
        Duration extras = calcularHorasTrabalhadas().minus(JORNADA_PADRAO);
        return extras.isNegative() ? Duration.ZERO : extras;
    }

    // Formata uma duração como "8h30"
    public static String formatar(Duration duracao) {
        long minutos = duracao.toMinutes();
        return String.format("%dh%02d", minutos / 60, minutos % 60);
    }

    @Override
    public String toString() {
        return "Data: " + data.format(FORMATO_DATA)
                + " | Entrada: " + entrada.format(FORMATO_HORA)
                + " | Saída: " + saida.format(FORMATO_HORA)
                + " | Trabalhadas: " + formatar(calcularHorasTrabalhadas())
                + " | Extras: " + formatar(calcularHorasExtras());
    }
}
