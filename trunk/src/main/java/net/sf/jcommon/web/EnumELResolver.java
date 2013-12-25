package net.sf.jcommon.web;

import java.beans.FeatureDescriptor;
import java.util.Iterator;

import javax.el.ELContext;
import javax.el.ELResolver;

public class EnumELResolver extends ELResolver {
	
	private static class EnumPath {
		private String path;
	}
	
	private String propertyName = "ENUMS";
	private String defaultPackage;
	
	public EnumELResolver() {
	}

	public EnumELResolver(String propertyName) {
		this.propertyName = propertyName;
	}

	public EnumELResolver(String propertyName, String defaultPackage) {
		this.propertyName = propertyName;
		this.defaultPackage = defaultPackage;
		if (defaultPackage != null && !defaultPackage.endsWith(".")) {
			this.defaultPackage += ".";
		}
	}

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return String.class;
	}

	@Override
	public Iterator<FeatureDescriptor> getFeatureDescriptors(ELContext context, Object base) {
		return null;
	}

	@Override
	public Class<?> getType(ELContext context, Object base, Object property) {
		Object v = getValue(context, base, property);
		return v != null ? v.getClass() : null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object getValue(ELContext context, Object base, Object property) {
		if (!(property instanceof String)) {
			return false;
		}
		
		if (base == null && propertyName.equals(property)) {
			context.setPropertyResolved(true);
			return new EnumPath();
		}
		if (base instanceof EnumPath) {
			String path = ((EnumPath)base).path;
			path = (path == null ? "" : path + ".") + property.toString();
			((EnumPath)base).path = path;
			
			Class<Enum<?>> enumClass = null;
			try {
				enumClass = (Class<Enum<?>>) Class.forName(path);
			} catch (ClassNotFoundException e) {
				try {
					enumClass = (Class<Enum<?>>) Class.forName(defaultPackage + path);
				} catch (ClassNotFoundException e1) {
					context.setPropertyResolved(true);
					return base;
				}
			}
			if (enumClass != null) {
				context.setPropertyResolved(true);
				return enumClass.getEnumConstants();
			}
		}
		if (base instanceof Enum<?>[]) {
			for (Enum<?> e : (Enum<?>[])base) {
				if (e.name().equals(property)) {
					context.setPropertyResolved(true);
					return e;
				}
			}
		}
		
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return true;
	}

	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		throw new UnsupportedOperationException("Enum values can only be read");
	}
	
}
