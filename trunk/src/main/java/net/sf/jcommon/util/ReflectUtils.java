package net.sf.jcommon.util;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public final class ReflectUtils {

	/** Use only the utility static methods. */
	private ReflectUtils() {}
	
	/** 
	 * @param clazz the class for which we try to determine an index of a generic variable
	 * @param genericTypeName the generic type name as it appears in the declaration
	 * @return the zero-based index of the type in the given class declaration, or -1 if the type is not found 
	 */
	public static int getTypeIndex(Class<?> clazz, String genericTypeName) {
		int i = 0;
		for (TypeVariable<?> type : clazz.getTypeParameters()) {
			if (type.getName().equals(genericTypeName)) {
				return i;
			}
			++i;
		}
		return -1;
	}
	
	/**
	 * Finds the class before the ancestor in the hierarchy starting at the descendant 
	 * @param descendant the starting class
	 * @param ancestor the ancestor class
	 * @return the closest class in the hierarchy to the ancestor
	 */
	public static Class<?> getClosestClass(Class<?> descendant, Class<?> ancestor) {
		while (descendant.getSuperclass() != ancestor && descendant != null) {
			descendant = descendant.getSuperclass();
		}
		return descendant;
	}
	
	/**
	 * Given a parameterized class and the generic ancestor one, finds the actual type of a type name
	 * @param actualClass the parameterized class
	 * @param genericClass the generic ancestor class
	 * @param typeName the type name as it appears in the generic ancestor class declaration
	 * @return the actual type for the specified type name 
	 */
	public static Class<?> getActualType(Class<?> actualClass, Class<?> genericClass, String typeName) {
		Class<?> s = getClosestClass(actualClass, genericClass);
		if (s == null) {
			return null;
		}
		int idx = getTypeIndex(genericClass, typeName);
		if (idx < 0) {
			return null;
		}
		if (s.getGenericSuperclass() instanceof ParameterizedType) {
			Type actualType = ((ParameterizedType)s.getGenericSuperclass()).getActualTypeArguments()[idx];
			if (actualType instanceof Class<?>) {
				return (Class<?>) actualType;
			}
			if (actualType instanceof TypeVariable<?>) {
				return getActualType(actualClass, s, ((TypeVariable<?>)actualType).getName());
			}
		}
		return null;
	}
}
