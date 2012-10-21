package net.sf.jcommon.geo;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.StringType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;
import org.hibernate.usertype.UserType;

public abstract class GenericStringType<T> implements UserType {

	@Override
	public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner)
			throws HibernateException, SQLException {
		String value = (String) StringType.INSTANCE.get(rs, names[0], session);
		if (value == null) {
			return null;
		} else {
			return stringToValue(value);
		}
	}
	
	protected String valueToString(Object value) {
		return value.toString();
	}
	
	protected abstract T stringToValue(String valueAsString);

	@Override
	public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) 
			throws HibernateException, SQLException {
		if (value == null) {
			StringType.INSTANCE.set(st, null, index, session);
		} else {
			StringType.INSTANCE.set(st, valueToString(value), index, session);
		}
	}

	@Override
	public int[] sqlTypes() {
		return new int[] { VarcharTypeDescriptor.INSTANCE.getSqlType() };
	}

	@Override
	public boolean equals(Object x, Object y) throws HibernateException {
		return x.equals(y);
	}

	@Override
	public int hashCode(Object x) throws HibernateException {
		return x.hashCode();
	}

	@Override
	public Object deepCopy(Object value) throws HibernateException {
		return value;
	}

	@Override
	public boolean isMutable() {
		return false;
	}

	@Override
	public Serializable disassemble(Object value) throws HibernateException {
		return (Serializable) value;
	}

	@Override
	public Object assemble(Serializable cached, Object owner) throws HibernateException {
		return cached;
	}

	@Override
	public Object replace(Object original, Object target, Object owner)	throws HibernateException {
		return original;
	}
}
