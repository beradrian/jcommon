package net.sf.jcommon.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;


/**
 * Utility methods for working with beans.
 */
public class BeanUtils {

	/** Use only static methods. */
	private BeanUtils() {}
	
	/**
	 * Populate a bean properties from a map.
	 * @param bean the bean to be populated
	 * @param properties the map containing the name and value properties
	 */
	public static void populateProperties(Object bean, Map<String, Object> properties) {
		for (String property : properties.keySet()) {
			populateProperty(bean, property, properties.get(property));
		}
	}
		
	/**
	 * Populate a bean property.
	 * @param bean the bean to be populated
	 * @param property the property name - a setter must exist for it
	 * @param value the property value
	 */
	public static void populateProperty(Object bean, String property, Object value) {
		Method setter = getSetterMethod(bean, property);
		if (setter != null) {
			Class<?> type = setter.getParameterTypes()[0];
			try {
				if (type.equals(String.class)) {
					setter.invoke(bean, value.toString());
				} else if (type.equals(int.class)) {
					setter.invoke(bean, value instanceof Number ? ((Number)value).intValue() : Integer.parseInt(value.toString()));
				} else if (type.equals(float.class)) {
					setter.invoke(bean, value instanceof Number ? ((Number)value).floatValue() : Float.parseFloat(value.toString()));
				} else if (type.equals(boolean.class)) {
					setter.invoke(bean, value instanceof Boolean ? value : Boolean.parseBoolean(value.toString()));
				} else if (type.equals(int.class)) {
					setter.invoke(bean, value instanceof Number ? ((Number)value).doubleValue() : Double.parseDouble(value.toString()));
				} 
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Finds the getter method for a given property.
	 * @param bean the bean
	 * @param property the property name
	 * @return the getter method, or null if not found
	 */
	public static Method getGetterMethod(Object bean, String property) {
		String getterName = "get" + StringUtils.capitalize(property);
		String booleanGetterName = "is" + StringUtils.capitalize(property);
		for (Method m : bean.getClass().getMethods()) {
			if ((getterName.equals(m.getName()) || (booleanGetterName.equals(m.getName()))) 
					&& m.getParameterTypes().length == 1) {
				return m;
			}
		}
		return null;
	}

	/**
	 * Finds the setter method for a given property.
	 * @param bean the bean
	 * @param property the property name
	 * @return the setter method, or null if not found
	 */
	public static Method getSetterMethod(Object bean, String property) {
		String setterName = "set" + StringUtils.capitalize(property);
		for (Method m : bean.getClass().getMethods()) {
			if (setterName.equals(m.getName()) && m.getParameterTypes().length == 1) {
				return m;
			}
		}
		return null;
	}
	
	public static Object createBean(String className, Object... initArguments) {
		try {
			return createBean(Class.forName(className), initArguments);
		} catch (ClassNotFoundException e) {
		} catch (IllegalArgumentException e) {
		} catch (SecurityException e) {
		} catch (InstantiationException e) {
		} catch (IllegalAccessException e) {
		} catch (InvocationTargetException e) {
		} catch (NoSuchMethodException e) {
		}
		return null;
	}
	
	public static <T> T createBean(Class<T> clazz, Object... initArguments) 
			throws IllegalArgumentException, InstantiationException, IllegalAccessException, 
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Class<?>[] initTypes = new Class<?>[initArguments.length];
		for (int i = 0; i < initArguments.length; i++) {
			initTypes[i] = initArguments[i].getClass();
		}
		Constructor<T> c = clazz.getConstructor(initTypes);
		if (c != null) {
			return c.newInstance(initArguments);
		}
		return null;
	}
	
}
