public class ListeBonus {

  private int countFrites = 0;
  private int countBieres = 0;

  public ListeBonus() {}

  public void addFrites(int nb) {
    countFrites = countFrites + nb;
  }

  public void addBieres(int nb) {
    countBieres = countBieres + nb;
  }

  public int getCountFrites() {
    return countFrites;
  }

  public int getCountBieres() {
    return countBieres;
  }

  public void useFrites() {
    countFrites--;
  }

  public void useBieres() {
    countBieres--;
  }
}
