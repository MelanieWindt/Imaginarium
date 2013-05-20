package common;

// Class provides with custom process of events.
// Method apply should contains correct logic to process
// some parametrized by type T event.

abstract public class EventHandler<T> {
  public abstract void apply (T arg) ;
}
