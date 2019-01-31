package com.quadible.mvp;

import com.quadible.mvp.Presenter.UiAction;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * <p>
 *
 * </p>
 */
class InnerClassLambdaCaller implements IRestoreLambdaCaller {

    @Override
    public void restore(Presenter presenter, UiAction action) {
        Field[] fields = action.getClass().getDeclaredFields();
        for (Field classField : fields) {
            if ("arg$1".equals(classField.getName())) {
                try {
                    boolean accessible = classField.isAccessible();
                    classField.setAccessible(true);
                    Constructor<?> constructor = classField.getType().getDeclaredConstructors()[0];

                    boolean constructorAccessible = constructor.isAccessible();
                    constructor.setAccessible(true);

                    //now we are ready to create inner class instance
                    Object innerObject = constructor.newInstance(new Object[constructor.getParameterTypes().length]);
                    restorePresenterToInner(presenter, innerObject);
                    constructor.setAccessible(constructorAccessible);
                    classField.set(action, innerObject);
                    classField.setAccessible(accessible);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void restorePresenterToInner(Presenter presenter, Object innerObject) {
        Field[] fields = innerObject.getClass().getDeclaredFields();
        for (Field classField : fields) {
            if ("this$0".equals(classField.getName())) {
                try {
                    boolean accessible = classField.isAccessible();
                    classField.setAccessible(true);
                    classField.set(innerObject, presenter);
                    classField.setAccessible(accessible);
                    break;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
