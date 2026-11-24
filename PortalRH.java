import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PortalRH {

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("dd/MM/uuuu").withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    // Funcionários mantidos apenas em memória
    private static final List<Funcionario> funcionarios = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("Seja bem-vindo(a) ao Portal de RH!");

        int opcao;
        do {
            exibirMenu();
            opcao = lerInteiro("Escolha uma opção: ");

            switch (opcao) {
                case 1:
                    criarFuncionario();
                    break;
                case 2:
                    listarFuncionarios();
                    break;
                case 3:
                    buscarFuncionario();
                    break;
                case 4:
                    removerFuncionario();
                    break;
                case 5:
                    registrarHorario();
                    break;
                case 6:
                    listarRegistros();
                    break;
                case 7:
                    System.out.println("Encerrando o Portal de RH. Até logo!");
                    break;
                default:
                    System.out.println("Opção inválida! Escolha um número de 1 a 7.");
            }
        } while (opcao != 7);

        scanner.close();
    }

    private static void exibirMenu() {
        System.out.println();
        System.out.println("===== PORTAL DE RH =====");
        System.out.println("1 - Criar funcionário");
        System.out.println("2 - Listar funcionários");
        System.out.println("3 - Buscar funcionário por id");
        System.out.println("4 - Remover funcionário por id");
        System.out.println("5 - Registrar horário de entrada e saída");
        System.out.println("6 - Listar registros de horas de um funcionário");
        System.out.println("7 - Sair");
    }

    // Cria um funcionário do tipo escolhido, com id único
    private static void criarFuncionario() {
        System.out.println();
        System.out.println("Tipos de funcionário:");
        System.out.println("1 - Gerente");
        System.out.println("2 - Coordenador");
        System.out.println("3 - Analista");
        System.out.println("4 - Assistente");
        System.out.println("5 - Estagiário");
        int tipo = lerInteiro("Escolha o tipo: ");

        if (tipo < 1 || tipo > 5) {
            System.out.println("Tipo inválido! Escolha um número de 1 a 5.");
            return;
        }

        int id = lerInteiro("Informe o id: ");
        if (buscarPorId(id) != null) {
            System.out.println("Já existe um funcionário com o id " + id + ".");
            return;
        }

        String nome = lerTexto("Informe o nome: ");

        Funcionario funcionario;
        switch (tipo) {
            case 1:
                funcionario = new Gerente(id, nome);
                break;
            case 2:
                funcionario = new Coordenador(id, nome);
                break;
            case 3:
                funcionario = new Analista(id, nome);
                break;
            case 4:
                funcionario = new Assistente(id, nome);
                break;
            default:
                funcionario = new Estagiario(id, nome);
        }

        funcionarios.add(funcionario);
        System.out.println("Funcionário cadastrado: " + funcionario);
    }

    private static void listarFuncionarios() {
        if (funcionarios.isEmpty()) {
            System.out.println("Nenhum funcionário cadastrado.");
            return;
        }

        System.out.println();
        System.out.println("===== Funcionários =====");
        for (Funcionario funcionario : funcionarios) {
            System.out.println(funcionario);
        }
    }

    private static void buscarFuncionario() {
        int id = lerInteiro("Informe o id do funcionário: ");
        Funcionario funcionario = buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
        } else {
            System.out.println(funcionario);
        }
    }

    private static void removerFuncionario() {
        int id = lerInteiro("Informe o id do funcionário a remover: ");
        Funcionario funcionario = buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        funcionarios.remove(funcionario);
        System.out.println("Funcionário removido: " + funcionario);
    }

    // Registra entrada e saída; as regras de negócio são validadas dentro de Funcionario
    private static void registrarHorario() {
        int id = lerInteiro("Informe o id do funcionário: ");
        Funcionario funcionario = buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        try {
            // Barra logo no início quem não bate ponto, antes de pedir data e horários
            funcionario.validarBatePonto();

            LocalDate data = lerData("Informe a data (dd/mm/aaaa): ");
            LocalTime entrada = lerHora("Informe o horário de entrada (HH:mm): ");
            LocalTime saida = lerHora("Informe o horário de saída (HH:mm): ");

            funcionario.registrarPonto(data, entrada, saida);

            List<RegistroPonto> registros = funcionario.getRegistros();
            RegistroPonto ultimo = registros.get(registros.size() - 1);
            System.out.println("Registro realizado! Horas trabalhadas: "
                    + RegistroPonto.formatar(ultimo.calcularHorasTrabalhadas())
                    + " | Horas extras: " + RegistroPonto.formatar(ultimo.calcularHorasExtras())
                    + " (já descontada 1 hora de almoço)");
        } catch (RegistroInvalidoException e) {
            System.out.println("Registro não realizado: " + e.getMessage());
        }
    }

    private static void listarRegistros() {
        int id = lerInteiro("Informe o id do funcionário: ");
        Funcionario funcionario = buscarPorId(id);

        if (funcionario == null) {
            System.out.println("Funcionário não encontrado.");
            return;
        }

        List<RegistroPonto> registros = funcionario.getRegistros();
        if (registros.isEmpty()) {
            System.out.println("Nenhum registro de horas para " + funcionario.getNome() + ".");
            return;
        }

        System.out.println();
        System.out.println("===== Registros de " + funcionario.getNome() + " =====");

        Duration total = Duration.ZERO;
        for (RegistroPonto registro : registros) {
            System.out.println(registro);
            total = total.plus(registro.calcularHorasTrabalhadas());
        }

        System.out.println("Total de horas trabalhadas: " + RegistroPonto.formatar(total));
    }

    // Busca um funcionário pelo id; devolve null se não existir
    private static Funcionario buscarPorId(int id) {
        for (Funcionario funcionario : funcionarios) {
            if (funcionario.getId() == id) {
                return funcionario;
            }
        }
        return null;
    }

    // ---------- Leitura de dados com tratamento de entradas inválidas ----------

    private static int lerInteiro(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return Integer.parseInt(entrada);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida! Digite apenas números inteiros.");
            }
        }
    }

    private static String lerTexto(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            if (!entrada.isEmpty()) {
                return entrada;
            }
            System.out.println("O valor não pode ficar vazio.");
        }
    }

    private static LocalDate lerData(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return LocalDate.parse(entrada, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida! Use o formato dd/mm/aaaa (ex.: 24/11/2026).");
            }
        }
    }

    private static LocalTime lerHora(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            String entrada = scanner.nextLine().trim();

            try {
                return LocalTime.parse(entrada, FORMATO_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Horário inválido! Use o formato HH:mm (ex.: 08:30).");
            }
        }
    }
}
