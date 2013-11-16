package common;

public class Pair<T1, T2> {
  final public T1 first;
  final public T2 second;
  
  @Override
  public String toString() {
	  return "(" + first + ", " + second + ")";
  };

  public T1 getFirst() {
    return this.first;
  }

  public T2 getSecond() {
    return this.second;
  }

  public Pair(T1 first, T2 second) {
    this.first = first;
    this.second = second;
  }
}
