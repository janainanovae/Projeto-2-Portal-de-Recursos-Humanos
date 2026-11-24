// Coordenador: bate ponto e pode fazer até 5 horas extras por dia
public class Coordenador extends Funcionario {

    public Coordenador(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Coordenador";
    }

    @Override
    public boolean batePonto() {
        return true;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 5;
    }
}
