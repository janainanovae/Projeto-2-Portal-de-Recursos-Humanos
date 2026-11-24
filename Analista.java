// Analista: bate ponto e pode fazer até 3 horas extras por dia
public class Analista extends Funcionario {

    public Analista(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Analista";
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
