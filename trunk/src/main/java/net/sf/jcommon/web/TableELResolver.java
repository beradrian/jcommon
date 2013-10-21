package net.sf.jcommon.web;

import java.beans.FeatureDescriptor;
import java.util.Iterator;
import java.util.Map;

import javax.el.ELContext;
import javax.el.ELResolver;

import com.google.common.collect.Table;

public class TableELResolver extends ELResolver {

	@Override
	public Class<?> getCommonPropertyType(ELContext context, Object base) {
		return base instanceof Table ? Map.class : Object.class;
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
		if (base instanceof Table) {
			@SuppressWarnings("rawtypes")
			Table t = (Table)base;
			if (t.containsRow(property)) {
				context.setPropertyResolved(true);
				return t.row(property);
			}
			if (t.containsColumn(property)) {
				context.setPropertyResolved(true);
				return t.column(property);
			}
		}
		
		return null;
	}

	@Override
	public boolean isReadOnly(ELContext context, Object base, Object property) {
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void setValue(ELContext context, Object base, Object property, Object value) {
		if (!(value instanceof Map)) {
			return;
		}
		Map<Object, Object> valueMap = (Map)value;
		if (base instanceof Table) {
			Table<Object, Object, Object> t = (Table)base;
			if (t.containsRow(property)) {
				for (Map.Entry e : valueMap.entrySet()) {
					t.put(property, e.getKey(), e.getValue());
				}
				context.setPropertyResolved(true);
			}
			if (t.containsColumn(property)) {
				for (Map.Entry e : valueMap.entrySet()) {
					t.put(e.getKey(), property, e.getValue());
				}
				context.setPropertyResolved(true);
			}
		}
	}
	
}
