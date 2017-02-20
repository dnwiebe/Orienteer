package com.dnwiebe.orienteer.converters;

import com.dnwiebe.orienteer.lookups.Lookup;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by dnwiebe on 2/17/17.
 */
public class Converters {
  private Map<Pair<Class, Class>, Converter> map = new HashMap<Pair<Class, Class>, Converter>();

  public Converters () {
    add (String.class, null, new StringConverter ());
    add (Integer.class, null, new IntegerConverter ());
    add (Long.class, null, new LongConverter ());
    add (Double.class, null, new DoubleConverter ());
    add (Boolean.class, null, new BooleanConverter ());
  }

  public <T, L extends Lookup> void add (Class<T> targetType, Class<L> lookupType, Converter<T> converter) {
    map.put (new Pair<Class, Class> (targetType, lookupType), converter);
  }

  public <T, L extends Lookup> Converter<T> find (Class<T> targetType, Class<L> lookupType) {
    validateTargetType (targetType);
    Converter<T> candidate = map.get (new Pair<Class, Class> (targetType, lookupType));
    if (candidate != null) {return candidate;}
    return map.get (new Pair<Class, Class> (targetType, null));
  }

  public Set<Class<?>> getTargetTypes () {
    Set<Class<?>> result = new HashSet<Class<?>>();
    Set<Pair<Class, Class>> pairs = map.keySet();
    for (Pair<Class, Class> pair : pairs) {
      result.add (pair.a ());
    }
    return result;
  }

  private void validateTargetType (Class<?> targetType) {
    if (targetType == null) {
      throw new IllegalArgumentException ("You must provide a target type to find a Converter");
    }
    if (targetType.isPrimitive ()) {
      throw new IllegalArgumentException("Converters do not support primitive types like '" + targetType.getName()
          + "', only object types");
    }
  }

  static class Pair<A, B> {
    private A _a;
    private B _b;

    public Pair (A a, B b) {
      this._a = a;
      this._b = b;
    }

    public A a () {return _a;}

    public B b () {return _b;}

    @Override
    public boolean equals (Object o) {
      if (o == this) {return true;}
      if (o == null) {return false;}
      if (!(o instanceof Pair)) {return false;}
      Pair<A, B> that = (Pair<A, B>)o;
      if (!matches (this.a (), that.a ())) {return false;}
      if (!matches (this.b (), that.b ())) {return false;}
      return true;
    }

    private boolean matches (Object a, Object b) {
      if ((a == null) && (b != null)) {return false;}
      if ((a != null) && (b == null)) {return false;}
      if (a == null) {return true;}
      return a.equals (b);
    }

    @Override
    public int hashCode () {
      int aCode = (a () == null) ? 0 : a ().hashCode ();
      int bCode = (b () == null) ? 0 : b ().hashCode ();
      return (aCode << 16) + bCode;
    }
  }
}
