// Estagiário: não bate ponto
public class Estagiario extends Funcionario {

    public Estagiario(int id, String nome) {
        super(id, nome);
    }

    @Override
    public String getTipo() {
        return "Estagiário";
    }

    @Override
    public boolean batePonto() {
        return false;
    }

    @Override
    public int getLimiteHorasExtras() {
        return 0;
    }
}
