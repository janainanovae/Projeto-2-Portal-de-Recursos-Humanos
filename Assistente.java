// Assistente: bate ponto e pode fazer até 3 horas extras por dia
public class Assistente extends Funcionario {

    public Assistente(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Assistente";
    }

    @Override
    public boolean batePonto() {
        return true;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 3;
    }
}
